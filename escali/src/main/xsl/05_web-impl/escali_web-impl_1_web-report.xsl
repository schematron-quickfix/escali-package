<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:idPkg="http://ns.adobe.com/AdobeInDesign/idml/1.0/packaging" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" exclude-result-prefixes="xs" version="2.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:saxon="http://saxon.sf.net/">
    <xsl:include href="../01_compiler/escali_compiler_0_functions.xsl"/>
    <xsl:output indent="no" method="xml"/>

    <xsl:param name="xmlInstancePath" select="/svrl:schematron-output/@es:instance" as="xs:string"/>
    <xsl:param name="xmlInstanceDoc" select="doc(resolve-uri($xmlInstancePath))" as="document-node()"/>
    <xsl:param name="schemaName" select="''"/>

    <xsl:key name="node-nodePath" match="node() | @*" use="es:getNodePath(.)"/>

    <xsl:variable name="preview">
        <div id="previewContent">
            <xsl:call-template name="srcOnly">
                <xsl:with-param name="docRoot" select="$xmlInstanceDoc/node()"/>
            </xsl:call-template>
        </div>
    </xsl:variable>

    <!-- 
        copies all nodes:
    -->
    <xsl:variable name="dateFormatDe" select="'[D00].[M00].[Y0000]'"/>
    <xsl:variable name="timeFormatDe" select="'[H00]:[m00]:[s00] Uhr'"/>
    <xsl:template match="node() | @*" mode="#all" priority="-15">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/svrl:schematron-output">
        <div style="display:none">
            <div id="report" class="schemaRoot">
                <div class="drop_zone_overwrite"/>
                <div class="alert alert-info schema-head" role="alert">
                    <span>
                        <xsl:choose>
                            <xsl:when test="@title">
                                <xsl:value-of select="@title"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>Escali Web Report of document </xsl:text>
                                <xsl:value-of select="$fileName"/>
                                <xsl:text> validated by schema </xsl:text>
                                <xsl:value-of select="$schemaName"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </span>
                    <xsl:variable name="ue-message-onclick">askForUserEntry(null, function(){hideMessageBox();})</xsl:variable>
                    <xsl:variable name="schema-tools">
                        <span class="ejectBtn">
                            <span title="Clear all seletion of this pattern" class="link eject" onclick="unselectQF(getPatternOrRoot(this)[0]);">
                                <span class="glyphicon glyphicon-eject"/>
                            </span>
                        </span>
                        <span class="runOrUE">
                            <span class="UE-message link" title="There are QuickFixes selected with unset User Entries" onclick="{$ue-message-onclick}">
                                <span class="glyphicon glyphicon-edit"/>
                            </span>
                            <span class="link runButton" title="Run all selected QuickFixes" onclick="runQuickFix()">
                                <span class="glyphicon glyphicon-play-circle"/>
                            </span>
                        </span>
                    </xsl:variable>
                    <span class="schema-tools tools">
                        <xsl:copy-of select="$schema-tools"/>
                    </span>
                    <span class="global-tools tools">
                        <xsl:copy-of select="$schema-tools"/>
                    </span>
                </div>
                <xsl:for-each-group select="/svrl:schematron-output/svrl:*" group-starting-with="svrl:active-pattern">
                    <xsl:variable name="svrlMsgs" select="current-group()/(self::svrl:successful-report | self::svrl:failed-assert)"/>
                    <div class="panel panel-default patternPanel">
                        <div class="panel-heading patternPanel">
                            <span>
                                <xsl:value-of select="
                                        if (@title) then
                                            (@title)
                                        else
                                            ('Pattern', position())" separator=" "/>
                            </span>
                            <span class="patternPanel errorCounter">
                                <xsl:for-each-group select="$svrlMsgs" group-by="@es:roleLabel">
                                    <xsl:variable name="svrlMsgs" select="current-group()"/>
                                    <xsl:variable name="count" select="count($svrlMsgs)"/>
                                    <xsl:variable name="role" select="es:simplyfyRole(current-grouping-key())"/>
                                    <xsl:variable name="labelClass" select="
                                            if ($role = ('error', 'fatal')) then
                                                ('danger')
                                            else
                                                replace($role, 'warn', 'warning')"/>
                                    <span class="label label-{$labelClass}">
                                        <xsl:if test="$role = 'fatal'">
                                            <xsl:attribute name="style" select="'background-color: #900;'"/>
                                        </xsl:if>
                                        <xsl:value-of select="($count, ' ', current-grouping-key(), 's'[$count gt 1])" separator=""/>
                                    </span>
                                </xsl:for-each-group>

                                <xsl:if test="not($svrlMsgs)">
                                    <span class="label label-success">No messages</span>
                                </xsl:if>
                            </span>
                            <xsl:if test="$svrlMsgs">
                                <span class="pattern-tools tools">
                                    <span title="Clear all seletion of this pattern" class="link eject" onclick="unselectQF(getNextAncByClass(this, 'panel panel-default patternPanel'));">
                                        <span class="glyphicon glyphicon-eject"/>
                                    </span>
                                    <xsl:if test="$svrlMsgs[sqf:fix/@default = 'true']">
                                        <span title="Select all defaults" class="link" onclick="selectDefaults(this)">
                                            <span class="glyphicon glyphicon-screenshot"/>
                                        </span>
                                    </xsl:if>
                                    <span class="patternPanel expandCollapseBtn link">
                                        <span title="Collapse pattern" class="expandPattern" onclick="hidePatternMenu(this)">
                                            <span class="glyphicon glyphicon-chevron-up"/>
                                        </span>
                                        <span title="Expand all Messages" class="expandAllMsgs" onclick="showQFMenu(this, true)">
                                            <script>addDoubleArrow('down');</script>
                                        </span>
                                        <span title="Collapse all Messages" class="collapseAllMsgs" onclick="showQFMenu(this, false)">
                                            <script>addDoubleArrow('up');</script>
                                        </span>
                                    </span>
                                </span>
                            </xsl:if>
                        </div>

                        <ol class="list-group patternBody">
                            <xsl:for-each select="$svrlMsgs">

                                <xsl:variable name="contextLoc" select="@location"/>
                                <xsl:variable name="context" select="$xmlInstanceDoc/key('node-nodePath', $contextLoc)"/>
                                <xsl:variable name="context-id" select="$context/es:generate-id(.)"/>
                                <xsl:variable name="contextType" select="$context/es:getNodeType(.)"/>
                                <xsl:variable name="role" select="@es:roleLabel/es:simplyfyRole(.)"/>
                                <xsl:variable name="fixId" select="concat('fixes_', generate-id(), '_', @es:id)"/>

                                <li class="list-group-item svrl-msg">

                                    <div class="panel panel-default svrl-msg">
                                        <div class="panel-body svrl-msg">
                                            <xsl:copy-of select="es:getIconByMsg(.)"/>
                                            <span id="msg-{generate-id()}">
                                                <xsl:text> </xsl:text>
                                                <xsl:value-of select="svrl:text"/>
                                            </span>
                                            <span class="msg-tools tools">
                                                <xsl:variable name="markFunction" select="
                                                        if (@es:substring and not($contextType = 'element')) then
                                                            ('markClass')
                                                        else
                                                            ('mark')"/>
                                                <xsl:variable name="markId" select="
                                                        if (@es:substring and not($contextType = 'element')) then
                                                            (@es:id)
                                                        else
                                                            ($context-id)"/>
                                                <a href="#jumpTo_{$context-id}" itemref="{$context-id}" onclick="{$markFunction}('{$markId}', '{$role}Mark')" title="Show error in document">
                                                    <span class="glyphicon glyphicon-search"/>
                                                </a>
                                                <xsl:if test="sqf:fix">
                                                    <span class="fixPin">
                                                        <span onclick="showQFMenu(this, '{$fixId}')" class="link" title="Show QuickFixes">
                                                            <span class="glyphicon glyphicon-chevron-down"/>
                                                        </span>
                                                    </span>
                                                </xsl:if>
                                            </span>
                                        </div>
                                        <xsl:if test="sqf:fix">
                                            <xsl:call-template name="createMultiQuickFixMenu">
                                                <xsl:with-param name="id" select="$fixId"/>
                                                <xsl:with-param name="svrlMsg" select="."/>
                                            </xsl:call-template>
                                            <!--<xsl:call-template name="createQuickFix">
                                                <xsl:with-param name="path" select="@location"/>
                                                <xsl:with-param name="svrlMsg" select="."/>
                                                <xsl:with-param name="parentShowBlock" select="'this.parentElement.parentElement'"/>
                                            </xsl:call-template>-->
                                        </xsl:if>
                                    </div>
                                </li>
                            </xsl:for-each>
                        </ol>
                    </div>
                </xsl:for-each-group>
            </div>
            <xsl:copy-of select="$preview"/>
        </div>
    </xsl:template>
    <xsl:function name="es:getIconByMsg" as="element()?">
        <xsl:param name="msg" as="element()?"/>
        <xsl:if test="$msg">
            <xsl:variable name="role" select="$msg/@es:roleLabel/es:simplyfyRole(.)"/>
            <span class="{$iconClasses[@role = $role]/@class}"/>
        </xsl:if>
    </xsl:function>

    <xsl:variable name="iconClasses" as="element()*">
        <i class="glyphicon glyphicon-info-sign infoIcon" role="info"/>
        <i class="glyphicon glyphicon-exclamation-sign warnIcon" role="warn"/>
        <i class="glyphicon glyphicon-remove-sign errorIcon" role="error"/>
        <i class="glyphicon glyphicon-remove-sign fatalIcon" role="fatal"/>
    </xsl:variable>

    <xsl:function name="es:simplyfyRole" as="xs:string?">
        <xsl:param name="role" as="xs:string?"/>
        <xsl:sequence select="es:simplyfyRole($role, (), ())"/>
    </xsl:function>
    <xsl:function name="es:simplyfyRole" as="xs:string?">
        <xsl:param name="role" as="xs:string?"/>
        <xsl:param name="prefix" as="xs:string?"/>
        <xsl:param name="suffix" as="xs:string?"/>
        <xsl:variable name="role" select="replace(replace($role, 'information', 'info'), 'warning', 'warn')[$role]"/>
        <xsl:sequence select="concat($prefix, $role, $suffix)[$role]"/>
    </xsl:function>

    <xsl:template name="createMultiQuickFixMenu">
        <xsl:param name="id" required="yes"/>
        <xsl:param name="svrlMsg" required="yes"/>

        <div class="list-group fixMenu" id="{$id}">

            <form class="fixForm">
                <xsl:call-template name="createQuickFixList">
                    <xsl:with-param name="svrlMsg" select="$svrlMsg"/>
                    <xsl:with-param name="msgId" select="$id"/>
                    <xsl:with-param name="withRadioBtn" select="true()"/>
                </xsl:call-template>
            </form>
        </div>

    </xsl:template>

    <xsl:template match="sqf:user-entry" mode="es:report">
        <xsl:variable name="param" select="sqf:param"/>
        <div id="ueRow_{$param/@param-id}_wrapper">
            <div class="row ueRow" id="ueRow_{$param/@param-id}">
                <div class="col-md-9 col">
                    <div class="ue-description">
                        <span>
                            <xsl:value-of select="sqf:description/sqf:title"/>
                        </span>
                    </div>
                </div>
                <div class="col-md-3 col">
                    <div class="input-group input-group-sm ue-tools unsetUE">
                        <xsl:variable name="defaultValue" select="($param/text(), 'value')[1]"/>
                        <span class="input-group-addon link" onclick="setUEValue(this, '{$defaultValue}')" title="Use default value">
                            <span class="glyphicon glyphicon-edit"/>
                        </span>
                        <input type="text" class="form-control ue-input" aria-describedby="basic-addon1" id="{$param/@param-id}">
                            <xsl:attribute name="placeholder" select="$defaultValue"/>
                            <script type="application/javascript">addUEChangeListener(document.currentScript);</script>
                        </input>
                    </div>
                </div>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>
