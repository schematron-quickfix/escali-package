<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    
    
    
    <sch:pattern>
        <sch:rule context="error">
            <sch:assert test="false()" sqf:fix="fix_wd">Type Error!</sch:assert>

            <sqf:fix id="fix_wd">
                <sqf:description>
                    <sqf:title>Add type structure</sqf:title>
                </sqf:description>
                <sqf:user-entry name="double" type="xs:double">
                    <sqf:description>
                        <sqf:title>Enter the double value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:user-entry name="time" type="xs:time">
                    <sqf:description>
                        <sqf:title>Enter the time value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:user-entry name="date" type="xs:date">
                    <sqf:description>
                        <sqf:title>Enter the date value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:user-entry name="boolean" type="xs:boolean">
                    <sqf:description>
                        <sqf:title>Enter the boolean value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:user-entry name="integer" type="xs:integer">
                    <sqf:description>
                        <sqf:title>Enter the integer value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:user-entry name="string" type="xs:string">
                    <sqf:description>
                        <sqf:title>Enter the string value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:user-entry name="enum" type="xs:string" default="('value1', 'value2', 'value3', 'value4')">
                    <sqf:description>
                        <sqf:title>Enter the enum value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add select="$double" target="double" node-type="element"/>
                <sqf:add select="$date" target="date" node-type="element"/>
                <sqf:add select="$time" target="time" node-type="element"/>
                <sqf:add select="$boolean" target="boolean" node-type="element"/>
                <sqf:add select="$integer" target="integer" node-type="element"/>
                <sqf:add select="$string" target="string" node-type="element"/>
                <sqf:add select="$enum" target="enum" node-type="element"/>
            </sqf:fix>
            
        </sch:rule>
    </sch:pattern>
    
</sch:schema>
