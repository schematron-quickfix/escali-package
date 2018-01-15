<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" sqf:fix="fixId" xml:lang="en" diagnostics="diagn1">Error message.</sch:assert>

            <sqf:fix id="fixId">
                <sqf:description>
                    <sqf:title ref="qfDesc1">QuickFix description</sqf:title>
                </sqf:description>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:diagnostics>
        <sch:diagnostic id="diagn1" xml:lang="de">Fehlermeldung</sch:diagnostic>
        <sch:diagnostic id="qfDesc1" xml:lang="de">QuickFix Beschreibung</sch:diagnostic>
    </sch:diagnostics>
</sch:schema>
