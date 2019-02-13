<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" queryBinding="xslt2">
    <es:default-namespace uri="http://docbook.org/ns/docbook"/>
    <pattern>
        <rule context="footnote">
            <report test=".//footnote" sqf:fix="delete brackets parentBrackets">Footnote in footnote is forbidden.</report>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete all forbidden footnotes.</sqf:title>
                </sqf:description>
                <sqf:delete match=".//footnote"/>
            </sqf:fix>
            <sqf:fix id="brackets" use-when="not(.//footnote[count(*) > 1 or not(para)])">
                <sqf:description>
                    <sqf:title>Resolve the forbidden footnotes as text in brackets.</sqf:title>
                </sqf:description>
                <sqf:replace match=".//footnote"> (<sqf:keep select="./para/node()"/>) </sqf:replace>
            </sqf:fix>
            <sqf:fix id="parentBrackets" use-when="not(ancestor::footnote) and count(*)=1 and para">
                <sqf:description>
                    <sqf:title>Resolve the surrounding footnote as text in brackets.</sqf:title>
                </sqf:description>
                <sqf:replace> (<sqf:keep select="./para/node()"/>) </sqf:replace>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
