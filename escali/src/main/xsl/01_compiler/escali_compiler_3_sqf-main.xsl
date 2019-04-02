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

    <xsl:key name="elementsByNamespace" match="*" use="namespace-uri()"/>
    <xsl:param name="sqf:useSQF" select="exists(key('elementsByNamespace', 'http://www.schematron-quickfix.com/validator/process') | //@es:ignorableId)"/>

    <xsl:function name="es:fix-for-fired-rule" as="node()*">
        <xsl:param name="rule" as="element(sch:rule)"/>
        <xsl:apply-templates select="$rule/sqf:fix" mode="sqf:fix-for-fired-rule"/>
    </xsl:function>


    <xsl:variable name="globalFixes" select="/*/sqf:fixes/sqf:fix"/>
    <xsl:variable name="globalGroups" select="/*/sqf:fixes/sqf:group"/>

    <xsl:function name="es:fix-for-tests" as="node()*">
        <xsl:param name="test" as="element()"/>


        <xsl:if test="$sqf:useSQF">

            <xsl:variable name="fix-calls" select="tokenize($test/@sqf:fix, '\s+')"/>
            <xsl:variable name="localFixes" select="$test/../sqf:fix"/>
            <xsl:variable name="localGroups" select="$test/../sqf:group"/>

            <xsl:variable name="availableFixes" select="
                    $localFixes | $globalFixes[not(@id = ($localFixes/@id,
                    $localGroups/@id))]"/>
            <xsl:variable name="availableGroups" select="
                    $localGroups | $globalGroups[not(@id = ($localGroups/@id,
                    $localFixes/@id))]"/>
            
            <axsl:variable name="sqf:rule-context" select="."/>


            <xsl:apply-templates select="
                    ($availableFixes | $availableGroups)[@id = $fix-calls]
                    " mode="sqf:fix-for-tests">
                <xsl:with-param name="test" select="$test" tunnel="yes"/>
            </xsl:apply-templates>

        </xsl:if>
    </xsl:function>

    <xsl:template match="sqf:fix/sqf:description" mode="sqf:fix-for-fired-rule"/>

    <xsl:template match="sqf:user-entry" mode="sqf:fix-for-fired-rule">
        <sqf:param>
            <xsl:copy-of select="@name | @type"/>
        </sqf:param>
    </xsl:template>
    
    <xsl:template match="sqf:fix/@use-when | sqf:fix/@use-for-each" mode="sqf:fix-for-fired-rule"/>
        
    <xsl:template match="sqf:fix[@use-for-each]" mode="sqf:fix-for-fired-rule-add-first-child">
        <sqf:param name="sqf:current"/>
    </xsl:template>
    
    <xsl:template match="node() | @*" mode="sqf:fix-for-fired-rule-add-first-child sqf:fix-for-fired-rule-add-last-child"/>
    
    
    <xsl:template match="@*[matches(., '[{}]')]" mode="sqf:fix-for-fired-rule">
        <xsl:attribute name="{name()}">
            <xsl:analyze-string select="." regex="[{{}}]">
                <xsl:matching-substring>
                    <xsl:sequence select="concat(.,.)"/>
                </xsl:matching-substring>
                <xsl:non-matching-substring>
                    <xsl:sequence select="."/>
                </xsl:non-matching-substring>
            </xsl:analyze-string>
        </xsl:attribute>
    </xsl:template>
    
    <!-- 
        copies all nodes:
    -->
    <xsl:template match="node() | @*" mode="sqf:fix-for-fired-rule">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="." mode="sqf:fix-for-fired-rule-add-first-child"/>
            <xsl:apply-templates select="node()" mode="#current"/>
            <xsl:apply-templates select="." mode="sqf:fix-for-fired-rule-add-last-child"/>
        </xsl:copy>
    </xsl:template>


    <xsl:template match="sqf:fix[@use-for-each]" mode="sqf:fix-for-tests">
        <axsl:for-each select="{@use-for-each}">
            <axsl:variable name="sqf:current" select="."/>
            <axsl:variable name="sqf:current-position" select="position()"/>
            <axsl:for-each select="$sqf:rule-context">

                <xsl:next-match>
                    <xsl:with-param name="id-suffix" tunnel="yes">
                        <axsl:text>#</axsl:text>
                        <axsl:value-of select="$sqf:current-position"/>
                    </xsl:with-param>
                </xsl:next-match>
                
            </axsl:for-each>
        </axsl:for-each>
    </xsl:template>
    
    <xsl:template match="sqf:fix" mode="sqf:fix-for-tests">
        <xsl:param name="id-suffix" tunnel="yes"/>
        <xsl:param name="test" as="element()" tunnel="yes"/>
        <xsl:variable name="messageId" select="$test/@es:id"/>

        <axsl:if test="{(parent::sqf:group/@use-when,'true()')[1]}">
            <axsl:if test="{(@use-when, 'true()')[1]}">
                <axsl:variable name="sqf:id">
                    <axsl:text>
                        <xsl:value-of select="@id"/>
                    </axsl:text>
                    <axsl:text>
                        <xsl:text>-</xsl:text>
                        <xsl:value-of select="$messageId"/>
                        <xsl:text>-</xsl:text>
                    </axsl:text>
                    <axsl:value-of select="generate-id($es:context)"/>
                    <xsl:sequence select="$id-suffix"/>
                </axsl:variable>
                <xsl:variable name="description" select="sqf:description"/>
                <xsl:variable name="preDescription" select="$description/preceding-sibling::*"/>

                <xsl:apply-templates select="$preDescription"/>

                <sqf:fix messageId="{$messageId}">
                    <xsl:call-template name="namespace"/>
                    <xsl:attribute name="contextId">
                        <xsl:text>{generate-id($es:context)}</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="id">{$sqf:id}</xsl:attribute>
                    <xsl:attribute name="role" select="es:getRole(.)"/>
                    <axsl:attribute name="fixId">
                        <axsl:text><xsl:value-of select="@id"/></axsl:text>
                        <xsl:sequence select="$id-suffix"/>
                    </axsl:attribute>
                    <xsl:apply-templates select="$description[1]" mode="#current"/>
                    <xsl:apply-templates select="sqf:user-entry" mode="#current"/>
                    <sqf:call-fix ref="{@id}">
                        <xsl:for-each select="sqf:user-entry">
                            <sqf:with-param name="{@name}" select="${@name}_{{$sqf:id}}"/>
                        </xsl:for-each>
                        <xsl:if test="@use-for-each">
                            <sqf:with-param name="sqf:current" select="({@use-for-each})[{{$sqf:current-position}}]"/>
                        </xsl:if>
                    </sqf:call-fix>
                </sqf:fix>
            </axsl:if>
        </axsl:if>
    </xsl:template>

    <xsl:template match="sqf:user-entry" mode="sqf:fix-for-tests">
        <sqf:user-entry name="{@name}_{{$sqf:id}}" ueName="{@name}">
            <xsl:sequence select="@type"/>
            <xsl:perform-sort>
                <xsl:sort select="(index-of(('attribute'), local-name()), 99)[1]"/>
                <xsl:apply-templates select="@default" mode="#current"/>
                <xsl:apply-templates select="sqf:description" mode="#current"/>
            </xsl:perform-sort>
        </sqf:user-entry>
    </xsl:template>

    <xsl:template match="sqf:user-entry/@default" mode="sqf:fix-for-tests">
        <axsl:variable name="sqf:values" select="{.}"/>
        <axsl:choose>
            <axsl:when test="count($sqf:values) eq 1">
                <axsl:attribute name="default" select="es:valueToXPath($sqf:values)"/>
            </axsl:when>
            <axsl:otherwise>
                <es:enumeration>
                    <axsl:for-each select="$sqf:values">
                        <es:enum>
                            <axsl:value-of select="."/>
                        </es:enum>
                    </axsl:for-each>
                </es:enumeration>
            </axsl:otherwise>
        </axsl:choose>
    </xsl:template>

    <xsl:template match="sqf:description" mode="sqf:fix-for-tests">
        <xsl:choose>
            <xsl:when test="es:isDescriptionSimple(.)">
                <axsl:attribute name="title">
                    <xsl:apply-templates select="sqf:title/node()"/>
                </axsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <sqf:description>
                    <xsl:apply-templates/>
                </sqf:description>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="sqf:fix"/>

    <xsl:variable name="actionElements" select="
            ('add',
            'delete',
            'replace',
            'stringReplace')"/>

    <xsl:function name="es:getRole" as="xs:string">
        <xsl:param name="fix" as="element(sqf:fix)"/>
        <xsl:variable name="singleMode" select="
                ($actionElements[every $el in $fix/sqf:*[local-name() = $actionElements]
                    satisfies local-name($el) = .])[1]" as="xs:string?"/>
        <xsl:variable name="mode" select="
                if ($singleMode)
                then
                    ($singleMode)
                else
                    ('mix')"/>
        <xsl:sequence select="($fix/@role/string(.), $mode)[1]"/>
    </xsl:function>

    <xsl:function name="es:isDescriptionSimple" as="xs:boolean">
        <xsl:param name="description" as="element(sqf:description)"/>
        <xsl:sequence select="not($description/(* except sqf:title[1]))"/>
    </xsl:function>

</xsl:stylesheet>
