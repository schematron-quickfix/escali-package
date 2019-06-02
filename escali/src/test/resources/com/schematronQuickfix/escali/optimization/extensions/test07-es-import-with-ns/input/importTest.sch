<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <ns uri="b" prefix="b"/>
    <title>Import test 13 file</title>
    <pattern id="b">
        <rule context="b:pattern1">
            <assert test="false()" sqf:fix="delete">Prefix <value-of select="."/></assert>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete <name/></sqf:title>
                </sqf:description>
                <sqf:delete match="self::b:pattern1"/>
            </sqf:fix>
        </rule>
    </pattern>
</schema>