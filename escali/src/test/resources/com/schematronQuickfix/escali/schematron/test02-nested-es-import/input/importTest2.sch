<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Import test 1 file</title>
    <phase id="importPhase">
        <active pattern="importPattern1"/>
        <active pattern="importPattern2"/>
    </phase>
    <p>This are imported patterns</p>
    <pattern id="importPattern1">
        <rule context="importPattern1">
            <assert test="false()">Import test 1</assert>
        </rule>
    </pattern>
    <pattern id="importPattern2">
        <rule context="importPattern2">
            <assert test="false()">Import test 2</assert>
        </rule>
    </pattern>
    <pattern id="importPattern3">
        <rule context="importPattern3">
            <assert test="false()">Import test 3</assert>
        </rule>
    </pattern>
    <es:import href="import2Test2.sch" phase="importPhase"/>
</schema>