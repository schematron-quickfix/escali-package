<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" sqf:fix="fixId"> Assertion message. </sch:assert>

            <sqf:fix id="fixId">
                <sqf:description>
                    <sqf:title>Calling fix</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="genFix">
                    <sqf:with-param name="genParam" select="*">textContent</sqf:with-param>
                </sqf:call-fix>
            </sqf:fix>
            <sqf:fix id="genFix">
                <sqf:param name="genParam"></sqf:param>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
