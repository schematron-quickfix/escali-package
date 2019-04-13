<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - call-fix basic</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="replaceFoo">Foo should not be empty.</sch:assert>
            <sqf:fix id="replaceFoo">
                <sqf:description>
                    <sqf:title>Replace <sch:name/> by bar</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="abstract">
                    <sqf:with-param name="target" select="'bar'"/>
                </sqf:call-fix>
            </sqf:fix>
            <sqf:fix id="abstract">
                <sqf:param name="target"/>
                <sqf:replace node-type="element" target="{$target}"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>