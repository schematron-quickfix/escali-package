<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta title="Schematron unit test" phase="phase1"/>
    <es:pattern>
        <es:meta id="pattern1"/>
        <es:rule>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern1[namespace-uri()=''][1]" test="false()" base-id="t1">
                <es:text>Test 1</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="pattern2"/>
        <es:rule>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern2[namespace-uri()=''][1]" test="false()" base-id="t2">
                <es:text>Test 2</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
