<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" queryBinding="xslt2">
    <pattern>
        <rule context="/root/element">
            <assert test="false()" sqf:fix="addElement addAllElement deleteIdAttr replaceElement deleteAllIdAttr replaceAllElement">Include test</assert>
            <sqf:fix id="addElement">
                <sqf:description>
                    <sqf:title>Add element as first childe</sqf:title>
                </sqf:description>
                <sqf:add target="add" node-type="element" select="@id/string(.)"/>
            </sqf:fix>
            <sqf:fix id="addAllElement">
                <sqf:description>
                    <sqf:title>Add element as first childe</sqf:title>
                </sqf:description>
                <sqf:add match="/root/element" target="add" node-type="element" select="@id/string(.)"/>
            </sqf:fix>
            <sqf:fix id="deleteIdAttr">
                <sqf:description>
                    <sqf:title>Delete id attribute</sqf:title>
                </sqf:description>
                <sqf:delete match="@id"/>
            </sqf:fix>
            <sqf:fix id="deleteAllIdAttr">
                <sqf:description>
                    <sqf:title>Delete all id attribute</sqf:title>
                </sqf:description>
                <sqf:delete match="/root/element/@id"/>
            </sqf:fix>
            <sqf:fix id="replaceElement">
                <sqf:description>
                    <sqf:title>Replace element</sqf:title>
                </sqf:description>
                <sqf:replace match="." target="root" node-type="element" xmlns=""><content/></sqf:replace>
            </sqf:fix>
            <sqf:fix id="replaceAllElement">
                <sqf:description>
                    <sqf:title>Replace all element</sqf:title>
                </sqf:description>
                <sqf:replace match="/root/element" target="root" node-type="element" xmlns=""><content/></sqf:replace>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
