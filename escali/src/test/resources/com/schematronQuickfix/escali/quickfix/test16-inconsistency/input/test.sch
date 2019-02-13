<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sch:title>Two different language attributes.</sch:title>
    <sch:pattern>
        <sch:rule context="processing-instruction()">
            <sch:report test="name() = 'pi'" sqf:fix="addLastChild addFirstChild addAfter addBefore addAttr">PI error</sch:report>
        </sch:rule>
        <sch:rule context="e/@attribute">
            <sch:report test="true()" sqf:fix="addLastChild addFirstChild addAfter addBefore addAttr">Attribute test</sch:report>
        </sch:rule>
        <sch:rule context="comment()">
            <sch:report test="true()" sqf:fix="addLastChild addFirstChild addAfter addBefore addAttr">Comment test</sch:report>
        </sch:rule>
    </sch:pattern>
    <sqf:fixes>
        <sqf:fix id="addLastChild">
            <sqf:description>
                <sqf:title>Add last-child</sqf:title>
            </sqf:description>
            <sqf:add target="e" node-type="element" position="last-child"/>
        </sqf:fix>
        <sqf:fix id="addFirstChild">
            <sqf:description>
                <sqf:title>Add first-child</sqf:title>
            </sqf:description>
            <sqf:add target="e" node-type="element" position="first-child"/>
        </sqf:fix>
        <sqf:fix id="addBefore">
            <sqf:description>
                <sqf:title>Add before</sqf:title>
            </sqf:description>
            <sqf:add target="e" node-type="element" position="before"/>
        </sqf:fix>
        <sqf:fix id="addAfter">
            <sqf:description>
                <sqf:title>Add after</sqf:title>
            </sqf:description>
            <sqf:add target="e" node-type="element" position="after"/>
        </sqf:fix>
        <sqf:fix id="addAttr">
            <sqf:description>
                <sqf:title>Add attribute</sqf:title>
            </sqf:description>
            <sqf:add target="a" node-type="attribute"/>
        </sqf:fix>
    </sqf:fixes>
</sch:schema>
