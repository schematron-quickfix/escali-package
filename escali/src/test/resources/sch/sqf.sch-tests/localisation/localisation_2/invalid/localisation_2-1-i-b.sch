<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xml:lang="en">
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" xml:lang="en" diagnostics="diagn1_de diagn1_fr">Error message.</sch:assert>
            
        </sch:rule>
    </sch:pattern>
    <sch:pattern>
        <sch:rule context="*">
            <sch:assert test="true()" xml:lang="en">Error message 2.</sch:assert>
            
        </sch:rule>
    </sch:pattern>
    <sch:diagnostics>
        <sch:diagnostic id="diagn1_de" xml:lang="de">Fehlermeldung 1</sch:diagnostic>
        
        <sch:diagnostic id="diagn1_fr" xml:lang="fr">Message d'erreur 1</sch:diagnostic>
        
    </sch:diagnostics>
</sch:schema>
