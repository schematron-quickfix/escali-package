<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <title>Import test 1 file</title>
    <phase id="importPhase">
        <active pattern="importPattern"></active>
    </phase>
    <pattern id="importPattern">
        <rule context="importPattern">
            <assert test="false()" sqf:fix="importPatternFix">Import test</assert>
        </rule>
    </pattern>
    
    <pattern id="notImportPattern">
        <rule context="notImportPattern">
            <assert test="false()">Not import test</assert>
        </rule>
    </pattern>
    
    <sqf:fixes>
        <sqf:fix id="importPatternFix">
            <sqf:description>
                <sqf:title>Import fix for importPattern</sqf:title>
            </sqf:description>
            <sqf:delete match="self::importPattern"/>
        </sqf:fix>
        <sqf:fix id="pattern1Fix">
            <sqf:description>
                <sqf:title>Import fix for pattern1</sqf:title>
            </sqf:description>
            <sqf:delete match="self::pattern1"/>
        </sqf:fix>
    </sqf:fixes>
</schema>