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
<xsl:stylesheet xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" exclude-result-prefixes="xs xd svrl es" version="2.0">
    <xsl:import href="../01_compiler/escali_compiler_0_getSchemaInfo.xsl"/>

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

    <xsl:key name="paraByRefid" match="svrl:text" use="@es:ref"/>

    <xsl:param name="schema" select="
            if (/svrl:schematron-output/@es:schema) then
                (/svrl:schematron-output/@es:schema)
            else
                (/svrl:schematron-output/sqf:topLevel/@schema)"/>

    <xsl:param name="instance" select="
            if (/svrl:schematron-output/@es:instance) then
                (/svrl:schematron-output/@es:instance)
            else
                (/svrl:schematron-output/sqf:topLevel/@instance)"/>

    <xsl:variable name="root" select="/"/>

    <xsl:template match="/" mode="schemaInfo">
        <xsl:apply-imports/>
    </xsl:template>

    <xsl:template match="/">
        <xsl:variable name="schemaInfo">
            <xsl:choose>
                <xsl:when test="doc-available($schema)">
                    <xsl:apply-templates select="doc($schema)" mode="schemaInfo"/>
                </xsl:when>
                <xsl:otherwise>
                    <es:schemaInfo/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:apply-templates>
            <xsl:with-param name="schemaInfo" select="$schemaInfo" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="svrl:schematron-output">
        <xsl:param name="schemaInfo" tunnel="yes"/>
        <es:escali-reports>
            <xsl:variable name="phase" select="tokenize(@phase, '\s')"/>
            <xsl:variable name="activePatterns" select="$schemaInfo//es:phases/es:phase[@id = $phase]/@patterns/tokenize(., '\s')"/>
            <es:meta activePatterns="{$activePatterns}">
                <xsl:apply-templates select="@*"/>
                <es:title>
                    <xsl:value-of select="@title"/>
                </es:title>
                <es:schema>
                    <xsl:value-of select="$schema"/>
                </es:schema>
                <es:instance>
                    <xsl:value-of select="$instance"/>
                </es:instance>

                <xsl:for-each select="$schemaInfo//es:phases/es:phase">
                    <xsl:sort select="
                            if (@id = $phase) then
                                (0)
                            else
                                (1)" data-type="number"/>
                    <es:phase>
                        <xsl:copy-of select="@id | @isDefault"/>
                        <xsl:variable name="patterns" select="tokenize(@patterns, '\s')"/>
                        <xsl:attribute name="isActive" select="
                                if (@id = $phase) then
                                    ('yes')
                                else
                                    if (count($patterns[not(. = $activePatterns)]) gt 0) then
                                        ('no')
                                    else
                                        ('included')
                                "/>
                    </es:phase>
                </xsl:for-each>
                <xsl:apply-templates select="
                        svrl:ns-prefix-in-attribute-values,
                        svrl:text[@es:ref = '']"/>
            </es:meta>
            <xsl:for-each-group select="svrl:* except (svrl:text | svrl:ns-prefix-in-attribute-values)" group-starting-with="svrl:active-pattern">
                <xsl:variable name="id" select="@es:id"/>
                <xsl:choose>
                    <xsl:when test="self::svrl:active-pattern">
                        <es:pattern>
                            <es:meta>
                                <xsl:apply-templates select="@* except @name"/>
                                <xsl:apply-templates select="@name"/>
                                <xsl:apply-templates select="key('paraByRefid', $id)"/>
                            </es:meta>
                            <xsl:variable name="rules" as="element(es:rule)*">
                                <xsl:for-each-group select="current-group() except ." group-starting-with="svrl:fired-rule">
                                    <es:rule>
                                        <xsl:copy-of select="@*"/>
                                        <es:fixes>
                                            <xsl:apply-templates select="./node()"/>
                                        </es:fixes>
                                        <xsl:apply-templates select="current-group() except ."/>
                                    </es:rule>
                                </xsl:for-each-group>
                            </xsl:variable>
                            <xsl:for-each-group select="$rules" group-by="@es:id">
                                <xsl:variable name="fixes" select="current-group()[1]/es:fixes"/>
                                <es:rule>
                                    <xsl:apply-templates select="@xml:base"/>
                                    <es:meta>
                                        <xsl:apply-templates select="@* except @xml:base"/>
                                        <xsl:apply-templates select="key('paraByRefid', current-grouping-key(), $root)"/>
                                        <xsl:sequence select="$fixes/node()"/>
                                    </es:meta>
                                    <xsl:sequence select="current-group()/(node() except es:fixes)"/>
                                </es:rule>
                            </xsl:for-each-group>
                        </es:pattern>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="current-group()"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each-group>
        </es:escali-reports>
    </xsl:template>

    <xsl:template match="svrl:active-pattern/@name">
        <es:title>
            <xsl:value-of select="."/>
        </es:title>
    </xsl:template>

    <xsl:template match="svrl:failed-assert">
        <es:assert>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </es:assert>
    </xsl:template>

    <xsl:template match="svrl:successful-report">
        <es:report>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </es:report>
    </xsl:template>

    <xsl:template match="svrl:diagnostic-reference">
        <es:diagnostics>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </es:diagnostics>
    </xsl:template>

    <xsl:template match="sqf:sheet" priority="10"/>

    
    <xsl:template match="sqf:fix | sqf:fix//node() | svrl:fired-rule//node()">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="svrl:text" priority="5">
        <es:text>
            <xsl:value-of select="."/>
        </es:text>
    </xsl:template>

    <xsl:template match="svrl:*">
        <xsl:element name="es:{local-name(.)}">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="@es:*">
        <xsl:attribute name="{local-name()}" select="."/>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>
