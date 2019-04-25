<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <sch:title>Schematron unit test - deprecated sqf:keep element</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="bar" sqf:fix="unwrapBar">Foo should not contain bar.</sch:report>
            <sqf:fix id="unwrapBar">
                <sqf:description>
                    <sqf:title>Replace by bar</sqf:title>
                </sqf:description>
                <sqf:replace match=".//bar">
                    <sqf:keep select="node()"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>