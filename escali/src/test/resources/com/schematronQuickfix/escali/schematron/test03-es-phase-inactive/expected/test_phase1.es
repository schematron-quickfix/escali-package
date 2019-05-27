<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" title="Schematron unit test" phase="phase1" queryBinding="xslt2" schema="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test3/in/test3.sch" instance="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test3/in/test3.xml">
        <es:title>Schematron unit test</es:title>
        <es:schema>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test3/in/test3.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test3/in/test3.xml</es:instance>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="phase1 phase3 phase4 phase5 phase6 #ALL"/>
        <es:rule>
            <es:meta context="pattern1"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern1[namespace-uri()=''][1]" test="false()">
                <es:text>Test 1</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="pattern2" phases="phase1 phase2 phase3 phase5 phase6 #ALL"/>
        <es:rule>
            <es:meta context="pattern2"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern2[namespace-uri()=''][1]" test="false()">
                <es:text>Test 2</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
