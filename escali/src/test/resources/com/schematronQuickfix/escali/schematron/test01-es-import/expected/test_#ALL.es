<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" title="Schematron unit test" phase="#ALL" queryBinding="xslt2" schema="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test1/in/test1.sch" instance="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test1/in/test1.xml">
        <es:title>Schematron unit test</es:title>
        <es:schema>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test1/in/test1.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test1/in/test1.xml</es:instance>
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
        <es:meta id="pattern2" phases="#ALL"/>
        <es:rule>
            <es:meta context="pattern2"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern2[namespace-uri()=''][1]" test="false()">
                <es:text>Test 2</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="pattern3" phases="#ALL"/>
        <es:rule>
            <es:meta context="pattern3"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern3[namespace-uri()=''][1]" test="false()">
                <es:text>Test 3</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="importPattern" phases="importPhase #ALL"/>
        <es:rule>
            <es:meta context="importPattern"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:importPattern[namespace-uri()=''][1]" test="false()">
                <es:text>Import test</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
