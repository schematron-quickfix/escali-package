<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:sch="http://purl.oclc.org/dsdl/schematron">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - use-for-each with call-fix">
        <es:title>Schematron unit test - use-for-each with call-fix</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
        <sqf:fix id="abstract">
            <sqf:param name="target"/>
            <sqf:param name="current"/>
            <sqf:add node-type="element" target="{$target}" select="string($current)"/>
        </sqf:fix>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="useFoo">
                    <sqf:param name="sqf:current"/>
                    <sqf:call-fix ref="abstract">
                        <sqf:with-param name="target" select="'bar'"/>
                        <sqf:with-param name="current" select="$sqf:current"/>
                    </sqf:call-fix>
                </sqf:fix>
            </es:meta>
            <es:report id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test=". = ''">
                <es:text>Foo should not be empty.</es:text>
                <sqf:fix fixId="useFoo#1" title="Add bar with foo1." id="useFoo_w41aab1_w20aab3b1b1_1">
                    <sqf:call-fix ref="useFoo">
                        <sqf:with-param name="sqf:current" select="(following-sibling::foo)[1]"/>
                    </sqf:call-fix>
                </sqf:fix>
                <sqf:fix fixId="useFoo#2" title="Add bar with foo2." id="useFoo_w41aab1_w20aab3b1b1_2">
                    <sqf:call-fix ref="useFoo">
                        <sqf:with-param name="sqf:current" select="(following-sibling::foo)[2]"/>
                    </sqf:call-fix>
                </sqf:fix>
                <sqf:fix fixId="useFoo#3" title="Add bar with foo3." id="useFoo_w41aab1_w20aab3b1b1_3">
                    <sqf:call-fix ref="useFoo">
                        <sqf:with-param name="sqf:current" select="(following-sibling::foo)[3]"/>
                    </sqf:call-fix>
                </sqf:fix>
                <sqf:fix fixId="useFoo#4" title="Add bar with foo4." id="useFoo_w41aab1_w20aab3b1b1_4">
                    <sqf:call-fix ref="useFoo">
                        <sqf:with-param name="sqf:current" select="(following-sibling::foo)[4]"/>
                    </sqf:call-fix>
                </sqf:fix>
            </es:report>
        </es:rule>
    </es:pattern>
</es:escali-reports>