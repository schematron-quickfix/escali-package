<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - trivial stringReplace</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test=". = ''" sqf:fix="addBar">Foo should not be empty.</sch:report>
            <sqf:fix id="addBar">
                <sqf:description>
                    <sqf:title>Add bar element with content.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="element" type="xs:string">
                    <sqf:description>
                        <sqf:title>Enter content for new bar element</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add node-type="element" target="bar" select="$element"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>