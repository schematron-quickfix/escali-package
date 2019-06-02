<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:bar="foo.com" xmlns:foo="foo.com" xmlns:baz="baz.com" queryBinding="xslt2">
    <sch:title>Schematron unit test - namespace context with XML namespace decrations</sch:title>
    <sch:ns uri="bar.com" prefix="bar"/>
    <sch:pattern id="bar">
        <sch:rule context="foo:foo">
            <sch:assert test="false()" sqf:fix="delete">Prefix <sch:value-of select="."/></sch:assert>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete foo:foo</sqf:title>
                </sqf:description>
                <sqf:delete match="self::foo:foo"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:pattern id="foo">
        <sch:rule context="foo:bar" xmlns:foo="bar.com">
            <sch:assert test="false()" sqf:fix="delete">Prefix <sch:value-of select="."/></sch:assert>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete foo:bar</sqf:title>
                </sqf:description>
                <sqf:delete match="self::foo:bar"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:pattern id="baz" xmlns:baz="foo.com">
        <sch:rule context="baz:foo">
            <sch:assert test="false()" sqf:fix="delete">Prefix <sch:value-of select="."/></sch:assert>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete baz:foo</sqf:title>
                </sqf:description>
                <sqf:delete match="self::baz:foo"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>

</sch:schema>
