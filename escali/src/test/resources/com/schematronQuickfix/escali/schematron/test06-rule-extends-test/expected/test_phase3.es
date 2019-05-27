<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns="http://www.w3.org/1999/xhtml" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/">
    <es:meta activePatterns="" phase="phase3" title="Extends test" queryBinding="xslt2" schema="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test6/in/extends.sch" instance="file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test6/in/extends.xml">
        <es:title>Extends test</es:title>
        <es:schema>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test6/in/extends.sch</es:schema>
        <es:instance>file:/D:/nico/Work/Java/net.sqf.tester/data/schematron/sch-tests/test6/in/extends.xml</es:instance>
        <es:ns-prefix-in-attribute-values uri="http://www.schematron.info/arche" prefix="arc"/>
    </es:meta>
    <es:pattern>
        <es:meta id="pattern3" is-a="pattern4" phases="phase3 #ALL"/>
        <es:rule>
            <es:meta context="arc:tier">
                <let xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" name="minGewicht" value="arc:gewicht div 2"/>
            </es:meta>
        </es:rule>
        <es:rule>
            <es:meta context="arc:tier">
                <let xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" name="minGewicht" value="arc:gewicht div 2"/>
            </es:meta>
            <es:report location="/*:arche[namespace-uri()='http://www.schematron.info/arche'][1]/*:ladung[namespace-uri()='http://www.schematron.info/arche'][1]/*:zimmer[namespace-uri()='http://www.schematron.info/arche'][1]/*:tier[namespace-uri()='http://www.schematron.info/arche'][2]" test="parent::*/arc:tier/arc:gewicht &lt; $minGewicht">
                <es:text>Noah, das Tier ist zu schwer für seine Zimmergenossen! Es könnte einen zertrampeln/angreifen.</es:text>
            </es:report>
        </es:rule>
        <es:rule>
            <es:meta context="arc:tier">
                <let xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" name="minGewicht" value="arc:gewicht div 2"/>
            </es:meta>
            <es:report location="/*:arche[namespace-uri()='http://www.schematron.info/arche'][1]/*:ladung[namespace-uri()='http://www.schematron.info/arche'][1]/*:zimmer[namespace-uri()='http://www.schematron.info/arche'][2]/*:tier[namespace-uri()='http://www.schematron.info/arche'][1]" test="parent::*/arc:tier/arc:gewicht &lt; $minGewicht">
                <es:text>Noah, das Tier ist zu schwer für seine Zimmergenossen! Es könnte einen zertrampeln/angreifen.</es:text>
            </es:report>
        </es:rule>
        <es:rule>
            <es:meta context="arc:tier">
                <let xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" name="minGewicht" value="arc:gewicht div 2"/>
            </es:meta>
        </es:rule>
        <es:rule>
            <es:meta context="arc:tier">
                <let xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" name="minGewicht" value="arc:gewicht div 2"/>
            </es:meta>
            <es:report location="/*:arche[namespace-uri()='http://www.schematron.info/arche'][1]/*:ladung[namespace-uri()='http://www.schematron.info/arche'][1]/*:zimmer[namespace-uri()='http://www.schematron.info/arche'][3]/*:tier[namespace-uri()='http://www.schematron.info/arche'][1]" test="parent::*/arc:tier/arc:gewicht &lt; $minGewicht">
                <es:text>Noah, das Tier ist zu schwer für seine Zimmergenossen! Es könnte einen zertrampeln/angreifen.</es:text>
            </es:report>
        </es:rule>
        <es:rule>
            <es:meta context="arc:tier">
                <let xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" name="minGewicht" value="arc:gewicht div 2"/>
            </es:meta>
        </es:rule>
    </es:pattern>
</es:escali-reports>
