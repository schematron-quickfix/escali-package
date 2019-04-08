<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" defaultPhase="phase1">
    <sch:title>Schematron unit test - foreign phase let</sch:title>
    
    <sch:let name="name" value="'baz'"/>
    
    <sch:phase id="phase1">
        <sch:let name="name" value="'bar'"/>
        <sch:active pattern="pattern1"/>
    </sch:phase>
    <sch:phase id="phase2">
        <sch:let name="name" value="'foo'"/>
        <sch:active pattern="pattern2"/>
    </sch:phase>
    
    <sch:phase id="phase3">
        <sch:active pattern="pattern1"/>
        <sch:active pattern="pattern2"/>
    </sch:phase>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="name() = $name" sqf:fix="replace">Foo should have the name <sch:value-of select="$name"/>.</sch:assert>
            <sqf:fix id="replace">
                <sqf:description>
                    <sqf:title>Replace by <sch:value-of select="$name"/>.</sqf:title>
                </sqf:description>
                <sqf:replace node-type="element" target="{$name}">
                    <sqf:copy-of select="node()"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern id="pattern2">
        <sch:rule context="bar">
            <sch:assert test="name() = $name" sqf:fix="replace">Bar should have the name <sch:value-of select="$name"/>.</sch:assert>
            <sqf:fix id="replace">
                <sqf:description>
                    <sqf:title>Replace by <sch:value-of select="$name"/>.</sqf:title>
                </sqf:description>
                <sqf:replace node-type="element" target="{$name}">
                    <sqf:copy-of select="node()"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
</sch:schema>