<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <sch:ns uri="http://www.escali.schematron-quickfix.com/example" prefix="ex"/>
    <sch:pattern>
        <sch:rule context="foo">
            <sch:assert test="false()" sqf:fix="addBefore addAfter addChild addLastChild">test.</sch:assert>
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
