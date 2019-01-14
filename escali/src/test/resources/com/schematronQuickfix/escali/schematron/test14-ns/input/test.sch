<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:a="b" xmlns:b="b" xmlns:c="c" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <ns uri="a" prefix="a"/>
    <pattern id="a">
        <rule context="a:pattern1">
            <assert test="false()">Prefix <value-of select="."/></assert>
        </rule>
    </pattern>
    <pattern id="b">
        <rule context="b:pattern1" xmlns:b="a">
            <assert test="false()">Prefix <value-of select="."/></assert>
        </rule>
    </pattern>
    <pattern id="c" xmlns:c="b">
        <rule context="c:pattern1">
            <assert test="false()">Prefix <value-of select="."/></assert>
        </rule>
    </pattern>

</schema>
