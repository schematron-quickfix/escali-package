<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta title="Schematron unit test" phase="phase2"/>
    <es:pattern>
        <es:meta id="pattern3"/>
        <es:rule>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern3[namespace-uri()=''][1]" test="false()" base-id="t3">
                <es:text>Test 3</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="pattern4"/>
        <es:rule>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern4[namespace-uri()=''][1]" test="false()" base-id="t4">
                <es:text>Test 4</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
