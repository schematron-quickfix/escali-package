<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - node creation position before</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="normalize-space(.) != ''" sqf:fix="addBaz">Foo should not be empty.</sch:assert>
            <sqf:fix id="addBaz">
                <sqf:description>
                    <sqf:title>Add baz before</sqf:title>
                </sqf:description>
                <sqf:add target="baz" node-type="element" select="'baz-content'" position="before"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>