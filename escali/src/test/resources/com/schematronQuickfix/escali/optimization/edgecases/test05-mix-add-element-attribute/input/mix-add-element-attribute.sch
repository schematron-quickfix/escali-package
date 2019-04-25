<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <sch:title>Schematron unit test - mix add of an element and attribute</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="@*" sqf:fix="addMix">Foo should have at least one attribute.</sch:assert>
            <sqf:fix id="addMix">
                <sqf:description>
                    <sqf:title>Add an existing attribute and its element.</sqf:title>
                </sqf:description>
                <sch:let name="content" value="following-sibling::foo/(. | @*)"/>
                <sqf:add match="." select="$content"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>