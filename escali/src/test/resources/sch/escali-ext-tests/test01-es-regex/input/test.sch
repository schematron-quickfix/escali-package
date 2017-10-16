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
            <assert test="false()" sqf:fix="standardFixes">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern2">
        <rule context="text()[normalize-space() != '']" es:regex="d\.h\.">
            <assert test="false()" sqf:fix="standardFixes">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern3">
        <rule context="text()[normalize-space() != '']" es:regex="u\.a\.">
            <assert test="false()" sqf:fix="standardFixes">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern4">
        <rule context="p" es:regex="u\.a\.">
            <assert test="false()" sqf:fix="standardFixes">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern5">
        <rule context="comment()" es:regex="etc\.">
            <assert test="false()" sqf:fix="standardFixes">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern6">
        <rule context="p/@style" es:regex="uvm\.">
            <assert test="false()" sqf:fix="standardFixes">Bad content: "<value-of select="."/>"</assert>
        </rule>
        <rule context="@*" es:regex="&amp;|u\.a\.">
            <assert test="false()">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <pattern id="pattern7">
        <rule context="processing-instruction()" es:regex="e\.g\.">
            <assert test="false()" sqf:fix="standardFixes">Bad content: "<value-of select="."/>"</assert>
        </rule>
    </pattern>
    <sqf:fixes>
        <sqf:group id="standardFixes">
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete <value-of select="."/></sqf:title>
                </sqf:description>
                <sqf:delete/>
            </sqf:fix>
            <sqf:fix id="replace">
                <sqf:description>
                    <sqf:title>Replace <value-of select="."/> by REPLACE</sqf:title>
                </sqf:description>
                <sqf:replace match=".">REPLACE</sqf:replace>
            </sqf:fix>
            <sqf:fix id="replaceSpec">
                <sqf:description>
                    <sqf:title>Replace <value-of select="."/> by element.</sqf:title>
                </sqf:description>
                <sqf:replace match="." xmlns=""><e>REPLACE</e></sqf:replace>
            </sqf:fix>
            <sqf:fix id="addAfter">
                <sqf:description>
                    <sqf:title>Insert string after <value-of select="."/></sqf:title>
                </sqf:description>
                <sqf:add position="after" match=".">ADD</sqf:add>
            </sqf:fix>
            <sqf:fix id="addBefore">
                <sqf:description>
                    <sqf:title>Insert string before <value-of select="."/></sqf:title>
                </sqf:description>
                <sqf:add position="before" match=".">ADD</sqf:add>
            </sqf:fix>
            <sqf:fix id="addAfterSpec">
                <sqf:description>
                    <sqf:title>Insert element after <value-of select="."/></sqf:title>
                </sqf:description>
                <sqf:add position="after" match="." xmlns=""><e>ADD</e></sqf:add>
            </sqf:fix>
            <sqf:fix id="addBeforeSpec">
                <sqf:description>
                    <sqf:title>Insert element before <value-of select="."/></sqf:title>
                </sqf:description>
                <sqf:add position="before" match="." xmlns=""><e>ADD</e></sqf:add>
            </sqf:fix>
        </sqf:group>
    </sqf:fixes>
</schema>
