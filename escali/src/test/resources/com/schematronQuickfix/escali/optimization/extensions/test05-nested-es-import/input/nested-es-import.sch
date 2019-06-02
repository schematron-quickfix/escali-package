<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <phase id="phase1">
        <active pattern="pattern1"/>
        <active pattern="pattern2"/>
    </phase>
    <pattern id="pattern1">
        <rule context="pattern1">
            <assert test="false()">Test 1</assert>
        </rule>
    </pattern>
    <pattern id="pattern2">
        <rule context="pattern2">
            <assert test="false()">Test 2</assert>
        </rule>
    </pattern>
    <pattern id="pattern3">
        <rule context="pattern3">
            <assert test="false()">Test 3</assert>
        </rule>
    </pattern>
    
    <es:import href="import.sch" phase="importPhase"/>
</schema>