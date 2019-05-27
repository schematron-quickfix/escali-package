<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" title="localisation test" phase="#ALL" queryBinding="xslt2" schema="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test5/in/lang.sch" instance="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test5/in/lang.xhtml">
        <es:title>localisation test</es:title>
        <es:schema>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test5/in/lang.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test5/in/lang.xhtml</es:instance>
    </es:meta>
    <es:pattern>
        <es:meta phases="#ALL"/>
        <es:rule>
            <es:meta context="html"/>
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
