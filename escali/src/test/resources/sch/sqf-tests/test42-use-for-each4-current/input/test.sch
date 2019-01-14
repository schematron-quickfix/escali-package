<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
 <sch:pattern>
     <sch:rule context="foo">
         <sch:assert test="@ref" sqf:fix="use_for_each">
            Foo needs ref to bar.
         </sch:assert>
         
         <sqf:fix id="use_for_each" use-for-each="//bar[contains(@id, current()/@group)]">
             <sch:let name="bar" value="$sqf:current"/>
             <sch:let name="countGroup" value="count(//bar[contains(@id, current()/@group)])"/>
             <sqf:description>
                 <sqf:title>Add ref to "<sch:value-of select="$bar/@id"/>"</sqf:title>
             </sqf:description>
             <sqf:add node-type="attribute" target="ref">
                 <sch:value-of select="concat($bar/@id, '_', $countGroup)"/>
             </sqf:add>
         </sqf:fix>
     </sch:rule>
 </sch:pattern>   
</sch:schema>