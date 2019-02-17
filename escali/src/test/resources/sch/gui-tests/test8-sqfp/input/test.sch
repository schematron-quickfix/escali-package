<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xml:lang="en">
    
    
    
    <sch:pattern>
        <sch:rule context="error">
            <sch:assert test="false()" sqf:fix="fix_wd global" diagnostics="error1">Error 1</sch:assert>
            <sqf:fix id="fix_wd">
                <sqf:description>
                    <sqf:title ref="fix_wd_title_de">Local fix</sqf:title>
                    <sqf:p ref="fix_wd_p1_de">This is a local fix.</sqf:p>
                    <sqf:p ref="fix_p2_de">Use this to fix the error.</sqf:p>
                </sqf:description>
                <sqf:user-entry name="userEntry">
                    <sqf:description>
                        <sqf:title>UserEntry</sqf:title>
                        <sqf:p>This is a user entry.</sqf:p>
                        <sqf:p>Specify a value for it.</sqf:p>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add target="a" node-type="attribute" select="$userEntry"/>
            </sqf:fix>
        </sch:rule>
        
        <sch:rule context="error2">
            <sch:assert test="false()" sqf:fix="global" diagnostics="error2">Error 2</sch:assert>
        </sch:rule>
    </sch:pattern>
    
    <sqf:fixes>
        <sqf:fix id="global">
            <sqf:description>
                <sqf:title ref="global_title_de">Global fix</sqf:title>
                <sqf:p ref="global_p1_de">This is a global fix.</sqf:p>
                <sqf:p ref="fix_p2_de">Use this to fix the error.</sqf:p>
            </sqf:description>
            <sqf:add target="e" node-type="element"/>
        </sqf:fix>
    </sqf:fixes>
    
    <sch:diagnostics>
        <sch:diagnostic id="error1" xml:lang="de">Fehler 1</sch:diagnostic>
        <sch:diagnostic id="error2" xml:lang="de">Fehler 2</sch:diagnostic>
        <sch:diagnostic id="fix_wd_title_de" xml:lang="de">Lokaler QuickFix</sch:diagnostic>
        <sch:diagnostic id="fix_wd_p1_de" xml:lang="de">Das ist ein lokaler QuickFix.</sch:diagnostic>
        <sch:diagnostic id="global_title_de" xml:lang="de">Globaler QuickFix</sch:diagnostic>
        <sch:diagnostic id="global_p1_de" xml:lang="de">Das ist ein globaler QuickFix.</sch:diagnostic>
        <sch:diagnostic id="fix_p2_de" xml:lang="de">Verwenden Sie ihn um den Fehler zu beheben.</sch:diagnostic>
    </sch:diagnostics>
    
</sch:schema>
