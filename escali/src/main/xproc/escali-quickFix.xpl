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


<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:c="http://www.w3.org/ns/xproc-step" version="1.0" name="main-escali-quickFix">

    <!--    <p:input port="svrl" primary="true"/>-->
    <p:input port="config">
        <p:document href="../../META-INF/config.xml"/>
    </p:input>

    <!--<p:output port="source" primary="true"/>-->

    <p:option name="fixId"/>
    <p:option name="userEntries" select="''"/>
    <p:option name="system" select="'bat'"/>

    <p:import href="escali-schematron-lib.xpl"/>

    <p:variable name="tempFolder" select="resolve-uri(/es:config/es:tempFolder, document-uri(/))">
        <p:pipe port="config" step="main-escali-quickFix"/>
    </p:variable>
    <p:load name="svrlLoad">
        <p:with-option name="href" select="concat($tempFolder, 'temp.svrl')"/>
    </p:load>
    <es:getSvrlParam name="userEntrieParams">
        <p:with-option name="commandline" select="$userEntries"/>
        <p:with-option name="fixId" select="$fixId"/>
    </es:getSvrlParam>
    <p:load name="instanceLoad">
        <p:with-option name="href" select="/svrl:schematron-output/sqf:topLevel/@instance">
            <p:pipe port="result" step="svrlLoad"/>
        </p:with-option>
    </p:load>
    <p:choose>
        <p:variable name="xml-save-mode" select="/es:config/es:output/es:xml-save-mode='true' and $system='bat'">
            <p:pipe port="config" step="main-escali-quickFix"/>
        </p:variable>
        <p:when test="$userEntries=''">
            <es:quickFix>
                <p:input port="svrl">
                    <p:pipe port="result" step="svrlLoad"/>
                </p:input>
                <p:input port="source">
                    <p:pipe port="result" step="instanceLoad"/>
                </p:input>
                <p:with-param name="dummy" select="''"/>
                <p:with-option name="fixId" select="$fixId"/>
                <p:with-option name="xml-save-mode" select="$xml-save-mode"/>
            </es:quickFix>
        </p:when>
        <p:otherwise>
            <es:quickFix>
                <p:input port="svrl">
                    <p:pipe port="result" step="svrlLoad"/>
                </p:input>
                <p:input port="source">
                    <p:pipe port="result" step="instanceLoad"/>
                </p:input>
                <p:input port="params">
                    <p:pipe port="result" step="userEntrieParams"/>
                </p:input>
                <p:with-option name="fixId" select="$fixId"/>
                <p:with-option name="xml-save-mode" select="$xml-save-mode"/>
            </es:quickFix>
        </p:otherwise>
    </p:choose>
    <p:choose>
        <p:variable name="xml-save-mode" select="/es:config/es:output/es:xml-save-mode='true' and $system='bat'">
            <p:pipe port="config" step="main-escali-quickFix"/>
        </p:variable>
        <p:variable name="xsmFolder" select="resolve-uri(/es:config/es:output/es:xsm-processor, document-uri(/))">
            <p:pipe port="config" step="main-escali-quickFix"/>
        </p:variable>
        <p:when test="$xml-save-mode='true'">
            <es:xsm>
                <p:with-option name="tempFolder" select="$tempFolder"/>
                <p:with-option name="xsmFolder" select="$xsmFolder"/>
                <p:with-option name="system" select="$system"/>
            </es:xsm>
        </p:when>
        <p:otherwise>
            <p:store>
                <p:with-option name="href" select="concat($tempFolder, 'tempOutput.xml')"/>
            </p:store>
        </p:otherwise>
    </p:choose>
    <!--<p:store>
        <p:with-option name="href" select="$out"/>
    </p:store>-->
    <!--    <p:sink/>-->
</p:declare-step>