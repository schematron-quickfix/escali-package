<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:title>Schematron unit test - foreign global</sch:title>
    <sch:ns uri="foo.com" prefix="foo"/>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="bar/@id[contains(., foo:id(current()))]" sqf:fix="replaceBar">Bar should contain id of foo.</sch:assert>
            <sqf:fix id="replaceBar">
                <sqf:description>
                    <sqf:title>Replace bar with for each foo one bar.</sqf:title>
                </sqf:description>
                <sqf:replace match="bar">
                    <xsl:call-template name="createBar">
                        <xsl:with-param name="id" select="foo:id(..)"/>
                        <xsl:with-param name="counter" select="count($allFooBar)"/>
                    </xsl:call-template>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <xsl:key name="foo-id" match="foo" use="@id"/>
    
    <xsl:function name="foo:id">
        <xsl:param name="foo"/>
        <xsl:sequence select="$foo/(@id, concat('id_', count($foo/preceding::foo) + 1))[1]"/>
    </xsl:function>
    <sch:let name="allFooBar" value="//(foo|bar)"/>
    <xsl:variable name="root" select="/"/>
    <xsl:template name="createBar">
        <xsl:param name="id"/>
        <xsl:param name="counter"/>
        
        <xsl:variable name="isGeneric" select="not(key('foo-id', $id, $root))"/>
        
        <xsl:for-each select="1 to $counter">
            <bar barId="{$id}_{.}" generic="{$isGeneric}"/>
        </xsl:for-each>
    </xsl:template>
    
    
</sch:schema>