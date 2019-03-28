<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - trivial stringReplace</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="contains(., 'bar')" sqf:fix="replaceBar">Foo should not be empty.</sch:report>
            <sqf:fix id="replaceBar">
                <sqf:description>
                    <sqf:title>Replace substring bar by foo.</sqf:title>
                </sqf:description>
                <sqf:stringReplace match="text()" regex="bar" select="'foo'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>