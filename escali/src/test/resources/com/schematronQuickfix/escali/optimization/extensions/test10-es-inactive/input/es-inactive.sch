<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <phase id="phase1">
        <active pattern="pattern1"/>
    </phase>
    <phase id="phase2">
        <active pattern="pattern3"/>
    </phase>
    <phase id="phase3">
        <es:inactive pattern="pattern2"/>
    </phase>
    <!--    phase1 phase3 #ALL-->
    <pattern id="pattern1">
        <rule context="pattern1">
            <assert test="false()">Test 1</assert>
        </rule>
    </pattern>
    <!--    phase1 phase2 #ALL-->
    <pattern id="pattern2" es:active="true">
        <rule context="pattern2">
            <assert test="false()">Test 2</assert>
        </rule>
    </pattern>
    <!--    phase2-->
    <pattern id="pattern3" es:active="false">
        <rule context="pattern3">
            <assert test="false()">Test 3</assert>
        </rule>
    </pattern>
</schema>