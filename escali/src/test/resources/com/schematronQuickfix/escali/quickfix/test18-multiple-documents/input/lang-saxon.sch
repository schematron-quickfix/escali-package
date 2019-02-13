<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <title>Two different language attributes.</title>
    <let name="langConfig" value="doc('config.xml')//lang"/>
    <ns uri="http://saxon.sf.net/" prefix="saxon"/>
    <include href="http://schematron-quickfix.com"/>
    <pattern>
        <rule context="@lang">
            <assert test="$langConfig/@code = ." sqf:fix="setLang addToConfig"><value-of select="saxon:line-number(..)"/>: The allowed lang values are <value-of select="string-join($langConfig/@code, ', ')"/></assert>
        </rule>
    </pattern>
    <sqf:fixes>
        <sqf:fix id="setLang" use-for-each="$langConfig">
            <sqf:description>
                <sqf:title>Set the lang to <value-of select="$sqf:current/@code"/></sqf:title>
            </sqf:description>
            <sqf:replace target="{name()}" node-type="attribute" select="$sqf:current/@code"/>
        </sqf:fix>
        <sqf:fix id="addToConfig">
            <sqf:description>
                <sqf:title>Register this language in the config</sqf:title>
            </sqf:description>
            <let name="langValue" value="."/>
            <sqf:add match="$langConfig/.." position="last-child">
                <lang code="{$langValue}" xmlns=""/>
            </sqf:add>
        </sqf:fix>
    </sqf:fixes>
</schema>
