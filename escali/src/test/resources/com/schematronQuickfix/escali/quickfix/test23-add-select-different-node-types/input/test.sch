<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:pattern id="pattern1">
        <sch:title>select attribute</sch:title>
        <sch:rule context="element">
            <sch:let name="el" value="."/>
            <sch:let name="att" value="@attribute"/>
            <sch:report test="@attribute='value'" sqf:fix="addToNext addAsChild addBoth addBothMixed addAttAsLastChild">Bad value</sch:report>
            <sqf:fix id="addToNext">
                <sqf:description>
                    <sqf:title>Add the same attribute to the next elements.</sqf:title>
                </sqf:description>
                <sqf:add match="following-sibling::element" select="$att"/>
            </sqf:fix>
            <sqf:fix id="addAsChild">
                <sqf:description>
                    <sqf:title>Add the element to the next elements.</sqf:title>
                </sqf:description>
                <sch:let name="el" value="."/>
                <sqf:add match="following-sibling::element" select="$el"/>
            </sqf:fix>
            <sqf:fix id="addBoth">
                <sqf:description>
                    <sqf:title>Add the element and attribute to the next elements.</sqf:title>
                </sqf:description>
                <sqf:add match="following-sibling::element[1]" select="$el"/>
                <sqf:add match="following-sibling::element[1]" select="$att"/>
            </sqf:fix>
            <sqf:fix id="addBothMixed">
                <sqf:description>
                    <sqf:title>Add the element and attribute to the next elements.</sqf:title>
                </sqf:description>
                <sqf:add match="following-sibling::element[1]" select="$el | $att"/>
            </sqf:fix>
            <sqf:fix id="addAttAsLastChild">
                <sqf:description>
                    <sqf:title>Corrupt, because you can't add an attribute as last child.</sqf:title>
                </sqf:description>
                <sqf:add match="following-sibling::element[1]" position="last-child" select="$att"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>    
</sch:schema>
