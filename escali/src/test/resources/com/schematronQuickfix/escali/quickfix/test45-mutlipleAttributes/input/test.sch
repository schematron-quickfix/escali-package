<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <ns uri="http://www.escali.schematron-quickfix.com/example" prefix="ex"/>
    <pattern>
        <rule context="para">
            <assert test="@*" sqf:fix="addMultipleAttributes">Para needs attributes.</assert>
            <sqf:fix id="addMultipleAttributes">
                <sqf:description>
                    <sqf:title>Add attributes!</sqf:title>
                </sqf:description>
                <sqf:add match="." select="ex:requiredAttributes(.)"/>
            </sqf:fix>
        </rule>
    </pattern>
    <xsl:function name="ex:requiredAttributes" as="attribute()*">
        <xsl:param name="element" as="element()"/>
        <xsl:attribute name="attribute1" select="'value1'"/>
        <xsl:attribute name="attribute2" select="'value2'"/>
    </xsl:function>
</schema>
