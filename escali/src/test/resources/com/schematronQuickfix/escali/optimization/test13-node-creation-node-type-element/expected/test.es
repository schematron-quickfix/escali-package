<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - node creation node-type element"
        queryBinding="xslt2">
        <es:title>Schematron unit test - node creation node-type element</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="replaceFoo">
                    <sqf:replace target="bar" node-type="element" select="'bar-content'"/>
                </sqf:fix>
                <sqf:fix id="replaceFooKeep">
                    <sqf:replace target="bar" node-type="keep" select="'bar-content'"/>
                </sqf:fix>
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test="normalize-space(.) != ''">
                <es:text>Foo should not be empty.</es:text>
                <sqf:fix fixId="replaceFoo" title="Replace foo by bar" id="replaceFoo_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="replaceFoo"/>
                </sqf:fix>
                <sqf:fix fixId="replaceFooKeep" title="Replace foo by bar (keep)" id="replaceFooKeep_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="replaceFooKeep"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>