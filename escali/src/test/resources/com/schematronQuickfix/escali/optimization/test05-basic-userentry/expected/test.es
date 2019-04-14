<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - basic userentry">
        <es:title>Schematron unit test - basic userentry</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
        <sqf:fix id="global_addBar">
            <sqf:param name="element" type="xs:string"/>
            <sqf:add node-type="element" target="bar" select="$element"/>
        </sqf:fix>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="addBar">
                    <sqf:param name="element" type="xs:string"/>
                    <sqf:add node-type="element" target="bar" select="$element"/>
                </sqf:fix>
            </es:meta>
            <es:report id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test=". = ''">
                <es:text>Foo should not be empty.</es:text>
                <sqf:fix fixId="addBar" title="Add bar element with content." id="addBar_w41aab1_w20aab3b1b1">
                    <sqf:user-entry name="element_addBar_w41aab1_w20aab3b1b1" ueName="element" title="Enter content for new bar element" type="xs:string"/>
                    <sqf:call-fix ref="addBar">
                        <sqf:with-param name="element" select="$element_addBar_w41aab1_w20aab3b1b1"/>
                    </sqf:call-fix>
                </sqf:fix>
                <sqf:fix fixId="global_addBar" title="Add bar element with content." id="global_addBar_w41aab1_w20aab3b1b1">
                    <sqf:user-entry name="element_global_addBar_w41aab1_w20aab3b1b1" ueName="element" title="Enter content for new bar element" type="xs:string"/>
                    <sqf:call-fix ref="global_addBar">
                        <sqf:with-param name="element" select="$element_global_addBar_w41aab1_w20aab3b1b1"/>
                    </sqf:call-fix>
                </sqf:fix>
            </es:report>
        </es:rule>
    </es:pattern>
</es:escali-reports>