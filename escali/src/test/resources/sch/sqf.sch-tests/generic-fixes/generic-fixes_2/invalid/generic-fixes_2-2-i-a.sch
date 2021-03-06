<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" sqf:fix="fixId"> Assertion message. </sch:assert>
            
            <sqf:fix id="fixId">
                <sqf:call-fix ref="genFix">
                    <sqf:with-param name="genParam" select="value"/>
                </sqf:call-fix>
            </sqf:fix>
            <sqf:fix id="genFix">
                <sqf:param name="genParam" abstract="true"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
