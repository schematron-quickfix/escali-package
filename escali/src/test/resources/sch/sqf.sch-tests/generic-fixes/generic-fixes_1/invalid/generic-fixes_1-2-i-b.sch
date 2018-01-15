<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" sqf:fix="fixId"> Assertion message. </sch:assert>

            
        </sch:rule>
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="fixId">
            <sqf:description>
                <sqf:title>Calling fix</sqf:title>
            </sqf:description>
            <sqf:call-fix ref="fixId"/>
        </sqf:fix>
    </sqf:fixes>
</sch:schema>
