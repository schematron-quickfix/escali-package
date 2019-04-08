<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:title>Schematron unit test - foreign xsl:for-each</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="@id = ../foo/bar/@id" sqf:fix="replaceBar">Foo should point to a bar.</sch:assert>
            <sqf:fix id="replaceBar">
                <sqf:description>
                    <sqf:title>Replace bar with for each foo one bar.</sqf:title>
                </sqf:description>
                <sqf:replace match="bar">
                    <xsl:for-each select="../../foo">
                        <bar id="{@id}"/>
                    </xsl:for-each>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>