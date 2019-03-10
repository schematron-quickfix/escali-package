<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <title>Test of mix of standalone and regular schematron</title>
    <ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <pattern>
        <rule context="element">
            <report test="lower-case(@name) = @name" sqf:fix="first_standalone_group">Name should contain at least one upper case character.</report>
            <sqf:group id="first_standalone_group">
                <sqf:fix id="first_standalone">
                    <sqf:description>
                        <sqf:title>This is my first standalone QuickFix in a group</sqf:title>
                    </sqf:description>
                    <sqf:add match="." target="id" node-type="attribute" select="concat(upper-case(@name), '_', count(preceding::*) + 1)"/>
                </sqf:fix>
                <sqf:fix id="second_standalone">
                    <sqf:description>
                        <sqf:title>This is my second standalone QuickFix in a group</sqf:title>
                    </sqf:description>
                    <sqf:add match="." target="id" node-type="attribute" select="concat(lower-case(@name), '_', count(preceding::*))"/>
                </sqf:fix>
                
            </sqf:group>
        </rule>
    </pattern>
    <sqf:fixes>
        <sqf:group id="first_standalone_group">
            <sqf:fix id="first_standalone">
                <sqf:description>
                    <sqf:title>This is my first standalone QuickFix in a group</sqf:title>
                </sqf:description>
                <sqf:add match="." target="id" node-type="attribute" select="concat(upper-case(@name), '_', count(preceding::*) + 1)"/>
            </sqf:fix>
            <sqf:fix id="second_standalone">
                <sqf:description>
                    <sqf:title>This is my second standalone QuickFix in a group</sqf:title>
                </sqf:description>
                <sqf:add match="." target="id" node-type="attribute" select="concat(lower-case(@name), '_', count(preceding::*))"/>
            </sqf:fix>
            
        </sqf:group>
    </sqf:fixes>
</schema>