<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:pattern>
        <sch:rule context="/">
            <sch:report test="ends-with(document-uri(/), 'xml')">XML-Document</sch:report>
        </sch:rule>
    </sch:pattern>
</sch:schema>
