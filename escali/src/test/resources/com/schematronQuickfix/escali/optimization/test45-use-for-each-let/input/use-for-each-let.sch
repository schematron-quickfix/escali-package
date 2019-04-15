<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - use-for-each with lets</sch:title>
    <sch:let name="allFoos" value="//foo"/>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:let name="foo" value="."/>
            <sch:report test=". = ''" sqf:fix="useFoo">Foo should not be empty.</sch:report>
            
            <sqf:fix id="useFoo" use-for-each="following-sibling::foo">
                <sch:let name="fooBetween" value="count($allFoos[. >> $foo][. &lt;&lt; $sqf:current]) + 1"/>
                <sqf:description>
                    <sqf:title>Add bar with <sch:value-of select="$sqf:current, '+', $fooBetween"/>.</sqf:title>
                </sqf:description>
                <sqf:add node-type="element" target="bar" select="concat(string($sqf:current), ' + ', $fooBetween)"/>
            </sqf:fix>
            
        </sch:rule>
    </sch:pattern>
    
</sch:schema>