<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - node creation node-type attribute</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="normalize-space(.) != ''" sqf:fix="deleteBar">Foo should not be empty.</sch:assert>
            <sqf:fix id="deleteBar">
                <sqf:description>
                    <sqf:title>Remove bar attribute</sqf:title>
                </sqf:description>
                <sqf:delete match="@bar"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>