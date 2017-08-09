<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" queryBinding="xslt2">
    <ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <pattern>
        <rule context="html:table">
            <let name="colWidthSummarized" value="number(sum(       
                for $colWidth in html:col/@width           
                return number(substring-before($colWidth,'%'))     
                ))"/>
            <let name="lastWidth" value="number(substring-before(html:col[last()]/@width,'%'))"/>
            <assert test="$colWidthSummarized &lt;= 100" sqf:fix="last proportional setOneWidth">The sum of the column widths (<value-of select="$colWidthSummarized"/>%) is greater than 100%.</assert>
            <sqf:fix id="last" use-when="$colWidthSummarized - 100 lt $lastWidth" role="replace">
                <sqf:description>
                    <sqf:title>Subtract the excessive width from the last &lt;col&gt; element.</sqf:title>
                </sqf:description>
                <let name="delta" value="$colWidthSummarized - 100"/>
                <sqf:add match="html:col[last()]" target="width" node-type="attribute">
                    <let name="newWidth" value="number(substring-before(@width,'%')) - $delta"/>
                    <value-of select="concat($newWidth,'%')"/>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="setOneWidth" role="replace">
                <sqf:description>
                    <sqf:title>Auto-correct the width of one column.</sqf:title>
                </sqf:description>
                <sqf:user-entry name="colNumber" default="1 to count(html:col)">
                    <sqf:description>
                        <sqf:title>Enter the column number.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <let name="delta" value="$colWidthSummarized - 100"/>
                <sqf:add match="html:col" target="width" node-type="attribute">
                    <let name="pos" value="count(preceding-sibling::html:col) + 1"/>
                    <xsl:choose>
                        <xsl:when test="$pos = number($colNumber)">
                            <let name="newWidth" value="number(substring-before(@width,'%')) - $delta"/>
                            <value-of select="concat($newWidth,'%')"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <value-of select="@width"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="proportional" role="replace">
                <sqf:description>
                    <sqf:title>Subtract the excessive width from each &lt;col&gt; element proportionally.</sqf:title>
                </sqf:description>
                <let name="delta" value="$colWidthSummarized - 100"/>
                <sqf:add match="html:col" target="width" node-type="attribute">
                    <let name="width" value="number(substring-before(@width,'%'))"/>
                    <let name="newWidth" value="$width - ($delta * ($width div $colWidthSummarized))"/>
                    <value-of select="concat($newWidth,'%')"/>
                </sqf:add>
            </sqf:fix>
        </rule>
    </pattern>
</schema>
