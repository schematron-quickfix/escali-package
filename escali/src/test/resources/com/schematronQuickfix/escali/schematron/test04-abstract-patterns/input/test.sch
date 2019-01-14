<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <phase id="phase1">
        <active pattern="pattern3"/>
    </phase>
    <phase id="phase2">
        <active pattern="pattern3"/>
        <active pattern="pattern1"/>
    </phase>
    <pattern id="pattern3" is-a="pattern4">
        <!--<rule context="pattern3">
            <assert test="false()">Test 3</assert>
        </rule>-->
    </pattern>
    <pattern abstract="true" id="pattern4">
        <rule context="pattern4">
            <assert test="false()">Test 4</assert>
        </rule>
    </pattern>


    <pattern id="pattern1" is-a="pattern2">
        <param name="test" value="false()"/>
    </pattern>
    <pattern abstract="true" id="pattern2">
        <rule context="pattern2">
            <assert test="$test">Test 2</assert>
        </rule>
    </pattern>
    <pattern id="pattern5" is-a="pattern6">
        <param name="test1" value="fal"/>
        <param name="test.2" value="se()"/>
    </pattern>
    <pattern abstract="true" id="pattern6">
        <let name="testa2" value="6"/>
        <rule context="pattern6">
            <assert test="$test1$test.2">Test <value-of select="$testa2"/></assert>
        </rule>
    </pattern>
</schema>
