<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:sch="http://purl.oclc.org/dsdl/schematron">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - basic replace"
        queryBinding="xslt2">
        <es:title>Schematron unit test - basic replace</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="addBarId">
                    <sch:let name="id" value="@id"/>
                    <sqf:add match="bar" node-type="attribute" target="id" select="$id"/>
                </sqf:fix>
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test="@id = ./bar/@id">
                <es:text>Bar should have same id foo1.</es:text>
                <sqf:fix fixId="addBarId" title="Add ID &quot;foo1&quot; to bar." id="addBarId_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="addBarId"/>
                </sqf:fix>
            </es:assert>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][2]"
                roleLabel="error"
                test="@id = ./bar/@id">
                <es:text>Bar should have same id foo2.</es:text>
                <sqf:fix fixId="addBarId" title="Add ID &quot;foo2&quot; to bar." id="addBarId_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="addBarId"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>