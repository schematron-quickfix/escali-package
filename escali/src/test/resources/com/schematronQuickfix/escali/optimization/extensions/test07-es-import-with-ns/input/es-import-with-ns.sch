<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <ns uri="a" prefix="a"/>
    <pattern id="a">
        <rule context="a:pattern1">
            <assert test="false()" sqf:fix="delete">Prefix <value-of select="."/></assert>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete <name/></sqf:title>
                </sqf:description>
                <sqf:delete match="self::a:pattern1"/>
            </sqf:fix>
        </rule>
    </pattern>
    
    <es:import href="importTest.sch"/>
</schema>