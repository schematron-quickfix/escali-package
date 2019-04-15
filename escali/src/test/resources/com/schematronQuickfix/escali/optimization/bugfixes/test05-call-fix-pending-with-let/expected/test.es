<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sch="http://purl.oclc.org/dsdl/schematron">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - bug fix - call-fix pending with let">
        <es:title>Schematron unit test - bug fix - call-fix pending with let</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
        
        <sqf:fix id="abstract">
            <sch:let name="target" value="'bar'"/>
            <sqf:call-fix ref="very_abstract">
                <sqf:with-param name="target" select="$target"/>
                <sqf:with-param name="match" select="."/>
            </sqf:call-fix>
            
            <sqf:call-fix ref="very_abstract">
                <sqf:with-param name="target" select="$target"/>
            </sqf:call-fix>
        </sqf:fix>
        
        <sqf:fix id="very_abstract">
            <sqf:param name="target"/>
            <sqf:param name="match" default="''"/>
            <sch:let name="match" value="
                if ($match = '') then
                (/*)
                else
                $match"/>
            <sqf:add match="$match" position="first-child" node-type="element" target="{$target1}"/>
        </sqf:fix>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="addChild">
                    <sqf:call-fix ref="abstract"/>
                </sqf:fix>
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test=". != ''">
                <es:text>Foo should not be empty.</es:text>
                <sqf:fix fixId="addChild" title="Add child bar to foo and root." id="addChild_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="addChild"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>