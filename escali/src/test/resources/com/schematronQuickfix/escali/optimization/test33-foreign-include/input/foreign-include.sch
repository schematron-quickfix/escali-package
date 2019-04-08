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
    
   <xsl:include href="library.xsl"/>
    
    
</sch:schema>