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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns="" exclude-result-prefixes="xs xd sch" xmlns:null="http://www.escali.schematron-quickfix.com/null-namespace" version="2.0">
    <xsl:include href="escali_compiler_1_sqf-params.xsl"/>


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

    <!--
	   Escali preprocess 1
	   Schematron:
	       includes
	       @role
	       IDs
	       URIs
	       
	   es extensions:
	       import
	       phases -> inactive
	       languages
	       @icon, @is-a, @see, sch:p/@class
	   
	-->

    <!--
        Extension parameter es:lang:
        all sch:diagnostic, sch:assert, sch:report and sch:p could have @xml:lang
        if they have other @xml:lang values than $lang, they will be hidden
        
    -->
    <xsl:param name="es:type-available" select="'true'"/>
    <!--  
        gives a list of posible values of the role element
        the order of the values gives the level of the schematron test (e.g. warning, error, fatal error)
        synonyms values can be seperated by pipes (warn|warningLevel1)
    -->
    <xsl:param name="roles" select="
            ('sqf-standalone',
            'info|information',
            'warn|warning',
            'error|#default',
            'fatal')" as="xs:string*"/>

    <xsl:param name="es:lang" select="
            if (/sch:schema/@xml:lang)
            then
                (/sch:schema/@xml:lang)
            else
                ('#ALL')"/>

    <!--
    returns the language value of a node
    it respects the inherited languages of the ancesors
    -->
    <xsl:function name="es:getLang" as="xs:string">
        <xsl:param name="node" as="node()"/>
        <xsl:variable name="lang" select="($node/ancestor-or-self::*/@xml:lang)[last()]"/>
        <xsl:value-of select="
                if ($lang) then
                    ($lang)
                else
                    ('#NULL')"/>
    </xsl:function>

    <xsl:include href="escali_compiler_0_functions.xsl"/>

    <!--  
    deletes all child nodes of sch:assert and sch:report
    if there lang value (es:getLang()) is not equal to the $lang
    -->
    <xsl:template match="*[self::sch:assert | self::sch:report]/node()" priority="100">
        <xsl:variable name="msgLang" select="es:getLang(.)"/>
        <xsl:choose>
            <xsl:when test="
                ($msgLang,
                    '#ALL') = $es:lang">
                <xsl:next-match/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:comment>Deleted because selected language <xsl:value-of select="$es:lang"/> != <xsl:value-of select="$msgLang"/>.</xsl:comment>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--  
    deletes all sch:diagnostic and sch:p
    if there lang value (es:getLang()) is not equal to the $lang
    -->
    <xsl:template match="sch:diagnostic | sch:p" priority="100">
        <xsl:variable name="elLang" select="es:getLang(.)"/>
        <xsl:choose>
            <xsl:when test="
                ($elLang,
                    '#ALL') = $es:lang">
                <xsl:next-match/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:comment>Deleted because selected language <xsl:value-of select="$es:lang"/> != <xsl:value-of select="$elLang"/>.</xsl:comment>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <!--  
    it is an error if there are no sch:assert, sch:report, 
    sch:p or sch:diagnostic which have the lang value of $lang
    -->
    <xsl:template match="sch:schema">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:variable name="nodes" select=".//(sch:diagnostic | sch:assert/node() | sch:report/node() | sch:p)"/>
        <xsl:if test="
                not($nodes[(es:getLang(.),
                '#ALL') = $es:lang]) and $nodes">
            <xsl:message terminate="no">There are no asserts, reports, diagnostics or paragraphs in this Schematron schema with the language <xsl:value-of select="$es:lang"/>!</xsl:message>
        </xsl:if>
        <xsl:copy copy-namespaces="no">
            <xsl:call-template name="namespace"/>
            <xsl:call-template name="es:baseUri">
                <xsl:with-param name="es:xml-base" select="$es:xml-base"/>
                <xsl:with-param name="supress-check" select="true()"/>
            </xsl:call-template>
            <xsl:attribute name="es:uri" select="document-uri(.)"/>
            <xsl:attribute name="es:lang" select="$es:lang" separator=","/>
            <xsl:attribute name="es:type-available" select="$es:type-available"/>
            <xsl:attribute name="es:defaultRole" select="es:calcRole('#default')"/>
            <xsl:attribute name="es:defaultRoleLabel" select="tokenize($roles[tokenize(., '\|') = '#default'], '\|')[1]"/>
            <xsl:apply-templates select="node() | @*">
                <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <!--
        deletes sch:p
        if the first following sch:pattern
        it is inactive
        
        writes @es:ref for the relation to 
        the first following sch:pattern
    -->
    <xsl:template match="/sch:schema/sch:p">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:variable name="pattern" select="(following-sibling::sch:pattern)[1]"/>
        <xsl:copy copy-namespaces="no">
            <xsl:call-template name="namespace"/>
            <xsl:call-template name="es:baseUri">
                <xsl:with-param name="es:xml-base" select="$es:xml-base"/>
            </xsl:call-template>
            <xsl:attribute name="es:ref" select="generate-id($pattern)"/>
            <xsl:apply-templates select="node() | @*">
                <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <!--
    writes @es:ref for the relation to 
        the first following sch:rule
    -->
    <xsl:template match="sch:pattern/sch:p">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:variable name="rule" select="(following-sibling::sch:rule)[1]"/>
        <xsl:variable name="refNode" select="
                if ($rule) then
                    ($rule)
                else
                    (parent::sch:pattern)"/>
        <xsl:copy copy-namespaces="no">
            <xsl:call-template name="namespace"/>
            <xsl:call-template name="es:baseUri">
                <xsl:with-param name="es:xml-base" select="$es:xml-base"/>
            </xsl:call-template>
            <xsl:attribute name="es:ref" select="generate-id($refNode)"/>
            <xsl:apply-templates select="node() | @*">
                <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <!--  
        generates ids to refer the schematron elements
    -->
    <xsl:template match="sch:pattern | sch:rule | sch:assert | sch:report">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:copy copy-namespaces="no">
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates select="ancestor::*/(@role | @flag | @icon | @see | @xml:lang)"/>
            <xsl:call-template name="es:baseUri">
                <xsl:with-param name="es:xml-base" select="$es:xml-base"/>
            </xsl:call-template>
            <xsl:attribute name="es:id" select="generate-id()"/>
            <xsl:apply-templates select="node() | @*">
                <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <!--  
        generates ids to refer the schematron elements
    -->
    <xsl:template match="sch:pattern[@es:matchType = 'priority']/sch:rule[not(@es:priority)]" priority="10">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:copy copy-namespaces="no">
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates select="ancestor::*/(@role | @flag | @icon | @see | @xml:lang)"/>
            <xsl:call-template name="es:baseUri">
                <xsl:with-param name="es:xml-base" select="$es:xml-base"/>
            </xsl:call-template>
            <xsl:attribute name="es:id" select="generate-id()"/>
            <xsl:attribute name="es:priority" select="0"/>
            <xsl:apply-templates select="node() | @*">
                <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <!--  
        Implementation of the schematron element include
        experimental implementation to allow better includes
        use @href="schema.sch#id" to include elements per id of the refered document
    -->
    <xsl:template match="sch:include">
        <xsl:variable name="splitetPath" select="tokenize(@href, '#')"/>
        <xsl:variable name="docPath" select="$splitetPath[1]"/>
        <xsl:variable name="docAbsPath" select="resolve-uri($docPath, document-uri(/))"/>
        <xsl:choose>
            <xsl:when test="not(doc-available($docAbsPath) or $docPath = '')">
                <xsl:message terminate="no">Document <xsl:value-of select="$docAbsPath"/> is not available!</xsl:message>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="doc" select="doc($docAbsPath)" as="document-node()"/>
                <xsl:variable name="id" select="$splitetPath[2]" as="xs:string?"/>
                <xsl:variable name="includeElement" select="
                        if (key('nodeById', $id, $doc))
                        then
                            (key('nodeById', $id, $doc))
                        else
                            ($doc/*)" as="node()"/>

                <xsl:apply-templates select="$includeElement"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--
        es extension to allow better imports
        import other schematron schemas
        use @phase to include just patterns from this phase
    -->
    <xsl:template match="es:import[@phase]">
        <xsl:variable name="docPath" select="resolve-uri(@href, document-uri(/))"/>
        <xsl:choose>
            <xsl:when test="not(doc-available($docPath)) or @href = '' or not(@href)">
                <xsl:message terminate="no">Document "<xsl:value-of select="@href"/>" is not available!</xsl:message>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="doc" select="doc($docPath)" as="document-node()"/>
                <xsl:variable name="phase" select="tokenize(@phase, '\s+')" as="xs:string+"/>
                <xsl:variable name="phaseNodes" select="es:getRefPhases($doc/sch:schema/sch:phase[@id = $phase])"/>
                <xsl:variable name="inActivePatterns" select="$doc/sch:schema/sch:pattern[not(es:isActive(., $phase))]"/>
                <xsl:variable name="notToImport" select="$doc/sch:schema/(sch:title | sch:phase) except $phaseNodes"/>
                <xsl:comment>
                    <xsl:text>Imported from document "</xsl:text>
                    <xsl:value-of select="$docPath"/>
                    <xsl:text>"</xsl:text>
                </xsl:comment>
                <xsl:apply-templates select="
                        $doc/sch:schema/* except ($notToImport,
                        $inActivePatterns)"/>
                <xsl:comment>Import end</xsl:comment>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--  
        es extension to allow better imports
        import all patterns from other schematron schemas
    -->
    <xsl:template match="es:import[not(@phase)]">
        <xsl:variable name="docPath" select="resolve-uri(@href, document-uri(/))"/>
        <xsl:choose>
            <xsl:when test="not(doc-available($docPath)) or @href = '' or not(@href)">
                <xsl:message terminate="no">Document "<xsl:value-of select="@href"/>" is not available!</xsl:message>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="doc" select="doc($docPath)" as="document-node()"/>
                <xsl:variable name="notToImport" select="$doc/sch:schema/sch:title"/>
                <xsl:comment>
                    <xsl:text>Imported from document "</xsl:text>
                    <xsl:value-of select="$docPath"/>
                    <xsl:text>"</xsl:text>
                </xsl:comment>
                <xsl:apply-templates select="$doc/sch:schema/* except $notToImport"/>
                <xsl:comment>Import end</xsl:comment>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--  
    it turns the @role value into a double between 0 and 1
    -->
    <xsl:template match="sch:*/@role">
        <xsl:param name="role" select="."/>

        <xsl:variable name="roleDouble" select="es:calcRole($role)"/>
        <xsl:attribute name="role" select="$roleDouble"/>
        <xsl:attribute name="es:roleLabel" select="$role"/>
        <xsl:if test="not($roleDouble)">
            <xsl:message terminate="no">Unknown @role value <xsl:value-of select="$role"/></xsl:message>
        </xsl:if>
    </xsl:template>

    <!--
    es extension:
    copies unsupported attributes into the es namespace
    -->
    <xsl:template match="@icon" mode="#all">
        <xsl:param name="icon" select="."/>
        <xsl:attribute name="es:{$icon/name()}" select="resolve-uri($icon, document-uri(/))"/>
    </xsl:template>
    <xsl:template match="@is-a" mode="#all">
        <xsl:attribute name="es:{name()}" select="."/>
        <xsl:next-match/>
    </xsl:template>
    <xsl:template match="@see" mode="#all">
        <xsl:param name="see" select="."/>
        <xsl:attribute name="es:link" select="resolve-uri($see, document-uri(/))"/>
    </xsl:template>
    <xsl:template match="sch:p/@class">
        <xsl:attribute name="es:class" select="."/>
    </xsl:template>

    <!-- 
        copies all nodes:
    -->
    <xsl:template match="node() | @*" mode="#all">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:copy copy-namespaces="no">
            <xsl:call-template name="namespace"/>
            <xsl:call-template name="es:baseUri">
                <xsl:with-param name="es:xml-base" select="$es:xml-base"/>
            </xsl:call-template>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current">
                <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="es:baseUri">
        <xsl:param name="es:xml-base" select="es:base-uri(.)"/>
        <xsl:param name="supress-check" select="false()" as="xs:boolean"/>
        <xsl:if test="$es:xml-base != es:base-uri(.) or $supress-check">
            <xsl:attribute name="xml:base" select="es:base-uri(.)"/>
        </xsl:if>
    </xsl:template>

    <xsl:function name="es:calcRole" as="xs:double?">
        <xsl:param name="role"/>
        <xsl:variable name="roleString" select="$roles[tokenize(., '\|') = $role]"/>
        <xsl:variable name="rolePos" select="index-of($roles, $roleString)"/>
        <xsl:variable name="count" select="count($roles)"/>
        <xsl:variable name="step" select="1 div $count div 2"/>
        <xsl:variable name="roleDouble" select="$rolePos div $count - $step"/>
        <xsl:sequence select="
                if ($roleString) then
                    ($roleDouble)
                else
                    ()"/>
    </xsl:function>
</xsl:stylesheet>
