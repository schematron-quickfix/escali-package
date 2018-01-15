<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" sqf:fix="fixId"> Assertion message. </sch:assert>

            <sqf:fix id="fixId">
                <sqf:replace select="*">textContent</sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
