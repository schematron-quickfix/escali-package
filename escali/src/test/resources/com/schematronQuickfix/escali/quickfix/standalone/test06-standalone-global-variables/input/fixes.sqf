<sqf:fixes xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" es:queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sch="http://purl.oclc.org/dsdl/schematron">
    <xsl:variable name="rootXslt" select="/node()"/>
    <sch:let name="elCountSch" value="count(//*)"/>
    <sqf:fix es:context="element" id="first_standalone">
        <sqf:description>
            <sqf:title>This is my first standalone QuickFix in a group</sqf:title>
        </sqf:description>
        <sqf:add match="." target="id" node-type="attribute" select="concat(upper-case(@name), '_', count((preceding::node() except $rootXslt)) + 1, '_', $elCountSch)"/>
    </sqf:fix>

</sqf:fixes>
