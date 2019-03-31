<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - node creation node-type comment</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="normalize-space(.) != ''" sqf:fix="addComment">Foo should not be empty.</sch:assert>
            <sqf:fix id="addComment">
                <sqf:description>
                    <sqf:title>Add comment</sqf:title>
                </sqf:description>
                <sqf:add node-type="comment" select="'comment-content'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>