<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2" xmlns:html="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml">
    <sch:ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <sch:pattern>
        <sch:rule context="html:title">
            <sch:let name="titleText" value="string-join(.//text(),'')"/>
            <sch:report test="comment()" sqf:fix="deleteComment resolveComment" sqf:default-fix="deleteComment">Comments are forbidden in &lt;title&gt; elements.</sch:report>
            <sch:report test="string-length($titleText) gt 70" sqf:fix="cutTitle setTitle takeComment newTitle">The text in the &lt;title&gt; element is too long.</sch:report>
            <sch:report test="normalize-space($titleText) = ''" sqf:fix="setTitle newTitle">There is no title!</sch:report>
            <sqf:fix id="deleteComment">
                <sqf:description>
                    <sqf:title>Delete the comment.</sqf:title>
                </sqf:description>
                <sqf:delete match="comment()"/>
            </sqf:fix>
            <sqf:fix id="resolveComment" use-when="not(string-length($titleText) + string-length(string-join(.//comment(),'')) gt 70)">
                <sqf:description>
                    <sqf:title>Change the comment into text.</sqf:title>
                </sqf:description>
                <sqf:replace match="comment()">
                    <sch:value-of select="."/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="cutTitle">
                <sqf:description>
                    <sqf:title>Cut the title after the 70th character.</sqf:title>
                </sqf:description>
                <sqf:replace target="title" node-type="element">
                    <sch:value-of select="substring(.,1,70)"/>
                </sqf:replace>
            </sqf:fix><!---->
            <sqf:fix id="setTitle" use-when="//html:h1" use-for-each="//html:h1">
                <sqf:description>
                    <sqf:title>Take the title from the headline no <sch:value-of select="$sqf:pos"/>.</sqf:title>
                </sqf:description>
                <sqf:replace target="title" node-type="element">
                    <sch:value-of select="$sqf:current"/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="newTitle">
                <sqf:description>
                    <sqf:title>Set a new title.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="newText" type="xs:string">
                    <sqf:description>
                        <sqf:title>Enter the text of the new title.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:replace target="title" node-type="element">
                    <sch:value-of select="$newText"/>
                </sqf:replace>
            </sqf:fix>
            <sqf:fix id="takeComment" use-when="comment()">
                <sqf:description>
                    <sqf:title>Use the forbidden comment as new title.</sqf:title>
                </sqf:description>
                <sqf:replace target="title" node-type="element">
                    <sch:value-of select="comment()"/>
                </sqf:replace>
            </sqf:fix>
        </sch:rule>
    </sch:pattern>
</sch:schema>
