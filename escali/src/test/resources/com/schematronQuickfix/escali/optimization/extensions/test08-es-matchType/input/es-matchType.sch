<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    
    <pattern id="pattern1">
        <rule context="element">
            <assert test="false()" id="p1.r1">Pattern 1, Rule 1</assert>
        </rule>
        <rule context="element">
            <assert test="false()" id="p1.r2">Pattern 1, Rule 2</assert>
        </rule>
        <rule context="/*/*">
            <assert test="false()" id="p1.r3">Pattern 1, Rule 3</assert>
        </rule>
    </pattern>
    <pattern id="pattern2" es:matchType="all">
        <rule context="element">
            <assert test="false()" id="p2.r1">Pattern 2, Rule 1</assert>
        </rule>
        <rule context="element">
            <assert test="false()" id="p2.r2">Pattern 2, Rule 2</assert>
        </rule>
        <rule context="/*/*">
            <assert test="false()" id="p2.r3">Pattern 2, Rule 3</assert>
        </rule>
    </pattern>
    <pattern id="pattern3" es:matchType="priority">
        <rule context="element" es:priority="-10">
            <assert test="false()" id="p3.r1">Pattern 3, Rule 1</assert>
        </rule>
        <rule context="element" es:priority="0">
            <assert test="false()" id="p3.r2">Pattern 3, Rule 2</assert>
        </rule>
        <rule context="/*/*" es:priority="20">
            <assert test="false()" id="p3.r3">Pattern 3, Rule 3</assert>
        </rule>
    </pattern>
    <pattern id="pattern4" es:matchType="priority">
        <rule context="element" es:priority="10">
            <assert test="false()" id="p4.r1">Pattern 4, Rule 1</assert>
        </rule>
        <rule context="element" es:priority="5">
            <assert test="false()" id="p4.r2">Pattern 4, Rule 2</assert>
        </rule>
        <rule context="/*/*" es:priority="20">
            <assert test="false()" id="p4.r3">Pattern 4, Rule 3</assert>
        </rule>
    </pattern>
    <pattern id="pattern5" es:matchType="first">
        <rule context="element">
            <assert test="false()" id="p5.r1">Pattern 5, Rule 1</assert>
        </rule>
        <rule context="element">
            <assert test="false()" id="p5.r2">Pattern 5, Rule 2</assert>
        </rule>
        <rule context="/*/*">
            <assert test="false()" id="p5.r3">Pattern 5, Rule 3</assert>
        </rule>
    </pattern>
    
</schema>