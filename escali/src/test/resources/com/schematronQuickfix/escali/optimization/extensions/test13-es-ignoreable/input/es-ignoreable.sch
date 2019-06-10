<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <pattern id="pattern1">
        <rule context="t1">
            <assert test="false()" es:ignorableId="id_t1" role="warn">Test 1</assert>
        </rule>
    </pattern>
    <pattern id="pattern2">
        <rule context="comment()">
            <assert test="false()" es:ignorableId="id_t2">Test 2</assert>
        </rule>
    </pattern>
    <pattern id="pattern3">
        <rule context="t3/@a">
            <assert test="false()" es:ignorableId="id_t3a">Test 3</assert>
        </rule>
    </pattern>
    <pattern id="pattern4">
        <rule context="processing-instruction(t4)">
            <assert test="false()" es:ignorableId="id_t4">Test 4</assert>
        </rule>
    </pattern>
    <pattern id="pattern5">
        <rule context="t5">
            <assert test="false()" es:ignorableId="id_t5">Test 5</assert>
        </rule>
    </pattern>
    <pattern id="pattern6">
        <rule context="t6">
            <assert test="false()" es:ignorableId="id_t6">Test 6</assert>
        </rule>
    </pattern>
    
</schema>