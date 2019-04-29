<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:est="escali.schematron-quickfix.com/test">
    <sch:title>Schematron unit test - action order</sch:title>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="bar" sqf:fix="replaceBar replaceBarByBazzer replaceAttribute replaceAllAttributes replaceAncestorAndChild">Foo should not be empty.</sch:report>
            <sqf:fix id="replaceBar">
                <sqf:description>
                    <sqf:title>Replace bar by baz</sqf:title>
                </sqf:description>
                <sqf:replace match="bar" node-type="element" target="baz"/>
                <sqf:replace match="*" node-type="element" target="bazzer"/>
            </sqf:fix>
            <sqf:fix id="replaceBarByBazzer">
                <sqf:description>
                    <sqf:title>Replace bar by bazzer</sqf:title>
                </sqf:description>
                <sqf:replace match="*" node-type="element" target="bazzer"/>
                <sqf:replace match="bar" node-type="element" target="baz"/>
            </sqf:fix>
            <sqf:fix id="replaceAttribute">
                <sqf:description>
                    <sqf:title>Replace attributes</sqf:title>
                </sqf:description>
                <sqf:replace match="bar/@id" node-type="attribute" target="idref" select="."/>
                <sqf:replace match="bar/@*" node-type="attribute" target="_{local-name(.)}"/>
            </sqf:fix>
            <sqf:fix id="replaceAllAttributes">
                <sqf:description>
                    <sqf:title>Replace all attributes</sqf:title>
                </sqf:description>
                <sqf:replace match="bar/@*" node-type="attribute" target="_{local-name(.)}"/>
                <sqf:replace match="bar/@id" node-type="attribute" target="idref" select="."/>
            </sqf:fix>
            <sqf:fix id="replaceAncestorAndChild">
                <sqf:description>
                    <sqf:title>Replace ancestor and child.</sqf:title>
                </sqf:description>
                <sqf:replace match="bar" node-type="element" target="baz"/>
                <sqf:replace match="." node-type="element" target="bar"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>