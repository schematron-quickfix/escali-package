<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - userentry types">
        <es:title>Schematron unit test - userentry types</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error"/>
            <sqf:fix id="removeBars">
                <sqf:param name="barPos" type="xs:integer"/>
                <sqf:delete match="bar[position() ne $barPos]"/>
            </sqf:fix>
            <es:report id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test="count(bar) gt 1">
                <es:text>More than one bar in foo.</es:text>
                <sqf:fix fixId="removeBars" title="Remove all bars except of one." id="removeBars_w41aab1_w20aab3b1b1">
                    <sqf:user-entry name="barPos_removeBars_w41aab1_w20aab3b1b1" ueName="barPos" title="Enter the position of the keeped bar" type="xs:integer"/>
                    <sqf:call-fix ref="removeBars">
                        <sqf:with-param name="barPos" select="$barPos_removeBars_w41aab1_w20aab3b1b1"/>
                    </sqf:call-fix>
                </sqf:fix>
            </es:report>
        </es:rule>
    </es:pattern>
</es:escali-reports>