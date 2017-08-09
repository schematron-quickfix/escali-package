<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:ns1="namespace1" xmlns:ns2="namespace2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:pattern>
        <sch:rule context="*:section">
            <sch:report test="true()" sqf:fix="allNSFixes">Error in <sch:value-of select="@title"/>.</sch:report>
            <sqf:group id="allNSFixes">
                <sqf:fix id="addElementWithNs1">
                    <sqf:description>
                        <sqf:title>Add element with namespace ns1</sqf:title>
                    </sqf:description>
                    <sqf:add position="first-child" target="ns1:e" node-type="element"/>
                </sqf:fix>
                <sqf:fix id="addElementWithPrfxConflict">
                    <sqf:description>
                        <sqf:title>Add element with prefix ns1, but namespace2</sqf:title>
                    </sqf:description>
                    <sqf:add position="first-child" target="ns1:e" node-type="element" xmlns:ns1="namespace2"/>
                </sqf:fix>
                <sqf:fix id="addElementWithNs2">
                    <sqf:description>
                        <sqf:title>Add element with namespace ns2</sqf:title>
                    </sqf:description>
                    <sqf:add position="first-child" target="ns2:e" node-type="element"/>
                </sqf:fix>
                <sqf:fix id="addElementWithNullNs">
                    <sqf:description>
                        <sqf:title>Add element with null namespace</sqf:title>
                    </sqf:description>
                    <sqf:add position="first-child" target="e" node-type="element"/>
                </sqf:fix>
                <sqf:fix id="addElementWithNs2AsDef">
                    <sqf:description>
                        <sqf:title>Add element with namespace</sqf:title>
                    </sqf:description>
                    <sqf:add position="first-child" target="e" node-type="element" xmlns="namespace2"/>
                </sqf:fix>
                
                <sqf:fix id="addAttributeWithNs1">
                    <sqf:description>
                        <sqf:title>Add element with namespace ns1</sqf:title>
                    </sqf:description>
                    <sqf:add target="ns1:a" node-type="attribute"/>
                </sqf:fix>
                <sqf:fix id="addAttributeWithPrfxConflict">
                    <sqf:description>
                        <sqf:title>Add element with prefix ns1, but namespace2</sqf:title>
                    </sqf:description>
                    <sqf:add target="ns1:a" node-type="attribute" xmlns:ns1="namespace2"/>
                </sqf:fix>
                <sqf:fix id="addAttributeWithNs2">
                    <sqf:description>
                        <sqf:title>Add element with namespace ns2</sqf:title>
                    </sqf:description>
                    <sqf:add target="ns2:a" node-type="attribute"/>
                </sqf:fix>
                <sqf:fix id="addAttributeWithNullNs">
                    <sqf:description>
                        <sqf:title>Add element with null namespace</sqf:title>
                    </sqf:description>
                    <sqf:add target="a" node-type="attribute"/>
                </sqf:fix>
            </sqf:group>
        </sch:rule>
    </sch:pattern>
</sch:schema>
