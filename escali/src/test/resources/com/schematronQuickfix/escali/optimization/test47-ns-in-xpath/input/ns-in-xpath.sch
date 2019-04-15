<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - sch:ns in XPath</sch:title>
    <sch:ns uri="foo.com" prefix="foo"/>
    <sch:pattern id="pattern1">
        <sch:rule context="foo:foo">
            <sch:report test="foo:bar" sqf:fix="deleteBar">Foo should not contain bar.</sch:report>
            <sqf:fix id="deleteBar">
                <sqf:description>
                    <sqf:title>Delete bar</sqf:title>
                </sqf:description>
                <sqf:delete match="foo:bar"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>