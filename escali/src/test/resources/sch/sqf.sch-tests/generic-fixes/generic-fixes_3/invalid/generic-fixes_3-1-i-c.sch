<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" sqf:fix="genFix"> Assertion message. </sch:assert>
            
            <sqf:fix id="genFix">
                <sqf:param name="abstractParam" abstract="true" type="xs:string"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
