<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xml:lang="en">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" xml:lang="en" diagnostics="diagn1_de">Error message.</sch:assert>
            
        </sch:rule>
    </sch:pattern>
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" xml:lang="en" diagnostics="diagn2_de" sqf:fix="fixId1">Error message 2.</sch:assert>
            <sqf:fix id="fixId1">
                <sqf:description>
                    <sqf:title>QuickFix description 1</sqf:title>
                </sqf:description>
                <sqf:description xml:lang="de">
                    <sqf:title>QuickFix Beschreibung 2</sqf:title>
                </sqf:description>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:diagnostics>
        <sch:diagnostic id="diagn1_de" xml:lang="de">Fehlermeldung 1</sch:diagnostic>
        <sch:diagnostic id="diagn2_de" xml:lang="de">Fehlermeldung 2</sch:diagnostic>
        
        
        
    </sch:diagnostics>
</sch:schema>
