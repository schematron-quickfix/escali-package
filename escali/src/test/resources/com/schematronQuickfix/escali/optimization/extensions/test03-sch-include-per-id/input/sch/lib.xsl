<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"  xmlns:est="escali.schematron-quickfix.com/test">
    <xsl:function name="est:foo" as="node()" xml:id="est.foo">
        <xsl:param name="foo"/>
        <xsl:sequence select="$foo/bar"/>
    </xsl:function>
</xsl:stylesheet>
