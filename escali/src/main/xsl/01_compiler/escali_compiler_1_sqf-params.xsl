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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" exclude-result-prefixes="xs xd" version="2.0">

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

    <xsl:include href="escali_compiler_1_sqf-standalone.xsl"/>

    <xsl:template match="sqf:fixes" priority="1000000">
        <xsl:param name="included" tunnel="yes" select="false()" as="xs:boolean"/>
        <xsl:variable name="self" select="."/>

        <xsl:variable name="standalones" select=".//(sqf:group | sqf:fix)[@es:context]"/>
        <xsl:variable name="tempSchema" as="document-node()">
            <xsl:document>
                <xsl:call-template name="sqf:transformStandalone">
                    <xsl:with-param name="sqf:fixes" select="$self"/>
                </xsl:call-template>
            </xsl:document>
        </xsl:variable>

        <xsl:choose>
            <xsl:when test="$standalones and not($included) and not(parent::sch:schema)">
                <!--<xsl:apply-templates select="$tempSchema/sch:schema"/>-->
                <xsl:apply-templates select="$tempSchema/*"/>
            </xsl:when>
            <xsl:when test="$standalones">
                <xsl:apply-templates select="$tempSchema/sch:schema/*"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:next-match/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

</xsl:stylesheet>
