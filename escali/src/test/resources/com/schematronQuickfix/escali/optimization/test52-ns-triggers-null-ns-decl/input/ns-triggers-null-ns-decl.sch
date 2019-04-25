<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:foo="foo.com">
    <sch:title>Schematron unit test - sch:ns with null namespace</sch:title>
    <sch:ns uri="foo.com" prefix="foo"/>
    <sch:pattern id="pattern1">
        <sch:rule context="foo:foo">
            <sch:report test="foo:bar" sqf:fix="replaceBar">Foo should not contain bar.</sch:report>
            <sqf:fix id="replaceBar">
                <sqf:description>
                    <sqf:title>Replace bar by baz.</sqf:title>
                </sqf:description>
                <sqf:replace match="foo:bar">
                    <baz>baz-content</baz>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>