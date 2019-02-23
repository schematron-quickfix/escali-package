<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt3" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xml:lang="en">
    
    <sch:title>XPath 3</sch:title>
    
    <sch:pattern>
        <sch:rule context="error">
            <sch:report test="string-join((@value, @value2)) = 'foobar'" sqf:fix="fix">Test with XPath 3</sch:report>
            <sqf:fix id="fix">
                <sqf:description>
                    <sqf:title>Fix with XPath 3</sqf:title>
                </sqf:description>
                <sqf:add target="value3" node-type="attribute" select="string-join((@value, @value2) ! ('_' || . || '_'))"/>
            </sqf:fix>
        </sch:rule>
        
    </sch:pattern>
    
    
</sch:schema>
