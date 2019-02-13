<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <p>Absatz ohne Sprache</p>
    <p xml:lang="en">Paragraph in English</p>
    <pattern>
        <rule context="*:html">
            <assert test="false()" diagnostics="en">Assert ohne Sprache <value-of select="es:getLang()"/></assert>
        </rule>
    </pattern>
    <diagnostics>
        <diagnostic id="en" xml:lang="en">Diagnostic in English.</diagnostic>
    </diagnostics>
</schema>