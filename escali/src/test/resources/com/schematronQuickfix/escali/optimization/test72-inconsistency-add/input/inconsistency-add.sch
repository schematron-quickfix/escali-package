<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:est="escali.schematron-quickfix.com/test">
    <sch:title>Schematron unit test - inconsistency add</sch:title>
    
    <sch:pattern id="pattern1">
        <sch:rule context="node() | @*">
            <sch:assert test=". instance of element() or . instance of text()" sqf:fix="addAfter addBefore addAttr addFChild addLChild addAttrBar">Node should be text or element.</sch:assert>
            <sch:report test="self::text()[normalize-space(.) != '']" sqf:fix="addAfter addBefore addAttr addFChild addLChild addAttrBar">Text should be empty.</sch:report>
        </sch:rule>
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="addAfter">
            <sqf:description>
                <sqf:title>Add bar after</sqf:title>
            </sqf:description>
            <sqf:add position="after" node-type="element" target="bar"/>
        </sqf:fix>
        <sqf:fix id="addBefore">
            <sqf:description>
                <sqf:title>Add bar before</sqf:title>
            </sqf:description>
            <sqf:add position="before" node-type="element" target="bar"/>
        </sqf:fix>
        <sqf:fix id="addFChild">
            <sqf:description>
                <sqf:title>Add bar as first child</sqf:title>
            </sqf:description>
            <sqf:add position="first-child" node-type="element" target="bar"/>
        </sqf:fix>
        <sqf:fix id="addLChild">
            <sqf:description>
                <sqf:title>Add bar as last child</sqf:title>
            </sqf:description>
            <sqf:add position="last-child" node-type="element" target="bar"/>
        </sqf:fix>
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