<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - basic xinclude support</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="bar" sqf:fix="replaceBar replaceAllBar deleteIdAttr">Foo should not contain bar.</sch:report>
            <sqf:fix id="replaceBar">
                <sqf:description>
                    <sqf:title>Replace bar by baz</sqf:title>
                </sqf:description>
                <sqf:replace match="bar" node-type="element" target="baz"/>
            </sqf:fix>
            <sqf:fix id="replaceAllBar">
                <sqf:description>
                    <sqf:title>Replace all bar by baz</sqf:title>
                </sqf:description>
                <sqf:replace match="//bar" node-type="element" target="baz"/>
            </sqf:fix>
            <sqf:fix id="deleteIdAttr">
                <sqf:description>
                    <sqf:title>Delete all ids</sqf:title>
                </sqf:description>
                <sqf:delete match="/root/foo/bar/@id"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>