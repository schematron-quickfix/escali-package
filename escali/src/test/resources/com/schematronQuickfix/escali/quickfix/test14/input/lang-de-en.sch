<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" queryBinding="xslt2" xml:lang="de">
    <p>Absatz ohne Sprache</p>
    <p xml:lang="en">Paragraph in English</p>
    <pattern>
        <rule context="*:html">
            <assert test="false()" diagnostics="en" sqf:fix="abc" sqf:default-fix="abc_4">Assert in deutscher Sprache </assert>
            <sqf:fix id="abc_4">
                <sqf:description>
                    <sqf:title/>
                </sqf:description>
            </sqf:fix>
            <sqf:fix id="abc">
                <sqf:description>
                    <sqf:title/>
                </sqf:description>
            </sqf:fix>
            <sqf:fix id="abc_1">
                <sqf:description>
                    <sqf:title/>
                </sqf:description>
            </sqf:fix>
            <sqf:fix id="abc_3">
                <sqf:description>
                    <sqf:title/>
                </sqf:description>
            </sqf:fix>
            <sqf:fix id="abc_2">
                <sqf:description>
                    <sqf:title/>
                </sqf:description>
            </sqf:fix>
            <assert test="false()" diagnostics="en" sqf:fix="abc">Assert in deutscher Sprache </assert>
            <assert test="false()" diagnostics="en" sqf:fix="createLocal def">Assert in deutscher Sprache </assert>

        </rule>
        <rule context="*:html">
            <assert test="false()" diagnostics="en">Assert in deutscher Sprache </assert>
        </rule>
    </pattern>
    <diagnostics>
        <diagnostic id="en" xml:lang="en">Diagnostic in English.</diagnostic>
        <diagnostic id="fr" xml:lang="fr">Diagnostic in French.</diagnostic>
        <diagnostic id="es" xml:lang="es">Diagnostic in French.</diagnostic>
        <diagnostic id="ru" xml:lang="ru">Diagnostic in French.</diagnostic>
        <diagnostic id="hu" xml:lang="hu">Diagnostic in French.</diagnostic>
    </diagnostics>
    <sqf:fixes>
        <sqf:fix id="createFix">
            <sqf:param name="ids" type="xs:string+" required="yes"/>
            <sqf:param name="global" type="xs:boolean" default="false()"/>
            <sqf:description>
                <sqf:title>fix it</sqf:title>
            </sqf:description>
            <sqf:description xml:lang="en">
                <sqf:title>fix it</sqf:title>
            </sqf:description>
            <sqf:description xml:lang="fr">
                <sqf:title>fix it</sqf:title>
            </sqf:description>
            <xsl:variable name="newFixes">
                <xsl:for-each select="$ids">
                    <xsl:element name="sqf:fix">
                        <xsl:attribute name="id" select="."/>
                        <xsl:element name="sqf:description">
                            <xsl:element name="sqf:title"/>
                        </xsl:element>
                    </xsl:element>
                </xsl:for-each>
            </xsl:variable>

            <sqf:add match="." position="after" select="$newFixes" use-when="not($global)"/>

            <sqf:add match="/sch:schema/sqf:fixes" position="last-child" select="$newFixes" use-when="$global"/>
            <sqf:add match="/sch:schema" target="sqf:fixes" node-type="element" select="$newFixes" position="last-child" use-when="not(/sch:schema/sqf:fixes) and $global"/>



        </sqf:fix>
        <sqf:fix id="createLocal">
            <sqf:description>
                <sqf:title/>
            </sqf:description>
        </sqf:fix>
        <sqf:fix id="def">
            <sqf:description>
                <sqf:title/>
            </sqf:description>
        </sqf:fix>
    </sqf:fixes>
</schema>
