<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2" xmlns:html="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
    <sch:ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <sch:pattern>
        <sch:rule context="html:head/*">
            <sch:let name="titleText" value="string-join(.//text(),'')"/>
            <sch:assert test="namespace-uri() = 'http://www.w3.org/1999/xhtml'" sqf:fix="nsConvertion nsConvertionPrx" sqf:default-fix="nsConvertion">Bad namespace!</sch:assert>
            <sqf:fix id="nsConvertionPrx">
                <sqf:description>
                    <sqf:title>Convert to html element (with prefix).</sqf:title>
                </sqf:description>
                <sqf:replace target="html:{local-name()}" node-type="keep"/>
            </sqf:fix>
            <sqf:fix id="nsConvertion">
                <sqf:description>
                    <sqf:title>Convert to html element (without prefix).</sqf:title>
                </sqf:description>
                <sqf:replace target="{local-name()}" node-type="keep"/>
            </sqf:fix>
        </sch:rule>
        <sch:rule context="html:extern/*">
            <sch:report test="namespace-uri() = 'http://www.w3.org/1999/xhtml'" sqf:fix="nsConvertionNull">Bad namespace!</sch:report>
            <sqf:fix id="nsConvertionNull">
                <sqf:description>
                    <sqf:title>Convert to null namespace element.</sqf:title>
                </sqf:description>
                <sqf:replace target="{local-name()}" node-type="element" xmlns=""/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
