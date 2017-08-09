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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias" xmlns:bxsl="http://www.w3.org/1999/XSL/TransformAliasAlias" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsm="http://www.schematron-quickfix.com/manipulator/process" exclude-result-prefixes="xs xd" version="2.0">
    <!--
    Implementation of sqf:DELETE
-->

    <xsl:template match="sqf:delete" mode="xsm:no-xsm">
        <xsl:variable name="sqf:match" select=" if (@match) then (@match) else ('.')"/>
        <axsl:choose>
            <axsl:when test="({$sqf:match})[1] instance of attribute()">
                <bxsl:attribute select="'deleted'">
                    <xsl:namespace name="{$sqf:changePrefix}">http://www.schematron-quickfix.com/validator/process/changes</xsl:namespace>
                    <xsl:attribute name="name">
                        <xsl:value-of select="$sqf:changePrefix"/>
                        <xsl:text>:{replace(name(), ':', '_')}</xsl:text>
                    </xsl:attribute>
                </bxsl:attribute>
            </axsl:when>
            <axsl:otherwise>
                <xsl:call-template name="sqf:changeMarker"/>
                <xsl:call-template name="sqf:changeMarker">
                    <xsl:with-param name="start" select="false()"/>
                </xsl:call-template>
            </axsl:otherwise>
        </axsl:choose>
    </xsl:template>

    <!--
        Implementation of sqf:REPLACE
    -->


    <xsl:template match="sqf:replace" mode="xsm:no-xsm">
        <xsl:param name="fixId" tunnel="yes"/>
        <xsl:variable name="sqf:match" select=" if (@match) then (@match) else ('.')"/>
        
        <bxsl:variable name="sqf:nodeFac" as="node()*">
            <xsl:call-template name="nodeFac"/>
        </bxsl:variable>
        <bxsl:choose>
            <bxsl:when test=". instance of attribute()">
                <bxsl:if test="$sqf:nodeFac[not(. instance of attribute())]">
                    <bxsl:message terminate="yes">
                        <xsl:text>Only an attribute can replaced by an attribute!</xsl:text>
                        <xsl:text> (FixId: </xsl:text>
                        <xsl:value-of select="$fixId"/>
                        <xsl:text>, Context: </xsl:text>
                        <axsl:value-of select="es:getNodePath(.)"/>
                        <xsl:text>)</xsl:text>
                    </bxsl:message>
                </bxsl:if>
                <bxsl:copy-of select="$sqf:nodeFac"/>
                <bxsl:attribute select="'replaced'">
                    <xsl:namespace name="{$sqf:changePrefix}">http://www.schematron-quickfix.com/validator/process/changes</xsl:namespace>
                    <xsl:attribute name="name">
                        <xsl:value-of select="$sqf:changePrefix"/>
                        <xsl:text>:{replace((name(), 'null-name')[.!=''][1], ':', '_')}</xsl:text>
                    </xsl:attribute>
                </bxsl:attribute>
            </bxsl:when>
            <bxsl:otherwise>
                <bxsl:if test="$sqf:nodeFac[. instance of attribute()]">
                    <bxsl:message terminate="yes">
                        <xsl:text>Only an attribute can replaced by an attribute!</xsl:text>
                        <xsl:text> (FixId: </xsl:text>
                        <xsl:value-of select="$fixId"/>
                        <xsl:text>, Context: </xsl:text>
                        <axsl:value-of select="es:getNodePath(.)"/>
                        <xsl:text>)</xsl:text>
                    </bxsl:message>
                </bxsl:if>
                <xsl:call-template name="sqf:changeMarker"/>
                <bxsl:copy-of select="$sqf:nodeFac"/>
                <xsl:call-template name="sqf:changeMarker">
                    <xsl:with-param name="start" select="false()"/>
                </xsl:call-template>
            </bxsl:otherwise>
        </bxsl:choose>
        
    </xsl:template>


    <!--
        Implementation of sqf:STRINGREPLACE
    -->
    <xsl:template match="sqf:stringReplace" mode="xsm:no-xsm">
        <bxsl:analyze-string select="." regex="{@regex}">
            <xsl:copy-of select="@flags"/>
            <bxsl:matching-substring>
                <xsl:call-template name="sqf:changeMarker">
                    <xsl:with-param name="context" select="'$sqf:activityContext'"/>
                </xsl:call-template>
                <xsl:apply-templates select="@select | node()" mode="template"/>
                <xsl:call-template name="sqf:changeMarker">
                    <xsl:with-param name="start" select="false()"/>
                    <xsl:with-param name="context" select="'$sqf:activityContext'"/>
                </xsl:call-template>
            </bxsl:matching-substring>
            <bxsl:non-matching-substring>
                <bxsl:value-of select="."/>
            </bxsl:non-matching-substring>
        </bxsl:analyze-string>
    </xsl:template>
    <!--
        Implementation of sqf:ADD
    -->
    <xsl:template match="sqf:add" mode="xsm:no-xsm">
        <xsl:choose>
            <xsl:when test="@position = ('after', 'before')">
                <xsl:call-template name="sqf:changeMarker"/>
                <xsl:call-template name="nodeFac"/>
                <xsl:call-template name="sqf:changeMarker">
                    <xsl:with-param name="start" select="false()"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="nodeFac"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="xsm:no-save-add">
        <bxsl:variable name="sqf:addAttributes" select="($sqf:addChilds | $sqf:addLastChilds)[. instance of attribute()]"/>
        <bxsl:variable name="sqf:addChilds" select="$sqf:addChilds except $sqf:addAttributes"/>
        <bxsl:variable name="sqf:addLastChilds" select="$sqf:addLastChilds except $sqf:addAttributes"/>

        <bxsl:if test="$sqf:addAttributes and (. instance of element() or . instance of attribute())">
            <xsl:call-template name="sqf:changeMarker">
                <xsl:with-param name="label" select="concat('attribute-', local-name(), '-change')"/>
            </xsl:call-template>
        </bxsl:if>
        <axsl:variable name="es:new-content">
            <axsl:if test=". instance of element() or . instance of attribute()">
                <bxsl:apply-templates select="@*" mode="#current"/>
                <bxsl:copy-of select="$sqf:addAttributes"/>
                <bxsl:if test="$sqf:addAttributes">
                    <xsl:call-template name="sqf:changeMarker">
                        <xsl:with-param name="label" select="concat('attribute-', local-name(), '-change')"/>
                        <xsl:with-param name="start" select="false()"/>
                    </xsl:call-template>
                </bxsl:if>
            </axsl:if>
            <bxsl:if test="$sqf:addChilds">
                <xsl:call-template name="sqf:changeMarker"/>
            </bxsl:if>
            <bxsl:copy-of select="$sqf:addChilds"/>
            <bxsl:if test="$sqf:addChilds">
                <xsl:call-template name="sqf:changeMarker">
                    <xsl:with-param name="start" select="false()"/>
                </xsl:call-template>
            </bxsl:if>
            <axsl:if test=". instance of element() or . instance of document-node() or . instance of attribute()">
                <bxsl:apply-templates select="node()" mode="#current"/>
            </axsl:if>
            <bxsl:if test="$sqf:addLastChilds">
                <xsl:call-template name="sqf:changeMarker"/>
            </bxsl:if>
            <bxsl:copy-of select="$sqf:addLastChilds"/>
            <bxsl:if test="$sqf:addLastChilds">
                <xsl:call-template name="sqf:changeMarker">
                    <xsl:with-param name="start" select="false()"/>
                </xsl:call-template>
            </bxsl:if>
        </axsl:variable>
        <axsl:choose>
            <axsl:when test=". instance of element() or . instance of document-node() or . instance of attribute()">
                <bxsl:copy>
                    <axsl:copy-of select="$es:new-content"/>
                </bxsl:copy>
            </axsl:when>
            <axsl:otherwise>
                <bxsl:copy/>
                <axsl:copy-of select="$es:new-content"/>
            </axsl:otherwise>
        </axsl:choose>
    </xsl:template>
    <xsl:template name="sqf:changeMarker">
        <xsl:param name="label" select="local-name()"/>
        <xsl:param name="start" select="true()" as="xs:boolean"/>
        <xsl:param name="context" select="'.'"/>
        <bxsl:processing-instruction name="{$sqf:changePrefix}-{if ($start) then ('start') else ('end')}" sqf:changeMarker="true">
            <bxsl:text>
                <xsl:value-of select="concat($label, '-')"/>
            </bxsl:text>
            <bxsl:value-of select="generate-id({$context})"/>
        </bxsl:processing-instruction>
    </xsl:template>
</xsl:stylesheet>
