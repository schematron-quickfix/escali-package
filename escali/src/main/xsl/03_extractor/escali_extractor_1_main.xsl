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




    <xsl:include href="../01_compiler/escali_compiler_0_functions.xsl"/>
    
    <xsl:template match="/es:escali-reports">
        
        
        <axsl:stylesheet version="2.0" exclude-result-prefixes="#all">
            <xsl:attribute name="xml:base" select="/es:escali-reports/es:meta/@schema"/>

            <xsl:apply-templates select="es:meta/es:ns-prefix-in-attribute-values" mode="sqf:xsm"/>

            <axsl:include href="{resolve-uri('../01_compiler/escali_compiler_0_functions.xsl')}"/>

            <xsl:apply-templates select="es:meta/(xsl:* | sch:*)" mode="sqf:xsm"/>

            <xsl:apply-templates select="es:pattern/es:meta/sch:let" mode="sqf:xsm"/>

            <axsl:template match="/">
                <axsl:variable name="sqf:document" select="/"/>
                <axsl:variable name="sqf:manipulator-body" as="node()*">
                    <axsl:apply-templates/>
                </axsl:variable>
                <empty/>
                <axsl:for-each-group select="$sqf:manipulator-body" group-by="base-uri(.)">
                    <axsl:result-document href="{{concat(current-grouping-key(), '.xsm')}}">
                        <xsm:manipulator document="{{current-grouping-key()}}">


                            <axsl:for-each-group select="current-group()[@sqf:markAttributeChange]" group-by="
                                    if (@sqf:markAttributeChange = 'this') then
                                        @node
                                    else
                                        es:getNodePath(es:nodeByPath(@node, $sqf:document)/parent::*, true())">
                                <xsm:add position="before">
                                    <axsl:attribute name="node" select="current-grouping-key()"/>
                                    <xsm:content>
                                        <axsl:processing-instruction name="sqfc-start"/>
                                    </xsm:content>
                                </xsm:add>
                                <xsm:add position="first-child">
                                    <axsl:attribute name="node" select="current-grouping-key()"/>
                                    <xsm:content>
                                        <axsl:processing-instruction name="sqfc-end"/>
                                    </xsm:content>
                                </xsm:add>
                            </axsl:for-each-group>
                            
                            <axsl:apply-templates select="es:xsmActionOrder(current-group())" mode="cleanup"/>
                            
                        </xsm:manipulator>


                    </axsl:result-document>

                </axsl:for-each-group>

            </axsl:template>

            <xsl:apply-templates select=".//(es:assert | es:report)"/>
            
            <axsl:template match="*">
                <axsl:apply-templates select="@*"/>
                <axsl:apply-templates/>
            </axsl:template>
            
            <axsl:template match="@*|text()|processing-instruction()|comment()" priority="-1"/>

            <axsl:template match="@sqf:markAttributeChange | @xml:base" mode="cleanup"/>

            <!-- 
                copies all nodes:
            -->
            <axsl:template match="node() | @*" mode="cleanup">
                <axsl:copy>
                    <axsl:apply-templates select="@*" mode="#current"/>
                    <axsl:apply-templates select="node()" mode="#current"/>
                </axsl:copy>
            </axsl:template>

        </axsl:stylesheet>
    </xsl:template>



    <xsl:template match="es:assert | es:report">
        <xsl:apply-templates select="sqf:fix">
            <xsl:with-param name="location" select="@location/string()" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="key('fix-id', $id)">
        <xsl:for-each select="sqf:user-entry">
            <axsl:param>
                <xsl:apply-templates select="@name | @type | @default" mode="sqf:xsm"/>
                <xsl:sequence select="namespace::*"/>
            </axsl:param>
        </xsl:for-each>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="sqf:user-entry/*"/>



    <xsl:template match="key('fix-id', $id)/sqf:call-fix">
        <xsl:apply-templates select="es:getRefFix(@ref)" mode="sqf:xsm">
            <xsl:with-param name="params" select="sqf:with-param" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="sqf:call-fix" mode="sqf:xsm">
        <axsl:for-each select=".">
            <xsl:apply-templates select="es:getRefFix(@ref)/(sqf:param | sch:* | xsl:* | sqf:delete | sqf:add | sqf:replace | sqf:stringReplace | sqf:call-fix)" mode="sqf:xsm">
                <xsl:with-param name="params" select="sqf:with-param" tunnel="yes"/>
            </xsl:apply-templates>
        </axsl:for-each>
    </xsl:template>

    <xsl:template match="sqf:fix"/>



    <xsl:function name="es:getElementsInScope">
        <xsl:param name="context"/>
        <xsl:sequence select="es:getElementsInScope($context, ('http://purl.oclc.org/dsdl/schematron', ''))"/>
    </xsl:function>
    <xsl:function name="es:getElementsInScope">
        <xsl:param name="context"/>
        <xsl:param name="namespaces"/>
        <xsl:variable name="ancestorChilds" select="$context/ancestor::*/*"/>
        <xsl:variable name="precedings" select="$context/preceding::*"/>
        <xsl:variable name="precedingAncestorChilds" select="$ancestorChilds intersect $precedings"/>
        <xsl:variable name="precedingAncestorChildsNS" select="$precedingAncestorChilds[namespace-uri(.) = $namespaces]"/>
        <xsl:sequence select="$precedingAncestorChildsNS"/>
    </xsl:function>
    
    <xsl:variable name="global-meta" select="/es:escali-reports/es:meta"/>

    <xsl:template match="sqf:fix" mode="sqf:xsm">
        <xsl:param name="location" tunnel="yes" as="xs:string"/>

        <axsl:template match="{$location}">

            <xsl:apply-templates select="ancestor::es:pattern/es:meta/sch:let" mode="sqf:letpattern"/>
            
            <xsl:variable name="ancestorMeta" select="(ancestor::es:meta except $global-meta)"/>
            
            <xsl:variable name="precedingForeigns" select="$ancestorMeta/* intersect (preceding::sch:* | preceding::xsl:*)"/>

            <xsl:apply-templates select="$precedingForeigns | ./sch:* | ./xsl:* | sqf:param | sqf:delete | sqf:add | sqf:replace | sqf:stringReplace | sqf:call-fix" mode="#current"/>

            <axsl:next-match/>
        </axsl:template>
    </xsl:template>

    <xsl:template match="sqf:param" mode="sqf:xsm">
        <xsl:param name="params" tunnel="yes"/>
        <xsl:variable name="name" select="@name"/>
        <xsl:variable name="withParam" select="$params[@name = $name]"/>
        <axsl:variable name="{@name}">
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:sequence select="namespace::*"/>
            <xsl:sequence select="$withParam/@select"/>
        </axsl:variable>
    </xsl:template>

    <xsl:template match="sqf:*/@type" mode="sqf:xsm">
        <xsl:attribute name="as" select="."/>
    </xsl:template>

    <xsl:template match="sqf:*/@default" mode="sqf:xsm">
        <xsl:attribute name="select" select="."/>
    </xsl:template>

    <xsl:template match="sqf:replace | sqf:add | sqf:delete" mode="sqf:xsm" priority="50">
        <xsl:variable name="match" select="(@match, '.')[1]"/>
        <axsl:for-each select="{$match}">
            <axsl:variable name="xsm:content" as="node()*">
                <xsl:next-match/>
            </axsl:variable>
            <xsl:variable name="xsm-node" select="
                    if (self::sqf:add) then
                        ('(if(. instance of attribute()) then .. else .)')
                    else
                        ('.')
                    "/>
            <axsl:variable name="xsm:node" select="es:getNodePath({$xsm-node}, true())"/>
            <xsl:variable name="node-type" select="
                    if (local-name(.) = 'delete') then
                        'replace'
                    else
                        local-name(.)"/>

            <xsl:element name="xsm:{$node-type}">
                <axsl:attribute name="xml:base" select="base-uri(.)"/>
                <axsl:sequence select="
                      if (. instance of attribute() or {@position = ('after', 'before')}()) 
                    then (parent::*/namespace::*) 
                    else namespace::*
                    "/>
                <axsl:attribute name="node" select="$xsm:node"/>
                <xsl:if test="$markChanges">
                    <xsl:variable name="markerTest" select="
                            if (self::sqf:delete) then
                                '$xsm:content/@* or . instance of attribute()'
                            else
                                '$xsm:content/@*'
                            "/>
                    <axsl:if test="{$markerTest}">
                        <axsl:attribute name="sqf:markAttributeChange" select="
                                if (. instance of attribute()and {not(self::sqf:add)}()) then
                                    'parent'
                                else
                                    'this'
                                "/>
                    </axsl:if>
                </xsl:if>
                <axsl:sequence select="$xsm:content"/>
            </xsl:element>
        </axsl:for-each>
    </xsl:template>

    <xsl:template match="sqf:delete" mode="sqf:xsm">
        <xsm:content>
            <xsl:if test="$markChanges">
                <axsl:processing-instruction name="sqfc-start"/>
                <axsl:processing-instruction name="sqfc-end"/>
            </xsl:if>
        </xsm:content>
    </xsl:template>


    <xsl:template match="sqf:add" mode="sqf:xsm" priority="25">
        <axsl:attribute name="position" select="es:position-consisty-check('{(@position, 'first-child')[1]}', .)"/>
        <xsl:next-match/>
    </xsl:template>

    <xsl:template match="sqf:add | sqf:replace" mode="sqf:xsm">
        <axsl:variable name="sqf:content" as="item()*">
            <xsl:sequence select="namespace::*"/>
            <xsl:choose>
                <xsl:when test="@node-type = 'keep'">
                    <xsl:sequence select="es:nodeKeepCreation(.)"/>
                </xsl:when>
                <xsl:when test="@target | @node-type">
                    <xsl:variable name="node-type" select="
                            if (@node-type = 'pi') then
                                ('processing-instruction')
                            else
                                (@node-type)"/>
                    <xsl:element name="xsl:{$node-type}">
                        <xsl:if test="@target">
                            <xsl:attribute name="name" select="@target"/>
                        </xsl:if>
                        <xsl:if test="$node-type = 'attribute'">
                            <xsl:attribute name="separator" select="' '"/>
                        </xsl:if>
                        <xsl:apply-templates select="@select | node()" mode="#current"/>
                    </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="@select | node()" mode="#current"/>
                </xsl:otherwise>
            </xsl:choose>

        </axsl:variable>
        <axsl:variable name="sqf:attrContent" select="$sqf:content[es:is-attribute(.)]"/>
        <axsl:variable name="sqf:noAttrContent" select="$sqf:content[not(es:is-attribute(.))]"/>
        <xsm:content>
            <axsl:copy-of select="$sqf:attrContent" copy-namespaces="no"/>
            <xsl:if test="$markChanges">
                <axsl:if test="count($sqf:noAttrContent) gt 0">
                    <axsl:processing-instruction name="sqfc-start"/>
                </axsl:if>
            </xsl:if>
            <axsl:copy-of select="$sqf:noAttrContent" copy-namespaces="no"/>
            <xsl:if test="$markChanges">
                <axsl:if test="count($sqf:noAttrContent) gt 0">
                    <axsl:processing-instruction name="sqfc-end"/>
                </axsl:if>
            </xsl:if>
        </xsm:content>
    </xsl:template>

    <xsl:function name="es:nodeKeepCreation" as="element(xsl:choose)">
        <xsl:param name="addReplace" as="element()"/>
        <xsl:variable name="content">
            <xsl:apply-templates select="$addReplace/(@select | node())" mode="sqf:xsm"/>
        </xsl:variable>
        <axsl:choose>
            <axsl:when test=". instance of element()">
                <axsl:element name="{$addReplace/@target}">
                    <xsl:sequence select="$content"/>
                </axsl:element>
            </axsl:when>
            <axsl:when test=". instance of attribute()">
                <axsl:attribute name="{$addReplace/@target}">
                    <xsl:sequence select="$content"/>
                </axsl:attribute>
            </axsl:when>
            <axsl:when test=". instance of processing-instruction()">
                <axsl:processing-instruction name="{$addReplace/@target}">
                    <xsl:sequence select="$content"/>
                </axsl:processing-instruction>
            </axsl:when>
            <axsl:when test=". instance of comment()">
                <axsl:comment>
                    <xsl:sequence select="$content"/>
                </axsl:comment>
            </axsl:when>
            <axsl:when test=". instance of text()">
                <axsl:variable name="sqf:text-content" as="item()*">
                    <xsl:sequence select="$content"/>
                </axsl:variable>
                <axsl:value-of select="$sqf:text-content"/>
            </axsl:when>
            <axsl:otherwise/>
        </axsl:choose>
    </xsl:function>
    
    <xsl:template match="sqf:copy-of[@unparsed-mode = 'true']" mode="sqf:xsm" priority="10">
        <axsl:for-each select="{(@select, 'node()')[1]}">
            <xsm:copy select="{{ es:getNodePath(.) }}"/>
        </axsl:for-each>
    </xsl:template>
    
    <xsl:template match="sqf:keep | sqf:copy-of" mode="sqf:xsm">
        <axsl:copy-of select="{(@select, 'node()')[1]}"/>
    </xsl:template>

    <xsl:template match="sqf:add/@select | sqf:replace/@select | sqf:stringReplace/@select" mode="sqf:xsm">
        <axsl:sequence select="{.}"/>
    </xsl:template>


    <xsl:template match="sqf:stringReplace" mode="sqf:xsm">
        <xsl:variable name="match" select="(@match, '.')[1]"/>
        <axsl:variable name="sqf:nodePath" select="es:getNodePath({$match}, true())"/>
        <axsl:variable name="sqf:stringReplace">
            <axsl:analyze-string select="{$match}" regex="{@regex}">
                <xsl:sequence select="@flags"/>
                <axsl:matching-substring>
                    <xsm:step replace="true">
                        <axsl:attribute name="length" select="string-length(.)"/>
                        <xsl:if test="$markChanges">
                            <axsl:processing-instruction name="sqfc-start"/>
                        </xsl:if>
                        <xsl:apply-templates select="@select | node()" mode="#current"/>
                        <xsl:if test="$markChanges">
                            <axsl:processing-instruction name="sqfc-end"/>
                        </xsl:if>
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

    <xsl:template match="node() | @*" mode="sqf:xsm">
        <xsl:copy copy-namespaces="no">
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="xsl:*" mode="sqf:xsm">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>

    <xsl:variable name="pattern-namespaces" as="node()*">
        <xsl:for-each select="/es:escali-reports/es:pattern/es:meta">
            <xsl:namespace name="{@id}" select="concat('http://www.escali.schematron-quickfix.com/', @id)"/>
        </xsl:for-each>
    </xsl:variable>

    <xsl:template match="es:pattern/es:meta/sch:let" mode="sqf:xsm">
        <xsl:variable name="pid" select="../@id"/>
        <axsl:variable name="{$pid}:{@name}" select="{@value}">
            <xsl:sequence select="$pattern-namespaces[name() = $pid]"/>
        </axsl:variable>
    </xsl:template>

    <xsl:template match="es:pattern/es:meta/sch:let" mode="sqf:letpattern">
        <xsl:variable name="pid" select="../@id"/>
        <axsl:variable name="{@name}" select="${$pid}:{@name}">
            <xsl:sequence select="$pattern-namespaces[name() = $pid]"/>
        </axsl:variable>
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

    <!--    
        Namespace handling
    
    -->

    <xsl:template match="es:ns-prefix-in-attribute-values" mode="sqf:xsm">
        <xsl:choose>
            <xsl:when test="@prefix != ''">
                <xsl:namespace name="{@prefix}" select="@uri"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="xpath-default-namespace" select="@uri"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
