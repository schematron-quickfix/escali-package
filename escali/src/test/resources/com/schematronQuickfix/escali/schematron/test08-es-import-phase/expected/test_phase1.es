<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" title="Schematron unit test" phase="phase1" queryBinding="xslt2" schema="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test8/in/importPhase.sch" instance="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test8/in/importPhase.xml">
        <es:title>Schematron unit test</es:title>
        <es:schema>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test8/in/importPhase.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test8/in/importPhase.xml</es:instance>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="phase1 #ALL"/>
        <es:rule>
            <es:meta context="pattern1"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern1[namespace-uri()=''][1]" test="false()">
                <es:text>Test 1</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="importPattern" phases="phase1 importPhase #ALL"/>
        <es:rule>
            <es:meta context="importPattern"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:importPattern[namespace-uri()=''][1]" test="false()">
                <es:text>Import test</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
