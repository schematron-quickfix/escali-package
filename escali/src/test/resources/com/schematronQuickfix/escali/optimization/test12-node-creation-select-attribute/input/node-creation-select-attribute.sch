<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - node creation select attribute</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test="normalize-space(.) != ''" sqf:fix="replaceFoo replaceByAtomic replaceByAtomicSeq">Foo should not be empty.</sch:assert>
            <sqf:fix id="replaceFoo">
                <sqf:description>
                    <sqf:title>Replace <sch:name/> by bar</sqf:title>
                </sqf:description>
                <sqf:replace select="bar"/>
            </sqf:fix>
            <sqf:fix id="replaceByAtomic">
                <sqf:description>
                    <sqf:title>Replace by atomic value</sqf:title>
                </sqf:description>
                <sqf:replace match="bar" select="1"/>
            </sqf:fix>
            <sqf:fix id="replaceByAtomicSeq">
                <sqf:description>
                    <sqf:title>Replace by sequence of atomic values</sqf:title>
                </sqf:description>
                <sqf:replace match="bar" select="1 to 10"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>