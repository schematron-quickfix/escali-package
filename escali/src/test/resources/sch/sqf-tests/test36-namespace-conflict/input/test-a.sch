<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2" xmlns="www.some.default.namespace.com">
    <sch:ns uri="www.some.default.namespace.com" prefix="def"/>
    <sch:pattern>
        <sch:rule context="def:elementToReplace">
            <sch:assert test="false()" sqf:fix="replaceDefault replaceOther replaceNull" sqf:default-fix="replaceDefault">Bad namespace!</sch:assert>
            <sqf:fix id="replaceDefault">
                <sqf:description>
                    <sqf:title>Replace element</sqf:title>
                </sqf:description>
                <sqf:replace target="replaced" node-type="element">
                    <sqf:keep select="node()"/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="replaceOther">
                <sqf:description>
                    <sqf:title>Replace element</sqf:title>
                </sqf:description>
                <sqf:replace target="replaced" node-type="element" xmlns="www.some.other.namespace.com">
                    <sqf:keep select="node()"/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="replaceNull">
                <sqf:description>
                    <sqf:title>Replace element</sqf:title>
                </sqf:description>
                <sqf:replace target="replaced" node-type="element" xmlns="">
                    <sqf:keep select="node()"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
