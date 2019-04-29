<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:est="escali.schematron-quickfix.com/test">
    <sch:title>Schematron unit test - action order</sch:title>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="bar" sqf:fix="addBefore addAfter addChild addLastChild addMix">Foo should not be empty.</sch:report>
            <sqf:fix id="addMix">
                <sqf:description>
                    <sqf:title>Add Mix!</sqf:title>
                </sqf:description>
                <sqf:add match="." node-type="element" target="barBefore" position="before"/>
                <sqf:add match="." node-type="element" target="barAfter" position="after"/>
                <sqf:add match="." node-type="element" target="barChild" position="first-child"/>
                <sqf:add match="." node-type="element" target="barLastChild" position="last-child"/>
                <sqf:add match="." node-type="attribute" target="barAttribut"/>
            </sqf:fix>
            <sqf:fix id="addBefore">
                <sqf:description>
                    <sqf:title>Add before!</sqf:title>
                </sqf:description>
                <sqf:add match="." node-type="element" target="bar1" position="before"/>
                <sqf:add match="." node-type="element" target="bar2" position="before"/>
            </sqf:fix>
            <sqf:fix id="addAfter">
                <sqf:description>
                    <sqf:title>Add after!</sqf:title>
                </sqf:description>
                <sqf:add match="." node-type="element" target="bar1" position="after"/>
                <sqf:add match="." node-type="element" target="bar2" position="after"/>
            </sqf:fix>
            <sqf:fix id="addChild">
                <sqf:description>
                    <sqf:title>Add child!</sqf:title>
                </sqf:description>
                <sqf:add match="." node-type="element" target="bar1" position="first-child"/>
                <sqf:add match="." node-type="element" target="bar2" position="first-child"/>
            </sqf:fix>
            <sqf:fix id="addLastChild">
                <sqf:description>
                    <sqf:title>Add last child!</sqf:title>
                </sqf:description>
                <sqf:add match="." node-type="element" target="bar1" position="last-child"/>
                <sqf:add match="." node-type="element" target="bar2" position="last-child"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>