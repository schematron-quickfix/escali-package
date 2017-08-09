/*
 * Static variables and functions
 */
// miscs
var onSaxonLoad;
var spinOpts = {
    lines: 15 // The number of lines to draw
    , length: 28 // The length of each line
    , width: 9 // The line thickness
    , radius: 42 // The radius of the inner circle
    , scale: 0.75 // Scales overall size of the spinner
    , corners: 1 // Corner roundness (0..1)
    , color: '#000' // #rgb or #rrggbb or array of colors
    , opacity: 0.2 // Opacity of the lines
    , rotate: 39 // The rotation offset
    , direction: 1 // 1: clockwise, -1: counterclockwise
    , speed: 0.8 // Rounds per second
    , trail: 42 // Afterglow percentage
    , fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
    , zIndex: 2e9 // The z-index (defaults to 2000000000)
    , className: 'spinner' // The CSS class to assign to the spinner
    , top: '50%' // Top position relative to parent
    , left: '50%' // Left position relative to parent
    , shadow: false // Whether to render a shadow
    , hwaccel: false // Whether to use hardware acceleration
    , position: 'absolute' // Element positioning
}

// stylesheet paths
var escaliPath = '../xsl/';
var compilerPath = escaliPath + '01_compiler/';
var validatorPath = escaliPath + '02_validator/';
var extractorPath = escaliPath + '03_extractor/';
var manipulatorPath = escaliPath + '04_manipulator/';
var webImplPath = escaliPath + '05_web-impl/';

// compiled stylesheets
var escaliMain = null;
var compiledSch = null;
var webReport = null;
var extractor = null;
var manipulator = null;


// documents
var xmlInstanceDoc = null;
var xmlInstanceFile = null;
var schemaDoc = null;
var schemaFile = null;
var svrlDoc = null;

var defaultBaseUri = 'http://escali.schematron-quickfix.com/';

function load() {
    onSaxonLoad = function () {
        prepareCompiling();
    };
    resetInstance();
    resetSchema();
    
    $(document).keydown(function (e) {
        $body = $('#body');
        captureKeyDowns(e);
    });
    $(document).keyup(function (e) {
        captureKeyDowns(e);
    });
}

function captureKeyDowns(e) {
    var allClasses =[ 'ctrlDown', 'shiftDown', 'noDown'];
    var classes = new Array();
    if (e.ctrlKey) {
        classes.push('ctrlDown');
    }
    if (e.shiftKey) {
        classes.push('shiftDown')
    }
    if (classes.length == 0) {
        classes.push('noDown');
    }
    var $body = $('#body');
    $.each(allClasses, function (i, c) {
        $body.removeClass(c);
    });
    $.each(classes, function (i, c) {
        $body.addClass(c);
    });
}


function prepareCompiling() {
    
    Saxon.setErrorHandler(function (error) {
        errorList.push(error);
        showErrors();
    });
    Saxon.setLogLevel('SEVERE');
    
    webReport =[Saxon.newXSLT20Processor(
    Saxon.requestXML(webImplPath + 'escali_web-impl_1_xml-preview.xsl'))];
    
    escaliMainJS =[
    compilerPath + 'escali_compiler_1_include.sef',
    compilerPath + 'escali_compiler_2_abstract-patterns.sef',
    compilerPath + 'escali_compiler_3_main.sef'];
    
    escaliMain =[
    Saxon.newXSLT20Processor(
    Saxon.requestXML(compilerPath + 'escali_compiler_1_include.xsl')),
    Saxon.newXSLT20Processor(
    Saxon.requestXML(compilerPath + 'escali_compiler_2_abstract-patterns.xsl')),
    Saxon.newXSLT20Processor(Saxon.requestXML(compilerPath + 'escali_compiler_3_main.xsl'))];
    
    compiledSch =[
    null,
    Saxon.newXSLT20Processor(Saxon.requestXML(validatorPath + 'escali_validator_2_postprocess.xsl'))];
    
    extractor = Saxon.newXSLT20Processor(Saxon.requestXML(extractorPath + 'escali_extractor_1_main.xsl'));
    manipulator =[
    Saxon.newXSLT20Processor(Saxon.requestXML(manipulatorPath + 'escali_manipulator_0_removeChangePIs.xsl')),
    null,
    Saxon.newXSLT20Processor(Saxon.requestXML(manipulatorPath + 'escali_manipulator_2_postprocess.xsl'))];
    manipulator[0].setInitialTemplate('standalone');
}

function resetInstance() {
    xmlInstanceDoc = null;
    xmlInstanceFile = null;
    var instDrop = document.getElementById('instanceDrop');
    var instance = document.getElementById('instance');
    instance.innerHTML = "";
    instance.appendChild(instDrop);
    removeDropOverClass(instDrop);
}

function resetSchema() {
    schemaDoc = null;
    schemaFile = null;
    var schDrop = document.getElementById('schemaDrop');
    schDrop.className = schDrop.className.replace(/\swaitFor/i, "");
    var schema = document.getElementById('schema');
    schema.innerHTML = "";
    schema.appendChild(schDrop);
    removeDropOverClass(schDrop);
}

function setBaseURI(node, baseUri){
    if(node.baseURI == null){
        var el = null;
        if(Node.DOCUMENT_NODE == node.nodeType){
            el = node.documentElement;
        } else if(Node.ELEMENT_NODE == node.nodeType){
            el = node;
        } else {
            el = node.parentNode;
        }
        
        el.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:base', baseUri);
    }
    
}

/*
 *
 * Escali Web implementation
 *
 */


/*
 *
 * File handling

 * */

function createCombiDropZone(id) {
    var dropZone = $('#' + id)[0];
    function handleFileSelect(evt) {
        evt.stopPropagation();
        evt.preventDefault();
        
        var files = evt.dataTransfer.files;
        var schFile = null;
        var xmlFile = null;
        
        $.each(files, function (i, f) {
            
            if (f.name.match(/\.sch$/) && schFile == null) {
                schFile = f;
            } else if (xmlFile == null) {
                xmlFile = f;
            }
        });
        
        if (xmlFile == null) {
            loadSchema(schFile);
        } else if (schFile == null) {
            loadInstance(xmlFile);
        } else {
            loadInstanceAndSchema(xmlFile, schFile);
        }
    }
    var isOver = false;
    function handleDragOver(evt) {
        evt.stopPropagation();
        evt.preventDefault();
        evt.dataTransfer.dropEffect = 'copy';
        if (! isOver) {
            addDropOverClass(dropZone);
            isOver = true;
        }
    }
    function handleDragIn(evt) {
        if (! isOver) {
            addDropOverClass(dropZone);
            isOver = true;
        }
    }
    function handleDragOut(evt) {
        if (isOver) {
            removeDropOverClass(dropZone);
            isOver = false;
        }
    }
    
    dropZone.addEventListener('dragleave', handleDragOut, false);
    dropZone.addEventListener('dragexit', handleDragOut, false);
    dropZone.addEventListener('dragend', handleDragOut, false);
    dropZone.addEventListener('dragenter', handleDragIn, false);
    dropZone.addEventListener('dragover', handleDragOver, false);
    dropZone.addEventListener('drop', handleFileSelect, false);
}

function createDropZone(id, handler) {
    // Setup the dnd listeners.
    var dropZone = document.getElementById(id);
    
    
    function handleFileSelect(evt) {
        evt.stopPropagation();
        evt.preventDefault();
        
        var files = evt.dataTransfer.files;
        // FileList object.
        
        // files is a FileList of File objects. List some properties.
        
        handler(files[0]);
    }
    var isOver = false;
    function handleDragOver(evt) {
        evt.stopPropagation();
        evt.preventDefault();
        evt.dataTransfer.dropEffect = 'copy';
        if (! isOver) {
            addDropOverClass(dropZone);
            isOver = true;
        }
    }
    function handleDragIn(evt) {
        if (! isOver) {
            addDropOverClass(dropZone);
            isOver = true;
        }
    }
    function handleDragOut(evt) {
        if (isOver) {
            removeDropOverClass(dropZone);
            isOver = false;
        }
    }
    
    dropZone.addEventListener('dragleave', handleDragOut, false);
    dropZone.addEventListener('dragexit', handleDragOut, false);
    dropZone.addEventListener('dragend', handleDragOut, false);
    dropZone.addEventListener('dragenter', handleDragIn, false);
    dropZone.addEventListener('dragover', handleDragOver, false);
    dropZone.addEventListener('drop', handleFileSelect, false);
}

function removeDropOverClass(el) {
    if (el.className != null) {
        el.className = el.className.replace(/\sdropOver/i, "");
    }
}
function addDropOverClass(el) {
    el.className += " dropOver";
}

function createFileOpenDialog(handler) {
    function handleFileSelect(evt) {
        var files = evt.target.files;
        
        handler(files[0]);
    }
    
    var fileInputEl = document.createElement('input');
    fileInputEl.type = 'file';
    /*    moveToBottom(fileInputEl);*/
    fileInputEl.addEventListener('change', handleFileSelect, false);
    fileInputEl.click();
}

function getFileContentByFile(file, callback) {
    
    var fileStr = "";
    var reader = new FileReader();
    var isFinished = false;
    reader.onload = (function (theFile) {
        return function (e) {
            fileStr = e.target.result;
            isFinished = true;
            callback(Saxon.parseXML(fileStr));
        };
    })(file);
    
    reader.readAsText(file);
}


var backupSchema = null;
function loadSchema(file) {
    backupSchema = compiledSch;
    startSpinner('Loading and compiling schema ' + file.name, 2, function () {
        resetSchema();
    });
    var placeHolder = document.getElementById('schemaDrop');
    placeHolder.className += " waitFor";
    removeDropOverClass(placeHolder);
    
    if (escaliMain == null) {
        prepareCompiling();
    }
    
    getFileContentByFile(file, function (schema) {
        
        progressBarStep();
        schemaDoc = schema;
        schemaFile = file;
        
        setBaseURI(schemaDoc, defaultBaseUri + schemaFile.name);
        
        pipe(escaliMain, schema, function (result) {
            compiledSch[0] = Saxon.newXSLT20Processor(result);
            progressBarStep();
            var validated = checkForValidation(function () {
                createCombiDropZone('report');
                placeHolder.className = placeHolder.className.replace(/\swaitFor/i, "");
            });
            
            if (! validated) {
                var schema = document.getElementById('schema');
                schema.innerHTML = '';
                /*            schema.remove('#report')*/
                var msg = document.createElement('div');
                msg.id = "report";
                msg.innerHTML = "<div class='alert alert-info' role='alert'><div class='drop_zone_overwrite'></div>You have loaded " + file.name + ".</div>";
                schema.appendChild(msg);
                
                moveToBottom(placeHolder);
                stopSpinner();
                
                createCombiDropZone('report');
                placeHolder.className = placeHolder.className.replace(/\swaitFor/i, "");
            }
        });
    });
}
function loadInstance(file) {
    // if the schema is not loaded yet:
    startSpinner('Create XML view of ' + file.name, 2, function () {
        resetInstance();
    });
    if (webReport == null) {
        prepareCompiling();
    }
    if (webReport[0] != null) {
        getFileContentByFile(file, function (xml) {
            progressBarStep();
            loadInstanceXml(file, xml);
        });
    } else {
        alert('Failed to compile basic stylesheets');
    }
}

function loadInstanceXml(file, xml) {
    xmlInstanceDoc = xml;
    xmlInstanceFile = file;
    setBaseURI(xmlInstanceDoc, defaultBaseUri + xmlInstanceFile.name);
    
    var validated = checkForValidation(function () {
        createCombiDropZone('preview');
    });
    if (! validated) {
        webReport[0].setInitialTemplate('srcOnly');
        webReport[0].setParameter(null, 'fileName', file.name);
        webReport[0].transformToHTMLFragment(xml.documentElement);
        progressBarStep();
        movePreview();
        progressBarStep();
        markChanges();
        stopSpinner();
        createCombiDropZone('preview');
    }
}

function loadInstanceAndSchema(xmlFile, schFile) {
    backupSchema = compiledSch;
    startSpinner('Loading and compiling schema ' + schFile.name, 5, function () {
        resetSchema();
    });
    
    var placeHolder = document.getElementById('schemaDrop');
    placeHolder.className += " waitFor";
    removeDropOverClass(placeHolder);
    
    if (escaliMain == null) {
        prepareCompiling();
    }
    
    getFileContentByFile(schFile, function (schema) {
        progressBarStep();
        schemaDoc = schema;
        schemaFile = schFile;
        
        setBaseURI(schemaDoc, defaultBaseUri + schemaFile.name);
        
        getFileContentByFile(xmlFile, function (xml) {
            progressBarStep();
            pipe(escaliMain, schema, function (result) {
                compiledSch[0] = Saxon.newXSLT20Processor(result);
                progressBarStep();
                
                loadInstanceXml(xmlFile, xml);
                
                /*if (webReport[0] != null) {
                getFileContentByFile(file, function (xml) {
                progressBarStep();
                loadInstanceXml(file, xml);
                });
                } else {
                alert('Failed to compile basic stylesheets');
                }*/
                createCombiDropZone('report');
                placeHolder.className = placeHolder.className.replace(/\swaitFor/i, "");
            });
        });
    });
}


function saveInstance() {
    pipe([manipulator[0]], xmlInstanceDoc, function (docWOPIs) {
        var a = document.createElement("a"),
        file = new Blob([Saxon.serializeXML(docWOPIs)], {
            type: "text/xml"
        });
        if (window.navigator.msSaveOrOpenBlob) {
            // IE10+
            window.navigator.msSaveOrOpenBlob(file, xmlInstanceFile.name);
        } else {
            // Others
            var url = URL.createObjectURL(file);
            a.href = url;
            a.download = xmlInstanceFile.name;
            document.body.appendChild(a);
            a.click();
            setTimeout(function () {
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
            },
            0);
        }
    });
}

function saveFile(xmlContent, fileName) {
    if (xmlContent != null) {
        var a = document.createElement("a"),
        file = new Blob([Saxon.serializeXML(xmlContent)], {
            type: "text/xml"
        });
        if (window.navigator.msSaveOrOpenBlob) {
            // IE10+
            window.navigator.msSaveOrOpenBlob(file, fileName);
        } else {
            // Others
            var url = URL.createObjectURL(file);
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            setTimeout(function () {
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
            },
            0);
        }
    }
}



function isValidationEnable() {
    return (compiledSch[0] != null && xmlInstanceDoc != null);
}

function checkForValidation(callbackIfval) {
    if (isValidationEnable()) {
        startSpinner('Validation...', 4, function () {
            resetSchema();
            resetInstance();
        });
        pipe(compiledSch, xmlInstanceDoc, function (result) {
            if (result != null) {
                svrlDoc = result;
                progressBarStep();
                webReport[0].setParameter(null, 'fileName', xmlInstanceFile.name);
                webReport[0].setParameter(null, 'schemaName', schemaFile.name);
                webReport[0].setParameter(null, 'xmlInstanceDoc', xmlInstanceDoc);
                webReport[0].setInitialTemplate('');
                webReport[0].transformToHTMLFragment(svrlDoc.documentElement);
                progressBarStep();
                movePreview();
                progressBarStep();
                moveReport();
                afterValidation();
            }
            stopSpinner();
            callbackIfval();
        });
        
        return true;
    } else {
        return false;
    }
}

function afterValidation() {
    progressBarStep();
    markChanges();
    createCombiDropZone('report');
    createCombiDropZone('preview');
}

function movePreview(usedDoc) {
    if (usedDoc == null) {
        usedDoc = document;
    }
    moveToBottom(document.getElementById('instanceDrop'));
    var instancePrev = document.getElementById('instance');
    instancePrev.innerHTML = "";
    var prevContent = usedDoc.getElementById('preview');
    instancePrev.appendChild(prevContent);
}

function moveReport(usedDoc) {
    if (usedDoc == null) {
        usedDoc = document;
    }
    moveToBottom(document.getElementById('schemaDrop'));
    var reportView = document.getElementById('schema');
    reportView.innerHTML = "";
    var reportContent = usedDoc.getElementById('report');
    reportView.appendChild(reportContent);
}

/*
 *
 * QuickFixes
 */
var sqfNs = 'http://www.schematron-quickfix.com/validator/process';
function runQuickFix(fixId) {
    /*var extractor = Saxon.newXSLT20Processor(
    Saxon.requestXML(extractorPath + 'escali_extractor_1_main.xsl'));*/
    var parameter = null;
    var commitUnselection = false;
    if (fixId == null) {
        fixId = getAllSeletions();
        if (fixId.length == 1) {
            fixId = fixId[0];
        }
    } else if (getAllSeletions().length > 0) {
        commitUnselection = true;
    }
    
    var unsetUEs = getUnsetUserEntries(fixId);
    
    
    var oldDoc = xmlInstanceDoc;
    var oldFile = xmlInstanceFile;
    
    // PROCESS FUNCTION:
    var processQuickFix = function () {
        
        startSpinner('Execute QuickFix', 5, function () {
            loadInstanceXml(oldFile, oldDoc);
        });
        extractor.setParameter(null, 'id', fixId);
        
        pipe([extractor], svrlDoc, function (result) {
            manipulator[1] = Saxon.newXSLT20Processor(result);
            
            parameter = $('.ue-input');
            
            if (parameter != null) {
                parameter.each(function (i, cur) {
                    manipulator[1].setParameter(sqfNs, cur.id, cur.value);
                });
            }
            
            progressBarStep();
            pipe(manipulator, xmlInstanceDoc, function (newDoc) {
                progressBarStep();
                
                if (newDoc != null) {
                    var newFile = xmlInstanceFile;
                    addToUndo(function () {
                        loadInstanceXml(oldFile, oldDoc);
                    },
                    function () {
                        loadInstanceXml(newFile, newDoc);
                    });
                    loadInstanceXml(xmlInstanceFile, newDoc);
                }
                stopSpinner();
            });
        });
    }
    var processQuickFix2 = function () {
        if (unsetUEs.length > 0) {
            askForUserEntry(unsetUEs, processQuickFix);
        } else {
            processQuickFix();
        }
    }
    
    if (commitUnselection) {
        warnForUnselection(processQuickFix2);
    } else {
        processQuickFix2();
    }
}

function showQuickFixMenu(divId) {
    var fixMenu = document.getElementById(divId);
    implementMenu([fixMenu]);
    
    menu.showMenu = function () {
        fixMenu.className += " show";
        fixMenu.parentElement.className += " show";
    }
    
    menu.hideMenu = function () {
        fixMenu.className = fixMenu.className.replace(/\sshow/i, '');
        fixMenu.parentElement.className = fixMenu.parentElement.className.replace(/\sshow/i, '');
        
        menu.showMenu = null;
        menu.hideMenu = null;
    }
}

function implementMenu(elements) {
    menu.showMenu = function () {
        for (var i = 0; i < elements.length; i++) {
            elements[i].className += " forceShow";
        }
    }
    
    menu.hideMenu = function () {
        for (var i = 0; i < elements.length; i++) {
            elements[i].className = elements[i].className.replace(/(\sforceShow)+/i, '');
        }
        
        menu.showMenu = null;
        menu.hideMenu = null;
    }
}

var menu = {
    menuSupress: false,
    isVisible: false,
    showMenu: null,
    hideMenu: null
};
function hideOrShowMenu() {
    if (menu.menuSupress) {
        menu.menuSupress = false;
        return;
    }
    
    if (menu.isVisible) {
        if (menu.hideMenu != null) {
            menu.hideMenu();
            menu.isVisible = false;
        }
    } else {
        if (menu.showMenu != null) {
            menu.showMenu();
            menu.isVisible = true;
        }
    }
}
function supressMenu() {
    menu.menuSupress = true;
}

function markChanges() {
    var markStarts = document.getElementsByClassName('changePi-start');
    for (var i = 0; i < markStarts.length; i++) {
        var markStart = markStarts[i];
        
        var markEndId = markStart.id.replace(/^start/i, 'end');
        var markEnd = document.getElementById(markEndId);
        var markNodes = getFromTo(markStart, markEnd);
        var markerType = '';
        if (markStart.id.match(/delete/i)) {
            markerType = 'delete';
        } else if (markStart.id.match(/add/i)) {
            markerType = 'add';
        } else if (markStart.id.match(/replace/i)) {
            markerType = 'replace';
        }
        
        for (var j = 0; j < markNodes.length; j++) {
            if (! markNodes[j].className.match(/lineNumber/i)) {
                markNodes[j].className += " marked " + markerType;
            }
        }
    }
}

function getFromTo(from, to) {
    var docEls = document.getElementsByTagName('*');
    var j = 0;
    var resultVal =[];
    var isReading = false;
    for (var i = 0; i < docEls.length; i++) {
        if (docEls[i] == from) {
            isReading = true;
        }
        if (isReading) {
            resultVal[j++] = docEls[i];
        }
        if (docEls[i] == to) {
            return resultVal;
        }
    }
    return[];
}


function hidePatternMenu(self) {
    $self = $(self);
    var $context = getPatternOrRoot(self);
    self.onclick = function () {
        showPatternMenu(self);
    }
    self.title = 'Expand pattern';
    $self.children('span').removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
    
    $context.addClass('collapsedPattern');
}

function showPatternMenu(self) {
    $self = $(self);
    var $context = getPatternOrRoot(self);
    self.onclick = function () {
        hidePatternMenu(self);
    }
    self.title = 'Collapse pattern';
    $self.children('span').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
    
    $context.removeClass('collapsedPattern');
}


function showQFMenu(self, id) {
    if (typeof id === 'string') {
        var menu = document.getElementById(id);
        var icon = self.children[0];
        if (menu.className.match(/forceShow/i)) {
            icon.className = icon.className.replace(/chevron-up/i, 'chevron-down');
            menu.className = menu.className.replace(/(\sforceShow)+/, '')
        } else {
            icon.className = icon.className.replace(/chevron-down/i, 'chevron-up');
            menu.className += ' forceShow';
        }
    } else {
        var $context = getPatternOrRoot(self);
        if ($context.hasClass('collapsedPattern')) {
            $('.expandPattern', $context).click();
        }
        var selector = id ? '.svrl-msg.panel:not(:has(.fixMenu.forceShow))': '.svrl-msg.panel:has(.fixMenu.forceShow)';
        var $expMsgs = $(selector, $context);
        $expMsgs.each(function () {
            var link = $('.fixPin .link', $(this));
            link[0].click();
        });
    }
}

function selectDefaults(self) {
    var $context = getPatternOrRoot(self);
    if ($context.hasClass('collapsedPattern')) {
        $('.expandPattern', $context).click();
    }
    var $defaultFixes = $('a.list-group-item.fixItem.isDefaultFix', $context);
    $defaultFixes.each(function () {
        var $input = $('.qfTools input', $(this));
        $input[0].click();
    });
}

function getPatternOrRoot(self) {
    var panel = self.closest('.patternPanel.panel');
    var $context = panel != null ? $(panel): $(self.closest('.schemaRoot'));
    return $context;
}

function selectQF(self) {
    var fixListItem = getNextAncByClass(self, /\s?fixItem/i);
    var svrlMsg = getNextAncByClass(self, /\s?svrl-msg/i);
    var pattern = getNextAncByClass(self, /\s?patternPanel/i);
    var fixForm = getNextAncByClass(self, /\s?fixForm/i);
    var schemaRoot = getNextAncByClass(self, /\s?schemaRoot/i);
    unselectQF(fixForm);
    
    self.checked = true;
    fixForm.setAttribute('aria-value', self.id);
    
    updateSelection([schemaRoot, pattern, fixListItem, svrlMsg]);
}

function unselectQF(self) {
    if (self == null) {
        return;
    }
    var name = self.tagName.toLowerCase();
    if (name == 'input') {
        
        var fixListItem = getNextAncByClass(self, /\s?fixItem/i);
        var svrlMsg = getNextAncByClass(self, /\s?svrl-msg/i);
        var pattern = getNextAncByClass(self, /\s?patternPanel/i);
        var schemaRoot = getNextAncByClass(self, /\s?schemaRoot/i);
        
        self.checked = false;
        
        updateSelection([fixListItem, svrlMsg, pattern, schemaRoot]);
    } else if (name == 'form') {
        var currId = self.getAttribute('aria-value');
        if (currId != null) {
            var currInput = document.getElementById(currId);
            self.setAttribute('aria-value', '');
            unselectQF(currInput);
        }
    } else {
        var form = getNextAncByClass(self, /\s?fixForm/i);
        if (form != null) {
            unselectQF(form);
        } else {
            var forms = self.getElementsByClassName('fixForm');
            for (var i = 0; i < forms.length; i++) {
                unselectQF(forms[i]);
            }
        }
    }
}
function updateSelection(els) {
    if (! Array.isArray(els)) {
        els =[els];
    }
    for (var i = 0; i < els.length; i++) {
        var el = els[i]
        if (! checkForSelection(el)) {
            el.className = el.className.replace(/(\s?selected)+/i, '');
        } else if (! el.className.match(/(\s?selected)+/i)) {
            el.className += " selected";
        }
    }
    checkForUnsetUE(els);
}

function checkForSelection(el) {
    if (el == null) {
        return false;
    }
    var forms;
    if (el.className.match(/\s?fixForm/i)) {
        forms =[el];
    } else {
        forms = el.getElementsByClassName('fixForm');
    }
    if (forms.length == 0) {
        forms =[getNextAncByClass(el, /\s?fixForm/i)];
    }
    for (var i = 0; i < forms.length; i++) {
        var val = forms[i].getAttribute('aria-value');
        if (val != null && val != '') {
            return true;
        }
    }
    return false;
}

function getAllSeletions(context) {
    var ids = new Array();
    if (context == null) {
        context = document;
    }
    var forms = context.getElementsByClassName('fixForm');
    
    for (var i = 0; i < forms.length; i++) {
        var val = forms[i].getAttribute('aria-value');
        if (val != null && val != '') {
            ids.push(val);
        }
    }
    return ids;
}

/*
 *
 * User Entries
 */

function showUserEntryMenu(context) {
    var fixItem = getNextAncByClass(context, /\s?fixItem/i);
    var userEntryMenu = fixItem.getElementsByClassName('userEntryMenu')[0];
    
    menu.showMenu = function () {
        if (! userEntryMenu.className.match(/(\s?selected)+/i)) {
            userEntryMenu.className += " selected";
        }
    }
    
    menu.hideMenu = function () {
        userEntryMenu.className = userEntryMenu.className.replace(/(\s?selected)+/i, '');
        
        menu.showMenu = null;
        menu.hideMenu = null;
    }
}

function getUnsetUserEntries(fixIds) {
    var unsetUEs = new Array();
    if (! Array.isArray(fixIds)) {
        fixIds =[fixIds];
    }
    $.each(fixIds, function (i, fixId) {
        var fixItem = $('#fixItem_' + fixId);
        $('.ue-tools.unsetUE', fixItem).each(function () {
            unsetUEs.push($(this));
        });
    })
    return unsetUEs;
}

function askForUserEntry(userEntries, callback) {
    /*    if(userEntries == null || userEntries.length == 0){
    userEntries = getUnsetUserEntries(getAllSeletions());
    }*/
    
    var $body = $('#askForUserEntryPage_body');
    var body = $body[0];
    body.innerHTML = '';
    if (userEntries == null) {
        $('.fixItem.selected').each(function () {
            var $fix = $(this);
            var $unsetUEs = $('.ue-tools.unsetUE .ue-input', $fix);
            if ($unsetUEs.length > 0) {
                $fix = $fix.clone(false);
                $fix.removeAttr('id').find("*").removeAttr("id");
                $('.userEntryMenu', $fix).remove();
                $fix.appendTo($body);
                $unsetUEs.each(function () {
                    var id = $(this)[0].id;
                    var $row = $('#ueRow_' + id);
                    var $a = $('<a />', {
                        "class": 'list-group-item ueItem'
                    });
                    $a.appendTo($body);
                    $row.appendTo($a);
                })
            }
        });
    } else {
        $.each(userEntries, function (i, cur) {
            var id = $('.ue-input', cur)[0].id;
            var row = $('#ueRow_' + id);
            body.appendChild(row[0]);
        });
    }
    
    cancel = function () {
        $('#askForUserEntryPage_body .ue-input').each(function () {
            var $ueInput = $(this);
            $ueInput[0].value = '';
            var $ueTools = $($ueInput.closest('.ue-tools'));
            $ueTools.addClass('unsetUE');
        });
    }
    
    gCancelFunction = function () {
        $('.ueRow', body).each(function (i, cur) {
            var id = cur.id;
            var wrapper = $('#' + id + '_wrapper')[0];
            wrapper.appendChild(cur);
        });
    }
    
    $('#askForUserEntryPage_btnOK')[0].onclick = function () {
        hideMessageBox();
        callback();
    };
    
    $('#askForUserEntryPage_btnCancel')[0].onclick = function () {
        cancel();
        gCancelFunction();
        hideMessageBox();
        checkForUnsetUE();
    };
    
    showInMessageBox($('#askForUserEntryPage')[0], 'sm');
}

function addUEChangeListener(context) {
    /*    var ueTools = getNextAncByClass(context, /\s?ue-tools/i);*/
    /*    var input = ueTools.getElementsByClassName('ue-input');*/
    
    var ueTools = $(context.closest('.ue-tools'));
    var fixItem = $(ueTools.closest('.fixItem'));
    var schemaRoot = $(fixItem.closest('#report'));
    var input = $('.ue-input', ueTools);
    
    
    input.bind('input', function () {
        
        if (this.value == '') {
            ueTools.addClass("unsetUE");
        } else {
            ueTools.removeClass("unsetUE");
        }
        checkForUnsetUE([fixItem, schemaRoot]);
    });
}

function setUEValue(context, value) {
    var ueTools = $(context.closest('.ue-tools'));
    var fixItem = $(ueTools.closest('.fixItem'));
    var schemaRoot = $(fixItem.closest('#report'));
    var input = $('.ue-input', ueTools);
    input[0].value = value;
    
    ueTools.removeClass("unsetUE");
    
    checkForUnsetUE([fixItem, schemaRoot]);
}

function checkForUnsetUE(context) {
    var $context = $(context);
    
    if (context == null) {
        $context = $('.fixItem').add('#report');
    }
    
    $context.each(function (i, cur) {
        var isUnset = false;
        cur = $(cur);
        if (cur.hasClass('fixItem')) {
            if (cur.hasClass('selected')) {
                isUnset = ($('.ue-tools.unsetUE', cur).length > 0);
            }
        } else if (cur.hasClass('schemaRoot') && $('#content #askForUserEntryPage').length > 0) {
            isUnset = ($('#askForUserEntryPage .ue-tools.unsetUE').length > 0);
        } else {
            isUnset = ($('.fixItem.selected .ue-tools.unsetUE', cur).length > 0);
        }
        
        
        if (isUnset) {
            cur.addClass("unsetUE");
        } else {
            cur.removeClass("unsetUE");
        }
    });
}

function isSelf(item, selector) {
    return item.closest(selector)[0] == item[0];
}

/*
 * XSLT
 *
 */
var errorList = new Array();
/*function pipe(xslts, inputDoc, callback, onError) {
var process = 0;
for (var i = 0; i < xslts.length; i++) {
var xslt = xslts[i];

errorList = new Array();
gOnError = onError;
xslt.setSuccess(function (proc) {
var nextXslt = xslts[process++];
if (errorList.length > 0) {
showErrors();
} else {
errorList = new Array();

if (nextXslt == null) {
callback(proc.getResultDocument());
} else {
nextXslt.transformToDocument(proc.getResultDocument());
}
}
});
}
xslts[process++].transformToDocument(inputDoc);
}*/

function pipe(xslts, inputDoc, callback, onError) {
    
    var proccessed = 0;
    var inbetweenCall = function (result) {
        var inputDoc;
        if (Node.DOCUMENT_NODE == result.nodeType) {
            inputDoc = result;
        } else {
            inputDoc = new Document();
            inputDoc.append(result);
        }
        
        if (proccessed < xslts.length) {
        
            console.log("Start step " + (proccessed + 1));
            transform(xslts[proccessed++], inputDoc, inbetweenCall, onError);
        } else {
            console.log("Finished pipe");
            callback(inputDoc);
        }
    }
    console.log("Start step " + (proccessed + 1));
    transform(xslts[proccessed++], inputDoc, inbetweenCall, onError);
    
    /* var xslt = xslts.pop();
    
    callback = function (prevResult) {
    if (errorList.length > 0) {
    showErrors();
    } else {
    errorList = new Array();
    transform(xslt, prevResult, nextCall, onError);
    }
    }
    if (xslts.length > 0) {
    pipe(xslts, inputDoc, callback, onError);
    } else {
    callback(inputDoc);
    }*/
}
function transform(xslt, inputDoc, callback, onError) {
    if (typeof xslt === 'string') {
        SaxonJS.transform({
            stylesheetLocation: xslt, sourceNode: inputDoc
        },
        callback);
    } else {
        xslt.setSuccess(function (proc) {
            callback(proc.getResultDocument());
        });
        xslt.transformToDocument(inputDoc);
    }
}

var gOnError = null;
function showErrors() {
    var message = '';
    for (var i = 0; i < errorList.length; i++) {
        message += errorList[i].message + '\n';
    }
    document.getElementById('errorMessage').innerHTML = message;
    showInMessageBox('errorPage');
    if (onError != null) {
        onError();
    }
}

/*
 *
 * Misc
 */

function getElementFromString(elOrElId) {
    if (typeof elOrElId === 'string') {
        return document.getElementById(elOrElId);
    } else {
        return elOrElId;
    }
}

function getNextAncByClass(self, regex) {
    if (self == null) {
        return null;
    }
    if (self.className.match(regex)) {
        return self;
    } else {
        return getNextAncByClass(self.parentElement, regex);
    }
}

function moveToBottom(el) {
    if (el != null) {
        var bottom = document.getElementById('bottom');
        bottom.appendChild(getElementFromString(el));
    }
}

function moveToContent(el) {
    if (el != null) {
        var content = document.getElementById('content');
        content.appendChild(getElementFromString(el));
    }
}

var mySpinner = null;
var gCancelFunction = null;
function startSpinner(message, steps, cancelFunction) {
    hideOrShowMenu();
    var progressBar = document.getElementById('spinnerMessageBar');
    progressBar.setAttribute('aria-valuenow', 0.0);
    progressBar.setAttribute('aria-valuemax', steps);
    progressBarStep();
    /*document.getElementById('spinnerCancelBtn').onclick = function () {
    if (cancelFunction != null) {
    cancelFunction();
    }
    stopSpinner();
    };*/
    gCancelFunction = cancelFunction;
    if (mySpinner == null) {
        document.getElementById('content').appendChild(document.getElementById('spinnerScreen'));
        document.getElementById('content').appendChild(document.getElementById('spinnerFrame'));
        mySpinner = new Spinner(spinOpts).spin(document.getElementById('spinner'));
    }
    if (message != null) {
        document.getElementById('spinnerMessageBar').innerHTML = message;
    }
}
function progressBarStep() {
    var progressBar = document.getElementById('spinnerMessageBar');
    var valueNow = parseInt(progressBar.getAttribute('aria-valuenow'));
    var valueMax = parseInt(progressBar.getAttribute('aria-valuemax'));
    progressBar.setAttribute('aria-valuenow', valueNow + 1);
    
    var progress = (valueNow + 1) / valueMax * 100;
    progressBar.style = "width:" + progress + "%";
}

function stopSpinner() {
    if (mySpinner != null) {
        mySpinner.stop();
        moveToBottom(document.getElementById('spinnerFrame'));
        moveToBottom(document.getElementById('spinnerScreen'));
        document.getElementById('spinnerMessageBar').innerHTML = "Waiting";
        mySpinner = null;
    }
    gCancelFunction = null;
}

var boxSizes =[ 'lg', 'md', 'sm'];
function showInMessageBox(el, boxSize) {
    boxSize = boxSize == null ? 'lg': boxSize;
    
    var msgBox = getElementFromString('messageBox');
    var cnclBtn = getElementFromString('messageBoxCancel');
    
    $(msgBox).addClass(boxSize);
    
    moveToContent('spinnerScreen');
    moveToContent(msgBox);
    if (el != null) {
        el = getElementFromString(el);
        msgBox.innerHTML = '';
        msgBox.appendChild(cnclBtn);
        msgBox.appendChild(el);
    }
}
function hideMessageBox() {
    if (gCancelFunction != null) {
        gCancelFunction();
    }
    moveToBottom('spinnerScreen');
    moveToBottom('messageBox');
    
    var msgBox = $('#messageBox');
    $.each(boxSizes, function () {
        msgBox.removeClass($(this));
    })
    
    stopSpinner();
}

function warnForUnselection(callback) {
    $('#commitUnselectionFixes_btnOK')[0].onclick = function () {
        hideMessageBox();
        callback();
    }
    $('#commitUnselectionFixes_btnCancel')[0].onclick = function () {
        hideMessageBox();
    }
    showInMessageBox($('#commitUnselectionFixes')[0], 'sm');
}

/*
 *
 * Interactive
 */
var cash = {
    isArray: false,
    el: null,
    className: '',
    title: '',
    onclick: null
};
function clearCash() {
    
    if (cash.el != null) {
        if (cash.isArray) {
            for (var i = 0; i < cash.el.length; i++) {
                cash.el[i].className = cash.className[i];
                cash.el[i].onclick = "";
                cash.el[i].title = cash.title[i];
                cash.el[i].onclick = cash.onclick[i];
            }
        } else {
            cash.el.className = cash.className;
            cash.el.onclick = "";
            cash.el.title = cash.title;
            cash.el.onclick = cash.onclick;
        }
        
        cash = {
            el: null, className: '', title: '', onclick: null, isArray: false
        };
    }
}

function markClass(id, className) {
    clearCash();
    
    els = document.getElementsByClassName(id);
    
    if (els != null) {
        cash.el = els;
        cash.title = new Array();
        cash.onclick = new Array();
        cash.className = new Array();
        cash.isArray = true;
        
        $.each(els, function () {
            var el = this;
            var $el = $(el);
            cash.title.push(el.title);
            cash.className.push(el.className);
            cash.onclick.push(el.onclick);
            
            $el.addClass(className);
            el.onclick = function () {
                clearCash();
            };
        });
    }
}

function mark(id, className) {
    var uncheck;
    if (cash.el != null) {
        uncheck = id == cash.el.id;
    }
    
    clearCash();
    
    if (! uncheck) {
        el = document.getElementById(id);
        
        cash.el = el;
        cash.title = el.title;
        cash.className = el.className;
        cash.onclick = el.onclick;
        
        el.className += " " + className;
        el.onclick = function () {
            clearCash();
        };
    }
}

var gUndoAction = null;
var gRedoAction = null;

function addToUndo(undoAction, redoAction) {
    gUndoAction = undoAction;
    gRedoAction = redoAction;
    activateButton('undoButton', function () {
        startSpinner('Undo QuickFix', 4);
        undo();
        stopSpinner();
    });
    deactivateButton('redoButton');
}

function activateButton(id, onclick) {
    var li = document.getElementById(id);
    li.className = li.className.replace(/(\s?inactive)+/i, '');
    li.onclick = onclick;
}

function deactivateButton(id) {
    var li = document.getElementById(id);
    li.className += " inactive";
    li.onclick = null;
}

function undo() {
    var doit = function () {
        activateButton('redoButton', function () {
            startSpinner('Redo QuickFix', 4);
            redo();
            stopSpinner();
        });
        deactivateButton('undoButton');
        gUndoAction();
    };
    if (getAllSeletions().length > 0) {
        warnForUnselection(doit);
    } else {
        doit();
    }
}

function redo() {
    var doit = function () {
        activateButton('undoButton', function () {
            undo();
        });
        deactivateButton('redoButton');
        gRedoAction();
    };
    if (getAllSeletions().length > 0) {
        warnForUnselection(doit);
    } else {
        doit();
    }
}

var showsChanges = true;
function hideShowChanges() {
    var markedEls = document.getElementsByClassName('marked');
    for (var i = 0; i < markedEls.length; i++) {
        var markedEl = markedEls[i];
        if (showsChanges) {
            markedEl.className += " invisibleMark";
        } else {
            markedEl.className = markedEl.className.replace(/(\s?invisibleMark)+/i, '');
        }
    }
    var btn = document.getElementById('hideShowChangeButton');
    btn.className = showsChanges ? 'glyphicon glyphicon-eye-open': 'glyphicon glyphicon-eye-close';
    btn.title = showsChanges ? 'Show change markers': 'Hide change markers';
    showsChanges = ! showsChanges;
}