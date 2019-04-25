<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:foo="foo.com" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:title>Schematron unit test - bug fix - add multiple attributes with one action</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="@*" sqf:fix="addAttr">Foo should have at least one attribute.</sch:assert>
            <sqf:fix id="addAttr">
                <sqf:description>
                    <sqf:title>Add two attributes to be sure.</sqf:title>
                </sqf:description>
                <!--<xsl:variable name="attr" as="attribute()*">
                    <xsl:attribute name="foo" select="'foo-value'"/>
                    <xsl:attribute name="bar" select="'bar-value'"/>
                </xsl:variable>-->
                <sqf:add match=".">
                    <xsl:attribute name="foo" select="'foo-value'"/>
                    <xsl:attribute name="bar" select="'bar-value'"/>
                </sqf:add>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
