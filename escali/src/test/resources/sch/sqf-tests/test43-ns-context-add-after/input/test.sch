<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:d2t="http://www.data2type.de">
    
    <sch:ns uri="http://www.data2type.de" prefix="d2t"/>
    
    <xsl:include href="lib.xsl"/>
    
    <sch:pattern icon="vb.elementType">
        <sch:rule context="xs:complexType" role="info">
            <sch:let name="name" value="@name"/>
            <sch:let name="declWoType" value=".//xs:element[@name][not(xs:complexType|@type)]"/>
            <sch:report test="$declWoType" sqf:fix="vb.elementType.allComplex">There are elements in the type <sch:value-of select="$name"/> without a type.</sch:report>
            
            <sqf:fix id="vb.elementType.allComplex">
                <sqf:description>
                    <sqf:title>Create for all a new complex type.</sqf:title>
                </sqf:description>
                <sqf:add position="after">
                    <xsl:for-each-group select="$declWoType" group-by="@name">
                        <sqf:copy-of select="d2t:defComplexType(current-grouping-key(), '')"/>
                    </xsl:for-each-group>
                </sqf:add>
                <sqf:add match="$declWoType" node-type="attribute" target="type">
                    <sch:value-of select="concat(@name, 'Type', '')"/>
                </sqf:add>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>