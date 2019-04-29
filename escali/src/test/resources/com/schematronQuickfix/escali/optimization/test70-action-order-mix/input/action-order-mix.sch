<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:est="escali.schematron-quickfix.com/test">
    <sch:title>Schematron unit test - action order</sch:title>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="bar" sqf:fix="addBazBeforeAndReplaceBar addBazAsChildAndReplaceBar">Foo should not be empty.</sch:report>
            <sqf:fix id="addBazBeforeAndReplaceBar">
                <sqf:description>
                    <sqf:title>Add baz before bar and replace bar by bazzer</sqf:title>
                </sqf:description>
                <sqf:replace match="bar" node-type="element" target="bazzer"/>
                <sqf:add match="bar" node-type="element" target="baz" position="before"/>
            </sqf:fix>
            <sqf:fix id="addBazAsChildAndReplaceBar">
                <sqf:description>
                    <sqf:title>Add baz before bar and replace bar by bazzer</sqf:title>
                </sqf:description>
                <sqf:replace match="bar" node-type="element" target="bazzer"/>
                <sqf:add match="bar" node-type="element" target="baz" position="first-child"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>