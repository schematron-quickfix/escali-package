<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <title>Import test 1 file</title>
    <phase id="importPhase">
        <active pattern="importImportPattern"></active>
    </phase>
    <pattern id="importImportPattern">
        <rule context="importImportPattern">
            <assert test="false()">Import import test</assert>
        </rule>
    </pattern>
</schema>