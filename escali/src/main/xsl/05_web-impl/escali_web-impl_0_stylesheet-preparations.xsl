<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">
    
    <xsl:preserve-space elements="*"/>
    
    <xsl:param name="rewrite-prefix"/>
    <xsl:param name="target-prefix"/>
    <xsl:param name="baseuri-prefix"/>
    
    <xsl:output method="xml" indent="no" encoding="UTF-8"/>
    
    <xsl:template match="/">
        <xsl:variable name="base" select="base-uri(.)"/>

        <xsl:variable name="relative" select="substring-after($base, $rewrite-prefix)"/>
        <xsl:variable name="write-uri" select="concat($target-prefix, $relative)"/>
        <xsl:variable name="new-base" select="concat($baseuri-prefix, $relative)"/>

        <xsl:result-document href="{$write-uri}">
            <xsl:apply-templates>
                <xsl:with-param name="new-base" select="$new-base"/>
            </xsl:apply-templates>
        </xsl:result-document>
    </xsl:template>
    
    <xsl:template match="/*">
        <xsl:param name="new-base" select="concat($baseuri-prefix, substring-after(base-uri(.), $rewrite-prefix))"/>
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="xml:base" select="$new-base"/>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>
    
<!--    <xsl:template match="xsl:include[@href]" priority="10">
        <xsl:variable name="href" select="resolve-uri(@href, base-uri(.))"/>
        <xsl:apply-templates select="doc($href)/*/*"/>
    </xsl:template>-->
    


    <xsl:template match="xsl:include[@href] | xsl:import[@href]">
        <xsl:next-match/>
        <xsl:variable name="href" select="resolve-uri(@href, base-uri(.))"/>
        <xsl:apply-templates select="doc($href)"/>
    </xsl:template>
    
    <xsl:template match="@*" priority="10">
        <xsl:copy/>
    </xsl:template>
    
    <!-- 
        copies all nodes:
    -->
    <xsl:template match="node() | @*" mode="#all">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="#current"/>
            <xsl:apply-templates select="node()" mode="#current"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
