<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - node creation node-type attribute</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="normalize-space(.) != ''" sqf:fix="addBar addBarWSequence">Foo should not be empty.</sch:assert>
            <sqf:fix id="addBar">
                <sqf:description>
                    <sqf:title>Add bar attribute</sqf:title>
                </sqf:description>
                <sqf:add target="bar" node-type="attribute" select="'bar-value'"/>
            </sqf:fix>
            <sqf:fix id="addBarWSequence">
                <sqf:description>
                    <sqf:title>Add bar attribute</sqf:title>
                </sqf:description>
                <sqf:add target="bar" node-type="attribute" select="'bar-value1', 'bar-value2'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>