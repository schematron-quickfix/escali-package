<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - basic replace</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:let name="var" value="'v','a','l'"/>
            <sch:let name="var" value="string-join($var, '')"/>
            <sqf:fix id="fix_3" use-when="$var = 'val'">
                <sqf:description>
                    <sqf:title><sch:value-of select="concat('$var = ', $var)"/>.</sqf:title>
                </sqf:description>
                <sqf:add node-type="attribute" target="var" select="$var"/>
            </sqf:fix>
            <sch:report test="$var = 'val'" sqf:fix="fix_1 fix_2 fix_3"><sch:value-of select="concat('$var = ', $var)"/>.</sch:report>
            <sch:let name="var" value="concat($var, '_1')"/>
            <sqf:fix id="fix_1" use-when="$var = 'val_1'">
                <sqf:description>
                    <sqf:title><sch:value-of select="concat('$var = ', $var)"/>.</sqf:title>
                </sqf:description>
                <sqf:add node-type="attribute" target="var" select="$var"/>
            </sqf:fix>
            <sch:let name="var" value="replace($var, '_\d', '_2')"/>
            <sqf:fix id="fix_2" use-when="$var = 'val_2'">
                <sqf:description>
                    <sqf:title><sch:value-of select="concat('$var = ', $var)"/>.</sqf:title>
                </sqf:description>
                <sqf:add node-type="attribute" target="var" select="$var"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>