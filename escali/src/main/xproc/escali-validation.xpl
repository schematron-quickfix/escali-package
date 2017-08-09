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

<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:es="http://www.escali.schematron-quickfix.com/"
    xmlns:c="http://www.w3.org/ns/xproc-step" version="1.0" name="main-escali-validation">
    
    <p:input port="source" primary="true"/>
    <p:input port="schema"/>
    <p:input port="config">
        <p:document href="../../META-INF/config.xml"/>
    </p:input>
    
    <p:output port="html" primary="true">
        <p:pipe port="result" step="html"/>
    </p:output>
    <p:option name="system" select="'bat'"/>
    
    
    <p:import href="escali-schematron-lib.xpl"/>
    
    <p:variable name="tempFolder" select="resolve-uri(/es:config/es:tempFolder, document-uri(/))">
        <p:pipe port="config" step="main-escali-validation"/>
    </p:variable>
    
    <p:variable name="phase" select="/es:config/es:phase">
        <p:pipe port="config" step="main-escali-validation"/>
    </p:variable>
    
    <p:variable name="xml-save-mode" select="/es:config/es:output/es:xml-save-mode='true' and $system='bat'">
        <p:pipe port="config" step="main-escali-validation"/>
    </p:variable>
    
    <es:schematron name="svrl">
        <p:input port="source">
            <p:pipe port="source" step="main-escali-validation"/>
        </p:input>
        <p:input port="schema">
            <p:pipe port="schema" step="main-escali-validation"/>
        </p:input>
        <p:with-option name="phase" select="$phase"/>
        <p:with-option name="xml-save-mode" select="$xml-save-mode"/>
        
        <p:with-param name="dummy" select="''"/>
    </es:schematron>
    
    <p:xslt name="html">
        <p:input port="stylesheet">
            <p:document href="../xsl/02_validator/escali_validator_3_html-report.xsl"/>
        </p:input>
        <p:with-param name="dummy" select="''"/>
    </p:xslt>
    
    
    <p:store>
        <p:with-option name="href" select="concat($tempFolder, 'temp.svrl')"/>
        <p:input port="source">
            <p:pipe port="result" step="svrl"/>
        </p:input>
    </p:store>
<!--    <p:sink/>-->
</p:declare-step>