<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - userentry default">
        <es:title>Schematron unit test - userentry default</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="removeBars">
                    <sqf:param name="barId" type="xs:string"/>
                    <sqf:delete match="bar[@id != $barId]"/>
                </sqf:fix>
            </es:meta>
            <es:report id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test="count(bar) gt 1">
                <es:text>More than one bar in foo.</es:text>
                <sqf:fix fixId="removeBars" title="Remove all bars except of one." id="removeBars_w41aab1_w20aab3b1b1">
                    <sqf:user-entry name="barId_removeBars_w41aab1_w20aab3b1b1" ueName="barId" title="Enter the id of the keeped bar" type="xs:string">
                        <es:enumeration>
                            <es:enum>bar1</es:enum>
                            <es:enum>bar2</es:enum>
                            <es:enum>bar3</es:enum>
                            <es:enum>bar4</es:enum>
                        </es:enumeration>
                    </sqf:user-entry>
                    <sqf:call-fix ref="removeBars">
                        <sqf:with-param name="barId" select="$barId_removeBars_w41aab1_w20aab3b1b1"/>
                    </sqf:call-fix>
                </sqf:fix>
            </es:report>
        </es:rule>
    </es:pattern>
</es:escali-reports>