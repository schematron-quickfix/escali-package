<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <phase id="phase1">
        <active pattern="pattern1"/>
    </phase>
    <phase id="phase2">
        <active pattern="pattern2"/>
    </phase>
    <phase id="phase3">
        <active pattern="pattern3"/>
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
    <phase id="phase7">
        <active pattern="pattern7"/>
    </phase>
    <pattern id="pattern1">
        <rule context="text()[normalize-space() != '']" es:regex="z\.B\.">
            <assert test="false()" sqf:fix="replaceZB deletePhrase addPhrase">Bad content: "<value-of select="."/>"</assert>
            <sqf:fix id="replaceZB">
                <sqf:description>
                    <sqf:title>Replace "z.B." by "z. B."</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="replacePhrase">
                    <sqf:with-param name="phrase" select="'z. B.'"/>
                </sqf:call-fix>
            </sqf:fix>
        </rule>
    </pattern>
    <pattern id="pattern2">
        <rule context="text()[normalize-space() != '']" es:regex="d\.h\.">
            <assert test="false()">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern3">
        <rule context="text()[normalize-space() != '']" es:regex="u\.a\.">
            <assert test="false()">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern4">
        <rule context="p" es:regex="u\.a\.">
            <assert test="false()">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern5">
        <rule context="comment()" es:regex="etc\.">
            <assert test="false()">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern6">
        <rule context="p/@style" es:regex="uvm\.">
            <assert test="false()">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern7">
        <rule context="processing-instruction()" es:regex="e\.g\.">
            <assert test="false()">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    
    <sqf:fixes>
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