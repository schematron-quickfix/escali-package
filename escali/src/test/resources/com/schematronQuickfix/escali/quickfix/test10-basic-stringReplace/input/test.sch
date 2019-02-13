<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <pattern>
        <rule context="text()">
            <report test="matches(.,'____')" sqf:fix="autoStringReplace" role="error">Misuse of the underscore character? More than two in a row.</report>
            <report test="matches(.,'\.\.\.\.')" sqf:fix="autoStringReplace" role="fatal">Misuse of the period character? More than two in a row.</report>
            <sqf:fix id="autoStringReplace">
                <sqf:description>
                    <sqf:title>The misused characters will be replaced by a &lt;form&gt; element. The "length" and the "type" attribute will be generated.</sqf:title>
                </sqf:description>
                <sqf:stringReplace match="." regex="(__(_)+)|(\.\.(\.)+)">
                    <form xmlns="" length="{string-length(.)}"
                          type="{if (matches(.,'_')) then ('line') else ('doted')}"/>
                </sqf:stringReplace>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
