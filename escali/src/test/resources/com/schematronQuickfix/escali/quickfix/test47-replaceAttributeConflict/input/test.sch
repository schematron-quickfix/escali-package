<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <ns uri="http://www.escali.schematron-quickfix.com/example" prefix="ex"/>
    <pattern>
        <rule context="foo">
            <assert test="false()" sqf:fix="replaceAttribut">test.</assert>
            <sqf:fix id="replaceAttribut">
                <sqf:description>
                    <sqf:title>Replace Attribute!</sqf:title>
                </sqf:description>
                <sqf:add match="@a" node-type="attribute" target="c" select="'value1'"/>
                <sqf:add match="@b" node-type="attribute" target="c" select="'value2'"/>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
