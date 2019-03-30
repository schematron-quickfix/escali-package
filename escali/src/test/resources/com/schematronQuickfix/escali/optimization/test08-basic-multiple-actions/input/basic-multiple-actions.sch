<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - basic multi actions</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test=". = ''" sqf:fix="copyBaz">Foo should not be empty.</sch:report>
            <sqf:fix id="copyBaz">
                <sqf:description>
                    <sqf:title>Copy baz.</sqf:title>
                </sqf:description>
                <sqf:add select="../bar/baz"/>
                <sqf:add match="../bar" node-type="element" target="marker"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>