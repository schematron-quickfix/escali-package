<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <title>Two different language attributes.</title>
    <ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <pattern>
        <rule context="html:html">
            <assert test="@lang" sqf:fix="lang_xml_lang lang_xml_lang_2">The attribute "lang" is missing.</assert>
            <assert test="@xml:lang" sqf:fix="lang_xml_lang lang_xml_lang_2">The attribute "xml:lang" is missing.</assert>
        </rule>
    </pattern>
    <sqf:fixes>
        <sqf:fix id="lang_xml_lang">
            <sqf:description>
                <sqf:title>Adds a "xml:lang" and a "lang" attribute to the node.</sqf:title>
            </sqf:description>
            <sqf:user-entry name="xml_lang" default="true()" type="xs:boolean">
                <sqf:description>
                    <sqf:title>Create xml:lang</sqf:title>
                </sqf:description>
            </sqf:user-entry>
            <sqf:user-entry name="lang" default="('en', 'de', 'fr', 'it')">
                <sqf:description>
                    <sqf:title>Enter the correct language code.</sqf:title>
                </sqf:description>
                <!--<xs:simpleType>
                    <xs:restriction base="xs:string">
                        <xs:enumeration value="de"/>
                        <xs:enumeration value="en"/>
                        <xs:enumeration value="fr"/>
                        <xs:enumeration value="it"/>
                    </xs:restriction>
                </xs:simpleType>-->
            </sqf:user-entry>
            <sqf:add match="." target="xml:lang" node-type="attribute"><value-of select="$lang[$xml_lang]"/></sqf:add>
            <sqf:add match="." target="lang" node-type="attribute"><value-of select="$lang"/></sqf:add>
        </sqf:fix>
        
        <sqf:fix id="lang_xml_lang_2">
            <sqf:description>
                <sqf:title>Adds a "xml:lang" and a "lang" attribute to the node.</sqf:title>
            </sqf:description>
            <sqf:user-entry name="xml_lang" default="true()" type="xs:boolean">
                <sqf:description>
                    <sqf:title>Create xml:lang</sqf:title>
                </sqf:description>
            </sqf:user-entry>
            <sqf:user-entry name="lang" default="doc('config.xml')//langs/lang/@code">
                <sqf:description>
                    <sqf:title>Enter the correct language code.</sqf:title>
                </sqf:description>
                <!--<xs:simpleType>
                    <xs:restriction base="xs:string">
                        <xs:enumeration value="de"/>
                        <xs:enumeration value="en"/>
                        <xs:enumeration value="fr"/>
                        <xs:enumeration value="it"/>
                    </xs:restriction>
                </xs:simpleType>-->
            </sqf:user-entry>
            <sqf:add match="." target="xml:lang" node-type="attribute"><value-of select="$lang[$xml_lang]"/></sqf:add>
            <sqf:add match="." target="lang" node-type="attribute"><value-of select="$lang"/></sqf:add>
        </sqf:fix>
        
    </sqf:fixes>
</schema>
