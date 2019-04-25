<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:foo="foo.com">
    <sch:title>Schematron unit test - bug fix - matching elements in foreign namespace</sch:title>
    <sch:ns uri="foo.com" prefix="foo"/>
    <sch:pattern id="pattern1">
        <sch:rule context="*">
            <sch:assert test="namespace-uri(.) = 'foo.com'" sqf:fix="replaceEl">Only elements in namespace foo.com are permitted.</sch:assert>
            <sqf:fix id="replaceEl">
                <sqf:description>
                    <sqf:title>Replace element by bar in foo.com namespace.</sqf:title>
                </sqf:description>
                <sqf:replace target="foo:{local-name()}" node-type="keep"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
