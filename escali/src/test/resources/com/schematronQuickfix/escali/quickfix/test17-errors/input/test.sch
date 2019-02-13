<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <title>Two different language attributes.</title>
    <pattern>
<!--        <rule context="processing-instruction()">
            <report test="name() = 'pi'">PI error</report>
        </rule>-->
        <rule context="html:p[@lang]">
            <report test="true()" sqf:fix="replaceAtt">Attribute test</report>
        </rule>
    </pattern>
    <sqf:fixes>
        <sqf:fix id="replaceAtt">
            <sqf:description>
                <sqf:title>Replace node by an attribute</sqf:title>
            </sqf:description>
            <sqf:replace target="lang" node-type="attribute">test</sqf:replace>
        </sqf:fix>
    </sqf:fixes>
</schema>
