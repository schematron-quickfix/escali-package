<?xml version="1.0" encoding="UTF-8"?>
<sqf:fixes xmlns:sqf="http://www.schematron-quickfix.com/validator/process">
    <sqf:fix id="replaceBar" xml:id="replaceBar">
        <sqf:description>
            <sqf:title>Replace bar.</sqf:title>
        </sqf:description>
        <sqf:replace match="est:foo(.)" node-type="element" target="baz" select="'baz'"/>
    </sqf:fix>    
</sqf:fixes>