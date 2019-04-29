<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:est="escali.schematron-quickfix.com/test">
    <sch:title>Schematron unit test - action order</sch:title>
    
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:report test="contains(., 'foo')" sqf:fix="replaceFoo replaceFooBar replaceFooBarConflict">Foo should not be empty.</sch:report>
            <sqf:fix id="replaceFoo">
                <sqf:description>
                    <sqf:title>Replace foo by baz.</sqf:title>
                </sqf:description>
                <sqf:stringReplace match="text()" regex="foo" select="'baz'"/>
            </sqf:fix>
            <sqf:fix id="replaceFooBar">
                <sqf:description>
                    <sqf:title>Replace foo and _bar_ by baz or #bazzer#.</sqf:title>
                </sqf:description>
                <sqf:stringReplace match="text()" regex="foo" select="'baz'"/>
                <sqf:stringReplace match="text()" regex="_bar_?" select="'#bazzer#'"/>
            </sqf:fix>
            <sqf:fix id="replaceFooBarConflict">
                <sqf:description>
                    <sqf:title>Replace foo_ and bar by baz/ or #bazzer#.</sqf:title>
                </sqf:description>
                <sqf:stringReplace match="text()" regex="foo_" select="'baz/'"/>
                <sqf:stringReplace match="text()" regex="_bar_?" select="'#bazzer#'"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>