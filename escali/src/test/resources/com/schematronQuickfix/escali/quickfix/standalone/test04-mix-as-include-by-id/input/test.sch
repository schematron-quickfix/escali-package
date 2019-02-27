<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <title>Test of mix of standalone as include by id and regular schematron</title>
    <ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <pattern>
        <rule context="element">
            <report test="lower-case(@name) = @name" sqf:fix="uppercase_first">Name should contain at least one upper case character.</report>
        </rule>
    </pattern>
    <sqf:fixes>
        <sqf:fix id="uppercase_first">
            <sqf:description>
                <sqf:title>Upper case the first character</sqf:title>
            </sqf:description>
            <let name="first" value="upper-case(substring(@name, 1, 1))"/>
            <let name="rest" value="substring(@name, 2)"/>
            <sqf:replace match="@name" target="name" node-type="attribute" select="concat($first, $rest)"/>
        </sqf:fix>
    </sqf:fixes>
    <include href="include.sch#fixes"/>
</schema>