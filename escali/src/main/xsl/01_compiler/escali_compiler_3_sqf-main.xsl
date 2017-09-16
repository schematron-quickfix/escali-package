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

    <xsl:param name="sqf:changePrefix" select="'sqfc'" as="xs:string"/>
    <xsl:param name="sqf:useSQF" select="exists(key('elementsByNamespace', 'http://www.schematron-quickfix.com/validator/process') | //@es:ignorableId)"/>
    <xsl:param name="xsm:xml-save-mode" select="false()" as="xs:boolean"/>
    <xsl:param name="es:use-for-each.maximum" select="50" as="xs:integer"/>

    <xsl:param name="es:focus" select="false()" as="xs:boolean"/>

    <xsl:variable name="es:type-available" select="/sch:schema/@es:type-available = 'true'" as="xs:boolean"/>

    <xsl:key name="elementsByNamespace" match="*" use="namespace-uri()"/>
    <xsl:namespace-alias stylesheet-prefix="bxsl" result-prefix="axsl"/>

    <xsl:variable name="activityElements" select="
            ('add',
            'delete',
            'replace',
            'stringReplace')"/>

    <!--
    fix top level elements in validator
-->
    <xsl:template name="topLevelValidatorExtension">
        <xsl:choose>
            <xsl:when test="$sqf:useSQF">
                <axsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>
                <axsl:template match="processing-instruction()[matches(name(), '{$sqf:changePrefix}-(end|start)')]" priority="1000" sqf:changeMarker="true"/>
                <axsl:variable name="sqf:base-uri" select="es:base-uri(.)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:processing-instruction name="skiped-sqf"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:variable name="globalFixes" select="/*/sqf:fixes/sqf:fix"/>
    <xsl:variable name="globalGroups" select="/*/sqf:fixes/sqf:group"/>

    <!--
    Extension template
    call to use sqf:fix elements
    context should be the refering sch:assert / sch:report elements
    $messageId: ID of the sch:assert / sch:report element
    $default-fix: name of the default fix
-->
    <xsl:template name="extension">
        <xsl:param name="messageId" as="xs:string"/>
        <xsl:param name="defaultFix" select="@sqf:default-fix" as="xs:string?"/>
        <xsl:if test="$sqf:useSQF">
            <xsl:variable name="assertion" select="."/>
            <xsl:variable name="fix-calls" select="tokenize($assertion/@sqf:fix, '\s+')"/>
            <xsl:variable name="localFixes" select="../sqf:fix"/>
            <xsl:variable name="localGroups" select="../sqf:group"/>

            <xsl:variable name="availableFixes" select="
                    $localFixes | $globalFixes[not(@id = ($localFixes/@id,
                    $localGroups/@id))]"/>
            <xsl:variable name="availableGroups" select="
                    $localGroups | $globalGroups[not(@id = ($localGroups/@id,
                    $localFixes/@id))]"/>
            <xsl:variable name="ignoreFix" as="element(sqf:fix)?">
                <xsl:if test="@es:ignorableId">
                    <xsl:call-template name="createIgnoreFix"/>
                </xsl:if>
            </xsl:variable>

            <xsl:apply-templates select="
                    ($availableFixes | $availableGroups)[@id = $fix-calls],
                    $ignoreFix" mode="sqf:fix">
                <xsl:with-param name="defaultFix" as="xs:string?" tunnel="yes" select="@sqf:default-fix"/>
                <xsl:with-param name="assertion" as="element()" tunnel="yes" select="."/>
                <xsl:with-param name="messageId" as="xs:string" tunnel="yes" select="$messageId"/>
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <xsl:template name="createIgnoreFix" as="element(sqf:fix)">
        <xsl:param name="role" select="
                if (@es:roleLabel) then
                    (@es:roleLabel)
                else
                    ('error')"/>
        <xsl:param name="id" select="@es:ignorableId"/>
        <xsl:variable name="fix" as="element(sqf:fix)">
            <sqf:fix id="ignore_{$id}">
                <sqf:description>
                    <sqf:title>
                        <xsl:text>Ignore this </xsl:text>
                        <xsl:value-of select="
                                if ($role = 'fatal') then
                                    ('fatal error')
                                else
                                    ($role)"/>
                        <xsl:text>.</xsl:text>
                    </sqf:title>
                </sqf:description>
                <axsl:variable name="es:context" select="
                        if (. instance of attribute()) then
                            (..)
                        else
                            (.)"/>
                <axsl:variable name="precPI" select="$es:context/(preceding-sibling::node() except preceding-sibling::text()[normalize-space(.) = ''])[last()]/self::processing-instruction(es_ignore)"/>
                <sqf:add node-type="processing-instruction" target="es_ignore" select="'{$id}'" position="before" use-when="not($precPI)"/>
                <sqf:replace match="$precPI" node-type="processing-instruction" target="es_ignore" select="string(.), '{$id}'"/>
            </sqf:fix>
        </xsl:variable>
        <xsl:sequence select="$fix"/>
    </xsl:template>
    <!--
		top level elements in manipulator
		(functions, variables, keys, output element, copy templates)
	-->
    <xsl:template name="topLevelManipulatorExtension">
        <xsl:if test="$sqf:useSQF">
            <sqf:topLevel>
                <xsl:attribute name="schema" select="/sch:schema/@es:uri"/>
                <axsl:attribute name="instance" select="document-uri(/)"/>
                <bxsl:stylesheet version="2.0">
                    <xsl:attribute name="xml:base" select="es:base-uri(.)"/>
                    <xsl:variable name="namespaceAliase" select="
                            for $na in /sch:schema/xsl:namespace-alias
                            return
                                $na/namespace::*[name() = $na/@stylesheet-prefix]"/>
                    <xsl:copy-of select="$namespaceAliase"/>
                    <xsl:apply-templates select="/sch:schema/es:default-namespace"/>
                    <bxsl:include href="{resolve-uri('escali_compiler_0_functions.xsl')}"/>
                    <bxsl:variable name="sqf:base-doc" select="/"/>
                    <bxsl:template match="/">
                        <xsl:choose>
                            <xsl:when test="$xsm:xml-save-mode">
                                <sqf:createExternals type="doc">
                                    <xsm:manipulator>
                                        <axsl:attribute name="document" select="document-uri(/)"/>
                                        <bxsl:apply-templates select="node()">
                                            <bxsl:with-param name="xsm:xml-save-mode" select="true()" as="xs:boolean" tunnel="yes"/>
                                        </bxsl:apply-templates>
                                    </xsm:manipulator>
                                </sqf:createExternals>
                            </xsl:when>
                            <xsl:otherwise>
                                <sqf:createExternals type="doc">
                                    <bxsl:apply-templates select="node()">
                                        <bxsl:with-param name="xsm:xml-save-mode" select="false()" as="xs:boolean" tunnel="yes"/>
                                    </bxsl:apply-templates>
                                </sqf:createExternals>
                            </xsl:otherwise>
                        </xsl:choose>
                    </bxsl:template>
                    <xsl:apply-templates select="sch:let | *[namespace-uri() = 'http://www.w3.org/1999/XSL/Transform']" mode="topLevelResult"/>
                    <bxsl:output method="xml"/>
                    <bxsl:key name="sqf:nodesById" match="node()" use="generate-id()"/>
                    <sqf:createExternals type="template">
                        <bxsl:template match="node() | @*" priority="-2" mode="addChild addLastChild"/>
                    </sqf:createExternals>
                    <bxsl:template match="node() | @*" priority="-3" mode="#all">
                        <bxsl:param name="xsm:xml-save-mode" tunnel="yes" select="false()" as="xs:boolean"/>
                        <bxsl:choose>
                            <bxsl:when test="$xsm:xml-save-mode">
                                <bxsl:apply-templates select="node() | @*" mode="#current"/>
                            </bxsl:when>
                            <bxsl:otherwise>
                                <bxsl:copy>
                                    <bxsl:apply-templates select="node() | @*" mode="#current"/>
                                </bxsl:copy>
                            </bxsl:otherwise>
                        </bxsl:choose>
                    </bxsl:template>
                    <bxsl:include href="{resolve-uri('../04_manipulator/escali_manipulator_0_removeChangePIs.xsl')}"/>
                </bxsl:stylesheet>
            </sqf:topLevel>
        </xsl:if>
    </xsl:template>
    <xsl:template match="sch:let" mode="topLevelResult">
        <bxsl:variable name="{@name}" select="{@value}">
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates mode="#current"/>
        </bxsl:variable>
    </xsl:template>
    <xsl:template match="*[namespace-uri() = 'http://www.w3.org/1999/XSL/Transform']" mode="topLevelResult">
        <xsl:element name="axsl:{local-name()}">
            <xsl:call-template name="namespace"/>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="#current"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="*" mode="topLevelResult">
        <xsl:copy copy-namespaces="no">
            <xsl:call-template name="namespace"/>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="#current"/>
        </xsl:copy>
    </xsl:template>

    <!--    
    F I X   E L E M E N T S
    -->

    <xsl:template match="sqf:fix[@use-for-each]" mode="sqf:fix">
        <axsl:variable name="sqf:context" select="."/>
        <xsl:variable name="es:use-for-each.maximum" select="
                if (@es:maximum) then
                    (@es:maximum)
                else
                    ($es:use-for-each.maximum)"/>
        <axsl:choose>
            <axsl:when test="count(({@use-for-each})) le {$es:use-for-each.maximum}">
                <axsl:for-each select="{@use-for-each}">
                    <axsl:variable name="sqf:pos" select="position()"/>
                    <axsl:variable name="sqf:current" select="."/>
                    <axsl:for-each select="$sqf:context">
                        <xsl:next-match>
                            <xsl:with-param name="is-generic" select="true()"/>
                            <xsl:with-param name="use-for-each" select="@use-for-each" tunnel="yes"/>
                        </xsl:next-match>
                    </axsl:for-each>
                </axsl:for-each>
            </axsl:when>
            <axsl:otherwise>
                <axsl:message>WARNING: skiped fix <xsl:value-of select="@id"/>. Reason the number of generated fixes by use-for-each (<xsl:value-of select="@use-for-each"/>) violate the limit of <xsl:value-of select="$es:use-for-each.maximum"/> allowed fixes.</axsl:message>
            </axsl:otherwise>
        </axsl:choose>
    </xsl:template>

    <xsl:template match="sch:rule[@es:regex]//sqf:fix" mode="sqf:fix" priority="10">
        <axsl:for-each select="$es:context">
            <xsl:next-match/>
        </axsl:for-each>
    </xsl:template>

    <xsl:template match="sqf:fix" mode="sqf:fix">
        <xsl:param name="defaultFix" as="xs:string?" tunnel="yes"/>
        <xsl:param name="assertion" as="element()" tunnel="yes"/>
        <xsl:param name="messageId" as="xs:string" tunnel="yes"/>
        <xsl:param name="is-generic" as="xs:boolean" select="false()"/>
        <xsl:param name="es:regex" tunnel="yes"/>
        <xsl:variable name="isDefault" select="$defaultFix = @id"/>
        <xsl:variable name="fix" select="."/>

        <xsl:variable name="precedings" select="(parent::sqf:group | .)/preceding-sibling::*"/>
        <xsl:variable name="precExceptions" select="$precedings/(self::sch:report | self::sch:assert | sqf:fix | sqf:group)"/>

        <xsl:apply-templates select="($precedings)[. >> $assertion] except $precExceptions"/>

        <axsl:if test="{(parent::sqf:group/@use-when,'true()')[1]}">
            <axsl:if test="{(@use-when, 'true()')[1]}">
                <axsl:variable name="sqf:id">
                    <axsl:text>
                        <xsl:value-of select="@id"/>
                    </axsl:text>
                    <xsl:if test="$is-generic">
                        <axsl:value-of select="concat('_', $sqf:pos)"/>
                    </xsl:if>
                    <axsl:text>
                        <xsl:text>-</xsl:text>
                        <xsl:value-of select="$messageId"/>
                        <xsl:text>-</xsl:text>
                    </axsl:text>
                    <axsl:value-of select="generate-id($es:context)"/>
                    <xsl:if test="$es:regex">
                        <axsl:text>
                            <xsl:text>_</xsl:text>
                            <axsl:value-of select="$es:pos"/>
                        </axsl:text>
                    </xsl:if>
                </axsl:variable>
                <sqf:fix fixId="{@id}" messageId="{$messageId}">
                    <xsl:call-template name="namespace"/>
                    <xsl:attribute name="fixId">
                        <xsl:value-of select="@id"/>
                        <xsl:if test="$is-generic">
                            <xsl:text>_{$sqf:pos}</xsl:text>
                        </xsl:if>
                    </xsl:attribute>
                    <xsl:attribute name="contextId">
                        <xsl:text>{generate-id($es:context)}</xsl:text>
                    </xsl:attribute>

                    <xsl:attribute name="id">{$sqf:id}</xsl:attribute>
                    <xsl:attribute name="role">

                        <xsl:variable name="singleMode" select="
                                ($activityElements[every $el in $fix/sqf:*[local-name() = $activityElements]
                                    satisfies local-name($el) = .])[1]" as="xs:string?"/>
                        <xsl:variable name="mode" select="
                                if ($singleMode)
                                then
                                    ($singleMode)
                                else
                                    ('mix')"/>
                        <xsl:value-of select="
                                if (@role) then
                                    (@role)
                                else
                                    ($mode)"/>
                    </xsl:attribute>
                    <xsl:if test="$isDefault">
                        <xsl:choose>
                            <xsl:when test="$is-generic">
                                <axsl:if test="$sqf:pos = 1">
                                    <axsl:attribute name="default" select="true()"/>
                                </axsl:if>
                            </xsl:when>
                            <xsl:otherwise>
                                <axsl:attribute name="default" select="true()"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:if>


                    <xsl:variable name="descriptions" select="sqf:description"/>
                    <!--<xsl:variable name="beforeDesc" select="*[. &lt;&lt; $descriptions] except $descriptions"/>
                    <xsl:variable name="afterDesc" select="*[. >> $descriptions] except $descriptions"/>-->
                    <!--                    <xsl:apply-templates select="$beforeDesc"/>-->
                    <sqf:description>
                        <xsl:apply-templates select="$descriptions[1]/(* | preceding-sibling::*)"/>
                    </sqf:description>
                    <xsl:apply-templates select="sqf:user-entry" mode="copy"/>
                    <xsl:if test="not($es:focus)">
                        <sqf:sheet>
                            <xsl:attribute name="id">{$sqf:id}</xsl:attribute>
                            <xsl:apply-templates select="* except $descriptions">
                                <xsl:with-param name="fixId" select="@id" tunnel="yes"/>
                            </xsl:apply-templates>
                        </sqf:sheet>
                    </xsl:if>
                </sqf:fix>
            </axsl:if>
        </axsl:if>
    </xsl:template>




    <!--
	A C T I V I T Y   E L E M E N T S
-->
    <xsl:template match="sqf:delete[@use-when] | sqf:replace[@use-when] | sqf:stringReplace[@use-when] | sqf:add[@use-when]" priority="20">
        <axsl:if test="{@use-when}">
            <xsl:next-match/>
        </axsl:if>
    </xsl:template>
    <xsl:template match="sqf:delete | sqf:replace | sqf:stringReplace" priority="10">
        <xsl:param name="messageId" tunnel="yes"/>
        <xsl:variable name="sqf:match" select="
                if (@match) then
                    (@match)
                else
                    ('self::node()')"/>
        <axsl:variable name="sqf:match">
            <xsl:call-template name="nodeMatching">
                <xsl:with-param name="nodes" select="$sqf:match"/>
                <xsl:with-param name="base-uri-scope" select="true()"/>
            </xsl:call-template>
        </axsl:variable>
        <axsl:variable name="sqf:ruleContext" select="."/>
        <axsl:for-each-group select="{$sqf:match}" group-by="es:base-uri(.)">
            <axsl:for-each select="$sqf:ruleContext">
                <axsl:if test="$sqf:match != ''">
                    <bxsl:template>
                        <xsl:apply-templates select="preceding-sibling::xsl:variable | preceding-sibling::sch:let | preceding-sibling::sqf:param"/>

                        <axsl:attribute name="match" select="$sqf:match"/>
                        <axsl:attribute name="priority">
                            <xsl:value-of select="count(following-sibling::node()) + 10"/>
                        </axsl:attribute>
                        <xsl:call-template name="multiplyDocsMode">
                            <xsl:with-param name="nodes" select="'current-group()'"/>
                        </xsl:call-template>
                        <bxsl:param name="xsm:xml-save-mode" select="false()" as="xs:boolean" tunnel="yes"/>

                        <xsl:call-template name="setVarContext">
                            <xsl:with-param name="messageId" select="$messageId"/>
                            <xsl:with-param name="templateBody">
                                <xsl:choose>
                                    <xsl:when test="$xsm:xml-save-mode">
                                        <xsl:apply-templates select="." mode="xsm:save-mode"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:apply-templates select="." mode="xsm:no-xsm"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:with-param>
                        </xsl:call-template>

                    </bxsl:template>
                </axsl:if>
            </axsl:for-each>
        </axsl:for-each-group>
    </xsl:template>

    <xsl:template match="sqf:add">
        <xsl:param name="messageId" tunnel="yes"/>
        <xsl:param name="es:regex" tunnel="yes" as="xs:string?"/>
        <xsl:variable name="sqf:match" select="
                if (@match) then
                    (@match)
                else
                    ('self::node()')"/>
        <axsl:variable name="sqf:ruleContext" select="."/>
        <axsl:for-each-group select="{$sqf:match}" group-by="es:base-uri(.)">
            <axsl:for-each select="$sqf:ruleContext">
                <axsl:variable name="sqf:match">
                    <xsl:call-template name="nodeMatching">
                        <xsl:with-param name="sqf:add" select="true() and not($es:regex)"/>
                        <xsl:with-param name="nodes" select="'current-group()'"/>
                        <xsl:with-param name="base-uri-scope" select="true()"/>
                    </xsl:call-template>
                </axsl:variable>

                <axsl:if test="$sqf:match != ''">
                    <bxsl:template>
                        <xsl:apply-templates select="preceding-sibling::xsl:variable | preceding-sibling::sch:let | preceding-sibling::sqf:param"/>
                        <axsl:attribute name="match" select="$sqf:match"/>
                        <axsl:attribute name="priority">
                            <xsl:value-of select="count(following-sibling::node()) + 10"/>
                        </axsl:attribute>
                        <xsl:call-template name="multiplyDocsMode">
                            <xsl:with-param name="nodes" select="'current-group()'"/>
                            <xsl:with-param name="suffix">
                                <xsl:choose>
                                    <xsl:when test="@position = ('last-child')">
                                        <axsl:text>addLastChild</axsl:text>
                                    </xsl:when>
                                    <xsl:when test="@node-type = ('attribute') or @position = ('first-child') or not(@position)">
                                        <axsl:text>addChild</axsl:text>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:with-param>
                        </xsl:call-template>
                        <bxsl:param name="xsm:xml-save-mode" select="false()" as="xs:boolean" tunnel="yes"/>
                        <xsl:choose>
                            <xsl:when test="@position = ('after')">
                                <bxsl:next-match/>
                                <xsl:call-template name="setVarContext">
                                    <xsl:with-param name="messageId" select="$messageId"/>
                                    <xsl:with-param name="templateBody">
                                        <xsl:choose>
                                            <xsl:when test="$xsm:xml-save-mode">
                                                <xsl:apply-templates select="." mode="xsm:save-mode"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:apply-templates select="." mode="xsm:no-xsm"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:with-param>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:call-template name="setVarContext">
                                    <xsl:with-param name="messageId" select="$messageId"/>
                                    <xsl:with-param name="templateBody">
                                        <xsl:choose>
                                            <xsl:when test="$xsm:xml-save-mode">
                                                <xsl:apply-templates select="." mode="xsm:save-mode"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:apply-templates select="." mode="xsm:no-xsm"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:with-param>
                                </xsl:call-template>
                                <bxsl:next-match/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </bxsl:template>
                </axsl:if>
                <xsl:if test="
                        @position = ('first-child',
                        'last-child') or not(@position)">
                    <xsl:variable name="sqf:match" select="
                            if (@match) then
                                (@match)
                            else
                                ('self::node()')"/>
                    <axsl:if test="$sqf:match != ''">
                        <bxsl:template>
                            <xsl:apply-templates select="preceding-sibling::xsl:variable | preceding-sibling::sch:let | preceding-sibling::sqf:param"/>
                            <axsl:attribute name="match" select="$sqf:match"/>
                            <axsl:attribute name="priority">
                                <xsl:value-of select="10 - count(preceding-sibling::*)"/>
                            </axsl:attribute>
                            <xsl:call-template name="multiplyDocsMode">
                                <xsl:with-param name="nodes" select="'current-group()'"/>
                            </xsl:call-template>
                            <bxsl:param name="xsm:xml-save-mode" select="false()" as="xs:boolean" tunnel="yes"/>

                            <bxsl:variable name="sqf:addChilds" as="node()*">
                                <bxsl:apply-templates select="self::node()">
                                    <axsl:attribute name="mode" select="
                                            for $sqf:m in $sqf:mode
                                            return
                                                if ($sqf:m = '#default') then
                                                    ('addChild')
                                                else
                                                    (concat($sqf:m, '_', 'addChild'))"/>
                                </bxsl:apply-templates>
                            </bxsl:variable>
                            <bxsl:variable name="sqf:addLastChilds" as="node()*">
                                <bxsl:apply-templates select="self::node()">
                                    <axsl:attribute name="mode" select="
                                            for $sqf:m in $sqf:mode
                                            return
                                                if ($sqf:m = '#default') then
                                                    ('addLastChild')
                                                else
                                                    (concat($sqf:m, '_', 'addLastChild'))"/>
                                </bxsl:apply-templates>
                            </bxsl:variable>

                            <xsl:choose>
                                <xsl:when test="$xsm:xml-save-mode">
                                    <xsl:call-template name="xsm:save-mode-add"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:call-template name="xsm:no-save-add"/>
                                </xsl:otherwise>
                            </xsl:choose>

                        </bxsl:template>
                    </axsl:if>
                </xsl:if>
            </axsl:for-each>
        </axsl:for-each-group>
    </xsl:template>



    <xsl:include href="escali_compiler_3_sqf-main-xsm.xsl"/>
    <xsl:include href="escali_compiler_3_sqf-main-no-xsm.xsl"/>

    <xsl:template match="sqf:add/@select | sqf:replace/@select | sqf:stringReplace/@select" mode="template" priority="20">
        <bxsl:sequence select="es:selectValue(({.}))"/>
    </xsl:template>







    <!--   
    Content of activity elements
-->
    <xsl:template match="sqf:fix//node()" mode="template">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="template"/>
            <xsl:apply-templates select="node()" mode="template"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="sqf:fix//@*" priority="10" mode="template">
        <axsl:attribute name="{name()}">
            <xsl:value-of select="."/>
        </axsl:attribute>
    </xsl:template>
    <xsl:template match="sqf:fix//xsl:*" priority="10" mode="template">
        <xsl:element name="axsl:{local-name()}">
            <xsl:apply-templates select="@*" mode="template"/>
            <xsl:apply-templates select="node()" mode="template"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="sqf:fix//sch:value-of" priority="10" mode="template">
        <bxsl:value-of select="{@select}">
            <xsl:call-template name="namespace"/>
        </bxsl:value-of>
    </xsl:template>
    <xsl:template match="sqf:fix//sch:let" priority="10" mode="template">
        <bxsl:variable name="{@name}" select="{@value}">
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates select="node()" mode="template"/>
        </bxsl:variable>
    </xsl:template>
    <!--
	O T H E R   S Q F   E L E M E N T S
-->
    <!--
        Implementation of sqf:KEEP (depreciated)
    -->
    <xsl:template match="sqf:keep" mode="#all" priority="10">
        <bxsl:apply-templates select="{if (@select) 
                                     then (@select) 
                                     else ('node()')}">
            <bxsl:with-param name="xsm:xml-save-mode" select="false()" as="xs:boolean" tunnel="yes"/>
        </bxsl:apply-templates>
    </xsl:template>
    <!--
    Implementation of sqf:copy-of/es:copy-of with unparsed-mode
    -->
    <xsl:template match="es:copy-of[@unparsed-mode = 'true'][$xsm:xml-save-mode] | sqf:copy-of[@unparsed-mode = 'true'][$xsm:xml-save-mode]" mode="#all" priority="15">
        <bxsl:for-each select="({@select})/es:getNodePath(.)">
            <xsm:copy>
                <bxsl:attribute name="select" select="."/>
            </xsm:copy>
        </bxsl:for-each>
    </xsl:template>
    <!--
    Implementation of sqf:copy-of/es:copy-of without unparsed-mode
    -->
    <xsl:template match="es:copy-of | sqf:copy-of" mode="#all" priority="10">
        <bxsl:copy-of select="{@select}"/>
    </xsl:template>

    <!--
	Implementation of sqf:PARAM
-->
    <xsl:template match="sqf:param">
        <axsl:variable>
            <xsl:apply-templates select="@* | node()"/>
        </axsl:variable>
    </xsl:template>
    <xsl:template match="sqf:param/@default" priority="5">
        <xsl:attribute name="select" select="."/>
    </xsl:template>
    <xsl:template match="sqf:param/@type" priority="5">
        <xsl:attribute name="as" select="."/>
    </xsl:template>
    <xsl:template match="sqf:param/@required" priority="5"/>
    <xsl:template match="sqf:param/@*">
        <xsl:copy/>
    </xsl:template>

    <xsl:template match="sqf:user-entry">
        <xsl:param name="messageId" tunnel="yes" required="yes"/>
        <bxsl:param>
            <xsl:apply-templates select="@*[not(name() = ('name', 'default'))]"/>
            <xsl:attribute name="name">
                <xsl:text>sqf:</xsl:text>
                <xsl:value-of select="generate-id()"/>
                <xsl:text>_</xsl:text>
                <xsl:value-of select="$messageId"/>
                <xsl:text>_{generate-id()}</xsl:text>
            </xsl:attribute>
            <xsl:if test="@default">
                <axsl:value-of select="({@default})[1]"/>
            </xsl:if>
        </bxsl:param>
    </xsl:template>
    <xsl:template match="sqf:user-entry/@type">
        <xsl:attribute name="as" select="."/>
    </xsl:template>

    <xsl:template match="sqf:user-entry" mode="copy" priority="101">
        <xsl:param name="messageId" tunnel="yes" required="yes"/>
        <xsl:variable name="paramAttr" select="(@name, @type, @default)"/>
        <xsl:copy copy-namespaces="no">
            <xsl:copy-of select="@* except $paramAttr"/>
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates mode="#current"/>
            <xsl:if test="@default">
                <axsl:variable name="es:default" select="{@default}"/>
            </xsl:if>
            <sqf:param name="sqf:{generate-id()}">
                <xsl:copy-of select="$paramAttr"/>
                <xsl:attribute name="param-id">
                    <!--                    <xsl:text>sqf:</xsl:text>-->
                    <xsl:value-of select="generate-id()"/>
                    <xsl:text>_</xsl:text>
                    <xsl:value-of select="$messageId"/>
                    <xsl:text>_{generate-id()}</xsl:text>
                </xsl:attribute>
                <xsl:if test="@default">
                    <axsl:value-of select="($es:default)[1]"/>
                </xsl:if>
            </sqf:param>
            <xsl:if test="@default">
                <axsl:if test="count($es:default) gt 1">
                    <es:enumeration>
                        <axsl:for-each select="$es:default">
                            <es:enum>
                                <axsl:value-of select="."/>
                            </es:enum>
                        </axsl:for-each>
                    </es:enumeration>
                </axsl:if>
            </xsl:if>
        </xsl:copy>
    </xsl:template>


    <!--
        Implementation of sqf:USER-ENTRY
    -->
    <xsl:template match="sqf:user-entry//*" mode="copy">
        <xsl:copy copy-namespaces="no">
            <xsl:copy-of select="@*"/>
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates mode="#current"/>
        </xsl:copy>
    </xsl:template>
    <!--
        Implementation of sqf:description
    -->
    <xsl:template match="sqf:description/sqf:p" mode="#all" priority="100">
        <svrl:text>
            <xsl:apply-templates/>
        </svrl:text>
    </xsl:template>
    <xsl:template match="sqf:description/sqf:title" mode="#all" priority="100">
        <sqf:title>
            <xsl:apply-templates/>
        </sqf:title>
    </xsl:template>
    <xsl:template match="sqf:description"/>


    <xsl:template match="sqf:*"/>

    <!--<xsl:template match="sqf:fix//*[namespace-uri()='http://www.w3.org/1999/XSL/Transform']">
        <xsl:element name="axsl:{local-name()}">
            <xsl:copy-of select="@*"/>
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>-->



    <!--
	V A R I A B L E S   C O N T E X T
-->
    <!--
	create xsl:for-each loops into the manipulator
	to create the context of variables
	
	First loop: set the $sqf:ruleContext
	   - context of the sch:rule, for variabels inside of the Schematron rule.
    Second loop: set the $sqf:activityContext
        - Context of the activity elements (sqf:add, sqf:replace, ...)
        - for variables inside of the activity elements
-->
    <xsl:template name="setVarContext">
        <xsl:param name="use-for-each" as="xs:string?" tunnel="yes"/>
        <xsl:param name="messageId" as="xs:string"/>
        <xsl:param name="templateBody" as="node()*"/>
        <xsl:variable name="activityElement" select="."/>
        <xsl:variable name="fix" select="parent::sqf:fix"/>
        <xsl:variable name="fixOrGroup" select="
                if ($fix/parent::sqf:group)
                then
                    ($fix/parent::sqf:group)
                else
                    ($fix)"/>
        <xsl:variable name="rule" select="$fixOrGroup/parent::sch:rule" as="element(sch:rule)?"/>

        <bxsl:variable name="sqf:ruleContext">
            <axsl:attribute name="select">
                <xsl:text>$sqf:base-doc</xsl:text>
                <xsl:call-template name="nodeMatching">
                    <xsl:with-param name="nodes" select="'self::node()'"/>
                </xsl:call-template>
            </axsl:attribute>
        </bxsl:variable>
        <bxsl:variable name="sqf:activityContext">
            <axsl:attribute name="select">self::node()</axsl:attribute>
        </bxsl:variable>
        <xsl:for-each select="$fix/sqf:user-entry">
            <bxsl:variable name="{@name}">
                <xsl:attribute name="select">
                    <xsl:text>$sqf:</xsl:text>
                    <xsl:value-of select="generate-id()"/>
                    <xsl:text>_</xsl:text>
                    <xsl:value-of select="$messageId"/>
                    <xsl:text>_{generate-id()}</xsl:text>
                </xsl:attribute>
            </bxsl:variable>
        </xsl:for-each>
        <xsl:variable name="loops">
            <bxsl:for-each select="$sqf:ruleContext">
                <xsl:variable name="ruleLevelVars" select="
                        $fixOrGroup/preceding-sibling::* intersect ($rule/sch:let,
                        $rule/xsl:variable)"/>
                <xsl:variable name="fixLevelVars" select="
                        $activityElement/preceding-sibling::* intersect ($fix/sqf:param,
                        $fix/sqf:user-entry,
                        $fix/sch:let,
                        $fix/xsl:variable)"/>
                <xsl:for-each select="
                        $ruleLevelVars,
                        $fixLevelVars">
                    <xsl:choose>
                        <xsl:when test="self::sqf:user-entry">
                            <bxsl:variable name="{@name}">
                                <xsl:attribute name="select">
                                    <xsl:text>$sqf:</xsl:text>
                                    <xsl:value-of select="generate-id()"/>
                                    <xsl:text>_</xsl:text>
                                    <xsl:value-of select="$messageId"/>
                                    <xsl:text>_{generate-id()}</xsl:text>
                                </xsl:attribute>
                            </bxsl:variable>
                        </xsl:when>
                        <xsl:when test="self::sqf:param">
                            <bxsl:variable name="{@name}">
                                <xsl:apply-templates select="@* | node()"/>
                            </bxsl:variable>
                        </xsl:when>
                        <xsl:otherwise>
                            <bxsl:variable name="{@name}">
                                <xsl:if test="@value | @select">
                                    <axsl:attribute name="select">
                                        <xsl:value-of select="replace(@value | @select, 'current\(\)', '\$sqf:ruleContext')"/>
                                    </axsl:attribute>
                                </xsl:if>
                                <xsl:copy-of select="node()"/>
                            </bxsl:variable>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>

                <bxsl:for-each select="$sqf:activityContext">
                    <xsl:for-each select="sch:let">
                        <bxsl:variable name="{@name}" select="{@value}"/>
                    </xsl:for-each>
                    <xsl:copy-of select="$templateBody"/>
                </bxsl:for-each>
            </bxsl:for-each>
        </xsl:variable>

        <xsl:choose>
            <xsl:when test="$use-for-each">
                <xsl:call-template name="setVarContext">
                    <xsl:with-param name="messageId" select="$messageId"/>
                    <xsl:with-param name="use-for-each" select="()" tunnel="yes"/>
                    <xsl:with-param name="templateBody">
                        <bxsl:for-each>
                            <xsl:attribute name="select">
                                <xsl:text>(</xsl:text>
                                <xsl:value-of select="$use-for-each"/>
                                <xsl:text>)[{$sqf:pos}]</xsl:text>
                            </xsl:attribute>
                            <bxsl:variable name="sqf:pos">
                                <axsl:attribute name="select" select="$sqf:pos"/>
                            </bxsl:variable>
                            <bxsl:variable name="sqf:current" select="."/>
                            <xsl:copy-of select="$loops"/>
                        </bxsl:for-each>
                    </xsl:with-param>
                </xsl:call-template>

            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="$loops"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--
	M A T C H - G E N E R A T O R
-->
    <!--
    Creates in validator an absolute xpath expression
    which matches on the node(s) of $nodes
-->
    <xsl:template name="nodeMatching">
        <xsl:param name="sqf:add" select="false()" as="xs:boolean"/>
        <xsl:param name="nodes" select="'self::node()'"/>
        <xsl:param name="base-uri-scope" select="false()" as="xs:boolean"/>
        <xsl:variable name="nodes" select="concat('$es:context/(', $nodes, ')')"/>
        <xsl:choose>
            <xsl:when test="$sqf:add">
                <axsl:value-of select="string-join(({$nodes})/es:getNodePath(if (. instance of attribute()) then (..) else (.), {$base-uri-scope}()), ' | ')" separator=" | "/>
            </xsl:when>
            <xsl:otherwise>
                <axsl:value-of select="string-join(({$nodes})/es:getNodePath(., {$base-uri-scope}()), ' | ')" separator=" | "/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="multiplyDocsMode">
        <xsl:param name="nodes" select="'self::node()'"/>
        <xsl:param name="suffix" as="node()*"/>
        <xsl:variable name="nodes" select="concat('$es:context/(', $nodes, ')')"/>
        <axsl:variable name="sqf:mode-suffix" as="xs:string?">
            <xsl:copy-of select="$suffix"/>
        </axsl:variable>
        <axsl:variable name="sqf:mode-prefix" as="xs:string*">
            <axsl:sequence select="({$nodes})/(if (es:base-uri(.) = $sqf:base-uri) then ('#default') else (generate-id()))"/>
        </axsl:variable>
        <axsl:variable name="sqf:mode" select="
                for $sqf:mp in $sqf:mode-prefix
                return
                    if ($sqf:mp = '#default' and $sqf:mode-suffix != '') then
                        $sqf:mode-suffix
                    else
                        string-join(($sqf:mp,
                        $sqf:mode-suffix), '_')"/>
        <axsl:variable name="sqf:mode" select="distinct-values($sqf:mode)"/>
        <axsl:attribute name="mode" select="$sqf:mode"/>
        <axsl:for-each select="({$nodes})[es:base-uri(.) != $sqf:base-uri]">
            <axsl:processing-instruction name="sqf_doc" select="concat(generate-id(), ' # ', es:base-uri(.))"/>
        </axsl:for-each>
    </xsl:template>
    <!-- 
	N O D E - F A C T O R Y
-->
    <!--
    Creates a new node in the manipulator
-->
    <xsl:template name="nodeFac">
        <xsl:choose>
            <xsl:when test="@node-type = 'element'">
                <xsl:call-template name="elementFac"/>
            </xsl:when>
            <xsl:when test="@node-type = 'attribute'">
                <xsl:call-template name="attributeFac"/>
            </xsl:when>
            <xsl:when test="@node-type = 'comment'">
                <xsl:call-template name="commentFac"/>
            </xsl:when>
            <xsl:when test="
                    @node-type = ('pi',
                    'processing-instruction')">
                <xsl:call-template name="piFac"/>
            </xsl:when>
            <xsl:when test="not(@node-type)">
                <bxsl:variable name="sqf:new-node" as="node()*">
                    <xsl:apply-templates select="@select | node()" mode="template"/>
                </bxsl:variable>

                <bxsl:copy-of select="$sqf:new-node"/>
            </xsl:when>
            <xsl:when test="@node-type = 'keep'">
                <xsl:variable name="sqf:match" select="
                        if (@match) then
                            (@match)
                        else
                            ('self::node()')"/>
                <axsl:choose>
                    <axsl:when test="({$sqf:match})[1] instance of element()">
                        <xsl:call-template name="elementFac"/>
                    </axsl:when>
                    <axsl:when test="({$sqf:match})[1] instance of attribute()">
                        <xsl:call-template name="attributeFac"/>
                    </axsl:when>
                    <axsl:when test="({$sqf:match})[1] instance of comment()">
                        <xsl:call-template name="commentFac"/>
                    </axsl:when>
                    <axsl:when test="({$sqf:match})[1] instance of processing-instruction()">
                        <xsl:call-template name="piFac"/>
                    </axsl:when>
                    <axsl:otherwise>
                        <axsl:message>unexpected node art</axsl:message>
                    </axsl:otherwise>
                </axsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:message>unexpected node art</xsl:message>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--
        Creates a new element in the manipulator
	-->
    <xsl:template name="elementFac">
        <!--<bxsl:processing-instruction name="{$sqf:changePrefix}-start" sqf:changeMarker="true">
            <bxsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>-</xsl:text>
            </bxsl:text>
            <bxsl:value-of select="generate-id()"/>
        </bxsl:processing-instruction>-->
        <bxsl:element>
            <xsl:call-template name="namespace"/>
            <axsl:attribute name="name">
                <xsl:value-of select="@target"/>
            </axsl:attribute>
            <xsl:apply-templates select="@select | node()" mode="template"/>
        </bxsl:element>
        <!--<bxsl:processing-instruction name="{$sqf:changePrefix}-end" sqf:changeMarker="true">
            <bxsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>-</xsl:text>
            </bxsl:text>
            <bxsl:value-of select="generate-id()"/>
        </bxsl:processing-instruction>-->
    </xsl:template>
    <!--
        Creates a new attribute in the manipulator
	-->
    <xsl:template name="attributeFac">
        <bxsl:attribute>
            <axsl:attribute name="name">
                <xsl:value-of select="@target"/>
            </axsl:attribute>
            <xsl:apply-templates select="node() | @select" mode="template"/>
        </bxsl:attribute>
        <!--<bxsl:attribute name="{$sqf:changePrefix}:attribute-change-no{count(preceding-sibling::sqf:*)}" namespace="http://www.schematron-quickfix.com/validator/process/changes" sqf:changeMarker="true">
            <xsl:value-of select="@target"/>
        </bxsl:attribute>-->
    </xsl:template>
    <!--
        Creates a new comment in the manipulator
	-->
    <xsl:template name="commentFac">
        <!--<bxsl:processing-instruction name="{$sqf:changePrefix}-start" sqf:changeMarker="true">
            <bxsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>-</xsl:text>
            </bxsl:text>
            <bxsl:value-of select="generate-id()"/>
        </bxsl:processing-instruction>-->
        <bxsl:comment>
            <xsl:apply-templates select="@select | node()" mode="template"/>
        </bxsl:comment>
        <!--<bxsl:processing-instruction name="{$sqf:changePrefix}-end" sqf:changeMarker="true">
            <bxsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>-</xsl:text>
            </bxsl:text>
            <bxsl:value-of select="generate-id()"/>
        </bxsl:processing-instruction>-->
    </xsl:template>
    <!--
        Creates a new pi in the manipulator
	-->
    <xsl:template name="piFac">
        <!--<bxsl:processing-instruction name="{$sqf:changePrefix}-start" sqf:changeMarker="true">
            <bxsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>-</xsl:text>
            </bxsl:text>
            <bxsl:value-of select="generate-id()"/>
        </bxsl:processing-instruction>-->
        <bxsl:processing-instruction>
            <axsl:attribute name="name">
                <xsl:value-of select="@target"/>
            </axsl:attribute>
            <xsl:apply-templates select="@select | node()" mode="template"/>
        </bxsl:processing-instruction>
        <!--<bxsl:processing-instruction name="{$sqf:changePrefix}-end" sqf:changeMarker="true">
            <bxsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>-</xsl:text>
            </bxsl:text>
            <bxsl:value-of select="generate-id()"/>
        </bxsl:processing-instruction>-->
    </xsl:template>
</xsl:stylesheet>
