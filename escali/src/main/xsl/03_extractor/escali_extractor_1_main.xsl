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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqfc="http://www.schematron-quickfix.com/validator/process/changes" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsm="http://www.schematron-quickfix.com/manipulator/process" xmlns:sch="http://purl.oclc.org/dsdl/schematron" exclude-result-prefixes="xs xd" version="2.0">
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


    <xsl:param name="id" as="xs:string+" select="''"/>
    <xsl:param name="additionalTypes" select="()"/>
    <xsl:param name="markChanges" as="xs:boolean" select="true()"/>
    <xsl:param name="missing-fixes-handle" select="2" as="xs:integer"/>

    <xsl:key name="fix-id" match="sqf:fix" use="@id"/>

    <xsl:include href="../01_compiler/escali_compiler_0_functions.xsl"/>

    <xsl:template match="/es:escali-reports">
        <axsl:stylesheet version="2.0">
            <axsl:include href="{resolve-uri('../01_compiler/escali_compiler_0_functions.xsl')}"/>

            <axsl:template match="/">
                <xsm:manipulator document="{/es:escali-reports/es:meta/es:instance}">
                    <axsl:apply-templates/>
                </xsm:manipulator>
            </axsl:template>

            <xsl:apply-templates select=".//(es:assert | es:report)"/>
        </axsl:stylesheet>
    </xsl:template>

    <xsl:template match="es:assert | es:report">
        <xsl:apply-templates select="sqf:fix">
            <xsl:with-param name="location" select="@location/string()" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="key('fix-id', $id)">
        <xsl:for-each select="sqf:user-entry">
            <axsl:param name="{@name}"/>
        </xsl:for-each>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="key('fix-id', $id)/sqf:call-fix">
        <xsl:apply-templates select="es:getRefFix(@ref)" mode="sqf:xsm">
            <xsl:with-param name="params" select="sqf:with-param" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="sqf:fix"/>

    <xsl:function name="es:getRefFix" as="element(sqf:fix)?">
        <xsl:param name="ref" as="attribute()"/>
        <xsl:variable name="root" select="root($ref)"/>
        <xsl:variable name="allqfs" select="$root/key('fix-id', $ref)"/>
        <xsl:sequence select="$allqfs[1]"/>
    </xsl:function>

    <xsl:template match="sqf:fix" mode="sqf:xsm">
        <xsl:param name="location" tunnel="yes" as="xs:string"/>
        <axsl:template match="{$location}">
            <xsl:apply-templates select="sqf:param | sqf:delete | sqf:add | sqf:replace | sqf:stringReplace" mode="#current"/>
        </axsl:template>
    </xsl:template>
    
    <xsl:template match="sqf:param" mode="sqf:xsm">
        <xsl:param name="params" tunnel="yes"/>
        <xsl:variable name="name" select="@name"/>
        <xsl:variable name="withParam" select="$params[@name = $name]"/>
        <axsl:param name="{@name}">
            <xsl:sequence select="$withParam/@select"/>
        </axsl:param>
    </xsl:template>
    
    <xsl:template match="sqf:replace | sqf:add | sqf:delete" mode="sqf:xsm" priority="50">
        <xsl:variable name="match" select="(@match, '.')[1]"/>
        <xsl:element name="xsm:{local-name(.)}">
            <axsl:attribute name="node" select="es:getNodePath({$match})"/>
            <xsl:next-match/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="sqf:delete" mode="sqf:xsm"/>


    <xsl:template match="sqf:add" mode="sqf:xsm" priority="25">
        <axsl:attribute name="position" select="'{(@position, 'first-child')[1]}'"/>
        <xsl:next-match/>
    </xsl:template>

    <xsl:template match="sqf:add | sqf:replace" mode="sqf:xsm">
        <xsl:variable name="body">
            <xsl:apply-templates select="@select | node()" mode="#current"/>
        </xsl:variable>
        <xsm:content>
            <xsl:choose>
                <xsl:when test="@target | @node-type">
                    <xsl:element name="xsl:{@node-type}">
                        <xsl:if test="@target">
                            <xsl:attribute name="name" select="@target"/>
                        </xsl:if>
                        <xsl:sequence select="$body"/>
                    </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:sequence select="$body"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsm:content>
    </xsl:template>

    <xsl:template match="sqf:add/@select | sqf:replace/@select | sqf:stringReplace/@select" mode="sqf:xsm">
        <axsl:sequence select="{.}"/>
    </xsl:template>


    <xsl:template match="sqf:stringReplace" mode="sqf:xsm">
        <xsl:variable name="match" select="(@match, '.')[1]"/>
        <axsl:variable name="sqf:nodePath" select="es:getNodePath({$match})"/>
        <axsl:variable name="sqf:stringReplace">
            <axsl:analyze-string select="{$match}" regex="{@regex}">
                <xsl:sequence select="@flags"/>
                <axsl:matching-substring>
                    <xsm:step replace="true">
                        <axsl:attribute name="length" select="string-length(.)"/>
                        <xsl:apply-templates select="@select | node()" mode="#current"/>
                    </xsm:step>
                </axsl:matching-substring>
                <axsl:non-matching-substring>
                    <xsm:step>
                        <axsl:attribute name="length" select="string-length(.)"/>
                    </xsm:step>
                </axsl:non-matching-substring>
            </axsl:analyze-string>
        </axsl:variable>


        <axsl:for-each select="$sqf:stringReplace/xsm:step[@replace = 'true']">
            <xsm:replace>
                <axsl:attribute name="node" select="$sqf:nodePath"/>
                <axsl:variable name="sqf:start" select="sum(preceding-sibling::xsm:step/@length)"/>
                <axsl:attribute name="start-position" select="$sqf:start"/>
                <axsl:attribute name="end-position" select="$sqf:start + @length"/>
                <xsm:content>
                    <axsl:sequence select="node()"/>
                </xsm:content>
            </xsm:replace>
        </axsl:for-each>
    </xsl:template>



    <xsl:template match="xsl:*" mode="sqf:xsm">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="sch:let" mode="sqf:xsm">
        <axsl:variable name="{@name}" select="{@value}"/>
    </xsl:template>

    <xsl:template match="sch:value-of" mode="sqf:xsm">
        <axsl:value-of select="{@select}"/>
    </xsl:template>

    <xsl:template match="sqf:copy-of" mode="sqf:xsm">
        <axsl:copy-of select="{@select}"/>
    </xsl:template>

</xsl:stylesheet>
