<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xml:lang="fr">
    <sch:pattern id="p1">
        <sch:title>Pattern title</sch:title>
        <sch:rule context="/*">
            <sch:assert test="false()" diagnostics="blub_de blub_fr" xml:lang="en">Error</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:diagnostics>
        <sch:diagnostic id="blub_de" xml:lang="de">Fehler</sch:diagnostic>
        <sch:diagnostic id="blub_fr" xml:lang="fr">Erreurs</sch:diagnostic>
    </sch:diagnostics>
</sch:schema>