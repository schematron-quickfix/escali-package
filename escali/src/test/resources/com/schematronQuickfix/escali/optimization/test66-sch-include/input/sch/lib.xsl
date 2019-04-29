<?xml version="1.0" encoding="UTF-8"?>
<xsl:function xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:est="escali.schematron-quickfix.com/test"
    version="2.0" name="est:foo" as="node()">
    <xsl:param name="foo"/>
    <xsl:sequence select="$foo/bar"/>
</xsl:function>