<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" title="Schematron unit test" phase="phase2" queryBinding="xslt2" schema="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test4/in/test4.sch" instance="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test4/in/test4.xml">
        <es:title>Schematron unit test</es:title>
        <es:schema>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test4/in/test4.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test4/in/test4.xml</es:instance>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern3" is-a="pattern4" phases="phase1 phase2 #ALL"/>
        <es:rule>
            <es:meta context="pattern4"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern4[namespace-uri()=''][1]" test="false()">
                <es:text>Test 4</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="pattern1" is-a="pattern2" phases="phase2 #ALL"/>
        <es:rule>
            <es:meta context="pattern2"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern2[namespace-uri()=''][1]" test="false()">
                <es:text>Test 2</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
