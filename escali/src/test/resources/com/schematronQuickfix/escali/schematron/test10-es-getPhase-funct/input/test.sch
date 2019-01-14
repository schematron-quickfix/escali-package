<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <phase id="phase1">
        <active pattern="pattern1"/>
    </phase>
    <phase id="phase2">
        <active pattern="pattern1"/>
    </phase>
    <pattern id="pattern1">
        <rule context="pattern1">
            <assert test="false()"><value-of select="es:getPhase()"/></assert>
        </rule>
    </pattern>
</schema>