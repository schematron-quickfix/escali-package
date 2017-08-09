<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:pa="http://www.pagina-online.de/xlib" queryBinding="xslt2">
    <sch:let name="test" value="value"/>
    <sch:pattern id="SFV">
        <sch:rule context="versal">
            <sch:report test="following-sibling::node()[1]/self::text()[. = ' ']/following-sibling::node()[1]/self::versal" role="warn" sqf:fix="versal-Wortabstd"> ▌GvH ▌[SQF] Wortabstand zwischen [versal]-Elementen </sch:report>

            <sch:let name="versalkette" value="
                    following-sibling::node()[self::versal | self::text()[. = ' ']]
                    [every $node in
                    (
                    current()/following-sibling::node()
                    intersect
                    preceding-sibling::node()
                    )
                        satisfies
                        $node/(self::versal | self::text()[. = ' '])
                    ]"/>

            <sqf:fix id="versal-Wortabstd">
                <sqf:description>
                    <sqf:title>Wortabstand innerhalb [versal] setzen?</sqf:title>
                    <sqf:p>Nachfolgende durch Wortabstand getrennte [versal]-Elemente werden zusammengefasst.</sqf:p>
                </sqf:description>
                <sqf:replace node-type="element" target="versal" select="node() | $versalkette/(self::versal/node() | self::text())"/>
                <sqf:delete match="$versalkette"/>
            </sqf:fix>
        </sch:rule>

    </sch:pattern>
</sch:schema>
