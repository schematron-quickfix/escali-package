<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Schematron unit test - call-fix with abstract description</sch:title>
    <sch:pattern id="pattern1">
        <sch:rule context="foo">
            <sch:assert test=". != ''" sqf:fix="addChild addChilds">Foo should not be empty.</sch:assert>
            <sqf:fix id="addChild">
                <sqf:call-fix ref="abstract"/>
            </sqf:fix>
            <sqf:fix id="addChilds">
                <sqf:call-fix ref="very_abstract">
                    <sqf:with-param name="target1" select="'bar'"/>
                    <sqf:with-param name="target2" select="'baz'"/>
                </sqf:call-fix>
            </sqf:fix>

        </sch:rule>
    </sch:pattern>
    <sqf:fixes>
        <sqf:fix id="abstract">
            <sch:let name="target" value="'bar'"/>
            <sqf:call-fix ref="very_abstract">
                <sqf:with-param name="target1" select="$target"/>
            </sqf:call-fix>
        </sqf:fix>

        <sqf:fix id="very_abstract">
            <sqf:param name="target1"/>
            <sqf:param name="target2" default="''"/>
            <sch:let name="targets" value="
                    if ($target2 = '') then
                        $target1
                    else
                        concat($target1, ' and ', $target2)"/>
            <sqf:description>
                <sqf:title>Add child(s) <sch:value-of select="$targets"/> to <sch:name/>.</sqf:title>
            </sqf:description>
            <sqf:add position="first-child" node-type="element" target="{$target1}"/>
            <sqf:add match=".[$target2 != '']" position="last-child" node-type="element" target="{($target2, 'dummy')[. != ''][1]}"/>
        </sqf:fix>
    </sqf:fixes>
</sch:schema>
