<sqf:fixes xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" es:queryBinding="xslt2">
    <sqf:fix es:context="element" id="first_standalone">
        <sqf:description>
            <sqf:title>This is my first standalone QuickFix</sqf:title>
        </sqf:description>
        <sqf:add match="." target="id" node-type="attribute" select="concat(upper-case(@name), '_', count(preceding::*) + 1)"/>
    </sqf:fix>
</sqf:fixes>