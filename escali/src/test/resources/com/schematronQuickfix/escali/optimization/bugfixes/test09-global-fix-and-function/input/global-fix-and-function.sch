<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:est="escali.schematron-quickfix.com/test">
    <sch:title>Schematron unit test - external document in match by doc function</sch:title>
    
    <sch:ns uri="escali.schematron-quickfix.com/test" prefix="est"/>
    
    <sqf:fixes>
        <sqf:fix id="replaceBar">
            <sqf:description>
                <sqf:title>Replace bar.</sqf:title>
            </sqf:description>
            <sqf:replace match="est:foo(.)" node-type="element" target="baz" select="'baz'"/>
        </sqf:fix>    
    </sqf:fixes>
    <xsl:function
        version="2.0" name="est:foo" as="node()">
        <xsl:param name="foo"/>
        <xsl:sequence select="$foo/bar"/>
    </xsl:function>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="bar" sqf:fix="replaceBar">Foo should not be empty.</sch:report>
        </sch:rule>
    </sch:pattern>
</sch:schema>