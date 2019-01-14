<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    
    <pattern id="pattern1">
        <rule context="element">
            <assert test="false()">Pattern 1, Rule 1</assert>
        </rule>
        <rule context="element">
            <assert test="false()">Pattern 1, Rule 2</assert>
        </rule>
        <rule context="/*/*">
            <assert test="false()">Pattern 1, Rule 3</assert>
        </rule>
    </pattern>
    <pattern id="pattern2" es:matchType="all">
        <rule context="element">
            <assert test="false()">Pattern 2, Rule 1</assert>
        </rule>
        <rule context="element">
            <assert test="false()">Pattern 2, Rule 2</assert>
        </rule>
        <rule context="/*/*">
            <assert test="false()">Pattern 2, Rule 3</assert>
        </rule>
    </pattern>
    <pattern id="pattern3" es:matchType="priority">
        <rule context="element" es:priority="-10">
            <assert test="false()">Pattern 3, Rule 1</assert>
        </rule>
        <rule context="element" es:priority="0">
            <assert test="false()">Pattern 3, Rule 2</assert>
        </rule>
        <rule context="/*/*" es:priority="20">
            <assert test="false()">Pattern 3, Rule 3</assert>
        </rule>
    </pattern>
    <pattern id="pattern4" es:matchType="priority">
        <rule context="element" es:priority="10">
            <assert test="false()">Pattern 4, Rule 1</assert>
        </rule>
        <rule context="element" es:priority="5">
            <assert test="false()">Pattern 4, Rule 2</assert>
        </rule>
        <rule context="/*/*" es:priority="20">
            <assert test="false()">Pattern 4, Rule 3</assert>
        </rule>
    </pattern>
    
</schema>