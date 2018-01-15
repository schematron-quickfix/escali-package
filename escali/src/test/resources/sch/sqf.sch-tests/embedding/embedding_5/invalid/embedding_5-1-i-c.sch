<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" sqf:foo="">
    <sch:pattern>
        <sch:rule context="*">
            <sch:report test="false()"> Assertion message. </sch:report>

        </sch:rule>
    </sch:pattern>
</sch:schema>
