<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:foo="foo.com"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:title>Schematron unit test - external document in match by var, key and xsl function</sch:title>
    <sch:let name="global-ext-doc" value="doc('external-match-from-var_ext.xml')"/>
    <sch:ns uri="foo.com" prefix="foo"/>
    <xsl:key name="el-id" match="*[@id]" use="@id"/>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="addBarGlobalVar addBarKey addBarFunction">Foo should not be empty.</sch:assert>
            <sqf:fix id="addBarGlobalVar">
                <sqf:description>
                    <sqf:title>Add bar to external document from global var.</sqf:title>
                </sqf:description>
                <sqf:add match="$global-ext-doc/*/bar" node-type="element" target="baz" select="'baz'"/>
            </sqf:fix>
            
            <sqf:fix id="addBarKey">
                <sqf:description>
                    <sqf:title>Add bar to external document from key.</sqf:title>
                </sqf:description>
                <sqf:add match="key('el-id', 'bar-id', $global-ext-doc)" node-type="element" target="baz" select="'baz'"/>
            </sqf:fix>
            
            <sqf:fix id="addBarFunction">
                <sqf:description>
                    <sqf:title>Add bar to external document from function.</sqf:title>
                </sqf:description>
                <sqf:add match="foo:getNode('bar-id')" node-type="element" target="baz" select="'baz'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <xsl:function name="foo:getNode" as="node()?">
        <xsl:param name="id"/>
        <xsl:sequence select="key('el-id', $id, $global-ext-doc)"/>
    </xsl:function>
</sch:schema>