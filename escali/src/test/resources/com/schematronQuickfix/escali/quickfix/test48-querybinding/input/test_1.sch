<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xml:lang="en">
    
    <sch:title>XPath 1</sch:title>
    
    <sch:pattern>
        <sch:rule context="error">
            <sch:report test="true()" sqf:fix="fix">Test with XPath 1</sch:report>
            <sqf:fix id="fix">
                <sqf:description>
                    <sqf:title>Fix with XPath 1</sqf:title>
                </sqf:description>
                <sqf:add target="value3" node-type="attribute" select="@value"/>
            </sqf:fix>
        </sch:rule>
        
    </sch:pattern>
    
    
</sch:schema>
