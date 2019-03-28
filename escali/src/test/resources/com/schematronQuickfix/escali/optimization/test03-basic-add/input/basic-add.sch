<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - trivial add</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="addBar">Foo should not be empty.</sch:assert>
            <sqf:fix id="addBar">
                <sqf:description>
                    <sqf:title>Add bar to <sch:name/>.</sqf:title>
                </sqf:description>
                <sqf:add node-type="element" target="bar" select="'baz'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>