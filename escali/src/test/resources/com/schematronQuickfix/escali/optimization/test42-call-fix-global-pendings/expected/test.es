<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - call-fix with global pending calls"
        queryBinding="xslt2">
        <es:title>Schematron unit test - call-fix with global pending calls</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
        
        <sqf:fix id="addChilds">
            <sqf:call-fix ref="abstract">
                <sqf:with-param name="target" select="'bar'"/>
            </sqf:call-fix>
        </sqf:fix>
        <sqf:fix id="abstract">
            <sqf:param name="target"/>
            <sqf:add position="first-child" node-type="element" target="{$target}"/>
            <sqf:call-fix ref="very_abstract">
                <sqf:with-param name="match" select="parent::*"/>
                <sqf:with-param name="target" select="$target"/>
            </sqf:call-fix>
        </sqf:fix>
        <sqf:fix id="very_abstract">
            <sqf:param name="target"/>
            <sqf:param name="match"/>
            <sqf:add match="$match" position="last-child" node-type="element" target="{$target}"/>
        </sqf:fix>
        
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error"/>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test=". != ''">
                <es:text>Foo should not be empty.</es:text>
                <sqf:fix fixId="addChilds" title="Add childs bar and baz to root" id="addChilds_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="addChilds"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>