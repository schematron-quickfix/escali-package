<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <p>Absatz ohne Sprache</p>
    <pattern>
        <rule context="*:html">
            <assert test="false()">Assert ohne Sprache</assert>
        </rule>
    </pattern>
</schema>