<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" queryBinding="xslt2">
    <pattern>
        <rule context="/root/element">
            <assert test="false()" sqf:fix="addElement addAllElement">Include test</assert>
            <sqf:fix id="addElement">
                <sqf:description>
                    <sqf:title>Add element as first childe</sqf:title>
                </sqf:description>
                <sqf:add target="add" node-type="element" select="@id/string(.)"></sqf:add>
            </sqf:fix>
            <sqf:fix id="addAllElement">
                <sqf:description>
                    <sqf:title>Add element as first childe</sqf:title>
                </sqf:description>
                <sqf:add match="/root/element" target="add" node-type="element" select="@id/string(.)"></sqf:add>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
