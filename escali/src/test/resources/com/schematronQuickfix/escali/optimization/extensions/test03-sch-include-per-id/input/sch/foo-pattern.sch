<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern id="foo.pattern">
        <sch:rule context="foo">
            <sch:report test="bar" sqf:fix="replaceBar">Foo should not be empty.</sch:report>
            
            <sch:include href="quickfixes.sqf#replaceBar"/>
            
        </sch:rule>
    </sch:pattern>
</sch:schema>