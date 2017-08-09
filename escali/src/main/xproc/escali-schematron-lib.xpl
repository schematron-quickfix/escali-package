<?xml version="1.0" encoding="UTF-8"?>

<!--
    Copyright (c) 2014 Nico Kutscherauer
    
    This file is part of Escali Schematron (XProc implementation).
    
    Escali Schematron is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    Escali Schematron is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with Escali Schematron (../xsl/gpl-3.0.txt).  If not, see http://www.gnu.org/licenses/gpl-3.0.
    
-->

<p:library xmlns:p="http://www.w3.org/ns/xproc" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:cx="http://xmlcalabash.com/ns/extensions" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:c="http://www.w3.org/ns/xproc-step" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsm="http://www.schematron-quickfix.com/manipulator/process" version="1.0">

    <p:declare-step type="es:intern-validateAndFix" name="es_intern-validateAndFix">
        <p:input port="schema" primary="true"/>
        <p:input port="params">
            <p:empty/>
        </p:input>
        <p:output port="result" primary="true"/>
        <p:option name="msgPos"/>
        <p:option name="fixName"/>
        <p:option name="phase" select="'#ALL'"/>
        <p:option name="xml-save-mode" select="'false'"/>
        <p:option name="use-for-each.maximum" select="50"/>
        <p:option name="system" select="'bat'"/>
        <es:validateAndFix>
            <p:input port="schema">
                <p:document href="../schema/SQF/sqf.sch"/>
            </p:input>
            <p:input port="params">
                <p:pipe port="params" step="es_intern-validateAndFix"/>
            </p:input>
            <p:with-option name="msgPos" select="$msgPos"/>
            <p:with-option name="fixName" select="$fixName"/>
            <p:with-option name="phase" select="$phase"/>
            <p:with-option name="xml-save-mode" select="$xml-save-mode"/>
            <p:with-option name="use-for-each.maximum" select="$use-for-each.maximum"/>
            <p:with-option name="system" select="$system"/>
        </es:validateAndFix>
    </p:declare-step>

    <p:declare-step type="es:validateAndFix" name="es_validateAndFix">
        <p:input port="source" primary="true"/>
        <p:input port="schema"/>
        <p:input port="params">
            <p:empty/>
        </p:input>
        <p:output port="result" primary="true"/>
        <p:output port="secondary" sequence="true">
            <p:pipe port="secondary" step="xsmCheck"/>
        </p:output>
        <p:output port="svrl">
            <p:pipe port="result" step="validate"/>
        </p:output>
        <p:option name="msgPos"/>
        <p:option name="fixName"/>
        <p:option name="phase"/>
        <p:option name="xml-save-mode" select="'false'"/>
        <p:option name="use-for-each.maximum" select="50"/>
        <p:option name="system" select="'bat'"/>
        <es:schematron name="validate">
            <p:input port="schema">
                <p:pipe port="schema" step="es_validateAndFix"/>
            </p:input>
            <p:input port="source">
                <p:pipe port="source" step="es_validateAndFix"/>
            </p:input>
            <p:with-option name="phase" select="$phase"/>
            <p:with-option name="xml-save-mode" select="$xml-save-mode"/>
            <p:with-option name="use-for-each.maximum" select="$use-for-each.maximum"/>
            <p:input port="params">
                <p:pipe port="params" step="es_validateAndFix"/>
            </p:input>
        </es:schematron>
        <p:for-each name="parameters">
            <p:iteration-source select="/svrl:schematron-output/svrl:*[local-name() = 'failed-assert' or local-name() = 'successful-report'][xs:integer($msgPos)]/sqf:fix[@fixId=$fixName]/sqf:user-entry/sqf:param">
                <p:pipe port="result" step="validate"/>
            </p:iteration-source>
            <p:output port="result" primary="true" sequence="true"/>
            <p:variable name="id" select="sqf:param/@param-id"/>
            <p:variable name="name" select="sqf:param/@name"/>
            <p:variable name="value" select="/c:param-set/c:param[@name=$name]/@value">
                <p:pipe port="params" step="es_validateAndFix"/>
            </p:variable>
            <p:variable name="type" select="(/c:param-set/c:param[@name=$name]/@es:type, 'xs:string')[1]">
                <p:pipe port="params" step="es_validateAndFix"/>
            </p:variable>
            <es:parameter>
                <p:with-option name="param-name" select="$id"/>
                <p:with-option name="param-value" select="$value"/>
                <p:with-option name="type" select="$type"/>
            </es:parameter>
        </p:for-each>
        <p:wrap-sequence wrapper="c:param-set" name="paramset"/>
        <es:quickFix name="quickFix">
            <p:input port="svrl">
                <p:pipe port="result" step="validate"/>
            </p:input>
            <p:input port="source">
                <p:pipe port="source" step="es_validateAndFix"/>
            </p:input>
            <p:input port="params">
                <p:pipe port="result" step="paramset"/>
            </p:input>
            <p:with-option name="fixId" select="/svrl:schematron-output/svrl:*[local-name() = 'failed-assert' or local-name() = 'successful-report'][xs:integer($msgPos)]/sqf:fix[@fixId=$fixName]/@id">
                <p:pipe port="result" step="validate"/>
            </p:with-option>
            <p:with-option name="xml-save-mode" select="$xml-save-mode"/>
        </es:quickFix>
        <p:choose name="xsmCheck">
            <p:when test="$xml-save-mode='true'">
                <p:output port="result" primary="true">
                    <p:pipe port="result" step="xsmPrimaryResult"/>
                </p:output>
                <p:output port="secondary" sequence="true">
                    <p:pipe port="result" step="xsmSecondaryResult"/>
                </p:output>

                <p:variable name="sourceFolder" select="resolve-uri('.', document-uri(/))">
                    <p:pipe port="source" step="es_validateAndFix"/>
                </p:variable>
                <p:variable name="outPath" select="'../../temp/tempOutput.xml'"/>
                <es:xsm name="xsm">
                    <p:with-option name="tempFolder" select="$sourceFolder"/>
                    <p:with-option name="xsmFolder" select="resolve-uri('../../../net.sqf.xsm/xsm/v0.2/')"/>
                    <p:with-option name="system" select="$system"/>
                    <p:with-option name="outPath" select="$outPath"/>
                </es:xsm>
                <p:load cx:depends-on="xsm" name="xsmPrimaryResult">
                    <p:with-option name="href" select="$outPath"/>
                </p:load>
                <p:for-each>
                    <p:iteration-source>
                        <p:pipe port="secondary" step="quickFix"/>
                    </p:iteration-source>
                    <p:variable name="outPathSec" select="concat('../../temp/tempOutput_', p:iteration-position(), '.xml')"/>
                    <es:xsm name="xsmSec">
                        <p:with-option name="tempFolder" select="$sourceFolder"/>
                        <p:with-option name="xsmFolder" select="resolve-uri('../../../net.sqf.xsm/xsm/v0.1/')"/>
                        <p:with-option name="system" select="$system"/>
                        <p:with-option name="outPath" select="$outPathSec"/>
                    </es:xsm>
                    <!--                <p:sink name="xsm"/>-->
                    <p:load cx:depends-on="xsmSec">
                        <p:with-option name="href" select="$outPathSec"/>
                    </p:load>
                </p:for-each>
                <p:identity name="xsmSecondaryResult"/>
            </p:when>
            <p:otherwise>
                <p:output port="result" primary="true">
                    <p:pipe port="result" step="primary"/>
                </p:output>
                <p:output port="secondary" sequence="true">
                    <p:pipe port="result" step="secondary"/>
                </p:output>
                <p:identity name="primary"/>
                <p:identity name="secondary">
                    <p:input port="source">
                        <p:pipe port="secondary" step="quickFix"/>
                    </p:input>
                </p:identity>
            </p:otherwise>
        </p:choose>
    </p:declare-step>
    <p:declare-step type="es:parameter" name="es_parameter">
        <p:option name="param-name" select="''"/>
        <p:option name="param-value" select="''"/>
        <p:option name="commandline" select="''"/>
        <p:option name="type" select="'xs:string'"/>
        <p:output port="result" primary="true"/>
        <p:xslt template-name="createParam">
            <p:input port="source">
                <p:inline>
                    <dummy/>
                </p:inline>
            </p:input>
            <p:input port="stylesheet">
                <p:inline>
                    <xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:c="http://www.w3.org/ns/xproc-step" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" exclude-result-prefixes="xs xd" version="2.0">
                        <xsl:param name="name" select="''"/>
                        <xsl:param name="value" select="''"/>
                        <xsl:param name="commandline" select="''"/>
                        <xsl:param name="type" select="'xs:string'"/>
                        <xsl:template name="createParam">
                            <xsl:choose>
                                <xsl:when test="$commandline != ''">
                                    <xsl:variable name="pairs" select="tokenize($commandline, ';')"/>
                                    <c:param-set>
                                        <xsl:for-each select="$pairs">
                                            <xsl:variable name="name" select="substring-before(., '=')"/>
                                            <xsl:variable name="value" select="substring-after(., '=')"/>
                                            <xsl:if test="$name=''">
                                                <xsl:message terminate="yes">$name is empty! cmd: (<xsl:value-of select="$commandline"/>)</xsl:message>
                                            </xsl:if>
                                            <c:param name="{$name}" value="{$value}" es:type="{$type}" xmlns:sqf="http://www.schematron-quickfix.com/validator/process"/>
                                        </xsl:for-each>
                                    </c:param-set>
                                </xsl:when>
                                <xsl:when test="$value != ''">
                                    <c:param name="{concat('sqf:',$name)}" value="{$value}" es:type="{$type}" xmlns:sqf="http://www.schematron-quickfix.com/validator/process"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <c:param-set/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:template>
                    </xsl:stylesheet>
                </p:inline>
            </p:input>
            <p:with-param name="name" select="$param-name"/>
            <p:with-param name="value" select="$param-value"/>
            <p:with-param name="commandline" select="$commandline"/>
            <p:with-param name="type" select="$type"/>
        </p:xslt>
    </p:declare-step>

    <p:declare-step type="es:getSvrlParam" name="es_getSvrlParam">
        <p:input port="svrl" primary="true"/>
        <p:option name="commandline" select="''"/>
        <p:option name="fixId"/>
        <p:output port="result" primary="true"/>
        <p:xslt template-name="createParam">
            <p:input port="stylesheet">
                <p:inline>
                    <xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:c="http://www.w3.org/ns/xproc-step" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" exclude-result-prefixes="xs xd" version="2.0">
                        <xsl:param name="commandline" select="''"/>
                        <xsl:param name="fixId" select="''"/>
                        <xsl:template name="createParam">
                            <xsl:variable name="fix" select="/svrl:schematron-output/svrl:*/sqf:fix[@id=$fixId]"/>
                            <xsl:variable name="pairs" select="tokenize($commandline, ';')"/>
                            <c:param-set>
                                <xsl:for-each select="$pairs[.!='']">
                                    <xsl:variable name="name" select="substring-before(., '=')"/>
                                    <xsl:variable name="value" select="substring-after(., '=')"/>
                                    <xsl:variable name="name" select="$fix/sqf:user-entry/sqf:param[@name=$name]/@param-id"/>
                                    <xsl:if test="$name">
                                        <c:param name="sqf:{$name}" value="{$value}" xmlns:sqf="http://www.schematron-quickfix.com/validator/process"/>
                                    </xsl:if>
                                </xsl:for-each>
                            </c:param-set>
                        </xsl:template>
                    </xsl:stylesheet>
                </p:inline>
            </p:input>
            <p:with-param name="commandline" select="$commandline"/>
            <p:with-param name="fixId" select="$fixId"/>
        </p:xslt>
    </p:declare-step>

    <p:declare-step type="es:compile" name="es_compile">
        <p:input port="schema" primary="true"/>
        <p:input port="parameters" kind="parameter"/>
        <p:output port="validator" primary="true"/>

        <p:option name="phase" select="'#ALL'"/>
        <p:option name="lang" select="'#NULL'"/>
        <p:option name="xml-save-mode" select="'false'"/>
        <p:option name="use-for-each.maximum" select="50"/>
        <p:option name="validate-schema" select="'true'"/>

        <es:intern-validation>
            <p:with-option name="validate-schema" select="$validate-schema"/>
        </es:intern-validation>


        <p:xslt name="escali1">
            <p:input port="stylesheet">
                <p:document href="../xsl/01_compiler/escali_compiler_1_include.xsl"/>
            </p:input>
            <p:with-param name="es:lang" select="if ($lang = '#NULL' and /sch:schema/@xml:lang) 
                                               then (/sch:schema/@xml:lang) 
                                               else ($lang)"/>
            <p:with-param name="es:type-available" select="'false'"/>
            <p:input port="parameters">
                <p:pipe port="parameters" step="es_compile"/>
            </p:input>
        </p:xslt>
        <p:xslt name="escali2">
            <p:input port="stylesheet">
                <p:document href="../xsl/01_compiler/escali_compiler_2_abstract-patterns.xsl"/>
            </p:input>
            <p:input port="parameters">
                <p:pipe port="parameters" step="es_compile"/>
            </p:input>
        </p:xslt>
        <p:wrap-sequence wrapper="param-set" wrapper-namespace="http://www.w3.org/ns/xproc-step" wrapper-prefix="c" name="params">
            <p:input port="source">
                <p:pipe port="parameters" step="es_compile"/>
            </p:input>
        </p:wrap-sequence>
        <p:sink/>
        <p:identity>
            <p:input port="source">
                <p:inline>
                    <xsl:stylesheet version="2.0">
                        <xsl:import href="../xsl/01_compiler/escali_compiler_3_main.xsl"/>
                        <xsl:param name="xsm:xml-save-mode" select="true()" as="xs:boolean"/>
                        <xsl:param name="es:use-for-each.maximum" select="50" as="xs:integer"/>
                        <xsl:param name="sqf:useSQF" select="exists(key('elementsByNamespace', 'http://www.schematron-quickfix.com/validator/process'))" as="xs:boolean"/>
                    </xsl:stylesheet>
                </p:inline>
            </p:input>
        </p:identity>
        <p:add-attribute match="xsl:param[@name='xsm:xml-save-mode']" attribute-name="select">
            <p:with-option name="attribute-value" select="concat($xml-save-mode, '()')"/>
        </p:add-attribute>
        <p:add-attribute match="xsl:param[@name='es:use-for-each.maximum']" attribute-name="select">
            <p:with-option name="attribute-value" select="$use-for-each.maximum"/>
        </p:add-attribute>
        <p:choose>
            <p:variable name="useSQF" select="//c:param[@name='sqf:useSQF']/@value">
                <p:pipe port="result" step="params"/>
            </p:variable>
            <p:when test="$useSQF != ''">
                <p:add-attribute match="xsl:param[@name='sqf:useSQF']" attribute-name="select">
                    <p:with-option name="attribute-value" select="$useSQF"/>
                </p:add-attribute>
            </p:when>
            <p:otherwise>
                <p:identity/>
            </p:otherwise>
        </p:choose>
        <p:identity name="main-compiler"/>
        <p:store href="../../temp/debug-compiler.xsl"/>
        <p:delete match="c:param[@name='sqf:useSQF']">
            <p:input port="source">
                <p:pipe port="result" step="params"/>
            </p:input>
        </p:delete>
        <p:filter select="/c:param-set/*" name="paramsForEscali3"/>
        <p:xslt name="escali3">
            <p:input port="source">
                <p:pipe port="result" step="escali2"/>
            </p:input>
            <p:input port="stylesheet">
                <p:pipe port="result" step="main-compiler"/>
            </p:input>
            <p:with-param name="phase" select="$phase">
                <p:pipe port="result" step="escali2"/>
            </p:with-param>
            <p:input port="parameters">
                <p:pipe port="result" step="paramsForEscali3"/>
            </p:input>
        </p:xslt>
    </p:declare-step>

    <p:declare-step type="es:intern-validation" name="es_intern-validation">
        <p:input port="schema" primary="true"/>
        <p:output port="result" primary="true">
            <p:pipe port="schema" step="es_intern-validation"/>
        </p:output>
        <p:output port="report">
            <p:pipe port="result" step="svrl"/>
        </p:output>
        <p:option name="validate-schema" select="'true'"/>
        <p:option name="ignore-rng-xsd" select="'false'"/>
        <p:xslt>
            <p:input port="stylesheet">
                <p:document href="../xsl/01_compiler/escali_compiler_0_validate.xsl"/>
            </p:input>
            <p:with-param name="dummy" select="''"/>
        </p:xslt>
        <!--<p:validate-with-xml-schema>
            <p:input port="schema">
                <p:document href="../schema/escali/iso-schematron.xsd"/>
            </p:input>
        </p:validate-with-xml-schema>-->
        <p:choose>
            <p:when test="$ignore-rng-xsd = 'false'">
                <p:validate-with-relax-ng>
                    <p:input port="schema">
                        <p:document href="../schema/escali/schematron-extension.rng"/>
                    </p:input>
                </p:validate-with-relax-ng>
            </p:when>
            <p:otherwise>
                <p:identity/>
            </p:otherwise>
        </p:choose>
        <p:choose>
            <p:when test="$validate-schema = 'true'">
                <p:sink/>
                <es:schematron name="intern-sqf.sch">
                    <p:input port="schema">
                        <p:document href="../schema/SQF/sqf.sch"/>
                    </p:input>
                    <p:input port="source">
                        <p:pipe port="schema" step="es_intern-validation"/>
                    </p:input>
                    <p:with-option name="validate-schema" select="'false'"/>
                    <p:with-param name="dummy" select="''"/>
                </es:schematron>
            </p:when>
            <p:otherwise>
                <p:sink/>
                <p:identity>
                    <p:input port="source">
                        <p:inline>
                            <svrl:schematron-output/>
                        </p:inline>
                    </p:input>
                </p:identity>
            </p:otherwise>
        </p:choose>
        <p:identity name="svrl"/>
    </p:declare-step>

    <p:declare-step type="es:schematron" name="es_schematron">
        <p:input port="schema"/>
        <p:input port="source" primary="true"/>
        <p:input port="params" kind="parameter">
            <p:empty/>
        </p:input>
        <p:output port="result" primary="true"/>
        <p:output port="secundaries" sequence="true">
            <p:pipe port="source" step="es_schematron"/>
            <p:pipe port="result" step="xincluded"/>
            <p:pipe port="validator" step="compiled"/>
            <p:pipe port="result" step="svrl_raw"/>
            <p:pipe port="result" step="svrl"/>
        </p:output>
        <p:option name="phase" select="'#ALL'"/>
        <p:option name="lang" select="'#NULL'"/>
        <p:option name="outputFormat" select="'svrl'"/>
        <p:option name="xinclude" select="'false'"/>
        <p:option name="xml-save-mode" select="'false'"/>
        <p:option name="use-for-each.maximum" select="50"/>
        <p:option name="validate-schema" select="'false'"/>

        <es:compile name="compiled">
            <p:with-option name="phase" select="$phase"/>
            <p:with-option name="lang" select="$lang"/>
            <p:with-option name="xml-save-mode" select="$xml-save-mode"/>
            <p:with-option name="use-for-each.maximum" select="$use-for-each.maximum"/>
            <p:with-option name="validate-schema" select="$validate-schema"/>
            <p:input port="schema">
                <p:pipe port="schema" step="es_schematron"/>
            </p:input>
            <p:input port="parameters">
                <p:pipe port="params" step="es_schematron"/>
            </p:input>
        </es:compile>
        <p:choose>
            <p:when test="$xinclude = 'true'">
                <p:xinclude>
                    <p:input port="source">
                        <p:pipe port="source" step="es_schematron"/>
                    </p:input>
                </p:xinclude>
                <!--                <p:add-xml-base/>-->
            </p:when>
            <p:otherwise>
                <p:identity>
                    <p:input port="source">
                        <p:pipe port="source" step="es_schematron"/>
                    </p:input>
                </p:identity>
            </p:otherwise>
        </p:choose>
        <p:identity name="xincluded"/>
        <p:xslt name="svrl_raw">
            <p:input port="stylesheet">
                <p:pipe port="validator" step="compiled"/>
            </p:input>
            <p:input port="parameters">
                <p:pipe port="params" step="es_schematron"/>
            </p:input>
        </p:xslt>
        <p:xslt name="svrl">
            <p:input port="stylesheet">
                <p:document href="../xsl/02_validator/escali_validator_2_postprocess.xsl"/>
            </p:input>
            <p:with-param name="dummy" select="''"/>
        </p:xslt>
        <p:validate-with-xml-schema>
            <p:input port="schema">
                <p:document href="../schema/SVRL/svrl.xsd"/>
            </p:input>
        </p:validate-with-xml-schema>
        <p:choose>
            <p:when test="$outputFormat = 'html' or $outputFormat = 'escali'">
                <p:load name="outputPrinter">
                    <p:with-option name="href" select="concat('../xsl/02_validator/escali_validator_3_', $outputFormat, '-report.xsl')"/>
                </p:load>
                <p:xslt>
                    <p:input port="source">
                        <p:pipe port="result" step="svrl"/>
                    </p:input>
                    <p:input port="stylesheet">
                        <p:pipe port="result" step="outputPrinter"/>
                    </p:input>
                    <p:with-param name="dummy" select="''"/>
                </p:xslt>
            </p:when>
            <p:otherwise>
                <p:identity/>
            </p:otherwise>
        </p:choose>
    </p:declare-step>

    <p:declare-step type="es:quickFix" name="es_quickFix">
        <p:input port="svrl" primary="true"/>
        <p:input port="source"/>
        <p:input port="params" kind="parameter" sequence="true"/>
        <p:output port="result" primary="true">
            <p:pipe port="result" step="manipulator_2"/>
        </p:output>
        <p:output port="secondary" primary="false" sequence="true">
            <p:pipe port="result" step="seconds"/>
        </p:output>
        <p:option name="fixId"/>
        <p:option name="xml-save-mode" select="'false'"/>
        <p:xslt name="extractor">
            <p:input port="stylesheet">
                <p:document href="../xsl/03_extractor/escali_extractor_1_main.xsl"/>
            </p:input>
            <p:with-param name="id" select="$fixId"/>
        </p:xslt>
        <es:addParametersToSheet name="extractorParams">
            <p:input port="params">
                <p:pipe port="params" step="es_quickFix"/>
            </p:input>
        </es:addParametersToSheet>
        <p:store href="../../temp/debug/03_resolved.xsl"/>
        <p:xslt name="manipulator_1">
            <p:input port="source">
                <p:pipe port="source" step="es_quickFix"/>
            </p:input>
            <p:input port="stylesheet">
                <p:pipe port="result" step="extractorParams"/>
            </p:input>
            <p:input port="parameters">
                <p:empty/>
            </p:input>
            <p:with-param name="xsm:xml-save-mode" select="$xml-save-mode='true'" xmlns:xsm="http://www.schematron-quickfix.com/manipulator/process"/>
        </p:xslt>

        <p:xslt name="manipulator_2">
            <p:input port="stylesheet">
                <p:document href="../xsl/04_manipulator/escali_manipulator_2_postprocess.xsl"/>
            </p:input>
            <p:with-param name="dummy" select="''"/>
        </p:xslt>
        <p:for-each name="seconds">
            <p:iteration-source>
                <p:pipe port="secondary" step="manipulator_1"/>
            </p:iteration-source>
            <p:output port="result">
                <p:pipe port="result" step="manipulator_2s"/>
            </p:output>
            <p:xslt name="manipulator_2s">
                <p:input port="stylesheet">
                    <p:document href="../xsl/04_manipulator/escali_manipulator_2_postprocess.xsl"/>
                </p:input>
                <p:with-param name="dummy" select="''"/>
            </p:xslt>
        </p:for-each>
    </p:declare-step>

    <p:declare-step name="es_addParametersToSheet" type="es:addParametersToSheet">
        <p:input port="stylesheet" primary="true"/>
        <p:output port="result" primary="true"/>
        <p:input port="params" kind="parameter" sequence="true"/>


        <p:wrap-sequence wrapper="param-set" wrapper-namespace="http://www.w3.org/ns/xproc-step" wrapper-prefix="c" name="param-set">
            <p:input port="source" select="//c:param">
                <p:pipe port="params" step="es_addParametersToSheet"/>
            </p:input>
        </p:wrap-sequence>
        <p:insert match="/xsl:stylesheet" position="first-child">
            <p:input port="source">
                <p:pipe port="stylesheet" step="es_addParametersToSheet"/>
            </p:input>
            <p:input port="insertion">
                <p:pipe port="result" step="param-set"/>
            </p:input>
        </p:insert>

        <p:xslt>
            <p:input port="stylesheet">
                <p:inline>
                    <xsl:stylesheet version="2.0">
                        <xsl:variable name="c:param-set" select="/xsl:stylesheet/c:param-set/c:param"/>
                        <!--                        <xsl:template match="/xsl:stylesheet/c:param-set"/>-->

                        <xsl:template match="/xsl:stylesheet/xsl:param[@name = $c:param-set/@name]">
                            <xsl:variable name="c:param" select="$c:param-set[@name = current()/@name]"/>
                            <xsl:copy>
                                <xsl:apply-templates select="@*"/>
                                <xsl:variable name="quo">'</xsl:variable>
                                <xsl:variable name="select" select=" 
                                    if ($c:param/@es:select) 
                                    then ($c:param/@es:select) 
                                    else  if ($c:param/@es:type) 
                                    then (concat($c:param/@es:type, '(',$quo, $c:param/@value, $quo, ')')) 
                                    else (concat($quo, $c:param/@value, $quo))"/>
                                <xsl:attribute name="select" select="$select"/>
                            </xsl:copy>
                        </xsl:template>

                        <xsl:template match="node() | @*" mode="#all">
                            <xsl:copy>
                                <xsl:apply-templates select="@*" mode="#current"/>
                                <xsl:apply-templates select="node()" mode="#current"/>
                            </xsl:copy>
                        </xsl:template>

                    </xsl:stylesheet>
                </p:inline>
            </p:input>
        </p:xslt>

    </p:declare-step>

    <p:declare-step type="es:xsm" name="es_xsm">
        <p:input port="source" primary="true"/>
        <p:option name="xsmFolder"/>
        <p:option name="tempFolder"/>
        <p:option name="system" select="'bat'"/>
        <p:option name="outPath" select="'../../temp/tempOutput.xml'"/>

        <p:variable name="cwd" select="es:get-file-path($xsmFolder)"/>
        <p:variable name="xsmScript" select=" es:get-file-path(concat($xsmFolder,'xsm.', $system))"/>
        <p:variable name="command" select="   if ($system='bat') 
                                            then ($xsmScript) 
                                            else ($system)"/>
        <p:variable name="manipulator" select="replace(concat($tempFolder, '/manipulator.tmp'), '^file:', '')"/>
        <p:variable name="manipulatorFile" select="es:get-file-path(concat($tempFolder, '/manipulator.tmp'))"/>

        <p:variable name="quote" select="if ($system='bat') 
                                            then ('&quot;') 
                                            else ('')"/>
        <p:variable name="outFile" select="es:get-file-path(resolve-uri($outPath))"/>

        <p:variable name="args" select="concat( if ($system='bat') 
                                              then ('') 
                                              else ($xsmScript), ' ', $quote, $manipulatorFile, $quote, ' -o ', $quote, $outFile, $quote)"/>
        <p:load name="xsm-schema">
            <p:with-option name="href" select="resolve-uri('xml/schema/XSM/xpath-based-string-manipulator.xsd', $xsmFolder)"/>
        </p:load>
        <p:validate-with-xml-schema>
            <p:input port="source">
                <p:pipe port="source" step="es_xsm"/>
            </p:input>
            <p:input port="schema">
                <p:pipe port="result" step="xsm-schema"/>
            </p:input>
        </p:validate-with-xml-schema>
        <p:store name="storeManSheet">
            <p:with-option name="href" select="$manipulator"/>
        </p:store>
        <p:exec result-is-xml="false" cx:depends-on="storeManSheet" name="exec">
            <p:input port="source">
                <p:empty/>
            </p:input>
            <p:with-option name="command" select="$command"/>
            <p:with-option name="cwd" select="$cwd"/>
            <p:with-option name="args" select="$args"/>
        </p:exec>
        <p:add-attribute match="c:result" attribute-name="cwd" xmlns:c="http://www.w3.org/ns/xproc-step">
            <p:with-option name="attribute-value" select="$cwd"/>
        </p:add-attribute>
        <p:add-attribute match="c:result" attribute-name="args" xmlns:c="http://www.w3.org/ns/xproc-step">
            <p:with-option name="attribute-value" select="$args"/>
        </p:add-attribute>
        <p:store href="../../temp/xsm-out.xml"/>
        <p:store href="../../temp/xsm-err.xml">
            <p:input port="source">
                <p:pipe port="errors" step="exec"/>
            </p:input>
        </p:store>
    </p:declare-step>
</p:library>
