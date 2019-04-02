<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - userentry default</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="count(bar) gt 1" sqf:fix="removeBars">More than one bar in foo.</sch:report>
            <sqf:fix id="removeBars">
                <sqf:description>
                    <sqf:title>Remove all bars except of one.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="barId" type="xs:string" default="bar/@id">
                    <sqf:description>
                        <sqf:title>Enter the id of the keeped bar</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:delete match="bar[@id != $barId]"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>