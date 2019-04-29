<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <sch:title>Schematron unit test - mix add of an element and attribute</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo/@bar">
            <sch:report test=". = 'bar'" sqf:fix="replaceBar">Bar attribute should not have baz value.</sch:report>
            <sqf:fix id="replaceBar">
                <sqf:description>
                    <sqf:title>Replace bar value by baz</sqf:title>
                </sqf:description>
                <sqf:replace node-type="attribute" target="bar" select="'baz'"></sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>