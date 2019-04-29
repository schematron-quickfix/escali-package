<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - external document in match by doc function</sch:title>
    
    <sch:ns uri="escali.schematron-quickfix.com/test" prefix="est"/>
    
    <sch:include href="quickfixes.sqf"/>
    <sch:include href="lib.xsl"/>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="bar" sqf:fix="replaceBar">Foo should not be empty.</sch:report>
        </sch:rule>
    </sch:pattern>
</sch:schema>