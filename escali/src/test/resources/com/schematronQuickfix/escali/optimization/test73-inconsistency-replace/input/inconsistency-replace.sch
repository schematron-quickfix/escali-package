<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:est="escali.schematron-quickfix.com/test">
    <sch:title>Schematron unit test - inconsistency add</sch:title>
    
    <sch:pattern id="pattern1">
        <sch:rule context="root/node()">
            <sch:assert test=". instance of text()" sqf:fix="replaceByAttr">Root node should contain only text.</sch:assert>
        </sch:rule>
        <sch:rule context="@*">
            <sch:report test="true()" sqf:fix="replaceByEl replaceByPi replaceByComment replaceByText">Attributes are not permitted.</sch:report>
        </sch:rule>
        
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="replaceByAttr">
            <sqf:description>
                <sqf:title>Replace node by attribute.</sqf:title>
            </sqf:description>
            <sqf:replace match="." node-type="attribute" target="foo" select="'bar'"/>
        </sqf:fix>
        <sqf:fix id="replaceByEl">
            <sqf:description>
                <sqf:title>Replace attribute by element.</sqf:title>
            </sqf:description>
            <sqf:replace match="." node-type="element" target="foo" select="'bar'"/>
        </sqf:fix>
        <sqf:fix id="replaceByPi">
            <sqf:description>
                <sqf:title>Replace node by pi.</sqf:title>
            </sqf:description>
            <sqf:replace match="." node-type="pi" target="foo" select="'bar'"/>
        </sqf:fix>
        <sqf:fix id="replaceByComment">
            <sqf:description>
                <sqf:title>Replace node by comment.</sqf:title>
            </sqf:description>
            <sqf:replace match="." node-type="comment" select="'foo'"/>
        </sqf:fix>
        <sqf:fix id="replaceByText">
            <sqf:description>
                <sqf:title>Replace node by attribute.</sqf:title>
            </sqf:description>
            <sqf:replace match="." select="'foo'"/>
        </sqf:fix>
        
    </sqf:fixes>
</sch:schema>