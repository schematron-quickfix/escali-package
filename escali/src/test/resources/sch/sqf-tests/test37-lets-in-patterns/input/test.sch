<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <sch:pattern>
        <sch:let name="patternVar" value="'patternValue'"/>
        <sch:rule context="elementToReplace">
            <sch:assert test="false()" sqf:fix="replace" sqf:default-fix="replace">Bad element!</sch:assert>
            <sqf:fix id="replace">
                <sqf:description>
                    <sqf:title>Replace element</sqf:title>
                </sqf:description>
                <sqf:replace target="replaced" node-type="element">
                    <sch:value-of select="$patternVar"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
