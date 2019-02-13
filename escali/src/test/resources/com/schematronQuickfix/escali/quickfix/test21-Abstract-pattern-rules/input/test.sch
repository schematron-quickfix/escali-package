<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <sch:pattern is-a="abstractPattern">
        <sch:param name="element" value="element"/>
        <sch:param name="value" value="'value'"/>
    </sch:pattern>
    
    <sch:pattern abstract="true" id="abstractPattern">
        <sch:title>Abstract pattern</sch:title>
        <sch:rule context="$element">
            <sch:report test="@attribute=$value" sqf:fix="fix deleteOther">The attribute schould not have the value <sch:value-of select="$value"/>.</sch:report>
            <sqf:fix id="fix">
                <sqf:description>
                    <sqf:title>Replaces the value "value" to "new value"</sqf:title>
                </sqf:description>
                <sqf:replace match="@attribute" target="{local-name()}" node-type="attribute">new value</sqf:replace>
            </sqf:fix>
            <sqf:fix id="deleteOther">
                <sqf:description>
                    <sqf:title>Delete all <sch:value-of select="'$element'"/> elements</sqf:title>
                </sqf:description>
                <sqf:delete match="//$element"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern>
        <sch:title>Abstract rules</sch:title>
        <sch:rule context="element">
            <sch:report test="text()" sqf:fix="delete">text nodes are not allowed</sch:report>
            <sch:extends rule="abstractRule"/>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete all text nodes</sqf:title>
                </sqf:description>
                <sqf:delete match="text()"/>
            </sqf:fix>
        </sch:rule>
        <sch:rule abstract="true" id="abstractRule">
            <sch:report test="@*" sqf:fix="delete">attributes are not allowed</sch:report>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete all attributes</sqf:title>
                </sqf:description>
                <sqf:delete match="@*"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern>
        <sch:title>Abstract rules 2</sch:title>
        <sch:rule context="element">
            <sch:report test="text()" sqf:fix="abstractRule2.delete">text nodes are not allowed</sch:report>
            <sch:extends rule="abstractRule2"/>
            <sqf:fix id="abstractRule2.delete">
                <sqf:description>
                    <sqf:title>Delete all text nodes</sqf:title>
                </sqf:description>
                <sqf:delete match="text()"/>
            </sqf:fix>
        </sch:rule>
        <sch:rule abstract="true" id="abstractRule2">
            <sch:report test="@*" sqf:fix="delete">attributes are not allowed</sch:report>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete all attributes</sqf:title>
                </sqf:description>
                <sqf:delete match="@*"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    
    <sch:pattern>
        <sch:title>Abstract rules 3</sch:title>
        <sch:rule context="element" id="R1">
            <sch:report test="text()" sqf:fix="R2.F2 R2.R3.F3">F1 + F5</sch:report>
            <sch:extends rule="R2"/>
            <sch:extends rule="R3"/>
            <sqf:fix id="R2.F2">
                <sqf:description>
                    <sqf:title>F1</sqf:title>
                </sqf:description>
                <sqf:delete match="text()"/>
            </sqf:fix>
            <sqf:fix id="R2.R3.F3">
                <sqf:description>
                    <sqf:title>F5</sqf:title>
                </sqf:description>
                <sqf:delete match="text()"/>
            </sqf:fix>
        </sch:rule>
        <sch:rule abstract="true" id="R2">
            <sch:report test="@*" sqf:fix="F2 R3.F3">F2 + F4</sch:report>
            <sch:extends rule="R3"/>
            <sqf:fix id="F2">
                <sqf:description>
                    <sqf:title>F2</sqf:title>
                </sqf:description>
                <sqf:delete match="@*"/>
            </sqf:fix>
            <sqf:fix id="R3.F3">
                <sqf:description>
                    <sqf:title>F4</sqf:title>
                </sqf:description>
                <sqf:delete match="@*"/>
            </sqf:fix>
        </sch:rule>
        <sch:rule abstract="true" id="R3">
            <sch:report test="processing-instruction()" sqf:fix="F3">F3</sch:report>
            <sqf:fix id="F3">
                <sqf:description>
                    <sqf:title>F3</sqf:title>
                </sqf:description>
                <sqf:delete match="@*"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
