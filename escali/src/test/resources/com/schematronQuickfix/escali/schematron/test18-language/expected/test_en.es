<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" title="localisation test" phase="#ALL" queryBinding="xslt2" schema="file:/D:/nico/Work/Java2/escali-main-package/escali/src/test/resources/sch/schematron-tests/test18-language/input/test.sch" instance="file:/D:/nico/Work/Java2/escali-main-package/escali/src/test/resources/sch/schematron-tests/test18-language/input/test.xml">
        <es:title>localisation test</es:title>
        <es:schema>file:/D:/nico/Work/Java2/escali-main-package/escali/src/test/resources/sch/schematron-tests/test18-language/input/test.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java2/escali-main-package/escali/src/test/resources/sch/schematron-tests/test18-language/input/test.xml</es:instance>
    </es:meta>
    <es:pattern>
        <es:meta phases="#ALL"/>
        <es:rule>
            <es:meta context="error"/>
            <es:assert location="/*:test5[namespace-uri()=''][1]/*:error[namespace-uri()=''][1]" test="false()">
                <es:text>Error 1</es:text>
                <sqf:fix xmlns:svrl="http://purl.oclc.org/dsdl/svrl">
                    <sqf:description>
                        <sqf:title>Global fix</sqf:title>
                    </sqf:description>
                </sqf:fix>
                <sqf:fix xmlns:svrl="http://purl.oclc.org/dsdl/svrl">
                    <sqf:description>
                        <sqf:title>Local fix</sqf:title>
                    </sqf:description>
                </sqf:fix>
            </es:assert>
        </es:rule>
        <es:rule>
            <es:meta context="error2"/>
            <es:assert location="/*:test5[namespace-uri()=''][1]/*:error2[namespace-uri()=''][1]" test="false()">
                <es:text>Error 2</es:text>
                <sqf:fix xmlns:svrl="http://purl.oclc.org/dsdl/svrl">
                    <sqf:description>
                        <sqf:title>Global fix</sqf:title>
                    </sqf:description>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
