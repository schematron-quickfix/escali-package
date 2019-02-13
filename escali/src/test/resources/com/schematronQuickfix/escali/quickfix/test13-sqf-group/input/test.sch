<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <es:default-namespace uri="http://www.w3.org/1999/xhtml"/>
    <ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <pattern>
        <rule context="body">
            <let name="firstDescendantHeading" value="
                    descendant::node()[name() = ('h1',
                    'h2',
                    'h3',
                    'h4',
                    'h5',
                    'h6')][1]"/>
            <assert test="$firstDescendantHeading/self::h1" sqf:fix="firstHead">The first headline of a HTML document has to be of level 1.</assert>
            <sqf:group id="firstHead">
                <sqf:fix id="setH1">
                    <sqf:description>
                        <sqf:title>The first headline will be transformed into a &lt;h1&gt;.</sqf:title>
                    </sqf:description>
                    <sqf:replace match="$firstDescendantHeading" target="h1" node-type="element">
                        <value-of select="."/>
                    </sqf:replace>
                </sqf:fix>
                <sqf:fix id="add2bodyH1">
                    <sqf:description>
                        <sqf:title>The body gets a &lt;h1&gt; as first child.</sqf:title>
                    </sqf:description>
                    <sqf:user-entry name="h1" type="xs:string" default="/html/head/title">
                        <sqf:description>
                            <sqf:title>Enter the text of the new headline. The default is "<value-of select="//*[local-name() = 'title']"/>".</sqf:title>
                        </sqf:description>
                    </sqf:user-entry>
                    <sqf:add position="first-child" target="h1" node-type="element">
                        <value-of select="$h1"/>
                    </sqf:add>
                </sqf:fix>
                <sqf:fix id="addPrecH1">
                    <sqf:description>
                        <sqf:title>A &lt;h1&gt; element will be inserted before the first headline.</sqf:title>
                    </sqf:description>
                    <sqf:user-entry name="h1" type="xs:string" default="/html/head/title">
                        <sqf:description>
                            <sqf:title>Enter the text of the new headline. The default is "<value-of select="//*[local-name() = 'title']"/>".</sqf:title>
                        </sqf:description>
                    </sqf:user-entry>
                    <sqf:add match="$firstDescendantHeading" position="before" target="h1" node-type="element">
                        <value-of select="$h1"/>
                    </sqf:add>
                </sqf:fix>
                <sqf:fix id="delete" use-when="($firstDescendantHeading/following::node()[matches(name(), 'h[123456]')])[1]/self::h1">
                    <sqf:description>
                        <sqf:title>Delete the first headline (&lt;<name path="$firstDescendantHeading"/>&gt;).</sqf:title>
                    </sqf:description>
                    <sqf:delete match="$firstDescendantHeading"/>
                </sqf:fix>
            </sqf:group>
        </rule>
        <rule context="h3">
            <let name="firstPrecedingHeading" value="
                    preceding::node()[name() = ('h1',
                    'h2',
                    'h3',
                    'h4',
                    'h5',
                    'h6')][1]"/>
            <report test="$firstPrecedingHeading/self::h1" sqf:fix="setH1ToH2 setH3ToH2 addH2beforeH3 addH2afterH1 delete">A headline on level 2 is missing: A &lt;h3&gt; should not follow on a <name path="$firstPrecedingHeading"/>.</report>
            <sqf:fix id="delete" use-when="not((following::node()[matches(name(), 'h[123456]')])[1]/self::h4)">
                <sqf:description>
                    <sqf:title>Delete the headline &lt;h3&gt;.</sqf:title>
                </sqf:description>
                <sqf:delete/>
            </sqf:fix>
            <sqf:fix id="setH1ToH2" use-when="$firstPrecedingHeading/preceding::node()[matches(name(), 'h[123456]')]">
                <sqf:description>
                    <sqf:title>Replace the &lt;h1&gt; by a &lt;h2&gt;.</sqf:title>
                </sqf:description>
                <sqf:replace match="$firstPrecedingHeading" target="h2" node-type="element">
                    <value-of select="."/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="setH3ToH2" use-when="not((following::node()[matches(name(), 'h[123456]')])[1]/self::h4)">
                <sqf:description>
                    <sqf:title>Replace the &lt;h3&gt; by a &lt;h2&gt;.</sqf:title>
                </sqf:description>
                <sqf:replace target="h2" node-type="element">
                    <value-of select="."/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="addH2beforeH3">
                <sqf:description>
                    <sqf:title>Add a &lt;h2&gt; before the &lt;h3&gt;.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="h2" type="xs:string">
                    <sqf:description>
                        <sqf:title>Enter the text of the new headline.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add position="before" target="h2" node-type="element">
                    <value-of select="$h2"/>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="addH2afterH1">
                <sqf:description>
                    <sqf:title>Add a &lt;h2&gt; after the &lt;h1&gt;.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="h2" type="xs:string">
                    <sqf:description>
                        <sqf:title>Enter the text of the new headline.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add match="$firstPrecedingHeading" position="after" target="h2" node-type="element">
                    <value-of select="$h2"/>
                </sqf:add>
            </sqf:fix>
        </rule>
        <rule context="h4">
            <let name="firstPrecedingHeading" value="
                    preceding::node()[name() = ('h1',
                    'h2',
                    'h3',
                    'h4',
                    'h5',
                    'h6')][1]"/>
            <report test="$firstPrecedingHeading/self::h1" sqf:fix="add2 delete setH4ToH2">The headlines on level 2 and 3 are missing: A &lt;h4&gt; should not follow a &lt;<name path="$firstPrecedingHeading"/>&gt;.</report>
            <report test="$firstPrecedingHeading/self::h2" sqf:fix="add delete setH4ToH3">The headline on level 3 is missing: A &lt;h4&gt; should not follow a &lt;<name path="$firstPrecedingHeading"/>&gt;.</report>
            <sqf:fix id="add">
                <sqf:description>
                    <sqf:title>Add a &lt;h3&gt; before the &lt;h4&gt;.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="h3" type="xs:string">
                    <sqf:description>
                        <sqf:title>Enter the text of the &lt;h3&gt;.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add match="$firstPrecedingHeading" position="after" target="h3" node-type="element">
                    <value-of select="$h3"/>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="add2">
                <sqf:description>
                    <sqf:title>Add a &lt;h2&gt; and a &lt;h3&gt; after the &lt;h1&gt;.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="h2" type="xs:string">
                    <sqf:description>
                        <sqf:title>Enter the text of the &lt;h2&gt;.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:user-entry name="h3" type="xs:string">
                    <sqf:description>
                        <sqf:title>Enter the text of the &lt;h3&gt;.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add match="$firstPrecedingHeading" position="after" target="h3" node-type="element">
                    <value-of select="$h3"/>
                </sqf:add>
                <sqf:add match="$firstPrecedingHeading" position="after" target="h2" node-type="element">
                    <value-of select="$h2"/>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="setH4ToH2" use-when="not((following::node()[matches(name(), 'h[123456]')])[1]/(self::h4 | self::h5))">
                <sqf:description>
                    <sqf:title>Replace the &lt;h4&gt; by a &lt;h2&gt;.</sqf:title>
                </sqf:description>
                <sqf:replace target="h2" node-type="element">
                    <value-of select="."/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="setH4ToH3" use-when="not((following::node()[matches(name(), 'h[123456]')])[1]/self::h5)">
                <sqf:description>
                    <sqf:title>Replace the &lt;h4&gt; by a &lt;h3&gt;.</sqf:title>
                </sqf:description>
                <sqf:replace target="h3" node-type="element">
                    <value-of select="."/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="delete" use-when="not((following::node()[matches(name(), 'h[123456]')])[1]/(self::h4 | self::h5))">
                <sqf:description>
                    <sqf:title>Delete the headline &lt;h4&gt;.</sqf:title>
                </sqf:description>
                <sqf:delete/>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
