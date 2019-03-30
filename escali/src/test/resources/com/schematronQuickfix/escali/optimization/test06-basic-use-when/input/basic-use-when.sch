<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - basic use-when</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test=". = ''" sqf:fix="addBar">Foo should not be empty.</sch:report>
            <sqf:fix id="addBar" use-when="not(bar)">
                <sqf:description>
                    <sqf:title>Add bar element with content.</sqf:title>
                </sqf:description>
                <sqf:add node-type="element" target="bar" select="'staticContent'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>