<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" exclude-result-prefixes="#all" version="2.0">
    <xsl:import href="escali_web-impl_1_web-report.xsl"/>

    <xsl:param name="fileName" select="'Untitled.xml'"/>

    <xsl:key name="svrlMsg-location" match="svrl:successful-report | svrl:failed-assert" use="@location"/>

    <xsl:variable name="root" select="/"/>

    <xsl:template name="srcOnly">
        <xsl:param name="docRoot" select="/node()"/>
        <div id="preview">
            <div class="drop_zone_overwrite"/>
            <div class="alert alert-info" role="alert">
                <xsl:value-of select="$fileName"/>
            </div>
            <div id="previewContent">
                <xsl:variable name="wolines">
                    <xsl:apply-templates select="$docRoot" mode="es:preview"/>
                </xsl:variable>
                <xsl:apply-templates select="$wolines/node()" mode="es:addLineNumber"/>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="div" mode="es:addLineNumber">
        <xsl:if test="es:isNewLine(.)">
            <span class="lineNumber">
                <xsl:value-of select="count(preceding::div[es:isNewLine(.)]) + 1"/>
            </span>
        </xsl:if>
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>

            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>

    </xsl:template>

    <xsl:function name="es:isNewLine" as="xs:boolean">
        <xsl:param name="div" as="element()"/>
        <xsl:variable name="menuDivs" select="$div//div[@class/tokenize(., '\s') = 'fixMenu']/descendant-or-self::div"/>
        <xsl:sequence select="not($div//div except $menuDivs or $div/ancestor::div[@class/tokenize(., '\s') = 'fixMenu'])"/>
    </xsl:function>


    <xsl:template match="*[not(text()/normalize-space() != '')]/*" mode="es:preview" priority="50">
        <div class="blockElement">
            <xsl:next-match/>
        </div>
    </xsl:template>

    <xsl:template match="*[text()/normalize-space() != '']/* | *[text()/normalize-space() != '']" mode="es:preview" priority="10">
        <xsl:variable name="empty" select="not(node())"/>
        <xsl:call-template name="startTag">
            <xsl:with-param name="empty" select="$empty"/>
        </xsl:call-template>
        <xsl:if test="not($empty)">
            <xsl:choose>
                <xsl:when test="self::*[not(text()/normalize-space() != '')]/*">
                    <div class="hierarchy">
                        <xsl:apply-templates mode="#current"/>
                    </div>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates mode="#current"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:call-template name="endTag"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="*" mode="es:preview">
        <xsl:variable name="empty" select="not(node())"/>
        <div class="startTag">
            <xsl:call-template name="startTag">
                <xsl:with-param name="empty" select="$empty"/>
            </xsl:call-template>
        </div>
        <xsl:if test="not($empty)">
            <div class="hierarchy">
                <xsl:apply-templates mode="#current"/>
            </div>
            <div>
                <xsl:call-template name="endTag"/>
            </div>
        </xsl:if>
    </xsl:template>

    <xsl:template match="comment()" mode="es:preview" priority="10">
        
        <xsl:variable name="path" select="es:getNodePath(.)"/>
        <xsl:variable name="svrlMsg" select="key('svrlMsg-location', $path, $root)"/>
        <xsl:variable name="subStringSvrlMsg" select="$svrlMsg[@es:substring]"/>
        
        <span class="comment">
            <xsl:if test="$svrlMsg except $subStringSvrlMsg">
                <xsl:call-template name="createMarker">
                    <xsl:with-param name="class" select="'comment'"/>
                </xsl:call-template>
            </xsl:if>
            <span>&lt;!-- </span>
            <span>
                <xsl:choose>
                    <xsl:when test="$subStringSvrlMsg">
                        <xsl:call-template name="createSubstrings">
                            <xsl:with-param name="svrlMsgs" select="$subStringSvrlMsg"/>
                            <xsl:with-param name="textNode" select="."/>
                            <xsl:with-param name="path" select="$path" tunnel="yes"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="."/>
                    </xsl:otherwise>
                </xsl:choose>
            </span>
            <span>--&gt;</span>
            <xsl:call-template name="createQuickFix"/>
        </span>
    </xsl:template>

    <xsl:template match="processing-instruction()" mode="es:preview" priority="10">
        <xsl:variable name="path" select="es:getNodePath(.)"/>
        <xsl:variable name="svrlMsg" select="key('svrlMsg-location', $path, $root)"/>
        <xsl:variable name="subStringSvrlMsg" select="$svrlMsg[@es:substring]"/>

        <span class="pi">
            <xsl:if test="$svrlMsg except $subStringSvrlMsg">
                <xsl:call-template name="createMarker">
                    <xsl:with-param name="class" select="'pi'"/>
                </xsl:call-template>
            </xsl:if>
            <span>&lt;?</span>
            <span>
                <xsl:value-of select="name()"/>
            </span>
            <span>
                <xsl:text> </xsl:text>
                <xsl:choose>
                    <xsl:when test="$subStringSvrlMsg">
                        <xsl:call-template name="createSubstrings">
                            <xsl:with-param name="svrlMsgs" select="$subStringSvrlMsg"/>
                            <xsl:with-param name="textNode" select="."/>
                            <xsl:with-param name="path" select="$path" tunnel="yes"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="."/>
                    </xsl:otherwise>
                </xsl:choose>
            </span>
            <span>?></span>
            <xsl:call-template name="createQuickFix"/>
        </span>
    </xsl:template>

    <xsl:template match="processing-instruction()[name() = ('sqfc-start', 'sqfc-end')]" mode="es:preview" priority="15">
        <xsl:variable name="startOrEnd" select="
                if (name() = 'sqfc-start') then
                    ('start')
                else
                    ('end')"/>
        <xsl:variable name="deletionType" select="
                if (contains(., 'attribute-change')) then
                    ('attribute')
                else
                    if (../text()/normalize-space() = '') then
                        ('block')
                    else
                        ('inline')"/>
        <span class="changePi-{$startOrEnd} {$deletionType}" id="{$startOrEnd}_{.}"/>
    </xsl:template>

    <xsl:template match="text()" mode="es:preview" priority="10">

        <xsl:variable name="path" select="es:getNodePath(.)"/>
        <xsl:variable name="svrlMsg" select="key('svrlMsg-location', $path, $root)[@es:substring]"/>

        <xsl:choose>
            <xsl:when test="$svrlMsg">
                <span class="textNodeWitchInlineMsgs">
                    <xsl:call-template name="createSubstrings">
                        <xsl:with-param name="svrlMsgs" select="$svrlMsg"/>
                        <xsl:with-param name="textNode" select="."/>
                        <xsl:with-param name="path" select="$path" tunnel="yes"/>
                    </xsl:call-template>
                </span>
            </xsl:when>
            <xsl:otherwise>
                <span>
                    <xsl:call-template name="createMarker">
                        <xsl:with-param name="class" select="'textNode'"/>
                        <xsl:with-param name="path" select="$path" tunnel="yes"/>
                    </xsl:call-template>
                    <xsl:value-of select="."/>
                    <xsl:call-template name="createQuickFix"/>
                </span>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="createSubstrings">
        <xsl:param name="svrlMsgs" as="element()*"/>
        <xsl:param name="textNode" select="."/>
        <xsl:variable name="chars" select="es:tokenizeString($textNode)"/>
        <xsl:variable name="allMatchingPos" select="distinct-values($svrlMsgs/es:getSvrlMsgsSubstrings(.))"/>
        <xsl:for-each-group select="1 to count($chars)" group-adjacent="string-join($svrlMsgs[es:getSvrlMsgsSubstrings(.) = current()]/@es:id, '#:#')">
            <xsl:variable name="pos" select="current-group()"/>
            <span>
                <xsl:variable name="matchingMsg" select="$svrlMsgs[es:getSvrlMsgsSubstrings(.) = $pos]"/>
                <xsl:if test="$matchingMsg">
                    <xsl:call-template name="createMarker">
                        <xsl:with-param name="context" select="$textNode"/>
                        <xsl:with-param name="class" select="string-join($matchingMsg/@es:id, ' ')"/>
                        <xsl:with-param name="svrlMsg" select="$matchingMsg"/>
                    </xsl:call-template>
                </xsl:if>
                <xsl:value-of select="$chars[position() = $pos]" separator=""/>
            </span>
        </xsl:for-each-group>
    </xsl:template>

    <xsl:function name="es:tokenizeString" as="xs:string*">
        <xsl:param name="string" as="xs:string?"/>
        <xsl:if test="$string">
            <xsl:sequence select="
                    for $cp in string-to-codepoints($string)
                    return
                        codepoints-to-string($cp)"/>
        </xsl:if>
    </xsl:function>

    <xsl:function name="es:getSvrlMsgsSubstrings" as="xs:integer*">
        <xsl:param name="svrlMsgs" as="element()"/>
        <xsl:variable name="startEnd" select="$svrlMsgs/@es:substring/tokenize(., '\s')"/>
        <xsl:variable name="start" select="xs:integer($startEnd[1])"/>
        <xsl:variable name="end" select="xs:integer($startEnd[2]) - 1"/>
        <xsl:sequence select="$start to $end"/>
    </xsl:function>

    <xsl:template name="startTag">
        <xsl:param name="empty" select="false()" as="xs:boolean"/>
        <span>
            <xsl:call-template name="createMarker">
                <xsl:with-param name="class" select="'startTag'"/>
            </xsl:call-template>
            <xsl:text>&lt;</xsl:text>
            <xsl:value-of select="name()"/>
            <xsl:apply-templates select="@*" mode="es:preview"/>
            <xsl:if test="$empty">/</xsl:if>
            <xsl:text>&gt;</xsl:text>
            <xsl:call-template name="createQuickFix"/>
        </span>
    </xsl:template>


    <xsl:template name="endTag">
        <span class="endTag">
            <xsl:text>&lt;/</xsl:text>
            <xsl:value-of select="name()"/>
            <xsl:text>&gt;</xsl:text>
        </span>
    </xsl:template>

    <xsl:template match="@*" mode="es:preview">
        
        <xsl:variable name="path" select="es:getNodePath(.)"/>
        <xsl:variable name="svrlMsg" select="key('svrlMsg-location', $path, $root)"/>
        <xsl:variable name="subStringSvrlMsg" select="$svrlMsg[@es:substring]"/>
        
        <span>
            <xsl:if test="$svrlMsg except $subStringSvrlMsg">
                <xsl:call-template name="createMarker">
                    <xsl:with-param name="class" select="'attribute'"/>
                </xsl:call-template>
            </xsl:if>
            <xsl:text> </xsl:text>
            <xsl:value-of select="name()"/>
            <xsl:text>=</xsl:text>
        </span>
        <span class="attributeValue">
            <xsl:text>"</xsl:text>
            <xsl:choose>
                <xsl:when test="$subStringSvrlMsg">
                    <xsl:call-template name="createSubstrings">
                        <xsl:with-param name="svrlMsgs" select="$subStringSvrlMsg"/>
                        <xsl:with-param name="textNode" select="."/>
                        <xsl:with-param name="path" select="$path" tunnel="yes"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text>"</xsl:text>
        </span>
        <xsl:call-template name="createQuickFix"/>
    </xsl:template>

    <xsl:template name="createMarker">
        <xsl:param name="class"/>
        <xsl:param name="context" select="." as="node()"/>
        <xsl:param name="path" select="es:getNodePath($context)" tunnel="yes"/>

        <xsl:param name="svrlMsg" select="key('svrlMsg-location', $path, $root)"/>

        <xsl:variable name="maxRole" select="max($svrlMsg/@role/xs:double(.))"/>

        <xsl:variable name="sortedSVRLMsg" as="element()*">
            <xsl:for-each select="$svrlMsg">
                <xsl:sort select="@role" data-type="number" order="descending"/>
                <es:msg>
                    <xsl:value-of select="concat('[', @es:roleLabel, ']: ', svrl:text)"/>
                </es:msg>
            </xsl:for-each>
        </xsl:variable>

        <xsl:variable name="role" select="
                $svrlMsg[@role = $maxRole][1]
                /@es:roleLabel
                /es:simplyfyRole(.)"/>
        <xsl:attribute name="class" select="($class, concat($role, 'Line'))"/>
        <xsl:attribute name="id" select="es:generate-id($context)"/>
        <xsl:if test="$svrlMsg">
            <xsl:attribute name="title" select="$sortedSVRLMsg" separator="&#10;"/>
        </xsl:if>
        <span id="jumpTo_{es:generate-id($context)}" class="jumpToAncor"/>
    </xsl:template>

    <xsl:template name="createQuickFix">
        <xsl:param name="path" select="es:getNodePath(.)"/>
        <xsl:param name="svrlMsg" select="key('svrlMsg-location', $path, $root)"/>
        <xsl:param name="parentShowBlock" select="'this.parentElement'"/>
        <xsl:if test="$svrlMsg/sqf:fix">
            <xsl:variable name="svrlMsg" select="$svrlMsg[sqf:fix]"/>
            <xsl:variable name="id" select="concat('fixes_', generate-id(), '_', $svrlMsg[1]/@es:id)"/>
            <span class="fixPin">
                <span onclick="implementMenu([document.getElementById('{$id}'), {$parentShowBlock}])" class="link">
                    <span class="glyphicon glyphicon-flash"/>
                </span>
                <div class="fixMenu panel panel-default" id="{$id}">
                    <xsl:choose>
                        <xsl:when test="count($svrlMsg) gt 1">
                            <xsl:for-each select="$svrlMsg">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <xsl:copy-of select="es:getIconByMsg(.)"/>
                                        <span>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="svrl:text"/>
                                        </span>
                                    </div>
                                    <div class="list-group">
                                        <xsl:call-template name="createQuickFixList"/>
                                    </div>
                                </div>
                            </xsl:for-each>
                        </xsl:when>
                        <xsl:otherwise>
                            <div class="list-group">
                                <xsl:call-template name="createQuickFixList">
                                    <xsl:with-param name="svrlMsg" select="$svrlMsg"/>
                                </xsl:call-template>
                            </div>
                        </xsl:otherwise>
                    </xsl:choose>

                </div>
            </span>
        </xsl:if>
    </xsl:template>

    <xsl:template name="createQuickFixList">
        <xsl:param name="svrlMsg" select="."/>
        <xsl:param name="withRadioBtn" select="false()" as="xs:boolean"/>
        <xsl:param name="msgId" select="''"/>
        <xsl:for-each select="$svrlMsg/sqf:fix">

            <xsl:variable name="linkText">
                <xsl:variable name="icon" select="
                        if (@role = 'add') then
                            'plus'
                        else
                            if (@role = 'replace') then
                                'refresh'
                            else
                                if (@role = 'delete') then
                                    'remove'
                                else
                                    'flash'"/>
                <span class="glyphicon glyphicon-{$icon} qfIcon {@role}"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="sqf:description/sqf:title"/>
            </xsl:variable>
            <xsl:variable name="isDefaultClass" select="
                    if (@default = 'true') then
                        (' isDefaultFix')
                    else
                        ('')"/>
            <xsl:choose>
                <xsl:when test="$withRadioBtn">

                    <a class="list-group-item link fixItem{$isDefaultClass}" id="fixItem_{@id}">
                        <xsl:attribute name="title" select="sqf:description/svrl:text" separator="&#10;"/>
                        <span class="directLink" onclick="runQuickFix('{@id}')">
                            <xsl:copy-of select="$linkText"/>
                        </span>
                        <span class="qfTools">
                            <span class="unselectFix link" onclick="unselectQF(this)" title="Clear selection">
                                <span class="glyphicon glyphicon-eject"/>
                            </span>
                            <xsl:if test="sqf:user-entry">
                                <span style="padding-left:.5em;"/>
                                <span class="userEntry link" title="Set User Entries" onclick="showUserEntryMenu(this)">
                                    <span class="glyphicon glyphicon-edit"/>
                                </span>
                            </xsl:if>
                            <span style="padding-left:.5em;"> </span>
                            <input type="radio" name="{$msgId}" value="{@id}" id="{@id}" aria-label="Use this QuickFix" onclick="selectQF(this)"/>
                        </span>
                        <xsl:if test="sqf:user-entry">
                            <div class="panel panel-default userEntryMenu" id="userEntry-{@id}" onclick="supressMenu()">
                                <div class="panel-heading">
                                    <strong>User Entries</strong>
                                </div>
                                <div class="panel-body">
                                    <xsl:apply-templates select="sqf:user-entry" mode="es:report"/>
                                </div>
                            </div>
                        </xsl:if>
                    </a>
                </xsl:when>
                <xsl:otherwise>
                    <a class="list-group-item link" onclick="runQuickFix('{@id}')">
                        <xsl:attribute name="title" select="sqf:description/svrl:text" separator="&#10;"/>
                        <xsl:copy-of select="$linkText"/>
                    </a>
                    <xsl:apply-templates select="sqf:user-entry" mode="es:preview"/>
                </xsl:otherwise>
            </xsl:choose>

        </xsl:for-each>

    </xsl:template>

    <xsl:template match="sqf:user-entry" mode="es:preview" priority="100"/>



    <xsl:function name="es:generate-id">
        <xsl:param name="node" as="node()"/>
        <xsl:variable name="baseId" select="generate-id($node)"/>
        <xsl:variable name="prefix" select="
                if (es:getNodeType($node) = 'attribute') then
                    $node/replace(name(), ':', '_')
                else
                    ''"/>
        <xsl:sequence select="concat($prefix, $baseId)"/>
    </xsl:function>

</xsl:stylesheet>
