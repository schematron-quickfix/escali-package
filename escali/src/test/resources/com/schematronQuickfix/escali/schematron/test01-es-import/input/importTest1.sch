<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <title>Import test 1 file</title>
    <phase id="importPhase">
        <active pattern="importPattern"></active>
    </phase>
    <pattern id="importPattern">
        <rule context="importPattern">
            <assert test="false()">Import test</assert>
        </rule>
    </pattern>
</schema>