<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/" xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <es:meta activePatterns="pattern1" instance="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" phase="#ALL" schema="" title="Schematron unit test - basic delete" queryBinding="xslt2">
        <es:title>Schematron unit test - basic delete</es:title>
        <es:schema>file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.sch</es:schema>
        <es:instance>file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="deleteFoo">
                    <sqf:delete/>
                </sqf:fix>
            </es:meta>
            <es:assert base-id="delete1" location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]" roleLabel="error" test=". != ''">
                <es:text>Foo should not be empty.</es:text>
                <sqf:fix fixId="deleteFoo" messageId="d4e10" contextId="d2e3" id="deleteFoo-d4e10-d2e3" role="delete" title="Delete foo">
                    <sqf:call-fix ref="deleteFoo"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>
