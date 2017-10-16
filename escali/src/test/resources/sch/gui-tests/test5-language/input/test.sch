<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xml:lang="en">
    
    
    
    <sch:pattern>
        <sch:rule context="error">
            <sch:assert test="false()" sqf:fix="fix_wd global" diagnostics="error1">Error 1</sch:assert>
            <sqf:fix id="fix_wd">
                <sqf:description>
                    <sqf:title>Local fix</sqf:title>
                </sqf:description>
                <sqf:description xml:lang="de">
                    <sqf:title>Lokaler fix</sqf:title>
                </sqf:description>
                <sqf:add target="a" node-type="attribute" select="'newValue'"/>
            </sqf:fix>
        </sch:rule>
        
        <sch:rule context="error2">
            <sch:assert test="false()" sqf:fix="global" diagnostics="error2">Error 2</sch:assert>
        </sch:rule>
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="global">
            <sqf:description>
                <sqf:title>Global fix</sqf:title>
            </sqf:description>
            <sqf:description xml:lang="de">
                <sqf:title>Globaler fix</sqf:title>
            </sqf:description>
            <sqf:add target="e" node-type="element"/>
        </sqf:fix>
    </sqf:fixes>
    <sch:diagnostics>
        <sch:diagnostic id="error1" xml:lang="de">Fehler 1</sch:diagnostic>
        <sch:diagnostic id="error2" xml:lang="de">Fehler 2</sch:diagnostic>
    </sch:diagnostics>
    
</sch:schema>
