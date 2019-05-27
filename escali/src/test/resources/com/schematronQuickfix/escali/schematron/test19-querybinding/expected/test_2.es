<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" queryBinding="xslt2" title="XPath 2" phase="#ALL">
        <es:title>XPath 2</es:title>
        <es:schema/>
        <es:instance/>
    </es:meta>
    <es:pattern>
        <es:meta phases="#ALL"/>
        <es:rule>
            <es:meta context="error"/>
            <es:report location="/*:test19[namespace-uri()=''][1]/*:error[namespace-uri()=''][1]" test="(@value, @value2) = 'foo'">
                <es:text>Test with XPath 2</es:text>
                <sqf:fix xmlns:svrl="http://purl.oclc.org/dsdl/svrl">
                    <sqf:description>
                        <sqf:title>Fix with XPath 2</sqf:title>
                    </sqf:description>
                </sqf:fix>
            </es:report>
        </es:rule>
    </es:pattern>
</es:escali-reports>
