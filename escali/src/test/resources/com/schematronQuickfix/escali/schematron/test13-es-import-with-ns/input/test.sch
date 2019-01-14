<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <ns uri="a" prefix="a"/>
    <pattern id="a">
        <rule context="a:pattern1">
            <assert test="false()">Prefix <value-of select="."/></assert>
        </rule>
    </pattern>
    
    <es:import href="importTest.sch"/>
</schema>