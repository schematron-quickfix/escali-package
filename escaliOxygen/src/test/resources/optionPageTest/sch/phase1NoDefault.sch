<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:phase id="phase1">
        <sch:active pattern="p1"/>
    </sch:phase>
    
    <sch:pattern id="p1">
        <sch:rule context="/*">
            <sch:assert test="false()">Phase 1 or #ALL</sch:assert>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern id="p2">
        <sch:rule context="/*">
            <sch:assert test="false()">Phase #ALL</sch:assert>
        </sch:rule>
    </sch:pattern>
    
</sch:schema>