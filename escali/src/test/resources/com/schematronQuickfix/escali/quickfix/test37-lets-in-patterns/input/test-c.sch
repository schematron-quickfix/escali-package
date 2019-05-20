<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2" defaultPhase="phase1">
    
    <sch:phase id="phase1">
        <sch:active pattern="pattern1"/>
    </sch:phase>
    <sch:phase id="phase2">
        <sch:active pattern="pattern2"/>
    </sch:phase>
    
    <sch:pattern id="pattern1">
        <sch:let name="patternVar" value="'patternValue'"/>
        <sch:let name="patternVarUsage" value="concat($patternVar, '_Usage')"/>
        <sch:rule context="elementToReplace">
            <sch:assert test="$patternVar = 'xxx'" sqf:fix="replace" sqf:default-fix="replace">Bad element!</sch:assert>
            <sqf:fix id="replace">
                <sqf:description>
                    <sqf:title>Replace element</sqf:title>
                </sqf:description>
                <sqf:replace target="replaced" node-type="element">
                    <sch:value-of select="$patternVar, $patternVarUsage"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
    <sch:pattern id="pattern2">
        <sch:let name="patternVar" value="'patternValue2'"/>
        <sch:let name="patternVarUsage" value="concat($patternVar, '_Usage')"/>
        <sch:rule context="elementToReplace">
            <sch:assert test="$patternVar = 'xxx'" sqf:fix="replace" sqf:default-fix="replace">Bad element!</sch:assert>
            <sqf:fix id="replace">
                <sqf:description>
                    <sqf:title>Replace element</sqf:title>
                </sqf:description>
                <sqf:replace target="replaced" node-type="element">
                    <sch:value-of select="$patternVar, $patternVarUsage"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
