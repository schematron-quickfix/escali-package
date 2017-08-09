<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sqf="http://www.schematron-quickfix.com/validator/process"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:param name="sqf:changePrefix" select="'sqfc'" as="xs:string"/>
    
    <xsl:template name="standalone">
        <xsl:apply-templates select="/" mode="sqf:standalone"/>
    </xsl:template>
    
    <xsl:template match="processing-instruction()[matches(name(), concat($sqf:changePrefix, '-(end|start)'))]" priority="1000" mode="#all"/>
    <!-- 
        copies all nodes:
    -->
    <xsl:template match="node() | @*" mode="sqf:standalone">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>