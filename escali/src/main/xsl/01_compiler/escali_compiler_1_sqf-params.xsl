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
    
    <xsl:template match="/*" priority="1000000">
        <xsl:variable name="rootEl" select="/*"/>
        <xsl:choose>
            <xsl:when test="/sch:schema">
                <xsl:next-match/>
            </xsl:when>
            <xsl:when test="/sqf:fixes">
                <xsl:variable name="tempSchema" as="document-node()">
                    <xsl:document>
                        <xsl:call-template name="sqf:transformStandalone">
                            <xsl:with-param name="sqf:fixes" select="$rootEl"/>
                        </xsl:call-template>
                    </xsl:document>
                </xsl:variable>
                <xsl:apply-templates select="$tempSchema/sch:schema"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:message terminate="yes">Input file does not seems to be a schema!</xsl:message>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--
	S Q F - E X T E N S I O N S
-->
    <xsl:key name="fixById" match="sqf:fix" use="@id"/>
    <xsl:variable name="globalFixes" select="/sch:schema/sqf:fixes/(. | sqf:group)/sqf:fix"/>


    <xsl:template match="sqf:description/sqf:title/node() | sqf:description/sqf:p/node()" priority="100">
        <xsl:choose>
            <xsl:when test="
                    (es:getLang(.),
                    '#ALL') = $es:lang">
                <xsl:next-match/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:comment>Deleted because selected language <xsl:value-of select="$es:lang"/> != <xsl:value-of select="es:getLang(.)"/>.</xsl:comment>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>



    <!--
	sqf:call-fix
-->
    <xsl:template match="sqf:fix[sqf:call-fix]">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:copy copy-namespaces="no">
            <xsl:call-template name="namespace"/>
            <xsl:call-template name="es:baseUri">
                <xsl:with-param name="es:xml-base" select="$es:xml-base"/>
            </xsl:call-template>
            <xsl:apply-templates select="@*"/>

            <xsl:variable name="content" as="node()*">
                <xsl:apply-templates select="sqf:call-fix" mode="parameter">
                    <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
                </xsl:apply-templates>
                <xsl:apply-templates select="node() except (sqf:description, sqf:param)" mode="callFix">
                    <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
                </xsl:apply-templates>
            </xsl:variable>

            <xsl:variable name="selfDescriptions" select="sqf:description"/>
            
            <xsl:apply-templates select="sqf:param, $selfDescriptions" mode="callFix"/>

            <xsl:copy-of select="
                    $content except (if ($selfDescriptions) then
                        ($content/self::sqf:description)
                    else
                        ())"/>


        </xsl:copy>
    </xsl:template>

    <xsl:template match="sqf:call-fix" mode="callFix">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:variable name="idref" select="@ref"/>
        <xsl:variable name="calledFix" select="key('fixById', $idref)"/>
        <xsl:variable name="localFix" select="$calledFix intersect ancestor::sch:rule/(. | sqf:group)/sqf:fix"/>
        <xsl:variable name="globalFix" select="$calledFix intersect $globalFixes"/>
        <xsl:variable name="calledFix" select="
                ($localFix,
                $globalFix)[1]"/>
        <xsl:apply-templates select="$calledFix/node() except ($calledFix/sqf:param)">
            <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="sqf:call-fix" mode="parameter">
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:variable name="idref" select="@ref"/>

        <xsl:variable name="calledFix" select="key('fixById', $idref)"/>
        <xsl:variable name="localFix" select="$calledFix intersect ancestor::sch:rule/(. | sqf:group)/sqf:fix"/>
        <xsl:variable name="globalFix" select="$calledFix intersect $globalFixes"/>
        <xsl:variable name="calledFix" select="
                ($localFix,
                $globalFix)[1]"/>
        <xsl:apply-templates select="$calledFix/sqf:param" mode="#current">
            <xsl:with-param name="params" select="sqf:with-param" tunnel="yes"/>
            <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="sqf:fix/sqf:param" mode="parameter">
        <xsl:param name="params" select="()" tunnel="yes" as="element(sqf:with-param)*"/>
        <xsl:param name="es:xml-base" tunnel="yes" select="es:base-uri(.)"/>
        <xsl:variable name="overlayedParam" select="$params[@name = current()/@name]" as="element(sqf:with-param)?"/>
        <xsl:element name="xsl:variable">
            <xsl:call-template name="namespace"/>
            <xsl:call-template name="es:baseUri">
                <xsl:with-param name="es:xml-base" select="$es:xml-base"/>
            </xsl:call-template>
            <xsl:apply-templates select="@name"/>
            <xsl:if test="$es:type-available != 'false' and @type">
                <xsl:attribute name="as" select="@type"/>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="normalize-space($overlayedParam) != '' or $overlayedParam/@select">
                    <xsl:apply-templates select="$overlayedParam/@select | $overlayedParam/node()">
                        <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="@default">
                        <xsl:attribute name="select" select="@default"/>
                    </xsl:if>
                    <xsl:apply-templates>
                        <xsl:with-param name="es:xml-base" select="es:base-uri(.)" tunnel="yes"/>
                    </xsl:apply-templates>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:element>
    </xsl:template>



    <xsl:template match="sqf:with-param/@select">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>
