<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="phase1"
        schema=""
        title="Schematron unit test - foreign phase let">
        <es:title>Schematron unit test - foreign phase let</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="phase1" isActive="yes" isDefault="true"/>
        <es:phase id="#ALL" isActive="no"/>
        <es:phase id="phase2" isActive="no"/>
        <es:phase id="phase3" isActive="no"/>
        <sch:let name="name" value="'bar'"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="phase1 phase3 #ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="replace">
                    <sqf:replace node-type="element" target="{$name}">
                        <sqf:copy-of select="node()"/>
                    </sqf:replace>
                </sqf:fix>
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test="name() = $name">
                <es:text>Foo should have the name bar.</es:text>
                <sqf:fix fixId="replace" title="Replace by bar." id="replace_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="replace"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>