<?xml version="1.0" encoding="UTF-8"?>
<sqf:fixes xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <sqf:fix es:context="element" id="mixed_standalone">
        <sqf:description>
            <sqf:title>This is my first standalone QuickFix</sqf:title>
        </sqf:description>
        <sqf:add match="." target="id" node-type="attribute" select="concat(upper-case(@name), '_', count(preceding::*) + 1)"/>
    </sqf:fix>
</sqf:fixes>
