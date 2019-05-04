<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:est="escali.schematron-quickfix.com/test">
    <sch:title>Schematron unit test - inconsistency add</sch:title>
    
    <sch:pattern id="pattern1">
        <sch:rule context="@*">
            <sch:assert test="false()" sqf:fix="addAttr addAttrBar">Node should be text or element.</sch:assert>
        </sch:rule>
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="addAttr">
            <sqf:description>
                <sqf:title>Add foo attribute</sqf:title>
            </sqf:description>
            <sqf:add node-type="attribute" target="foo"/>
        </sqf:fix>
        <sqf:fix id="addAttrBar">
            <sqf:description>
                <sqf:title>Add bar attribute</sqf:title>
            </sqf:description>
            <sqf:add node-type="attribute" target="bar" select="concat('foo_', .)"/>
        </sqf:fix>
        
    </sqf:fixes>
</sch:schema>