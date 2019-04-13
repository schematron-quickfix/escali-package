<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - call-fix with pending calls</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="addLastChilds addBaz">Foo should not be empty.</sch:assert>
            <sqf:fix id="addLastChilds">
                <sqf:description>
                    <sqf:title>Add child bar to root</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="addLastChilds">
                    <sqf:with-param name="match" select="parent::*"/>
                    <sqf:with-param name="target" select="'bar'"/>
                </sqf:call-fix>
            </sqf:fix>
            <sqf:fix id="addBaz">
                <sqf:description>
                    <sqf:title>Add baz before</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="addBefore">
                    <sqf:with-param name="match" select="."/>
                    <sqf:with-param name="target" select="'baz'"/>
                </sqf:call-fix>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sqf:fixes>
        <sqf:fix id="addLastChilds">
            <sqf:param name="target"/>
            <sqf:param name="match"/>
            <sqf:add match="$match" position="last-child" node-type="element" target="{$target}"/>
        </sqf:fix>
        
        <sqf:fix id="addBefore">
            <sqf:param name="target"/>
            <sqf:param name="match"/>
            <sqf:add match="$match" position="before" node-type="element" target="{$target}"/>
        </sqf:fix>
        
    </sqf:fixes>
</sch:schema>