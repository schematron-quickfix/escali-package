<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    
    
    <sch:pattern is-a="type">
        <sch:param name="type" value="double"/>
        <sch:param name="default" value="-1.0"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="date"/>
        <sch:param name="default" value="xs:date('2017-10-02')"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="time"/>
        <sch:param name="default" value="current-time()"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="boolean"/>
        <sch:param name="default" value="false()"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="string"/>
        <sch:param name="default" value="'#null#'"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="integer"/>
        <sch:param name="default" value="-1"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="int"/>
        <sch:param name="default" value="-1"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="unsignedInt"/>
        <sch:param name="default" value="-1"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="unsignedShort"/>
        <sch:param name="default" value="-1"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="long"/>
        <sch:param name="default" value="-1.0"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="short"/>
        <sch:param name="default" value="-1"/>
    </sch:pattern>
    <sch:pattern is-a="type">
        <sch:param name="type" value="decimal"/>
        <sch:param name="default" value="-1.0"/>
    </sch:pattern>
    
    <sch:pattern abstract="true" id="type">
        <sch:rule context="$type">
            <sch:assert test=". castable as xs:$type and . != ''" sqf:fix="fix_type fix_type_default">Type Error! (<sch:name/>)</sch:assert>

            <sqf:fix id="fix_type">
                <sqf:param name="type" abstract="true"/>
                <sqf:description>
                    <sqf:title>Set <sch:name/></sqf:title>
                </sqf:description>
                <sqf:user-entry name="value" type="xs:$type">
                    <sqf:description>
                        <sqf:title>Enter the <sch:name/> value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add select="$value"/>
            </sqf:fix>
            
            <sqf:fix id="fix_type_default">
                <sqf:param name="type" abstract="true"/>
                <sqf:param name="default" abstract="true"/>
                <sqf:description>
                    <sqf:title>Set <sch:name/> (with default)</sqf:title>
                </sqf:description>
                <sqf:user-entry name="value" type="xs:$type" default="$default">
                    <sqf:description>
                        <sqf:title>Enter the <sch:name/> value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add select="$value"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern>
        <sch:rule context="enum">
            <sch:let name="values" value="('value1', 'value2', 'value3')"/>
            <sch:assert test=". = $values" sqf:fix="fix_enum">Type Error! (<sch:name/>)</sch:assert>
            <sqf:fix id="fix_enum">
                <sqf:description>
                    <sqf:title>Set <sch:name/></sqf:title>
                </sqf:description>
                <sqf:user-entry name="value" type="xs:string" default="$values">
                    <sqf:description>
                        <sqf:title>Enter the <sch:name/> value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add select="$value"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern>
        <sch:rule context="color">
            <sch:assert test="false()" sqf:fix="fix_enum">Type Error! (<sch:name/>)</sch:assert>
            <sqf:fix id="fix_enum">
                <sqf:description>
                    <sqf:title>Set <sch:name/></sqf:title>
                </sqf:description>
                <sqf:user-entry name="value" type="sqf:color">
                    <sqf:description>
                        <sqf:title>Enter the <sch:name/> value</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add select="$value"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
