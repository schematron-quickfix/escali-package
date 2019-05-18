<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <sch:title>Schematron unit test - edge case - replace attribute conflict</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="@bar|@baz" sqf:fix="replaceBarBaz addFoo addAndReplace">Foo should not have bar or baz.</sch:report>
            <sqf:fix id="replaceBarBaz">
                <sqf:description>
                    <sqf:title>Replace bar and baz attributes.</sqf:title>
                </sqf:description>
                <sqf:replace match="@bar|@baz" target="foo" node-type="attribute" select="''"/>
            </sqf:fix>
            <sqf:fix id="addFoo">
                <sqf:description>
                    <sqf:title>Add foo attribute to bar and baz.</sqf:title>
                </sqf:description>
                <sqf:add match="@bar|@baz" target="foo" node-type="attribute" select="''"/>
            </sqf:fix>
            <sqf:fix id="addAndReplace">
                <sqf:description>
                    <sqf:title>Add foo attribute to bar and baz.</sqf:title>
                </sqf:description>
                <sqf:add node-type="attribute" target="foo" select="'add-foo'"/>
                <sqf:replace match="@bar" node-type="attribute" target="foo" select="'replace-bar'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>