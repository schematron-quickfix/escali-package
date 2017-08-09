<?xml version="1.0" encoding="UTF-8"?>
<!--  
    Copyright (c) 2014 Nico Kutscherauer
        
    This file is part of Escali Schematron.
    
    Escali Schematron is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Escali Schematron is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Escali Schematron.  If not, see http://www.gnu.org/licenses/gpl-3.0.

    -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:xsm="http://www.schematron-quickfix.com/manipulator/process" xmlns:sch="http://purl.oclc.org/dsdl/schematron" exclude-result-prefixes="xs xd sch xsm" version="2.0">

    <xd:doc scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Nov 19, 2013</xd:p>
            <xd:p><xd:b>Author:</xd:b> Nico Kutscherauer</xd:p>
        </xd:desc>
    </xd:doc>


    <xd:doc scope="version">
        <xd:desc>
            <xd:p>Version information</xd:p>
            <xd:ul>
                <xd:li>
                    <xd:p>2014-03-14</xd:p>
                    <xd:ul>
                        <xd:li>
                            <xd:p>publishing version</xd:p>
                        </xd:li>
                    </xd:ul>
                </xd:li>
            </xd:ul>
        </xd:desc>
    </xd:doc>



    <xsl:key name="phaseByActivePatternId" match="sch:phase" use="es:getRefPhases(.)/sch:active/@pattern"/>
    <xsl:key name="phaseByInactivePatternId" match="sch:phase" use="es:getRefPhases(.)/es:inactive/@pattern"/>

    <xsl:key name="nodeById" match="*[@id | @xml:id]" use="@id | @xml:id"/>

    <!--  
    returns for a $pattern if it is active (true) or inactive (false)
    the $pattern is active if:
        1. the $phase is '#ALL' 
            or $pattern is abstract or
        2. the $phase contains not the $deactivatePhase 
            and the $phaseEl contains not sch:active (es extension) or
        3. the $pattern has an @id and a $callingPhase which contains the $phase
   otherwise the $pattern is inactive

    -->
    <xsl:function name="es:isActive" as="xs:boolean">
        <xsl:param name="pattern" as="node()"/>
        <xsl:param name="phase" as="xs:string+"/>
        <xsl:variable name="phaseEl" select="key('nodeById', $phase, root($pattern))"/>
        <xsl:variable name="phaseEl" select="es:getRefPhases($phaseEl)"/>
        <xsl:variable name="callingPhase" select="key('phaseByActivePatternId', $pattern/@id, root($pattern))"/>
        <xsl:variable name="deactivatePhase" select="key('phaseByInactivePatternId', $pattern/@id, root($pattern))"/>
        <xsl:choose>
            <xsl:when test="$phase = '#ALL' or $pattern/@abstract = 'true'">
                <xsl:sequence select="es:getActiveDefault($pattern, true())"/>
            </xsl:when>
            <xsl:when test="$deactivatePhase/@id = $phase">
                <xsl:sequence select="false()"/>
            </xsl:when>
            <xsl:when test="$phaseEl and not($phaseEl/sch:active) and $phaseEl/es:inactive">
                <xsl:sequence select="es:getActiveDefault($pattern, true())"/>
            </xsl:when>
            <xsl:when test="not($pattern/@id) or not($callingPhase)">
                <xsl:sequence select="es:getActiveDefault($pattern, false())"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:sequence select="$callingPhase/@id = $phase"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="es:getActiveDefault" as="xs:boolean">
        <xsl:param name="pattern" as="element()"/>
        <xsl:param name="default" as="xs:boolean"/>
        <xsl:sequence select="
                if ($pattern/@abstract = 'true')
                then
                    (true())
                else
                    if ($pattern/@es:active[. != 'auto'])
                    then
                        ($pattern/@es:active = 'true')
                    else
                        ($default)"/>
    </xsl:function>
    <!--  
        es extension:
        resolves references from phases to other phases
        respects the es:phase elements (with @ref) as childs of sch:phase
    -->
    <xsl:function name="es:getRefPhases" as="node()*">
        <xsl:param name="phase" as="element(sch:phase)*"/>

        <xsl:variable name="refPhases" select="
                for $ph
                in $phase
                return
                    key('nodeById', $ph/es:phase/@ref, root($ph))"/>
        <xsl:variable name="newPhases" select="$refPhases except $phase"/>
        <xsl:variable name="refNewPhases" select="
                if (exists($newPhases)) then
                    (es:getRefPhases(($newPhases union $phase)) except ($newPhases,
                    $phase))
                else
                    ()"/>
        <xsl:sequence select="
                $phase,
                $newPhases,
                $refNewPhases"/>
    </xsl:function>




    <xsl:function name="es:getNodeType" as="xs:string">
        <xsl:param name="node" as="node()+"/>
        <xsl:variable name="types" select="
                $node/(
                if (. instance of attribute()) then
                    ('attribute')
                else
                    if (. instance of element()) then
                        ('element')
                    else
                        if (. instance of text()) then
                            ('text')
                        else
                            if (. instance of comment()) then
                                ('comment')
                            else
                                if (. instance of processing-instruction()) then
                                    ('processing-instruction')
                                else
                                    if (. instance of document-node()) then
                                        ('document')
                                    else
                                        ('UNKNOWN_TYPE'))"/>
        <xsl:variable name="types" select="distinct-values($types)"/>
        <xsl:sequence select="
                if (count($types) gt 1) then
                    ('mixed')
                else
                    ($types)"/>
    </xsl:function>

    <xsl:function name="es:getNodePath" as="xs:string">
        <xsl:param name="node" as="node()"/>
        <xsl:sequence select="es:getNodePath($node, false())"/>
    </xsl:function>

    <xsl:function name="es:getNodePath" as="xs:string">
        <xsl:param name="node" as="node()"/>
        <xsl:param name="base-uri-scope" as="xs:boolean"/>


        <xsl:variable name="base-uri" select="
                if ($node) then
                    es:base-uri($node)
                else
                    ('')"/>

        <xsl:variable name="scope-nodes" select="$node/ancestor-or-self::node()[not($base-uri-scope) or es:base-uri(.) = $base-uri] except root($node)"/>

        <xsl:variable name="ancestor" as="xs:string*">
            <xsl:for-each select="$scope-nodes">
                <xsl:variable name="type" select="es:getNodeType(.)"/>
                <xsl:choose>
                    <xsl:when test="$type = 'attribute'">
                        <xsl:call-template name="es:makeElementXPath">
                            <xsl:with-param name="axis">@</xsl:with-param>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:when test="$type = 'element'">
                        <xsl:call-template name="es:makeElementXPath"/>
                    </xsl:when>
                    <xsl:when test="$type = ('text', 'comment', 'processing-instruction')">
                        <xsl:call-template name="es:makeElementXPath">
                            <xsl:with-param name="type">
                                <xsl:value-of select="$type"/>
                                <xsl:text>()</xsl:text>
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise/>
                </xsl:choose>
            </xsl:for-each>
        </xsl:variable>
        <xsl:value-of select="concat('/', string-join($ancestor, '/'))"/>

    </xsl:function>

    <xsl:template name="es:makeElementXPath" as="xs:string">
        <xsl:param name="axis" select="''"/>
        <xsl:param name="type">*</xsl:param>
        <xsl:variable name="name" select="name()"/>
        <xsl:variable name="local-name" select="local-name()"/>
        <xsl:variable name="ns-uri" select="namespace-uri()"/>
        <xsl:variable name="result" as="xs:string*">
            <xsl:choose>
                <xsl:when test="$type = '*'">
                    <xsl:value-of select="$axis"/>
                    <xsl:text>*:</xsl:text>
                    <xsl:value-of select="local-name()"/>
                    <xsl:text>[namespace-uri()='</xsl:text>
                    <xsl:value-of select="namespace-uri()"/>
                    <xsl:text>']</xsl:text>
                </xsl:when>
                <xsl:when test="$type = 'processing-instruction()'">
                    <xsl:text>processing-instruction()[local-name()='</xsl:text>
                    <xsl:value-of select="local-name()"/>
                    <xsl:text>']</xsl:text>
                </xsl:when>
                <xsl:when test="
                        $type = ('comment()',
                        'text()')">
                    <xsl:value-of select="$type"/>
                </xsl:when>
            </xsl:choose>
            <xsl:text>[</xsl:text>
            <xsl:choose>
                <xsl:when test="$type = 'text()'">
                    <xsl:value-of select="count(preceding-sibling::text()) + 1"/>
                </xsl:when>
                <xsl:when test="$type = 'comment()'">
                    <xsl:value-of select="count(preceding-sibling::comment()) + 1"/>
                </xsl:when>
                <xsl:when test="$type = 'processing-instruction()'">
                    <xsl:value-of select="count(preceding-sibling::processing-instruction()[local-name() = $local-name]) + 1"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="count(preceding-sibling::*[local-name() = $local-name][namespace-uri() = $ns-uri]) + 1"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text>]</xsl:text>
        </xsl:variable>
        <xsl:value-of select="$result" separator=""/>
    </xsl:template>

    <xsl:function name="es:quoteRegex" as="xs:string">
        <xsl:param name="regex" as="xs:string"/>
        <xsl:variable name="quoted" as="xs:string*">
            <xsl:analyze-string select="$regex" regex="[-\[\]()*+?.,\\^$|#]">
                <xsl:matching-substring>
                    <xsl:text>\</xsl:text>
                    <xsl:value-of select="."/>
                </xsl:matching-substring>
                <xsl:non-matching-substring>
                    <xsl:value-of select="."/>
                </xsl:non-matching-substring>
            </xsl:analyze-string>
        </xsl:variable>
        <xsl:sequence select="es:string-join($quoted, '')"/>
    </xsl:function>

    <xsl:function name="es:string-join">
        <xsl:param name="seq"/>
        <xsl:param name="sep" as="xs:string"/>
        <xsl:variable name="res">
            <xsl:for-each select="$seq">
                <xsl:value-of select="."/>
                <xsl:if test="position() != last()">
                    <xsl:value-of select="$sep"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <xsl:value-of select="$res" separator=""/>
    </xsl:function>

    <xsl:function name="es:selectValue" as="node()*">
        <xsl:param name="select"/>
        <xsl:choose>
            <xsl:when test="$select[1] instance of node()">
                <!--<xsl:apply-templates select="$select" mode="es:selectValue">
                    <xsl:with-param name="xsm:xml-save-mode" select="false()" as="xs:boolean" tunnel="yes"/>
                </xsl:apply-templates>-->
                <xsl:copy-of select="$select"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$select"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:template match="/" mode="es:selectValue" priority="10">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="node()" mode="es:selectValue">
        <xsl:apply-templates select="."/>
    </xsl:template>

    <xsl:variable name="processingNamespaces" select="
            (
            'http://www.w3.org/XML/1998/namespace',
            'http://purl.oclc.org/dsdl/schematron',
            'http://www.schematron-quickfix.com/validator/process',
            'http://www.schematron-quickfix.com/svrl/extension',
            'http://www.escali.schematron-quickfix.com/',
            'http://www.w3.org/1999/XSL/Transform',
            'http://www.w3.org/2001/XMLSchema-instance')"/>


    <xsl:variable name="namespace">
        <xsl:for-each select="/sch:schema/sch:ns">
            <es:ns pre="{@prefix}" uri="{@uri}" schemaDefined="true"/>
        </xsl:for-each>
    </xsl:variable>

    <xsl:template name="namespace">
        <xsl:param name="context" select="."/>
        <xsl:variable name="ns">
            <xsl:if test="$context/namespace-uri() != ''">
                <es:ns pre="" uri="http://www.escali.schematron-quickfix.com/null-namespace"/>
            </xsl:if>
            <xsl:for-each select="$context/namespace::*">
                <es:ns pre="{name()}" uri="{.}"/>
            </xsl:for-each>
            <xsl:copy-of select="$namespace/es:ns"/>
        </xsl:variable>
        <xsl:for-each-group select="$ns/es:ns" group-by="@pre">
            <xsl:variable name="uri" select="current-group()[last()]/@uri"/>
            <xsl:if test="not($uri = $processingNamespaces) or current-group()/@schemaDefined = 'true'">
                <xsl:namespace name="{current-grouping-key()}" select="$uri"/>
            </xsl:if>
        </xsl:for-each-group>
        <!--	<xsl:copy-of select="$ns/*"/>-->
    </xsl:template>

    <xsl:function name="es:getRegexPosition">
        <xsl:param name="context"/>
        <xsl:param name="regex"/>
        <xsl:param name="regexFlags"/>
        <xsl:param name="position"/>
        <xsl:variable name="analyzed">
            <xsl:analyze-string select="$context" regex="{$regex}" flags="{$regexFlags}">
                <xsl:matching-substring>
                    <es:span pos="{position()}">
                        <xsl:value-of select="."/>
                    </es:span>
                </xsl:matching-substring>
                <xsl:non-matching-substring>
                    <es:span pos="{position()}">
                        <xsl:value-of select="."/>
                    </es:span>
                </xsl:non-matching-substring>
            </xsl:analyze-string>
        </xsl:variable>
        <xsl:variable name="before" select="$analyzed/es:span[@pos = $position]/preceding-sibling::es:span"/>
        <xsl:variable name="beforeLength" select="sum($before/string-length())"/>
        <xsl:sequence select="$beforeLength + 1"/>
    </xsl:function>


    <xsl:param name="es:fallback-base" as="xs:string" select="'http://escali.schematron-quickfix.com/html/escali-web.html'"/>
    <xsl:function name="es:base-uri" as="xs:anyURI">
        <xsl:param name="node" as="node()"/>
        <xsl:variable name="nodeBaseUri" select="base-uri($node)"/>
        <xsl:sequence select="
                if ($nodeBaseUri[. != '']) then
                    ($nodeBaseUri)
                else
                    xs:anyURI($es:fallback-base)"/>
    </xsl:function>

    <xsl:function name="es:getNSNodesInterSect" as="node()*">
        <xsl:param name="node1" as="node()"/>
        <xsl:param name="node2" as="node()"/>

        <xsl:sequence select="$node1/namespace::*[es:getNSID(.) = $node2/es:getNSID(.)]"/>
    </xsl:function>

    <xsl:function name="es:getNSID" as="xs:string">
        <xsl:param name="ns" as="node()"/>
        <xsl:variable name="name" select="(name($ns), '#null')[. != ''][1]"/>
        <xsl:sequence select="concat($name, ':', $ns)"/>
    </xsl:function>

</xsl:stylesheet>
