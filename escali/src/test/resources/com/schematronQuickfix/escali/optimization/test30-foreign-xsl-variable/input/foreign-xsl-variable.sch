<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:title>Schematron unit test - basic replace</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <xsl:variable name="rule-id" select="concat('rule_', @id)"/>
            <sch:assert test="@id = ./bar/@id" sqf:fix="addBarId">Bar should have same id <sch:value-of select="@id"/>.</sch:assert>
            <sqf:fix id="addBarId">
                <xsl:variable name="fix-id" select="concat('fix_', @id)"/>
                <sqf:description>
                    <sqf:title>Add ID "<sch:value-of select="$rule-id, $fix-id"/>" to bar.</sqf:title>
                </sqf:description>
                <sqf:add match="bar" node-type="attribute" target="id">
                    <xsl:variable name="actionEl-id" select="concat('ae_', ../@id)"/>
                    <sch:value-of select="$rule-id, $fix-id, $actionEl-id"/>
                </sqf:add>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>