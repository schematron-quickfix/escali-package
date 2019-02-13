<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <pattern>
        <rule context="text()">
            <report test="matches(., 'quickfix', 'i')" sqf:fix="replaceQF replaceQFwithFlags">Missspelled name QuickFix.</report>
            <sqf:fix id="replaceQF">
                <sqf:description>
                    <sqf:title>Correct!</sqf:title>
                </sqf:description>
                <sqf:stringReplace match="." regex="quickfix">QuickFix</sqf:stringReplace>
            </sqf:fix>
            <sqf:fix id="replaceQFwithFlags">
                <sqf:description>
                    <sqf:title>Correct!</sqf:title>
                </sqf:description>
                <sqf:stringReplace match="." regex="quickfix" flags="i">QuickFix</sqf:stringReplace>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
