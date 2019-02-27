<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <title>Included QuickFixes</title>
    <sqf:fixes id="fixes">
        <sqf:fix es:context="element" id="mixed_standalone">
            <sqf:description>
                <sqf:title>This is my first standalone QuickFix</sqf:title>
            </sqf:description>
            <sqf:add match="." target="id" node-type="attribute" select="concat(upper-case(@name), '_', count(preceding::*) + 1)"/>
        </sqf:fix>
    </sqf:fixes>
</schema>