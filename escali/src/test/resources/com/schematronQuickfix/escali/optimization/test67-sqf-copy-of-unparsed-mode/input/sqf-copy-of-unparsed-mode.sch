<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - sqf:copy-of with unparsed mode true</sch:title>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="bar" sqf:fix="addBar addBarWithLet addBarWithSeq">Foo should contain bar.</sch:assert>
            <sqf:fix id="addBar">
                <sqf:description>
                    <sqf:title>Copy bar</sqf:title>
                </sqf:description>
                <sqf:add>
                    <sqf:copy-of select="//foo[@id = 'foo2']/bar" unparsed-mode="true"/>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="addBarWithLet">
                <sqf:description>
                    <sqf:title>Copy bar with let</sqf:title>
                </sqf:description>
                <sqf:add>
                    <sch:let name="id" value="'foo3'"/>
                    <sqf:copy-of select="//foo[@id = $id]/bar" unparsed-mode="true"/>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="addBarWithSeq">
                <sqf:description>
                    <sqf:title>Copy bar with sequence</sqf:title>
                </sqf:description>
                <sqf:add>
                    <sqf:copy-of select="//foo/bar" unparsed-mode="true"/>
                </sqf:add>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>