<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" title="localisation test" phase="#ALL"/>
    <es:pattern>
        <es:rule>
            <es:assert location="/*:html[namespace-uri()=''][1]" test="@lang">
                <es:text>The attribute "lang" is missing.</es:text>
            </es:assert>
            <es:assert location="/*:html[namespace-uri()=''][1]" test="@xml:lang">
                <es:diagnostics diagnostic="missLang2_en">
                    <es:text>You need @xml:lang and @lang.</es:text>
                </es:diagnostics>
                <es:text>The attribute xml:lang is missing</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
