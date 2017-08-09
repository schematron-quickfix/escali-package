<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:pa="http://www.pagina-online.de/xlib" queryBinding="xslt2">

    <sch:pattern id="SFV">

        <sch:rule context="b">
            <sch:report test=".//b" role="warn" sqf:fix="copy-of">b in b</sch:report>


            <sqf:fix id="copy-of">
                <sqf:description>
                    <sqf:title>Resolve to plain text</sqf:title>
                </sqf:description>
                <sqf:replace match=".//b">
                    <sqf:copy-of select="node()" unparsed-mode="true"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>

    </sch:pattern>
</sch:schema>
