<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - use-for-each global</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test=". = ''" sqf:fix="useFoo">Foo should not be empty.</sch:report>
            
        </sch:rule>
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="useFoo" use-for-each="following-sibling::foo">
            <sqf:description>
                <sqf:title>Add bar with <sch:value-of select="$sqf:current"/>.</sqf:title>
            </sqf:description>
            <sqf:add node-type="element" target="bar" select="string($sqf:current)"/>
        </sqf:fix>
    </sqf:fixes>
</sch:schema>