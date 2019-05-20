<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - call-fix basic"
        queryBinding="xslt2">
        <es:title>Schematron unit test - call-fix basic</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="replaceFoo">
                    <sqf:call-fix ref="abstract">
                        <sqf:with-param name="target" select="'bar'"/>
                    </sqf:call-fix>
                </sqf:fix>
                <sqf:fix id="abstract">
                    <sqf:param name="target"/>
                    <sqf:replace node-type="element" target="{$target}"/>
                </sqf:fix>
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test=". != ''">
                <es:text>Foo should not be empty.</es:text>
                <sqf:fix fixId="replaceFoo" title="Replace foo by bar" id="replaceFoo_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="replaceFoo"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>