<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" defaultPhase="phase1">
    <sch:title>Schematron unit test - foreign pattern let</sch:title>
    <sch:phase id="phase1">
        <sch:active pattern="pattern1"/>
    </sch:phase>
    <sch:phase id="phase2">
        <sch:active pattern="pattern2"/>
    </sch:phase>
    
    <sch:phase id="phase3">
        <sch:active pattern="pattern1"/>
        <sch:active pattern="pattern2"/>
    </sch:phase>
    
    <sch:pattern id="pattern1">
        <sch:let name="prefix" value="'foo'"/>
        <sch:rule context="foo">
            <sch:assert test="starts-with(@id, $prefix)" sqf:fix="addPrefix">Foo id should contain <sch:value-of select="$prefix"/>.</sch:assert>
            <sqf:fix id="addPrefix">
                <sqf:description>
                    <sqf:title>Add prefix <sch:value-of select="$prefix"/>.</sqf:title>
                </sqf:description>
                <sqf:replace match="@id" node-type="attribute" target="id" select="concat($prefix, .)"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern id="pattern2">
        <sch:let name="prefix" value="'bar'"/>
        <sch:rule context="bar">
            <sch:assert test="starts-with(@id, $prefix)" sqf:fix="addPrefix">Bar id should contain <sch:value-of select="$prefix"/>.</sch:assert>
            <sqf:fix id="addPrefix">
                <sqf:description>
                    <sqf:title>Add prefix <sch:value-of select="$prefix"/>.</sqf:title>
                </sqf:description>
                <sqf:replace match="@id" node-type="attribute" target="id" select="concat($prefix, .)"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
</sch:schema>