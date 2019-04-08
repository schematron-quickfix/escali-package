<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:foo="foo.com"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:key name="foo-id" match="foo" use="@id"/>
    
    <xsl:function name="foo:id">
        <xsl:param name="foo"/>
        <xsl:sequence select="$foo/(@id, concat('id_', count($foo/preceding::foo) + 1))[1]"/>
    </xsl:function>
    <xsl:variable name="allFooBar" select="//(foo|bar)"/>
    <xsl:variable name="root" select="/"/>
    <xsl:template name="createBar">
        <xsl:param name="id"/>
        <xsl:param name="counter"/>
        
        <xsl:variable name="isGeneric" select="not(key('foo-id', $id, $root))"/>
        
        <xsl:for-each select="1 to $counter">
            <bar barId="{$id}_{.}" generic="{$isGeneric}"/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>