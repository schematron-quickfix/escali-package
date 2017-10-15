<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    
    
    
    <sch:pattern>
        <sch:rule context="error">
            <sch:assert test="false()" sqf:fix="fix_wd global">Error 1</sch:assert>
            <sqf:fix id="fix_wd">
                <sqf:description>
                    <sqf:title>Local fix</sqf:title>
                </sqf:description>
                <sqf:add target="a" node-type="attribute" select="'newValue'"/>
            </sqf:fix>
        </sch:rule>
        
        <sch:rule context="error2">
            <sch:assert test="false()" sqf:fix="global">Error 2</sch:assert>
        </sch:rule>
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="global">
            <sqf:description>
                <sqf:title>Global fix</sqf:title>
            </sqf:description>
            <sqf:add target="e" node-type="element"/>
        </sqf:fix>
    </sqf:fixes>
    
</sch:schema>
