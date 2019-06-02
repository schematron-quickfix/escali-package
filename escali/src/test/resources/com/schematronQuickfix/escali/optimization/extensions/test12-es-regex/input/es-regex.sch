<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <phase id="phase1">
        <active pattern="pattern1"/>
    </phase>
    <phase id="phase2">
        <active pattern="pattern2"/>
    </phase>
    <phase id="phase3a">
        <active pattern="pattern3a"/>
    </phase>
    <phase id="phase3b">
        <active pattern="pattern3b"/>
    </phase>
    <phase id="phase4">
        <active pattern="pattern4"/>
    </phase>
    <phase id="phase5">
        <active pattern="pattern5"/>
    </phase>
    <phase id="phase6">
        <active pattern="pattern6"/>
    </phase>
    <pattern id="pattern1">
        <rule context="t1/text()" es:regex="bar">
            <assert test="false()" sqf:fix="replace deletePhrase addPhrase">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern2">
        <rule context="t2//text()" es:regex="bar">
            <assert test="false()" sqf:fix="replace deletePhrase addPhrase">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern3a">
        <rule context="t3//text()" es:regex="bar">
            <assert test="false()" sqf:fix="replace deletePhrase addPhrase">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern3b">
        <rule context="t3" es:regex="bar">
            <assert test="false()" sqf:fix="replace deletePhrase addPhrase">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern4">
        <rule context="comment()" es:regex="bar">
            <assert test="false()" sqf:fix="replace deletePhrase addPhrase">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern5">
        <rule context="t5/@a" es:regex="bar">
            <assert test="false()" sqf:fix="replace deletePhrase addPhrase">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern6">
        <rule context="processing-instruction()" es:regex="bar">
            <assert test="false()" sqf:fix="replace deletePhrase addPhrase">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    
    <sqf:fixes>
        <sqf:fix id="replace">
            <sqf:description>
                <sqf:title>Replace "<value-of select="."/>".</sqf:title>
            </sqf:description>
            <sqf:replace match="." select="'foo'"/>
        </sqf:fix>
        <sqf:fix id="replacePhrase">
            <sqf:param name="phrase"/>
            <sqf:description>
                <sqf:title>Replace phrase by <value-of select="$phrase"/></sqf:title>
            </sqf:description>
            <sqf:replace match=".">
                <value-of select="$phrase"/>
            </sqf:replace>
        </sqf:fix>
        <sqf:fix id="deletePhrase">
            <sqf:description>
                <sqf:title>Delete phrase.</sqf:title>
            </sqf:description>
            <sqf:delete match="."/>
        </sqf:fix>
        <sqf:fix id="addPhrase">
            <sqf:description>
                <sqf:title>Add marker around the phrase.</sqf:title>
            </sqf:description>
            <sqf:add match="." node-type="processing-instruction" target="marker" position="after"/>
            <sqf:add match="." node-type="processing-instruction" target="marker" position="before"/>
        </sqf:fix>
    </sqf:fixes>
</schema>