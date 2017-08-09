<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <ns uri="b" prefix="b"/>
    <title>Import test 13 file</title>
    <pattern id="b">
        <rule context="b:pattern1">
            <assert test="false()">Prefix <value-of select="."/></assert>
        </rule>
    </pattern>
</schema>