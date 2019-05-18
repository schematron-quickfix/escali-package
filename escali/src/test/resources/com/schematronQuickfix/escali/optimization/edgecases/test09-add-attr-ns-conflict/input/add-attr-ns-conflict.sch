<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <sch:title>Schematron unit test - edge case - add attribute namespace conflict</sch:title>
    <sch:ns uri="foo.de" prefix="bar"/>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="@*" sqf:fix="addAttr">Foo should not have an attribute.</sch:assert>
            <sqf:fix id="addAttr">
                <sqf:description>
                    <sqf:title>Add foo attribute.</sqf:title>
                </sqf:description>
                <sqf:add match="." target="bar:foo" node-type="attribute" select="''"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>