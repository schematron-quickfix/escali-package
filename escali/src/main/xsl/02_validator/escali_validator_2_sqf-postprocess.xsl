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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" exclude-result-prefixes="xs xd" version="2.0">

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

    <xsl:variable name="base-uri" select="/svrl:schematron-output/sqf:topLevel/@instance"/>


    <xsl:template match="svrl:successful-report[sqf:fix[@default = 'true']] | svrl:failed-assert[sqf:fix[@default = 'true']]">
        <xsl:variable name="default" select="sqf:fix[@default = 'true']"/>
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="sqf:default-fix" select="$default/@id"/>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="sqf:fix">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:variable name="base-uris" select="distinct-values((.//processing-instruction(sqf_doc)/tokenize(., '\s#\s')[last()], $base-uri))"/>
            <xsl:attribute name="base-uris" select="$base-uris" separator=" "/>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="sqf:fix//sqf:fix" priority="100">
        <xsl:call-template name="sqf:copy"/>
    </xsl:template>


    <xsl:template match="sqf:sheet">
        <xsl:variable name="sqf:id" select="
                if (@id) then
                    (@id)
                else
                    (generate-id())"/>
        <xsl:choose>
            <xsl:when test="$es:compact-svrl">
                <sqf:sheet href="fixes/{$sqf:id}.xml"/>
                <xsl:result-document href="fixes/{$sqf:id}.xml">
                    <xsl:next-match/>
                </xsl:result-document>
            </xsl:when>
            <xsl:otherwise>
                <xsl:next-match/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--
        copies all nodes
    -->
    <xsl:template match="node() | @*" priority="-10" name="sqf:copy">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
