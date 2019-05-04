<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:foo="foo.com">
    <sch:title>Schematron unit test - edge case - add after with namespace context change</sch:title>
    <sch:ns uri="foo.com" prefix="foo"/>
    <sch:pattern id="pattern1">
        <sch:rule context="foo:foo">
            <sch:report test=". = ''" sqf:fix="addBarAfter">Foo should not be empty.</sch:report>
            <sqf:fix id="addBarAfter">
                <sqf:description>
                    <sqf:title>Add bar after foo</sqf:title>
                </sqf:description>
                <sqf:add position="after" node-type="element" target="foo:bar" select="'baz'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>