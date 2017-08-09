<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:phase id="phase1">
        <sch:active pattern="pattern1"/>
    </sch:phase>
    <sch:phase id="phase2">
        <sch:active pattern="pattern2"/>
    </sch:phase>
    <sch:phase id="phase3">
        <sch:active pattern="pattern3"/>
    </sch:phase>
    <sch:phase id="phase4">
        <sch:active pattern="pattern4"/>
    </sch:phase>
    <sch:pattern id="pattern1">
        <sch:title>call fix without description</sch:title>
        <sch:rule context="element">
            <sch:report test="@attribute = 'value'" role="warning" sqf:fix="fix1">Report message</sch:report>
            <sqf:fix id="fix1">
                <sqf:call-fix ref="fix2"/>
            </sqf:fix>
            <sqf:fix id="fix2">
                <sqf:description>
                    <sqf:title>Fix 2</sqf:title>
                </sqf:description>
                <sqf:delete/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:pattern id="pattern2">
        <sch:title>call fix overwrite description</sch:title>
        <sch:rule context="element">
            <sch:report test="@attribute = 'value'" role="warning" sqf:fix="fix1">Report message</sch:report>
            <sqf:fix id="fix1">
                <sqf:description>
                    <sqf:title>Fix 1</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="fix2"/>
            </sqf:fix>
            <sqf:fix id="fix2">
                <sqf:description>
                    <sqf:title>Fix 2</sqf:title>
                </sqf:description>
                <sqf:delete/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:pattern id="pattern3">
        <sch:title>call fix parametrized description</sch:title>
        <sch:rule context="element">
            <sch:report test="@attribute = 'value'" role="warning" sqf:fix="fix1 fix2">Report message</sch:report>
            <sqf:fix id="fix1">
                <sqf:call-fix ref="fix2">
                    <sqf:with-param name="count" select="1"/>
                </sqf:call-fix>
            </sqf:fix>
            <sqf:fix id="fix2">
                <sqf:param name="count" default="2" type="xs:integer"/>
                <sqf:description>
                    <sqf:title>Fix <sch:value-of select="$count"/></sqf:title>
                </sqf:description>
                <sqf:delete/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:pattern id="pattern4">
        <sch:title>multiple call-fix</sch:title>
        <sch:rule context="element">
            <sch:report test="@attribute = 'value'" role="warning" sqf:fix="fix1">Report message</sch:report>
            <sqf:fix id="fix1">
                <sqf:description>
                    <sqf:title>Fix 1</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="fix2"/>
                <sqf:call-fix ref="fix3"/>
            </sqf:fix>
            <sqf:fix id="fix2">
                <sqf:description>
                    <sqf:title>Fix 2</sqf:title>
                </sqf:description>
                <sqf:delete/>
            </sqf:fix>
            <sqf:fix id="fix3">
                <sqf:description>
                    <sqf:title>Fix 3</sqf:title>
                </sqf:description>
                <sqf:delete/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
</sch:schema>
