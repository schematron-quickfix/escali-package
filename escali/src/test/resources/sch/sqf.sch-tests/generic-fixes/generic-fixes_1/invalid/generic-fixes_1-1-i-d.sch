<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" sqf:fix="fixId"> Assertion message. </sch:assert>

            <sqf:fix id="fixId">
                <sqf:call-fix ref="genGroup"/>
            </sqf:fix>
        </sch:rule>
        
            
        
    </sch:pattern>
    <sqf:fixes>
        <sqf:group id="genGroup">

        </sqf:group>
    </sqf:fixes>
</sch:schema>
