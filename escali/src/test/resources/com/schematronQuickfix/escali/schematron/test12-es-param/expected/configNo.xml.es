<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" title="Schematron unit test" phase="#ALL" queryBinding="xslt2" schema="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test12/in/param.sch" instance="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test12/in/param.xml">
        <es:title>Schematron unit test</es:title>
        <es:schema>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test12/in/param.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test12/in/param.xml</es:instance>
        <xsl:param xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" name="config"/>
        <let xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" name="configDoc" value="doc(resolve-uri($config))"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="pattern1"/>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="pattern2" phases="#ALL"/>
        <es:rule>
            <es:meta context="pattern2"/>
            <es:assert location="/*:root[namespace-uri()=''][1]/*:pattern2[namespace-uri()=''][1]" test="$configDoc/config/test='yes'">
                <es:text>The config says 'no'!</es:text>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
