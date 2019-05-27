<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" phase="phase2" queryBinding="xslt2" schema="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test15/in/schema.sch" instance="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test15/in/instance.xml">
        <es:title/>
        <es:schema>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test15/in/schema.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test15/in/instance.xml</es:instance>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern2" phases="phase2 #ALL">
            <es:title>ignore attribute assert</es:title>
        </es:meta>
        <es:rule>
            <es:meta context="@attribute" id="d3e8"/>
            <es:report location="/*:root[namespace-uri()=''][1]/*:element[namespace-uri()=''][1]/@*:attribute[namespace-uri()=''][1]" id="d2e4_d3e14" role="0.375" test=".='value'">
                <es:text>Bad value</es:text>
                <sqf:fix xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema">
                    <sqf:description>
                        <sqf:title>Ignore this warning.</sqf:title>
                    </sqf:description>
                </sqf:fix>
            </es:report>
        </es:rule>
    </es:pattern>
</es:escali-reports>
