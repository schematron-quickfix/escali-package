<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <sch:pattern>
        <sch:rule context="element">
            <sch:report test="@attribute='value'" sqf:fix="fix deleteOther">The attribute should not have the value "value".</sch:report>

            <sqf:fix id="fix">
                <sqf:description>
                    <sqf:title>Replaces the value "value" to "new value"</sqf:title>
                </sqf:description>
                <sqf:replace match="@attribute" target="{local-name()}" node-type="attribute">new value</sqf:replace>
            </sqf:fix>
            <sqf:fix id="deleteOther">
                <sqf:description>
                    <sqf:title>Delete other elements</sqf:title>
                </sqf:description>
                <sqf:delete match="()"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:pattern>
        <sch:rule context="element">
            <sch:report test="matches(., 'to\s+match')" sqf:fix="delete replace delete2 delete3">Do not use the phrase to match!</sch:report>
            <sqf:fix id="replace">
                <sqf:description>
                    <sqf:title>Replace to "not match"</sqf:title>
                </sqf:description>
                <sqf:stringReplace match=".//text()" regex="to(\s+)match" select="concat('not', regex-group(1), 'match')"/>
            </sqf:fix>
            <sqf:fix id="delete">
                <sqf:description>
                    <sqf:title>Delete "to"</sqf:title>
                </sqf:description>
                <sqf:stringReplace match=".//text()" regex="to(\s+)match" select="replace(., '^to\s+', '')"/>
            </sqf:fix>
            <sqf:fix id="delete2">
                <sqf:description>
                    <sqf:title>Delete first and second text node</sqf:title>
                </sqf:description>
                <sqf:delete match="text()[1]"/>
                <sqf:delete match="text()[2]"/>
            </sqf:fix>
            <sqf:fix id="delete3">
                <sqf:description>
                    <sqf:title>Replace and delete</sqf:title>
                </sqf:description>
                <sqf:replace match="text()[1]" target="pi" node-type="pi"/>
                <sqf:delete match="processing-instruction()/following-sibling::br"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
