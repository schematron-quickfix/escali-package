<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - use-for-each with call-fix</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test=". = ''" sqf:fix="useFoo">Foo should not be empty.</sch:report>
            
            <sqf:fix id="useFoo" use-for-each="following-sibling::foo">
                <sqf:description>
                    <sqf:title>Add bar with <sch:value-of select="$sqf:current"/>.</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="abstract">
                    <sqf:with-param name="target" select="'bar'"/>
                    <sqf:with-param name="current" select="$sqf:current"/>
                </sqf:call-fix>
            </sqf:fix>
            
        </sch:rule>
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="abstract">
            <sqf:param name="target"/>
            <sqf:param name="current"/>
            <sqf:add node-type="element" target="{$target}" select="string($current)"/>
        </sqf:fix>
    </sqf:fixes>
    
</sch:schema>