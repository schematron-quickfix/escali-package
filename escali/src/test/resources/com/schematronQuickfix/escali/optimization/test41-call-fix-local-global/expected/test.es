<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - call-fix with pending calls">
        <es:title>Schematron unit test - call-fix with pending calls</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
        
        <sqf:fix id="addLastChilds">
            <sqf:param name="target"/>
            <sqf:param name="match"/>
            <sqf:add match="$match" position="last-child" node-type="element" target="{$target}"/>
        </sqf:fix>
        
        <sqf:fix id="addBefore">
            <sqf:param name="target"/>
            <sqf:param name="match"/>
            <sqf:add match="$match" position="before" node-type="element" target="{$target}"/>
        </sqf:fix>
        
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="addLastChilds">
                    <sqf:call-fix ref="addLastChilds">
                        <sqf:with-param name="match" select="parent::*"/>
                        <sqf:with-param name="target" select="'bar'"/>
                    </sqf:call-fix>
                </sqf:fix>
                
                <sqf:fix id="addBaz">
                    <sqf:call-fix ref="addBefore">
                        <sqf:with-param name="match" select="."/>
                        <sqf:with-param name="target" select="'baz'"/>
                    </sqf:call-fix>
                </sqf:fix>
                
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test=". != ''">
                <es:text>Foo should not be empty.</es:text>
                <sqf:fix fixId="addLastChilds" title="Add child bar to root" id="addLastChilds_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="addLastChilds"/>
                </sqf:fix>
                <sqf:fix fixId="addBaz" title="Add baz before" id="addBaz_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="addBaz"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>