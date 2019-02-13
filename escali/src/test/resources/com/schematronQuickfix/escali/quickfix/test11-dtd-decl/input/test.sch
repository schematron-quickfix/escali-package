<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <pattern>
        <rule context="d[@def]">
            <report test="../b" sqf:fix="deleteB">Error with element b and @def</report>
            <sqf:fix id="deleteB">
                <sqf:description>
                    <sqf:title>Delete element &lt;b&gt;</sqf:title>
                </sqf:description>
                <sqf:delete match="../b"/>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
