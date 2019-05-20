<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - bug fix - call-fix pending with let</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="addChild">Foo should not be empty.</sch:assert>
            <sqf:fix id="addChild">
                <sqf:description>
                    <sqf:title>Add child bar to foo and root.</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="abstract"/>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sqf:fixes>
        <sqf:fix id="abstract">
            <sch:let name="target" value="'bar'"/>
            <sqf:call-fix ref="very_abstract">
                <sqf:with-param name="target" select="$target"/>
                <sqf:with-param name="match" select="."/>
            </sqf:call-fix>
            
            <sqf:call-fix ref="very_abstract">
                <sqf:with-param name="target" select="$target"/>
            </sqf:call-fix>
        </sqf:fix>

        <sqf:fix id="very_abstract">
            <sqf:param name="target"/>
            <sqf:param name="match" default="()"/>
            <sch:let name="match" value="
                    if (not($match)) then
                        (/*)
                    else
                        $match"/>
            <sqf:add match="$match" position="first-child" node-type="element" target="{$target}"/>
        </sqf:fix>


    </sqf:fixes>
</sch:schema>
