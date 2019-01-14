<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <phase id="phase1">
        <active pattern="pattern1"/>
        <es:phase ref="phase2"/>
    </phase>
    <phase id="phase2">
        <active pattern="pattern2"/>
    </phase>
    <phase id="phase3">
        <active pattern="pattern3"/>
        <es:phase ref="phase5"/>
    </phase>
    <phase id="phase4">
        <active pattern="pattern1"/>
        <active pattern="pattern4"/>
        <es:phase ref="phase6"/>
    </phase>
    <phase id="phase5">
        <active pattern="pattern2"/>
        <es:phase ref="phase3"/>
        <es:phase ref="phase2"/>
        <es:phase ref="phase1"/>
    </phase>
    <phase id="phase6">
        <es:inactive pattern="pattern4"/>
    </phase>
    <!--    phase1 phase3 phase4 phase5 phase6 #ALL-->
    <pattern id="pattern1">
        <rule context="pattern1">
            <assert test="false()">Test 1</assert>
        </rule>
    </pattern>
    <!--    phase1 phase2 phase3 phase5 phase6 #ALL-->
    <pattern id="pattern2">
        <rule context="pattern2">
            <assert test="false()">Test 2</assert>
        </rule>
    </pattern>
    <!--    phase3 phase5 phase6 #ALL-->
    <pattern id="pattern3">
        <rule context="pattern3">
            <assert test="false()">Test 3</assert>
        </rule>
    </pattern>
    <!--    #ALL-->
    <pattern id="pattern4">
        <rule context="pattern4">
            <assert test="false()">Test 4</assert>
        </rule>
    </pattern>
    
</schema>