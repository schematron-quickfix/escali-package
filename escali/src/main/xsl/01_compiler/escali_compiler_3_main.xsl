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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" exclude-result-prefixes="xs xd sch svrl" xmlns:sch="http://purl.oclc.org/dsdl/schematron" version="2.0">
    <xsl:include href="escali_compiler_3_sqf-main.xsl"/>


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


    <!--
        Escali Schematron main process
        
        process:
            expect a valid Schematron schema (../../schema/SQF/schematron-schema.xsd)
            creates the validator
            uses escali_compiler_3_sqf-main.xsl for SQF extensions
        
        uses axsl as the prefix for xslt elements to create for the validator
        
    -->

    <!--
    Parameter phase:
    use sch:schema/@defaultPhase as default
    #ALL -> every pattern is active
    
    es extension:
    it could be more than one phase active.
    -->
    <xsl:param name="phase" select="
            if (/sch:schema/@defaultPhase) then
                (/sch:schema/@defaultPhase)
            else
                ('#ALL')" as="xs:string+"/>

    <!--
    Parameter es:compact-svrl
    true:
        SVRL output will be reduced,
        so it contains only the necessary informations
        skiped will be:
           - svrl:fired-rule elements which do not have a svrl:failed-assert or svrl:successful-report
           - svrl:active-pattern elements which do not have a svrl:failed-assert or svrl:successful-report
    false: default behavior.
    -->
    <xsl:param name="es:compact-svrl" select="false()" as="xs:boolean"/>


    <xsl:key name="elementByesid" match="*[@es:id]" use="@es:id"/>

    <xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>

    <xsl:output method="xml" indent="yes"/>

    <xsl:include href="escali_compiler_0_functions.xsl"/>


    <xsl:variable name="uri" select="/sch:schema/@es:uri"/>
    <xsl:variable name="defaultRole" select="/sch:schema/@es:defaultRole"/>
    <xsl:variable name="defaultRoleLabel" select="/sch:schema/@es:defaultRoleLabel"/>

    <xsl:variable name="phaseVariables" select="/sch:schema/sch:phase[@id = $phase]/sch:let"/>

    <xsl:template match="sch:schema">

        <axsl:stylesheet version="2.0">

            <xsl:apply-templates select="@queryBinding"/>

            <xsl:variable name="defaultNS" select="/sch:schema/es:default-namespace"/>
            <xsl:variable name="imports" select="/sch:schema/xsl:import"/>
            <xsl:variable name="firsts" select="
                    ($defaultNS,
                    $imports)"/>
            <xsl:apply-templates select="$defaultNS"/>
            <xsl:namespace name="xs" select="'http://www.w3.org/2001/XMLSchema'"/>
            <xsl:call-template name="namespace"/>
            <xsl:attribute name="xml:base" select="es:base-uri(.)"/>

            <xsl:apply-templates select="$imports"/>

            <axsl:output method="xml" indent="yes"/>


            <axsl:include href="{resolve-uri('escali_compiler_0_functions.xsl')}"/>

            <axsl:variable name="es:base-uri-root" select="es:base-uri(/)"/>


            <xsl:sequence select="sch:pattern/es:createPatternVariables(., true())"/>


            <axsl:template match="/" priority="10000000">
                <axsl:processing-instruction name="es_compact-svrl" select="'{$es:compact-svrl}'"/>
                <svrl:schematron-output xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl">
                    <xsl:attribute name="es:queryBinding" select="@queryBinding"/>
                    <xsl:if test="sch:title">
                        <xsl:attribute name="title" select="sch:title" separator=", "/>
                    </xsl:if>
                    <xsl:attribute name="phase" select="$phase"/>
                    <xsl:copy-of select="@schemaVersion | @es:link | @es:icon"/>
                    <xsl:attribute name="es:schema" select="es:base-uri(.)"/>
                    <axsl:attribute name="es:instance" select="document-uri(/)"/>
                    <xsl:sequence select="es:topLevelManipulatorExtension(/*)"/>
                    <xsl:for-each-group select="/sch:schema/(sch:ns | es:default-namespace)" group-by="concat(@uri, @prefix)">
                        <svrl:ns-prefix-in-attribute-values uri="{@uri}" prefix="{@prefix}">
                            <!--                            <xsl:attribute name="prefix" select="distinct-values(current-group()/@prefix)" separator=" "/>-->
                        </svrl:ns-prefix-in-attribute-values>
                    </xsl:for-each-group>
                    <xsl:for-each select="sch:p | sch:pattern/sch:p">
                        <xsl:variable name="refElement" select="key('elementByesid', @es:ref)"/>
                        <xsl:choose>
                            <xsl:when test="$refElement/self::sch:pattern and not(es:isActive($refElement, $phase))"/>
                            <xsl:otherwise>
                                <svrl:text>
                                    <xsl:copy-of select="@id | @es:icon | @es:link | @es:ref | @es:class"/>
                                    <xsl:apply-templates select="node()"/>
                                </svrl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                    <xsl:for-each select="sch:pattern">
                        <xsl:variable name="pattern" select="."/>
                        <xsl:if test="es:isActive($pattern, $phase)">
                            <svrl:active-pattern>
                                <xsl:copy-of select="@id | @role | @es:icon | @es:link | @es:is-a | @es:id | @es:roleLabel"/>
                                <xsl:if test="sch:title">
                                    <xsl:attribute name="name" select="sch:title"/>
                                </xsl:if>
                                <xsl:attribute name="es:patternId" select="@es:id"/>
                                <xsl:attribute name="es:phases" select="
                                        distinct-values((/sch:schema/sch:phase[es:isActive($pattern, @id)]/@id,
                                        ('#ALL')[es:isActive($pattern, .)]))" separator=" "/>
                                <xsl:sequence select="sch:let"/>
                            </svrl:active-pattern>
                        </xsl:if>
                    </xsl:for-each>
                    <axsl:next-match/>

                </svrl:schematron-output>
            </axsl:template>
            <xsl:apply-templates select="(node() | $phaseVariables) except $firsts"/>


            <xsl:variable name="sep">', '</xsl:variable>
            <axsl:function name="es:getPhase" as="xs:string+">
                <axsl:sequence select="('{es:string-join($phase, $sep)}')"/>
            </axsl:function>

            <axsl:function name="es:getLang" as="xs:string+">
                <xsl:variable name="langs" select="tokenize(/*/@es:lang, ',')" as="xs:string+"/>
                <axsl:sequence select="('{es:string-join($langs, $sep)}')"/>
            </axsl:function>
            <!-- 
        copies all nodes in the validator
    -->
            <axsl:template match="node() | @*" priority="-1">
                <axsl:param name="precId" select="()" as="xs:string*"/>
                <axsl:apply-templates select="node() | @*" mode="#current">
                    <axsl:with-param name="precId" select="()"/>
                </axsl:apply-templates>
            </axsl:template>
            <axsl:template match="text()"/>
        </axsl:stylesheet>
    </xsl:template>

    <xsl:template match="sch:schema/@queryBinding">
        <xsl:variable name="lowerCase" select="lower-case(.)"/>
        <xsl:choose>
            <xsl:when test="$lowerCase = ('xslt3', 'xpath3')">
                <xsl:attribute name="version">3.0</xsl:attribute>
            </xsl:when>
            <xsl:when test="$lowerCase = ('xslt2', 'xpath2')">
                <xsl:attribute name="version">2.0</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:message terminate="yes">Not supported Query Binding "<xsl:value-of select="."/>"</xsl:message>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="sch:title | sch:phase | sch:pattern/sch:let"/>

    <xsl:template match="sch:schema/sch:let[@name = $phaseVariables/@name]"/>


    <xsl:template match="es:default-namespace">
        <xsl:attribute name="xpath-default-namespace" select="@uri"/>
    </xsl:template>

    <xsl:key name="rulesByPriorityOrNot" match="sch:rule" use="
            if (parent::sch:pattern/@es:matchType = 'priority') then
                ('priorities')
            else
                ('non-priorities')"/>



    <xsl:function name="es:getPriority" as="xs:double">
        <xsl:param name="rule" as="element(sch:rule)"/>
        <xsl:variable name="non-priorities" select="key('rulesByPriorityOrNot', 'non-priorities', root($rule))"/>
        <xsl:choose>
            <xsl:when test="$rule[parent::sch:pattern[@es:matchType = 'priority']]">
                <xsl:variable name="priorities" select="key('rulesByPriorityOrNot', 'priorities', root($rule))"/>
                <xsl:variable name="sortedRules">
                    <xsl:for-each select="$priorities">
                        <xsl:sort select="@es:priority" data-type="number"/>
                        <sch:rule>
                            <xsl:copy-of select="@es:id"/>
                        </sch:rule>
                    </xsl:for-each>
                </xsl:variable>
                <xsl:variable name="precRules" select="$sortedRules/sch:rule[@es:id = $rule/@es:id]/preceding-sibling::sch:rule"/>
                <xsl:value-of select="count($precRules) + count($non-priorities)"/>

            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="followingRules" select="$non-priorities[. >> $rule]"/>
                <xsl:variable name="countFollowingRules" select="count($followingRules)"/>
                <xsl:value-of select="$countFollowingRules"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:template match="sch:rule[not(sch:assert | sch:report | sch:extend)]">
        <xsl:variable name="matchType" select="parent::sch:pattern/@es:matchType"/>
        <axsl:template match="{@context}">
            <xsl:copy-of select="../@xml:base | @xml:base"/>
            <xsl:attribute name="priority" select="es:getPriority(.) + 10"/>
            <xsl:call-template name="namespace"/>
            <axsl:param name="precId" select="()" as="xs:string*"/>

            <axsl:variable name="es:base-uri" select="
                    es:base-uri(if (. instance of element() or . instance of document-node()) then
                        (.)
                    else
                        (..))"/>

            <xsl:variable name="patternId" select="parent::sch:pattern/@es:id"/>
            <axsl:next-match>
                <axsl:with-param name="precId" select="
                        ('{$patternId}',
                        $precId)"/>
            </axsl:next-match>
        </axsl:template>
    </xsl:template>


    <xsl:template match="sch:rule">
        <xsl:variable name="matchType" select="parent::sch:pattern/@es:matchType"/>
        <axsl:template match="{@context}">
            <xsl:copy-of select="../@xml:base | @xml:base"/>
            <xsl:attribute name="priority" select="es:getPriority(.) + 10"/>
            <xsl:call-template name="namespace"/>
            <axsl:param name="precId" select="()" as="xs:string*"/>

            <xsl:sequence select="es:createPatternVariables(parent::sch:pattern, false())"/>

            <axsl:variable name="es:base-uri" select="
                    es:base-uri(if (. instance of element() or . instance of document-node()) then
                        (.)
                    else
                        (..))"/>

            <xsl:variable name="patternId" select="parent::sch:pattern/@es:id"/>
            <axsl:choose>
                <axsl:when test="
                        '{$matchType}' = ('',
                        'first',
                        'priority') and '{$patternId}' = $precId"/>
                <axsl:otherwise>
                    <axsl:variable name="es:fired-rule">
                        <svrl:fired-rule context="{if (@subject) 
                                             then (@subject) 
                                             else (@context)}" es:patternId="{$patternId}" es:id="{@es:id}">
                            <xsl:copy-of select="@flag | @es:icon | @es:link | @es:regex"/>
                            <xsl:if test="@id">
                                <xsl:attribute name="id" select="@id, '{generate-id()}'" separator=""/>
                                <xsl:attribute name="es:base-id" select="@id"/>
                            </xsl:if>
                            <xsl:call-template name="getRoleFlag"/>

                            <xsl:sequence select="es:fix-for-fired-rule(.)"/>

                        </svrl:fired-rule>
                    </axsl:variable>
                    <xsl:variable name="lastAssertion" select="(sch:assert | sch:report)[last()]"/>
                    <xsl:variable name="varsWithoutDesc" select="(sch:let | xsl:variable)[. >> $lastAssertion]"/>
                    <xsl:choose>
                        <xsl:when test="@es:regex">
                            <axsl:variable name="es:context" select="."/>
                            <axsl:analyze-string select="." regex="{@es:regex}">
                                <xsl:if test="@es:regexFlags">
                                    <xsl:attribute name="flags" select="@es:regexFlags"/>
                                </xsl:if>
                                <axsl:matching-substring>
                                    <axsl:variable name="es:pos" select="position()"/>
                                    <axsl:variable name="es:match" select="."/>
                                    <axsl:copy-of select="$es:fired-rule"/>
                                    <xsl:apply-templates select="node() except $varsWithoutDesc"/>
                                </axsl:matching-substring>
                            </axsl:analyze-string>
                        </xsl:when>
                        <xsl:otherwise>
                            <axsl:copy-of select="$es:fired-rule"/>
                            <axsl:variable name="es:context" select="."/>
                            <xsl:apply-templates select="node() except $varsWithoutDesc"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </axsl:otherwise>
            </axsl:choose>
            <axsl:next-match>
                <axsl:with-param name="precId" select="
                        ('{$patternId}',
                        $precId)"/>
            </axsl:next-match>
        </axsl:template>
    </xsl:template>


    <xsl:template match="sch:assert[@es:ignorableId] | sch:report[@es:ignorableId]" priority="30">
        <xsl:variable name="es:ignorableId" select="@es:ignorableId"/>
        <axsl:variable name="es:ignoreContext" select="
                if (. instance of attribute()) then
                    (..)
                else
                    (.)"/>
        <axsl:variable name="es:ignorablePIs" select="$es:ignoreContext/preceding-sibling::processing-instruction(es_ignore)"/>
        <axsl:variable name="es:ignoreNodes" select="$es:ignoreContext/(preceding-sibling::processing-instruction() | preceding-sibling::comment() | preceding-sibling::text()[normalize-space(.) = '']) except $es:ignorablePIs"/>
        <axsl:variable name="es:focusNodes" select="$es:ignoreContext/preceding-sibling::node() except $es:ignoreNodes"/>
        <axsl:if test="not(($es:focusNodes[last()] intersect $es:ignorablePIs)[tokenize(., '\s') = '{$es:ignorableId}'])">
            <xsl:next-match/>
        </axsl:if>
    </xsl:template>

    <xsl:template match="sch:assert">
        <axsl:choose>
            <xsl:copy-of select="@xml:base"/>
            <xsl:call-template name="namespace"/>
            <axsl:when test="{@test}"/>
            <axsl:otherwise>
                <svrl:failed-assert>
                    <xsl:call-template name="reportAssertBody"/>
                </svrl:failed-assert>
            </axsl:otherwise>
        </axsl:choose>
    </xsl:template>
    <xsl:template match="sch:report">
        <axsl:if test="{@test}">
            <xsl:copy-of select="@xml:base"/>
            <xsl:call-template name="namespace"/>
            <svrl:successful-report>
                <xsl:call-template name="reportAssertBody"/>
            </svrl:successful-report>
        </axsl:if>
    </xsl:template>

    <xsl:template name="reportAssertBody">
        <xsl:variable name="messageId" select="@es:id"/>
        <xsl:copy-of select="@es:icon | @es:link"/>
        <xsl:if test="@id">
            <xsl:attribute name="es:base-id" select="@id"/>
        </xsl:if>
        <xsl:variable name="subject" select="
                (@subject,
                ../@subject)[1]"/>
        <xsl:variable name="contextPath" select="
                if (not($subject))
                then
                    ('$es:context')
                else
                    if (starts-with($subject, '/'))
                    then
                        ($subject)
                    else
                        (concat('$es:context/', $subject))"/>
        <xsl:attribute name="location">
            <xsl:text>{es:getNodePath(</xsl:text>
            <xsl:value-of select="$contextPath"/>
            <xsl:text>)}</xsl:text>
        </xsl:attribute>
        <xsl:variable name="regex" select="../@es:regex"/>
        <xsl:variable name="regexFlags" select="../@es:regexFlags"/>

        <xsl:attribute name="es:id">
            <xsl:text>{generate-id($es:context)}_</xsl:text>
            <xsl:value-of select="$messageId"/>
            <xsl:if test="$regex">
                <xsl:text>_{$es:pos}</xsl:text>
            </xsl:if>
        </xsl:attribute>
        <xsl:attribute name="es:patternId" select="parent::*/parent::sch:pattern/@es:id"/>
        <xsl:call-template name="getRoleFlag"/>
        <axsl:if test="$es:base-uri != $es:base-uri-root">
            <axsl:attribute name="es:base-uri" select="$es:base-uri"/>
        </axsl:if>
        <axsl:attribute name="test">
            <xsl:value-of select="@test"/>
        </axsl:attribute>
        <xsl:choose>
            <xsl:when test="$regex">
                <axsl:variable name="es:regexPos" select="es:getRegexPosition($es:context, '{$regex}', '{$regexFlags}', $es:pos)"/>
                <axsl:variable name="es:substrings" select="$es:regexPos - 1, $es:regexPos + string-length(.) - 1"/>
                <axsl:attribute name="es:substring" select="$es:substrings"/>
            </xsl:when>
            <xsl:otherwise>
                <axsl:variable name="es:regexPos" select="()"/>
                <axsl:variable name="es:substrings" select="()"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:variable name="diagnostics" select="tokenize(@diagnostics, '\s')"/>
        <xsl:apply-templates select="//sch:diagnostic[@id = $diagnostics]"/>
        <svrl:text>
            <xsl:apply-templates/>
        </svrl:text>
        <xsl:sequence select="es:fix-for-tests(.)"/>
    </xsl:template>

    <xsl:template name="getRoleFlag">
        <xsl:attribute name="role" select="((ancestor-or-self::*/@role)[last()], $defaultRole)[1]"/>
        <xsl:attribute name="es:roleLabel" select="((ancestor-or-self::*/@es:roleLabel)[last()], $defaultRoleLabel)[1]"/>
        <xsl:copy-of select="(ancestor-or-self::*/@flag)[last()]"/>
    </xsl:template>

    <xsl:template match="sch:let">
        <axsl:variable name="{@name}" select="{@value}">
            <xsl:copy-of select="@xml:base"/>
            <xsl:call-template name="namespace"/>
        </axsl:variable>
    </xsl:template>
    <xsl:template match="sch:value-of" mode="#all">
        <axsl:value-of select="{@select}">
            <xsl:copy-of select="@xml:base"/>
            <xsl:call-template name="namespace"/>
        </axsl:value-of>
    </xsl:template>
    <xsl:template match="sch:name">
        <xsl:variable name="select" select="
                if (@path)
                then
                    (concat(@path, '/name()'))
                else
                    ('name()')"/>
        <axsl:value-of select="{$select}">
            <xsl:copy-of select="@xml:base"/>
            <xsl:call-template name="namespace"/>
        </axsl:value-of>
    </xsl:template>
    <xsl:template match="sch:ns"/>
    <xsl:template match="sch:pattern">
        <xsl:if test="es:isActive(., $phase)">
            <xsl:apply-templates/>
        </xsl:if>
    </xsl:template>
    <xsl:template match="sch:p | sch:diagnostics"/>
    <xsl:template match="sch:diagnostic">
        <svrl:diagnostic-reference diagnostic="{@id}">
            <xsl:copy-of select="@es:icon | @es:link"/>
            <svrl:text>
                <xsl:apply-templates/>
            </svrl:text>
        </svrl:diagnostic-reference>
    </xsl:template>

    <xsl:template match="sch:span | sch:dir | sch:emph">
        <xsl:element name="es:{local-name(.)}">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="sch:span/@class | sch:dir/@value">
        <xsl:copy/>
    </xsl:template>

    <xsl:template match="es:param">
        <axsl:param>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
        </axsl:param>
    </xsl:template>

    <xsl:template match="@*" priority="-10">
        <xsl:copy/>
    </xsl:template>

    <xsl:template match="*:namespace-alias[namespace-uri() = 'http://www.w3.org/1999/XSL/Transform']"/>

    <xsl:template match="*[namespace-uri() = 'http://www.w3.org/1999/XSL/Transform']" priority="-5">
        <xsl:copy copy-namespaces="no">
            <xsl:copy-of select="@*"/>
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="*" priority="-10">
        <xsl:copy copy-namespaces="no">
            <xsl:copy-of select="@*"/>
            <xsl:call-template name="namespace"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    

</xsl:stylesheet>
