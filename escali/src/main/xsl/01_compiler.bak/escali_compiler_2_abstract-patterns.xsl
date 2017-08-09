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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias" exclude-result-prefixes="xs xd sch axsl es" version="2.0">
    
    <xsl:include href="escali_compiler_2_sqf-user-entry.xsl"/>
    <xsl:include href="escali_compiler_0_functions.xsl"/>

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
	   Escali preprocess 2
	   Schematron:
	        abstracts Schematron patterns
	       
	   es extensions:
	       languages of diagnostics
	   
	-->

    <xsl:output/>
    <!--	-->
    <xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>

    <xsl:key name="abstractById" match="*[@abstract='true']" use="@id"/>
    
    <xsl:template match="sch:schema">
        <xsl:for-each-group select="sch:ns" group-by="@prefix">
            <xsl:if test="count(current-group()) > 1 and count(distinct-values(current-group()/@uri)) > 1">
                <xsl:message terminate="yes">The prefix <xsl:value-of select="current-grouping-key()"/> is multiple defined by sch:ns elements.</xsl:message>
            </xsl:if>
        </xsl:for-each-group>
        <xsl:next-match/>
    </xsl:template>
    
    <!--  
        Handling a pattern call
    -->
    <xsl:template match="sch:pattern[@is-a]">
        <xsl:variable name="es-id" select="@es:id"/>
        <xsl:variable name="template" select="key('abstractById', @is-a)"/>
        <xsl:choose>
            <xsl:when test="not($template)">
                <xsl:message>The called pattern <xsl:value-of select="@is-a"/> is not available or no abstract pattern.</xsl:message>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="$template" mode="resolvePattern">
                    <xsl:with-param name="id" select="if (@id) then (@id) else ($es-id)"/>
                    <xsl:with-param name="es-id" select="$es-id"/>
                    <xsl:with-param name="params" select="sch:param"/>
                </xsl:apply-templates>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="sch:pattern[@abstract='true']"/>

    <!--
        Resolve the abstract patterns
        The resolved pattern gets the id of the calling pattern.
        @es:is-a safes the id of the called pattern.
    -->
    <xsl:template match="sch:pattern[@abstract='true']" mode="resolvePattern">
        <xsl:param name="id"/>
        <xsl:param name="es-id"/>
        <xsl:param name="params"/>
        <xsl:copy>
            <xsl:apply-templates select="@* except @abstract" mode="resolvePattern">
                <xsl:with-param name="params" select="$params" tunnel="yes"/>
            </xsl:apply-templates>
            <xsl:attribute name="es:is-a" select="@id"/>
            <xsl:attribute name="es:id" select="$es-id"/>
            <xsl:attribute name="id" select="$id"/>
            <xsl:apply-templates select="node()" mode="resolvePattern">
                <xsl:with-param name="params" select="$params" tunnel="yes"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>

    <!--  
        resolves the parameters of the called pattern
        
    -->
    <xsl:template match="@*" mode="resolvePattern" priority="10">
        <xsl:param name="params" select="()" tunnel="yes" as="node()*"/>
        <xsl:attribute name="{local-name()}" namespace="{namespace-uri()}">
            <xsl:call-template name="resolveAttribute">
                <xsl:with-param name="value" select="."/>
                <xsl:with-param name="params" select="$params"/>
            </xsl:call-template>
        </xsl:attribute>
    </xsl:template>
    <!--
        recursive template, resolves all parameters of a pattern call
        $value -> attribute value, wich could contains the param
        $params -> parameter of a calling pattern (sch:param)
    -->
    <xsl:template name="resolveAttribute">
        <xsl:param name="value" as="xs:string"/>
        <xsl:param name="params" as="node()*"/>
        <xsl:variable name="resParam" select="$params[1]"/>
        <xsl:choose>
            <xsl:when test="count($params) > 0">
                <xsl:variable name="paramName" select="es:quoteRegex($resParam/@name)">
                    <!--<xsl:analyze-string select="$resParam/@name" regex="[-\[\]()*+?.,\\^$|#]">
                        <xsl:matching-substring>
                            <xsl:text>\</xsl:text>
                            <xsl:value-of select="."/>
                        </xsl:matching-substring>
                        <xsl:non-matching-substring>
                            <xsl:value-of select="."/>
                        </xsl:non-matching-substring>
                    </xsl:analyze-string>-->
                </xsl:variable>
                <xsl:variable name="value">
                    <xsl:analyze-string select="$value" regex="\${$paramName}">
                        <xsl:matching-substring>
                            <xsl:value-of select="$resParam/@value"/>
                        </xsl:matching-substring>
                        <xsl:non-matching-substring>
                            <xsl:value-of select="."/>
                        </xsl:non-matching-substring>
                    </xsl:analyze-string>
                </xsl:variable>
                <xsl:call-template name="resolveAttribute">
                    <xsl:with-param name="value" select="$value"/>
                    <xsl:with-param name="params" select="$params except $resParam"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$value"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--  
        copies nodes of the refered rules
    -->
    <xsl:template match="sch:extends" mode="#all">
        <xsl:param name="abstractRuleId" tunnel="yes" select="()"/>
        <xsl:variable name="rule" select="@rule"/>
        <xsl:apply-templates select="key('abstractById', $rule)/node()" mode="#current">
            <xsl:with-param name="abstractRuleId" select="($abstractRuleId, @rule)" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <!--  
        deletes abstract rules
    -->
    <xsl:template match="sch:rule[@abstract='true']" mode="#all"/>
    
    
    
    <!--    
        es extension:
        concern asserts / reports which contains no message (after the es:lang filter):
        if they refer to exactly one diagnostic
        the message of the diagnostic will be used as the message of the assert / report
    -->
    <xsl:template match="sch:assert[not(node())] | sch:report[not(node())]" mode="#all">
        <xsl:variable name="diagnostics" select="tokenize(@diagnostics,'\s')"/>
        <xsl:variable name="refDiagnostic" select="/sch:schema/sch:diagnostics/sch:diagnostic[@id = $diagnostics]"/>
        <xsl:copy>
            <xsl:apply-templates select="if (count($refDiagnostic) = 1) 
                                       then (@* except @diagnostics, $refDiagnostic/@* except $refDiagnostic/@id, $refDiagnostic/node()) 
                                       else (@*, node())" mode="#current"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="sch:assert | sch:report" mode="#all">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- 
        copies all nodes:
    -->
    <xsl:template match="node() | @*" mode="#all">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
