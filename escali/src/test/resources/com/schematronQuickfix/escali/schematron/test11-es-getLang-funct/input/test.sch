<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2" xml:lang="de">
    <title>Schematron unit test</title>
    <pattern id="pattern1">
        <rule context="pattern1">
            <assert test="false()" diagnostics="en"><value-of select="es:getLang()"/></assert>
        </rule>
    </pattern>
    <diagnostics>
        <diagnostic id="en" xml:lang="en"><value-of select="es:getLang()"/></diagnostic>
    </diagnostics>
</schema>