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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" exclude-result-prefixes="xs xd" version="2.0">
    <xsl:include href="escali_validator_2_sqf-postprocess.xsl"/>

    <xsl:param name="es:compact-svrl" select="/processing-instruction(es_compact-svrl) = 'true'" as="xs:boolean"/>

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
    
    <xsl:template match="/processing-instruction(es_compact-svrl)"/>
    
    <xsl:template match="svrl:schematron-output">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="*[not(@es:patternId)]"/>
            <xsl:for-each-group select="*[@es:patternId]" group-by="@es:patternId">
                <xsl:if test="current-group()/(self::svrl:failed-assert | self::svrl:successful-report) or not($es:compact-svrl)">
                    <xsl:apply-templates select="current-group()"/>
                </xsl:if>
            </xsl:for-each-group>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="svrl:fired-rule">
        <xsl:variable name="next-error" select="(following-sibling::svrl:failed-assert | following-sibling::svrl:successful-report)[1]"/>
        <xsl:if test="following-sibling::*[1] intersect $next-error or not($es:compact-svrl)">
            <xsl:next-match/>
        </xsl:if>
    </xsl:template>


    <xsl:template match="@es:patternId"/>

    <!--
        copies all nodes
    -->
    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
