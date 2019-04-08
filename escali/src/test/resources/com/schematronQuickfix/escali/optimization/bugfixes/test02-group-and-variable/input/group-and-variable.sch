<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <sch:title>Schematron unit test - userentry with variable</sch:title>
    <sch:let name="rootId" value="/root/@id"/>
    <sch:pattern id="pattern1">

        <sch:let name="mode" value="'pattern1'"/>

        <sch:rule context="foo">
            <sch:let name="fooId" value="@id"/>

            <sch:report test=". = ''" sqf:fix="group">Foo should not be empty.</sch:report>
            <sqf:group id="group">
                <sqf:fix id="addBar">
                    <sch:let name="target" value="
                            if ($mode = 'pattern1') then
                                $rootId
                            else
                                'bar'"/>
                    <sqf:description>
                        <sqf:title>Add bar element with content.</sqf:title>
                    </sqf:description>
                    <sqf:add node-type="element" target="{$target}" select="string($fooId)"/>
                </sqf:fix>
            </sqf:group>
        </sch:rule>
    </sch:pattern>
</sch:schema>
