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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:sch="http://purl.oclc.org/dsdl/schematron" exclude-result-prefixes="xs xd sch" version="2.0">
    <xsl:import href="escali_compiler_1_include.xsl"/>
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
    <xsl:param name="es:lang" select="'#ALL'"/>

    <xsl:variable name="initialSchema" select="/"/>

    <xsl:key name="diagnostic-id" match="sch:diagnostic" use="@id"/>

    <xsl:variable name="included" as="document-node()">
        <xsl:document>
            <xsl:apply-templates select="/sch:schema"/>
        </xsl:document>
    </xsl:variable>


    <xsl:template match="/">
        <xsl:apply-templates select="$included" mode="step-2"/>
    </xsl:template>

    <xsl:template match="/*/@es:uri | /*/@es:type-available | /*/@es:lang" mode="step-2"/>
    <xsl:template match="@es:id | @es:ref | @es:is-a | @es:link" mode="step-2"/>

    <xsl:variable name="processingNamespaces" select="
            (
            'http://www.w3.org/XML/1998/namespace',
            'http://purl.oclc.org/dsdl/schematron',
            'http://www.schematron-quickfix.com/validator/process',
            'http://www.schematron-quickfix.com/svrl/extension',
            'http://www.escali.schematron-quickfix.com/',
            'http://www.w3.org/1999/XSL/Transform',
            'http://www.w3.org/2001/XMLSchema-instance')"/>

    <xsl:template match="sch:*/*[not(namespace-uri() = $processingNamespaces)]"/>

    <xsl:template match="sch:schema" mode="step-2">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:variable name="prePhase" select="sch:title, sch:ns, sch:p, sch:let"/>
            <xsl:apply-templates select="$prePhase" mode="#current"/>
            <xsl:for-each-group select="sch:phase" group-by="@id">
                <xsl:copy>
                    <xsl:apply-templates select="@*" mode="#current"/>
                    <xsl:apply-templates select="current-group()/node()" mode="#current"/>
                </xsl:copy>
            </xsl:for-each-group>
            <xsl:apply-templates select="node() except (sch:phase | $prePhase)" mode="#current"/>
        </xsl:copy>

    </xsl:template>

    <xsl:template match="@diagnostics" mode="step-2">
        <xsl:variable name="diagnRefs" select="tokenize(., '\s')"/>
        <xsl:variable name="diagnRefs" select="$diagnRefs[es:checkDiagnosticRef(.)]"/>
        <xsl:attribute name="diagnostics" select="$diagnRefs" separator=" "/>
    </xsl:template>

    <xsl:function name="es:checkDiagnosticRef" as="xs:boolean">
        <xsl:param name="id" as="xs:string"/>
        <xsl:variable name="id" select="normalize-space($id)"/>
        <xsl:variable name="isInOrignial" select="key('diagnostic-id', $id, $initialSchema)"/>
        <xsl:variable name="isInAfterIncl" select="key('diagnostic-id', $id, $included)"/>

        <xsl:variable name="removedFromOriginal" select="exists($isInOrignial) and not($isInAfterIncl)"/>
        <xsl:sequence select="not($removedFromOriginal)"/>
    </xsl:function>


    <!-- 
        copies all nodes:
    -->
    <xsl:template match="node() | @*" mode="step-2">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>


</xsl:stylesheet>
