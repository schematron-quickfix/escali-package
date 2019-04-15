<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - basic use-for-each</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test=". = ''" sqf:fix="useFoo">Foo should not be empty.</sch:report>
            <sqf:fix id="useFoo" use-for-each="//foo[.!= '']">
                <sch:let name="foo" value="$sqf:current"/>
                <sqf:description>
                    <sqf:title>Add ref to "<sch:value-of select="$foo"/>"</sqf:title>
                </sqf:description>
                <sqf:add node-type="element" target="bar" select="string($foo)"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>