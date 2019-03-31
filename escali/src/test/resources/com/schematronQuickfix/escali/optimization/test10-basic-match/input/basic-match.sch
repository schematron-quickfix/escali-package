<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - basic match</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="@att" sqf:fix="deleteAtt">Foo should not be empty.</sch:report>
            <sqf:fix id="deleteAtt">
                <sqf:description>
                    <sqf:title>Delete att attribute</sqf:title>
                </sqf:description>
                <sqf:delete match="@att"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>