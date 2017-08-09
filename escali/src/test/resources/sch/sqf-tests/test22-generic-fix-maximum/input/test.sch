<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:pattern id="pattern1">
        <sch:title>use-for-each</sch:title>
        <sch:rule context="element">
            <sch:report test="@attribute='value'" sqf:fix="genFix100 genFix20 genFix51">Bad value</sch:report>
            <sqf:fix id="genFix100" use-for-each="1 to 100">
                <sqf:description>
                    <sqf:title>GenFix 100 - Fix <sch:value-of select="$sqf:current"/></sqf:title>
                </sqf:description>
                <sqf:replace match="@attribute" node-type="attribute" target="attribute100"><sch:value-of select="$sqf:current"/></sqf:replace>
            </sqf:fix>
            <sqf:fix id="genFix20" use-for-each="1 to 20">
                <sqf:description>
                    <sqf:title>GenFix 20 - Fix <sch:value-of select="$sqf:current"/></sqf:title>
                </sqf:description>
                <sqf:replace match="@attribute" node-type="attribute" target="attribute20"><sch:value-of select="$sqf:current"/></sqf:replace>
            </sqf:fix>
            <sqf:fix id="genFix51" use-for-each="1 to 51" es:maximum="51">
                <sqf:description>
                    <sqf:title>GenFix 51 - Fix <sch:value-of select="$sqf:current"/></sqf:title>
                </sqf:description>
                <sqf:replace match="@attribute" node-type="attribute" target="attribute51"><sch:value-of select="$sqf:current"/></sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>    
</sch:schema>
