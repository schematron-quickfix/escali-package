<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xml:lang="en">
    <title>localisation test</title>
    <pattern>
        <rule context="html">
            <assert test="@lang" diagnostics="missLang_de missLang_en missLang_fr"/>
            <assert test="@xml:lang" diagnostics="missLang_de missLang_fr missLang2_en">The attribute xml:lang is missing</assert>
        </rule>
    </pattern>
    <diagnostics>
        <diagnostic id="missLang_de" xml:lang="de">Das Attribut "lang" fehlt.</diagnostic>
        <diagnostic id="missLang_en" xml:lang="en">The attribute "lang" is missing.</diagnostic>
        <diagnostic id="missLang_fr" xml:lang="fr">jetzt steht hier was auf französisch</diagnostic>
        <diagnostic id="missLang2_en" xml:lang="en">You need @xml:lang and @lang.</diagnostic>
    </diagnostics>
</schema>
