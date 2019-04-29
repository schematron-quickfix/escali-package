<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - external document in match by doc function</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="addBar addBarExtAndThis addBarAll">Foo should not be empty.</sch:assert>
            <sqf:fix id="addBar">
                <sqf:description>
                    <sqf:title>Add bar to external document.</sqf:title>
                </sqf:description>
                <sqf:add match="doc('doc-in-match_ext.xml')/*" node-type="element" target="bar" select="'baz'"/>
            </sqf:fix>
            
            <sqf:fix id="addBarExtAndThis">
                <sqf:description>
                    <sqf:title>Add bar to this and external document.</sqf:title>
                </sqf:description>
                <sqf:add match="/* | doc('doc-in-match_ext.xml')/*" node-type="element" target="bar" select="'baz'"/>
            </sqf:fix>
            
            <sqf:fix id="addBarAll">
                <sqf:description>
                    <sqf:title>Add bar to this and 2 external documents.</sqf:title>
                </sqf:description>
                <sqf:add match="/* | doc('doc-in-match_ext.xml')/* | doc('doc-in-match_ext2.xml')/*" node-type="element" target="bar" select="'baz'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>