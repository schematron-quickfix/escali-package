<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <sch:title>Schematron unit test - namespace with es:default-namespace extension</sch:title>
    <es:default-namespace uri="foo.com"/>
    <sch:pattern id="pattern1">
        <sch:rule context="root">
            <sch:assert test="foo != ''" sqf:fix="replaceFoo">Foo should not be empty.</sch:assert>
            <sqf:fix id="replaceFoo">
                <sqf:description>
                    <sqf:title>Replace by bar</sqf:title>
                </sqf:description>
                <sqf:replace match="foo">
                    <bar xmlns="foo.com">bar-content</bar>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>