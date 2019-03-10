<sqf:fixes xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" es:queryBinding="xslt2">
    <sqf:group es:context="element" id="first_standalone_group">
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