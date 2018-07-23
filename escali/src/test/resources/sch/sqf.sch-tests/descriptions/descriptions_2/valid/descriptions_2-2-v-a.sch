<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" sqf:fix="fixId"> Assertion message. </sch:assert>

            <sqf:fix id="fixId">
                <sqf:call-fix ref="genFix1"/>
            </sqf:fix>
            <sqf:fix id="genFix1">
                <sqf:description>
                    <sqf:title>Generic Fix 1</sqf:title>
                </sqf:description>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>