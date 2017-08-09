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
    <xsl:template match="sqf:delete" mode="xsm:save-mode">
        <xsl:param name="es:regex" tunnel="yes"/>
        <bxsl:variable name="sqf:node" select="es:getNodePath(.)"/>
        <bxsl:choose>
            <bxsl:when test="(. instance of attribute()) and '{$es:regex != ''}' = 'true'">
                <bxsl:variable name="sqf:nodeFac">
                    <xsm:delete>
                        <axsl:attribute name="start-position" select="$es:substrings[1] - 1"/>
                        <axsl:attribute name="end-position" select="$es:substrings[2] - 1"/>
                        <bxsl:attribute name="node" select="es:getNodePath(.)"/>
                    </xsm:delete>
                </bxsl:variable>
                <xsl:call-template name="xsm:addChangeMarker">
                    <xsl:with-param name="position" select="'attribute'"/>
                    <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                </xsl:call-template>
            </bxsl:when>
            <bxsl:when test=". instance of attribute()">
                <bxsl:variable name="sqf:parent" select="es:getNodePath(..)"/>
                <xsm:add position="before">
                    <bxsl:attribute name="node" select="$sqf:parent"/>
                    <xsm:content>
                        <bxsl:processing-instruction name="{$sqf:changePrefix}-start" sqf:changeMarker="true">
                            <bxsl:text>delete-</bxsl:text>
                            <bxsl:value-of select="generate-id()"/>
                        </bxsl:processing-instruction>
                    </xsm:content>
                </xsm:add>
                <xsm:add position="first-child">
                    <bxsl:attribute name="node" select="$sqf:parent"/>
                    <xsm:content>
                        <bxsl:processing-instruction name="{$sqf:changePrefix}-end" sqf:changeMarker="true">
                            <bxsl:text>delete-</bxsl:text>
                            <bxsl:value-of select="generate-id()"/>
                        </bxsl:processing-instruction>
                    </xsm:content>
                </xsm:add>
                <xsm:delete>
                    <bxsl:attribute name="node" select="$sqf:node"/>
                </xsm:delete>
            </bxsl:when>
            <bxsl:when test="(. instance of comment() or . instance of processing-instruction()) and '{$es:regex != ''}' = 'true'">
                <bxsl:variable name="sqf:nodeFac">
                    <xsm:delete>
                        <axsl:attribute name="start-position" select="$es:substrings[1] - 1"/>
                        <axsl:attribute name="end-position" select="$es:substrings[2] - 1"/>
                        <bxsl:attribute name="node" select="es:getNodePath(.)"/>
                    </xsm:delete>
                </bxsl:variable>
                <xsl:call-template name="xsm:addChangeMarker">
                    <xsl:with-param name="position" select="'regexReplace'"/>
                    <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                </xsl:call-template>
            </bxsl:when>
            <bxsl:otherwise>
                <xsm:replace>
                    <xsl:if test="$es:regex">
                        <axsl:attribute name="start-position" select="$es:substrings[1] - 1"/>
                        <axsl:attribute name="end-position" select="$es:substrings[2] - 1"/>
                    </xsl:if>
                    <bxsl:attribute name="node" select="$sqf:node"/>
                    <xsm:content>
                        <bxsl:processing-instruction name="{$sqf:changePrefix}-start" sqf:changeMarker="true">
                            <bxsl:text>delete-</bxsl:text>
                            <bxsl:value-of select="generate-id()"/>
                        </bxsl:processing-instruction>
                        <bxsl:processing-instruction name="{$sqf:changePrefix}-end" sqf:changeMarker="true">
                            <bxsl:text>delete-</bxsl:text>
                            <bxsl:value-of select="generate-id()"/>
                        </bxsl:processing-instruction>
                    </xsm:content>
                </xsm:replace>
            </bxsl:otherwise>
        </bxsl:choose>
    </xsl:template>

    <!--
        Implementation of sqf:REPLACE
    -->
    <xsl:template match="sqf:replace" mode="xsm:save-mode">
        <xsl:param name="es:regex" tunnel="yes"/>
        <xsl:param name="fixId" tunnel="yes"/>
        <xsl:variable name="sqf:match" select="
                if (@match) then
                    (@match)
                else
                    ('self::node()')"/>
        <axsl:variable name="match">
            <xsl:call-template name="nodeMatching">
                <xsl:with-param name="nodes" select="$sqf:match"/>
            </xsl:call-template>
        </axsl:variable>

        <bxsl:variable name="sqf:source-node" select="."/>
        <bxsl:variable name="sqf:source-node-type" select="es:getNodeType($sqf:source-node)"/>

        <bxsl:variable name="sqf:nodeFac" as="node()*">
            <xsl:call-template name="nodeFac"/>
        </bxsl:variable>
        <bxsl:variable name="sqf:result-node-types" select="$sqf:nodeFac/es:getNodeType(.)"/>
        <bxsl:variable name="sqf:isRegex" select="'{$es:regex != ''}' = 'true'" as="xs:boolean"/>
        <bxsl:choose>
            <bxsl:when test="$sqf:result-node-types = 'attribute'">
                <bxsl:variable name="sqf:nodeFac">
                    <xsm:replace>
                        <bxsl:attribute name="node" select="es:getNodePath(.)"/>
                        <xsm:content>
                            <bxsl:copy-of select="$sqf:nodeFac"/>
                        </xsm:content>
                    </xsm:replace>
                </bxsl:variable>
                <bxsl:if test="not($sqf:source-node-type = 'attribute')">
                    <bxsl:message terminate="yes">
                        <xsl:text>Only an attribute can replaced by an attribute!</xsl:text>
                        <xsl:text> (FixId: </xsl:text>
                        <xsl:value-of select="$fixId"/>
                        <xsl:text>, Context: </xsl:text>
                        <axsl:value-of select="es:getNodePath(.)"/>
                        <xsl:text>)</xsl:text>
                    </bxsl:message>
                </bxsl:if>
                <xsl:call-template name="xsm:addChangeMarker">
                    <xsl:with-param name="position" select="'attribute'"/>
                    <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                </xsl:call-template>
            </bxsl:when>
            <bxsl:when test="
                    ($sqf:source-node-type = 'attribute') and $sqf:isRegex">
                <bxsl:variable name="sqf:nodeFac">
                    <xsm:replace>
                        <axsl:attribute name="start-position" select="$es:substrings[1] - 1"/>
                        <axsl:attribute name="end-position" select="$es:substrings[2] - 1"/>
                        <bxsl:attribute name="node" select="es:getNodePath(.)"/>
                        <xsm:content>
                            <bxsl:value-of select="$sqf:nodeFac"/>
                        </xsm:content>
                    </xsm:replace>
                </bxsl:variable>
                <xsl:call-template name="xsm:addChangeMarker">
                    <xsl:with-param name="position" select="'attribute'"/>
                    <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                </xsl:call-template>
            </bxsl:when>
            <bxsl:when test="
                    ($sqf:source-node-type = ('comment', 'processing-instruction'))
                    and $sqf:isRegex">
                <bxsl:variable name="sqf:nodeFac">
                    <xsm:replace>
                        <axsl:attribute name="start-position" select="$es:substrings[1] - 1"/>
                        <axsl:attribute name="end-position" select="$es:substrings[2] - 1"/>
                        <bxsl:attribute name="node" select="es:getNodePath(.)"/>
                        <xsm:content>
                            <bxsl:copy-of select="$sqf:nodeFac"/>
                        </xsm:content>
                    </xsm:replace>
                </bxsl:variable>
                <xsl:call-template name="xsm:addChangeMarker">
                    <xsl:with-param name="position" select="'regexReplace'"/>
                    <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                </xsl:call-template>
            </bxsl:when>

            <bxsl:otherwise>
                <xsm:replace>
                    <xsl:if test="$es:regex">
                        <axsl:attribute name="start-position" select="$es:substrings[1] - 1"/>
                        <axsl:attribute name="end-position" select="$es:substrings[2] - 1"/>
                    </xsl:if>
                    <bxsl:attribute name="node" select="es:getNodePath(.)"/>
                    <xsm:content>
                        <xsl:call-template name="xsm:addChangeMarker">
                            <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                        </xsl:call-template>
                    </xsm:content>
                </xsm:replace>
            </bxsl:otherwise>
        </bxsl:choose>
        <!--<bxsl:if test="$sqf:nodeFac[. instance of attribute()">
            <bxsl:variable name="sqf:parent" select="es:getNodePath(..)"/>
            <xsm:add position="before">
                <bxsl:attribute name="node" select="$sqf:parent"/>
                <xsm:content>
                    <bxsl:processing-instruction name="{$sqf:changePrefix}-start" sqf:changeMarker="true">
                        <bxsl:text>attribute-change-</bxsl:text>
                        <bxsl:value-of select="generate-id()"/>
                    </bxsl:processing-instruction>
                </xsm:content>
            </xsm:add>
            <xsm:add position="first-child">
                <bxsl:attribute name="node" select="$sqf:parent"/>
                <xsm:content>
                    <bxsl:processing-instruction name="{$sqf:changePrefix}-end" sqf:changeMarker="true">
                        <bxsl:text>attribute-change-</bxsl:text>
                        <bxsl:value-of select="generate-id()"/>
                    </bxsl:processing-instruction>
                </xsm:content>
            </xsm:add>
            <axsl:if test="not(({$sqf:match}) instance of attribute())">
                <bxsl:message terminate="yes">
                    <xsl:text>Only an attribute can replaced by an attribute!</xsl:text>
                    <xsl:text> (FixId: </xsl:text>
                    <xsl:value-of select="$fixId"/>
                    <xsl:text>, Context: </xsl:text>
                    <axsl:value-of select="es:getNodePath(.)"/>
                    <xsl:text>)</xsl:text>
                </bxsl:message>
            </axsl:if>
        </bxsl:if>
        <xsm:replace>
            <bxsl:attribute name="node" select="es:getNodePath(.)"/>
            <xsm:content>
                <axsl:copy-of select="$nodeFac"/>
            </xsm:content>
        </xsm:replace>-->
    </xsl:template>
    <!--
        Implementation of sqf:STRINGREPLACE
    -->
    <xsl:template match="sqf:stringReplace" mode="xsm:save-mode">
        <bxsl:variable name="sqf:node" select="."/>
        <bxsl:variable name="sqf:stringReplace">
            <bxsl:analyze-string select="." regex="{@regex}">
                <xsl:copy-of select="@flags"/>
                <bxsl:matching-substring>
                    <xsm:step>
                        <bxsl:attribute name="length" select="string-length(.)"/>
                        <bxsl:variable name="sqf:nodeFac" as="node()*">
                            <xsl:apply-templates select="@select | node()" mode="template"/>
                        </bxsl:variable>
                        <xsl:call-template name="xsm:addChangeMarker">
                            <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                            <xsl:with-param name="generate-id" select="'generate-id($sqf:node)'"/>
                        </xsl:call-template>
                    </xsm:step>
                </bxsl:matching-substring>
                <bxsl:non-matching-substring>
                    <xsm:step>
                        <bxsl:attribute name="length" select="string-length(.)"/>
                    </xsm:step>
                </bxsl:non-matching-substring>
            </bxsl:analyze-string>
        </bxsl:variable>

        <bxsl:variable name="nodePath" select="es:getNodePath($sqf:node)"/>

        <bxsl:for-each select="$sqf:stringReplace/xsm:step[node()]">
            <xsm:replace>
                <bxsl:attribute name="node" select="$nodePath"/>
                <bxsl:variable name="sqf:start" select="sum(preceding-sibling::xsm:step/@length)"/>
                <bxsl:attribute name="start-position" select="$sqf:start"/>
                <bxsl:attribute name="end-position" select="$sqf:start + @length"/>
                <xsm:content>
                    <bxsl:copy-of select="node()"/>
                </xsm:content>
            </xsm:replace>
        </bxsl:for-each>
    </xsl:template>
    <!--
        Implementation of sqf:ADD
    -->
    <xsl:template match="sqf:add" mode="xsm:save-mode">
        <xsl:param name="es:regex" tunnel="yes"/>
        <xsl:variable name="sqf:match" select="
                        if (@match) then
                            (@match)
                        else
                            ('self::node()')"/>
                <axsl:variable name="sqf:node-type" select="'{@node-type}'"/>
                <axsl:variable name="sqf:source-node" select="({$sqf:match})"/>
        <axsl:variable name="sqf:source-node-type" select="es:getNodeType($sqf:source-node[1])"/>
        <axsl:variable name="sqf:result-node-type" select="
                if ($sqf:node-type = 'keep') then
                    ($sqf:source-node-type)
                else
                    if ($sqf:node-type = '') then
                        ('dynamic')
                    else
                        ($sqf:node-type)"/>
        <bxsl:variable name="sqf:position" select="'{@position}'"/>

                <bxsl:variable name="sqf:nodeFac" as="node()*">
                    <xsl:call-template name="nodeFac"/>
                </bxsl:variable>

<bxsl:for-each-group select="$sqf:nodeFac" group-by="es:getNodeType(.)">
                <bxsl:variable name="sqf:nodeFac" select="current-group()"/>
            <bxsl:for-each select="$sqf:activityContext">

                <bxsl:variable name="sqf:node-type" select="'{@node-type}'"/>
                <bxsl:variable name="sqf:source-node" select="."/>
                <bxsl:variable name="sqf:source-node-type" select="es:getNodeType($sqf:source-node)"/>

                <bxsl:variable name="sqf:result-node-type">
                    <xsl:attribute name="select">if ($sqf:node-type = '') then (es:getNodeType($sqf:nodeFac[1])) else ('{$sqf:result-node-type}')</xsl:attribute>
                </bxsl:variable>

                <bxsl:variable name="sqf:isRegex" select="'{$es:regex != ''}' = 'true'" as="xs:boolean"/>

                <!--
        Depending on $sqf:result-node-type, $sqf:source-node-type and $sqf:isRegex we need one xsm:add element only 
        or for the change PIs separate xsm:add elements.
        
        This is the logic matrix
        
        case    $sqf:source-node-type   $sqf:result-node-type       $sqf:isRegex=false  $sqf:isRegex=true
        1       comment                 any                         false               true
        2       pi                      any                         false               true
        3       element                 any except attribute        false               false
        4       element                 attribute                   true                n.a.
        5       text                    any                         false               false
        6       attribute               any (only text alowed)      n.a.                true
        
        
        
        -->
                <bxsl:variable name="sqf:attributeCase" select="($sqf:source-node-type, $sqf:result-node-type) = 'attribute'"/>
                <bxsl:variable name="sqf:changePi-separate-mode" as="xs:boolean" select="
                        (:case 4, 6:)
                        if ($sqf:attributeCase) then
                            true()
                        else
                            (:case 3, 5:)
                            if ($sqf:source-node-type = ('element', 'text')) then
                                false()
                            else
                                (:case 1, 2 regex=false :)
                                if ($sqf:source-node-type = ('comment', 'processing-instruction') and not($sqf:isRegex)) then
                                    false()
                                else
                                    (:case 1, 2 regex=true:)
                                    true()
                        "/>

                <bxsl:variable name="sqf:nodeFac">
                    <xsm:add>
                        <xsl:if test="$es:regex">
                            <axsl:attribute name="start-position" select="$es:substrings[1] - 1"/>
                            <axsl:attribute name="end-position" select="$es:substrings[2] - 1"/>
                        </xsl:if>

                        <bxsl:variable name="sqf:position" select="
                                if ($sqf:position = '') then
                                    (if ($sqf:result-node-type = 'attribute') then
                            ('attribute')
                            else
                                        ('first-child'))
                                
                        else
                                    if ($sqf:position = ('first-child', 'last-child') and $sqf:source-node-type = ('comment', 'processing-instruction', 'text'))
                        then
                        ('after')
                        else
                        ($sqf:position)"/>




                                        <bxsl:attribute name="position" select="$sqf:position"/>



                        <bxsl:attribute name="node" select="
                        es:getNodePath(
                                if (($sqf:source-node-type = 'attribute' and '{not($es:regex)}' = 'true'))
                        then
                            (..)
                        else
                            (.)
                        )"/>
                <xsm:content>
                    <bxsl:copy-of select="$sqf:nodeFac[es:getNodeType(.) = 'attribute']"/>
                    <bxsl:if test="$sqf:nodeFac[not(. instance of attribute())]">
                        <bxsl:choose>
                            <bxsl:when test="$sqf:changePi-separate-mode and $sqf:source-node-type = 'attribute'">
                                <bxsl:value-of select="$sqf:nodeFac[not(. instance of attribute())]"/>
                            </bxsl:when>
                            <bxsl:when test="$sqf:changePi-separate-mode">
                                <bxsl:copy-of select="$sqf:nodeFac[not(. instance of attribute())]"/>
                            </bxsl:when>
                            <bxsl:otherwise>
                                <xsl:call-template name="xsm:addChangeMarker">
                                    <xsl:with-param name="position" select="@position"/>
                                    <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                                </xsl:call-template>
                            </bxsl:otherwise>
                        </bxsl:choose>
                    </bxsl:if>
                </xsm:content>
            </xsm:add>
        </bxsl:variable>
        <bxsl:choose>
            <!--Do nothing, because this does not make sense:-->
                    <bxsl:when test="$sqf:source-node-type != 'element' and $sqf:result-node-type = 'attribute'"/>

                    <bxsl:when test="$sqf:changePi-separate-mode and $sqf:attributeCase">
                <xsl:call-template name="xsm:addChangeMarker">
                    <xsl:with-param name="position" select="'attribute'"/>
                    <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                </xsl:call-template>
            </bxsl:when>
            <bxsl:when test="$sqf:changePi-separate-mode">
                <xsl:call-template name="xsm:addChangeMarker">
                    <xsl:with-param name="position" select="'regexReplace'"/>
                    <xsl:with-param name="variableName" select="'sqf:nodeFac'"/>
                </xsl:call-template>
            </bxsl:when>
            <bxsl:otherwise>
                <bxsl:copy-of select="$sqf:nodeFac"/>
            </bxsl:otherwise>
        </bxsl:choose>
            </bxsl:for-each>
        </bxsl:for-each-group>

    </xsl:template>

    <xsl:template name="xsm:save-mode-add">
        <bxsl:variable name="sqf:addAttributes" select="($sqf:addChilds | $sqf:addLastChilds)[xsm:content/@*]"/>
        <bxsl:variable name="sqf:addChilds" select="$sqf:addChilds except $sqf:addAttributes"/>
        <bxsl:variable name="sqf:addLastChilds" select="$sqf:addLastChilds except $sqf:addAttributes"/>
        <bxsl:if test="$sqf:addAttributes and (. instance of element() or . instance of attribute())">
            <xsl:call-template name="xsm:addChangeMarker">
                <xsl:with-param name="position" select="'attribute'"/>
                <xsl:with-param name="variableName" select="'sqf:addAttributes'"/>
            </xsl:call-template>
        </bxsl:if>
        <bxsl:copy-of select="$sqf:addChilds | $sqf:addLastChilds"/>
        <bxsl:apply-templates select="@*" mode="#current"/>
        <bxsl:apply-templates select="node()" mode="#current"/>
    </xsl:template>

    <xsl:template name="xsm:addChangeMarker">
        <xsl:param name="position" select="'first-child'"/>
        <xsl:param name="variableName" required="yes"/>
        <xsl:param name="generate-id">generate-id()</xsl:param>
        <bxsl:variable name="startPi">
            <bxsl:processing-instruction name="{$sqf:changePrefix}-start" sqf:changeMarker="true">
                <bxsl:text>
                    <xsl:value-of select="
                            if ($position = 'attribute') then
                                ('attribute-change')
                            else
                                (local-name())"/>
                </bxsl:text>
                <bxsl:text>-</bxsl:text>
                <bxsl:value-of select="{$generate-id}"/>
            </bxsl:processing-instruction>
        </bxsl:variable>
        <bxsl:variable name="endPi">
            <bxsl:processing-instruction name="{$sqf:changePrefix}-end" sqf:changeMarker="true">
                <bxsl:text>
                    <xsl:value-of select="
                            if ($position = 'attribute') then
                                ('attribute-change')
                            else
                                (local-name())"/>
                </bxsl:text>
                <bxsl:text>-</bxsl:text>
                <bxsl:value-of select="{$generate-id}"/>
            </bxsl:processing-instruction>
        </bxsl:variable>
        <xsl:choose>
            <xsl:when test="$position = ('attribute', 'regexReplace')">
                <xsm:add position="before">
                    <bxsl:attribute name="node" select="
                            es:getNodePath(
                            if ((. instance of attribute()))
                            then
                                (..)
                            else
                                (.)
                            )"/>
                    <bxsl:attribute name="sqfc:changeMarkerGroup" select="{$generate-id}" xmlns:sqfc="http://www.schematron-quickfix.com/validator/process/changes"/>
                    <xsm:content>
                        <bxsl:copy-of select="$startPi"/>
                    </xsm:content>
                </xsm:add>
                <xsm:add position="{if ($position = 'attribute') then ('first-child') else ('after')}">
                    <bxsl:attribute name="node" select="
                            es:getNodePath(
                            if ((. instance of attribute()))
                            then
                                (..)
                            else
                                (.)
                            )"/>
                    <bxsl:attribute name="sqfc:changeMarkerGroup" select="{$generate-id}" xmlns:sqfc="http://www.schematron-quickfix.com/validator/process/changes"/>
                    <xsm:content>
                        <bxsl:copy-of select="$endPi"/>
                    </xsm:content>
                </xsm:add>
                <bxsl:copy-of select="${$variableName}"/>
            </xsl:when>
            <xsl:otherwise>
                <bxsl:copy-of select="$startPi"/>
                <bxsl:copy-of select="${$variableName}[not(. instance of attribute())]"/>
                <bxsl:copy-of select="$endPi"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
