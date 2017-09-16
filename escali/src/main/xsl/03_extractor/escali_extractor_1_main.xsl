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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqfc="http://www.schematron-quickfix.com/validator/process/changes" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias" xmlns:xsm="http://www.schematron-quickfix.com/manipulator/process" exclude-result-prefixes="xs xd" version="2.0">
    <xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>
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

    <xsl:include href="escali_extractor_1_get-record.xsl"/>

    <xsl:param name="id" as="xs:string+" select="''"/>
    <xsl:param name="inputRecord" as="xs:string" select="''"/>
    <xsl:param name="outputRecord" as="xs:string" select="''"/>
    <xsl:param name="additionalTypes" select="()"/>
    <xsl:param name="markChanges" as="xs:boolean" select="true()"/>
    <xsl:param name="missing-fixes-handle" select="2" as="xs:integer"/>

    <!--	<xsl:param name="fixId" select="('replace')"/>-->
    <!--<xsl:param name="contextId"/>
	<xsl:param name="fixId"/>-->
    <xsl:variable name="idSeq" select="
            for $i in $id
            return
                tokenize($i, '\s')"/>
    <xsl:variable name="namespaceAlias" select="//sqf:topLevel/xsl:stylesheet/xsl:namespace-alias[@result-prefix = 'sqf']/@stylesheet-prefix"/>

    <xsl:key name="fix-id" match="sqf:fix" use="@id"/>

    <xsl:variable name="selectedFix" select="key('fix-id', $idSeq)"/>
    <xsl:template match="svrl:schematron-output">
        <xsl:apply-templates select="//sqf:topLevel/xsl:stylesheet"/>
        <xsl:if test="$outputRecord != ''">
            <xsl:call-template name="getRecord"/>
        </xsl:if>
    </xsl:template>
    <xsl:template match="xsl:stylesheet">
        <xsl:variable name="root" select="/"/>
        <xsl:variable name="missingFixIds" select="$idSeq[not(key('fix-id', ., $root))]"/>
        <xsl:if test="count($missingFixIds) gt 0">
            <xsl:variable name="msg">
                <xsl:text>No fixes for the IDs </xsl:text>
                <xsl:value-of select="$missingFixIds" separator=", "/>
                <xsl:text> in this report available!</xsl:text>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="$missing-fixes-handle ge 2">
                    <xsl:message terminate="yes">
                        <xsl:value-of select="$msg"/>
                    </xsl:message>
                </xsl:when>
                <xsl:when test="$missing-fixes-handle ge 1">
                    <xsl:message terminate="no">
                        <xsl:value-of select="$msg"/>
                    </xsl:message>
                </xsl:when>
                <xsl:otherwise/>
            </xsl:choose>
        </xsl:if>
        <xsl:copy>
            <xsl:variable name="namespaces" select="./namespace::*"/>
            <xsl:copy-of select="$namespaces"/>
            <xsl:attribute name="exclude-result-prefixes" select="concat(string-join($namespaces/(name())[. != ''][1], ' '), ' xsl')"/>
            <xsl:variable name="selectedFix">
                <xsl:apply-templates select="$selectedFix"/>
            </xsl:variable>
            <xsl:apply-templates select="node() | @*">
                <xsl:with-param name="selectedFix" select="$selectedFix" tunnel="yes"/>
            </xsl:apply-templates>
            <!--            -->
            <xsl:copy-of select="$selectedFix"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="svrl:successful-report/sqf:fix | svrl:failed-assert/sqf:fix">
        <xsl:apply-templates select=".//sqf:sheet"/>
    </xsl:template>
    
    <xsl:template match="sqf:sheet[@href]">
        <xsl:variable name="href" select="resolve-uri(@href, base-uri(.))"/>
        <xsl:apply-templates select="doc($href)"/>
    </xsl:template>
    
    <xsl:template match="sqf:sheet">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="xsl:param[@as = $additionalTypes]">
        <xsl:next-match/>
        <xsl:if test="starts-with(@as, 'xs:')">
            <xsl:element name="xsl:variable">
                <xsl:attribute name="name" select="@name"/>
                <xsl:attribute name="select" select="concat(@as, '($', @name, ')')"/>
            </xsl:element>
        </xsl:if>
    </xsl:template>

    <xsl:template match="xsl:param/@as">
        <xsl:attribute name="as">
            <xsl:choose>
                <xsl:when test=". = $additionalTypes">
                    <xsl:text>xs:string</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>

    <!--  
        for sqf namespace alias  
    -->
    <xsl:template match="xsl:attribute/@name | xsl:element/@name">
        <xsl:variable name="namespaceAlias" select="string-join($namespaceAlias, '|')"/>
        <xsl:attribute name="{name()}">
            <xsl:call-template name="aliasReplace"/>
        </xsl:attribute>
    </xsl:template>
    <xsl:template match="xsl:attribute[matches(@name, 'sqfc:attribute.*')]">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:call-template name="aliasReplace"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*[@sqf:changeMarker = 'true'] | @sqfc:*" priority="100">
        <xsl:if test="$markChanges">
            <xsl:next-match/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="@sqf:changeMarker" priority="100"/>



    <xsl:template name="aliasReplace">
        <xsl:analyze-string select="." regex="^({$namespaceAlias}):">
            <xsl:matching-substring>sqf:</xsl:matching-substring>
            <xsl:non-matching-substring>
                <xsl:value-of select="."/>
            </xsl:non-matching-substring>
        </xsl:analyze-string>
    </xsl:template>
    <!--
        Kopiert alle Knoten
    -->
    <xsl:template match="node() | @*" mode="#all">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>


    <!--  Multiple documents  -->

    <xsl:template match="sqf:createExternals">
        <xsl:param name="selectedFix" tunnel="yes"/>
        <xsl:variable name="docPIs" select="$selectedFix//processing-instruction(sqf_doc)"/>
        <xsl:variable name="self" select="."/>
        <xsl:for-each-group select="$docPIs" group-by=".">
            <xsl:variable name="doc" select="substring-after(current-grouping-key(), ' # ')"/>
            <xsl:variable name="mode" select="substring-before(current-grouping-key(), ' # ')"/>
            <xsl:apply-templates select="$self" mode="sqf:next-match">
                <xsl:with-param name="doc" select="$doc" tunnel="yes"/>
                <xsl:with-param name="mode" select="$mode" tunnel="yes"/>
            </xsl:apply-templates>
        </xsl:for-each-group>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="sqf:createExternals[@type = 'doc']" mode="sqf:next-match">
        <xsl:param name="doc" tunnel="yes"/>
        <axsl:result-document href="{$doc}.tmp">
            <xsl:apply-templates mode="#current"/>
        </axsl:result-document>
    </xsl:template>
    <xsl:template match="sqf:createExternals" mode="sqf:next-match">
        <xsl:apply-templates mode="#current"/>
    </xsl:template>

    <xsl:template match="@mode" mode="sqf:next-match">
        <xsl:param name="mode" tunnel="yes"/>
        <xsl:attribute name="mode" select="
                for $m in tokenize(., '\s')
                return
                    concat($mode, '_', $m)" separator=" "/>
    </xsl:template>

    <xsl:template match="xsm:manipulator/@document" mode="sqf:next-match">
        <xsl:param name="doc" tunnel="yes"/>
        <xsl:attribute name="document" select="$doc"/>
    </xsl:template>

    <xsl:template match="xsl:apply-templates/@select" mode="sqf:next-match">
        <xsl:param name="doc" tunnel="yes"/>
        <xsl:attribute name="select">
            <xsl:text>doc('</xsl:text>
            <xsl:value-of select="$doc"/>
            <xsl:text>')/(</xsl:text>
            <xsl:value-of select="."/>
            <xsl:text>)</xsl:text>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="xsl:apply-templates" mode="sqf:next-match">
        <xsl:param name="mode" tunnel="yes"/>
        <xsl:copy>
            <xsl:attribute name="mode" select="$mode" separator=" "/>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>



</xsl:stylesheet>
