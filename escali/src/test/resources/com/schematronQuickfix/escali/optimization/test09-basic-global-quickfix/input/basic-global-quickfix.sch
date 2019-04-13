<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - basic global quickfix</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="deleteFoo replaceFoo">Foo should not be empty.</sch:assert>
            <sqf:fix id="replaceFoo">
                <sqf:description>
                    <sqf:title>Replace foo by bar</sqf:title>
                </sqf:description>
                <sqf:replace node-type="element" target="bar"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sqf:fixes>
        <sqf:fix id="deleteFoo">
            <sqf:description>
                <sqf:title>Delete foo</sqf:title>
            </sqf:description>
            <sqf:delete/>
        </sqf:fix>
        <sqf:fix id="replaceFoo">
            <sqf:description>
                <sqf:title>Replace foo by baz</sqf:title>
            </sqf:description>
            <sqf:replace node-type="element" target="baz"/>
        </sqf:fix>
    </sqf:fixes>
</sch:schema>