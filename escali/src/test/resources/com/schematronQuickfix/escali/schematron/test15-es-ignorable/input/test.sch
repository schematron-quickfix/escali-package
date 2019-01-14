<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:phase id="phase1">
        <sch:active pattern="pattern1"/>
    </sch:phase>    
    <sch:pattern id="pattern1">
        <sch:title>ignore element assert</sch:title>
        <sch:rule context="element">
            <sch:report test="@attribute='value'" role="warning" es:ignorableId="attrValue">Bad value</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:phase id="phase2">
        <sch:active pattern="pattern2"/>
    </sch:phase>    
    <sch:pattern id="pattern2">
        <sch:title>ignore attribute assert</sch:title>
        <sch:rule context="@attribute">
            <sch:report test=".='value'" role="warning" es:ignorableId="attrValue">Bad value</sch:report>
        </sch:rule>
    </sch:pattern>
    <sch:phase id="phase3">
        <sch:active pattern="pattern3"></sch:active>
    </sch:phase>
    <sch:pattern id="pattern3">
        <sch:rule context="element">
            <sch:report test=". = 'Text to match something.'" es:ignorableId="badContent">Bad content</sch:report>
            <sch:report test="@attribute = 'xyz'" es:ignorableId="attrValue2">Bad Value</sch:report>
        </sch:rule>
    </sch:pattern>
</sch:schema>
