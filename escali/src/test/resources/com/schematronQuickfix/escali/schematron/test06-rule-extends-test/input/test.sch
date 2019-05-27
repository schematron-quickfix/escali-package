<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <title>Extends test</title>
    <ns uri="http://www.schematron.info/arche" prefix="arc"/>
    <phase id="phase1">
        <active pattern="pattern1"/>
    </phase>
    <phase id="phase2">
        <active pattern="pattern2"/>
    </phase>
    <phase id="phase3">
        <active pattern="pattern3"/>
    </phase>
    <pattern id="pattern1">
        <rule context="arc:tier[@fleischfresser='nein']" role="info">
            <let name="minGewicht" value="arc:gewicht div 10"/>
            <extends rule="gewicht"/>
        </rule>
        <rule abstract="true" id="gewicht">
            <report test="parent::*/arc:tier/arc:gewicht &lt; $minGewicht">Noah, das Tier ist zu schwer für seine Zimmergenossen! Es könnte einen zertrampeln/angreifen.</report>
        </rule>
    </pattern>
    <pattern id="pattern2">
        <rule context="arc:tier[@fleischfresser='ja']" role="fatal">
            <let name="minGewicht" value="arc:gewicht div 2"/>
            <extends rule="gewicht"/>
        </rule>
    </pattern>
    <pattern id="pattern3" is-a="pattern4">
        <param name="rule" value="gewicht"/>
    </pattern>
    <pattern id="pattern4" abstract="true">
        <rule context="arc:tier" role="warning">
            <let name="minGewicht" value="arc:gewicht div 2"/>
            <extends rule="gewicht"/>
        </rule>
    </pattern>
</schema>
