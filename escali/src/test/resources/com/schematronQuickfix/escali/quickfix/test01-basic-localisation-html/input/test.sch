







<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <title>Two different language attributes.</title>
    <ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <pattern>
        <rule context="html:html">
            <assert test="@lang" sqf:fix="lang lang_xml_lang">The attribute "lang" is missing.</assert>
            <assert test="@xml:lang" sqf:fix="xml_lang lang_xml_lang">The attribute "xml:lang" is missing.</assert>
            <assert test="@lang = @xml:lang" sqf:fix="lang xml_lang"> The attributes "lang" and "xml:lang" should have the same value.</assert>
        </rule>
        <rule context="*[@xml:lang and @lang]">
            <assert test="@lang = @xml:lang" sqf:fix="lang xml_lang lang_xml_lang"
                > The attributes "lang" and "xml:lang" should have the same value.</assert>
        </rule>
        <rule context="*[@lang]">
            <assert test="@xml:lang" sqf:fix="xml_lang"
                >The attribute "xml:lang" is missing.</assert>
        </rule>
        <rule context="*[@xml:lang]">
            <assert test="@lang" sqf:fix="lang">The attribute "lang" is missing.</assert>
        </rule>
    </pattern>
    <sqf:fixes>
        <sqf:fix id="lang" use-when="@xml:lang">
            <sqf:description>
                <sqf:title>Adds a "lang" attribute with the value "<value-of select="@xml:lang"/>".</sqf:title>
            </sqf:description>
            <sqf:add match="." target="lang" node-type="attribute">
                <value-of select="@xml:lang"/>
            </sqf:add>
        </sqf:fix>
        <sqf:fix id="xml_lang" use-when="@lang">
            <sqf:description>
                <sqf:title>Adds a "xml:lang" attribute with the value "<value-of select="@lang"/>".</sqf:title>
            </sqf:description>
            <sqf:add match="." target="xml:lang" node-type="attribute">
                <value-of select="@lang"/>
            </sqf:add>
        </sqf:fix>
        <sqf:fix id="lang_xml_lang">
            <sqf:description>
                <sqf:title>Adds a "xml:lang" and a "lang" attribute to the node.</sqf:title>
            </sqf:description>
            <sqf:user-entry name="lang" type="xs:string" default="'en'">
                <sqf:description>
                    <sqf:title>Enter the correct language code.</sqf:title>
                </sqf:description>
            </sqf:user-entry>
            <sqf:add match="." target="xml:lang" node-type="attribute"><value-of select="$lang"/></sqf:add>
            <sqf:add match="." target="lang" node-type="attribute"><value-of select="$lang"/></sqf:add>
        </sqf:fix>
    </sqf:fixes>
</schema>