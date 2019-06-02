<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - external document in match by doc function</sch:title>

    <sch:ns uri="escali.schematron-quickfix.com/test" prefix="est"/>
    
    <sch:include href="lib.xsl#est.foo"/>
    
    <sch:include href="foo-pattern.sch#foo.pattern"/>

</sch:schema>
