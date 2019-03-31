<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - node creation node-type pi</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="normalize-space(.) != ''" sqf:fix="addBarPi">Foo should not be empty.</sch:assert>
            <sqf:fix id="addBarPi">
                <sqf:description>
                    <sqf:title>Add bar PI</sqf:title>
                </sqf:description>
                <sqf:add target="bar" node-type="processing-instruction" select="'bar-value'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>