<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <es:meta activePatterns="pattern1 pattern2"
        instance="#IGNORED#"
        phase="phase3"
        schema=""
        title="Schematron unit test - foreign pattern let">
        <es:title>Schematron unit test - foreign pattern let</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="phase3" isActive="yes"/>
        <es:phase id="phase1" isActive="included" isDefault="true"/>
        <es:phase id="#ALL" isActive="included"/>
        <es:phase id="phase2" isActive="included"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="phase1 phase3 #ALL">
            <sch:let name="prefix" value="'foo'"/>
        </es:meta>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="addPrefix">
                    <sqf:replace match="@id" node-type="attribute" target="id" select="concat($prefix, .)"/>
                </sqf:fix>
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][2]"
                roleLabel="error"
                test="starts-with(@id, $prefix)">
                <es:text>Foo id should contain foo.</es:text>
                <sqf:fix fixId="addPrefix" title="Add prefix foo." id="addPrefix_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="addPrefix"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
    <es:pattern>
        <es:meta id="pattern2" phases="phase2 phase3 #ALL">
            <sch:let name="prefix" value="'bar'"/>
        </es:meta>
        <es:rule>
            <es:meta context="bar" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="addPrefix">
                    <sqf:replace match="@id" node-type="attribute" target="id" select="concat($prefix, .)"/>
                </sqf:fix>
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][2]/*:bar[namespace-uri()=''][1]"
                roleLabel="error"
                test="starts-with(@id, $prefix)">
                <es:text>Bar id should contain bar.</es:text>
                <sqf:fix fixId="addPrefix" title="Add prefix bar." id="addPrefix_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="addPrefix"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>