<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - external document in match by doc function</sch:title>
    
    <sch:let name="relative-to-sch" value="doc('relative-to-sch.xml')"/>
    <sch:let name="relative-to-xml" value="doc(resolve-uri('relative-to-xml.xml', base-uri(/)))"/>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="addBarRTS addBarRTX">Foo should not be empty.</sch:assert>
            <sqf:fix id="addBarRTS">
                <sqf:description>
                    <sqf:title>Add bar to relative-to-sch document.</sqf:title>
                </sqf:description>
                <sqf:add match="$relative-to-sch/*" node-type="element" target="bar" select="'baz'"/>
            </sqf:fix>
            
            <sqf:fix id="addBarRTX">
                <sqf:description>
                    <sqf:title>Add bar to this and external document.</sqf:title>
                </sqf:description>
                <sqf:add match="$relative-to-xml/*" node-type="element" target="bar" select="'baz'"/>
            </sqf:fix>
            
        </sch:rule>
    </sch:pattern>
</sch:schema>