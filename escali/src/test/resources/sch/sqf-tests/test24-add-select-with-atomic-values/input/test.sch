<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:pattern id="pattern1">
        <sch:title>select attribute</sch:title>
        <sch:rule context="element">
            <sch:let name="int" value="count(following-sibling::element)"/>
            <sch:let name="seq" value="following-sibling::element/count(following-sibling::element)"/>
            <sch:report test="@attribute='value'" sqf:fix="addToNext addAsChild addBoth addBothMixed addUserEntry">Bad value</sch:report>
            <sqf:fix id="addToNext">
                <sqf:description>
                    <sqf:title>Add int as first child.</sqf:title>
                </sqf:description>
                <sqf:add match="following-sibling::element[1]" select="$int"/>
            </sqf:fix>
            <sqf:fix id="addAsChild">
                <sqf:description>
                    <sqf:title>Add sequence as first child.</sqf:title>
                </sqf:description>
                <sqf:add match="following-sibling::element[1]" select="$seq"/>
            </sqf:fix>
            <sqf:fix id="addBoth">
                <sqf:description>
                    <sqf:title>Add int as attribute.</sqf:title>
                </sqf:description>
                <sqf:add match="following-sibling::element[1]" node-type="attribute" target="att" select="$int"/>
            </sqf:fix>
            <sqf:fix id="addBothMixed">
                <sqf:description>
                    <sqf:title>Add sequence as attribute.</sqf:title>
                </sqf:description>
                <sqf:add match="following-sibling::element[1]" node-type="attribute" target="att" select="$seq, $int"/>
            </sqf:fix>
            <sqf:fix id="addUserEntry">
                <sqf:description>
                    <sqf:title>Add user entry as attribute.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="ue" default="'user-entry-value'">
                    <sqf:description>
                        <sqf:title>New value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add match="following-sibling::element[1]" node-type="attribute" target="att" select="$ue"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>    
</sch:schema>
