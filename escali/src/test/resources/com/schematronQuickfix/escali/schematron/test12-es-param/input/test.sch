<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2" xml:lang="de">
    <title>Schematron unit test</title>
    
    <es:param name="config">config.xml</es:param>
    
    <let name="configDoc" value="doc(resolve-uri($config))"/>
    <pattern id="pattern1">
        <rule context="pattern1">
            <assert test="$configDoc/config/test='no'">The config says 'yes'!</assert>
        </rule>
    </pattern>
    <pattern id="pattern2">
        <rule context="pattern2">
            <assert test="$configDoc/config/test='yes'">The config says 'no'!</assert>
        </rule>
    </pattern>
</schema>