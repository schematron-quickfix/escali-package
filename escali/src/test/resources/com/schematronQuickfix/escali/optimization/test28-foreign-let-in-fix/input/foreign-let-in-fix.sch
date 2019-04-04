<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - basic replace</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="@id = ./bar/@id" sqf:fix="addBarId">Bar should have same id <sch:value-of select="@id"/>.</sch:assert>
            <sqf:fix id="addBarId">
                <sch:let name="id" value="@id"/>
                <sqf:description>
                    <sqf:title>Add ID "<sch:value-of select="$id"/>" to bar.</sqf:title>
                </sqf:description>
                <sqf:add match="bar" node-type="attribute" target="id" select="$id"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>