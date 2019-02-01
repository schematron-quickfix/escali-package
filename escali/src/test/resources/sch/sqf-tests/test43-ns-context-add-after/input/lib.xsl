<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:d2t="http://www.data2type.de"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:function name="d2t:defComplexType" as="element(xs:complexType)">
        <xsl:param name="name" as="xs:string"/>
        <xsl:sequence select="d2t:defComplexType($name, '')"/>
    </xsl:function>
    
    <xsl:function name="d2t:defComplexType" as="element(xs:complexType)">
        <xsl:param name="name" as="xs:string"/>
        <xsl:param name="suf" as="xs:string?"/>
        <xs:complexType name="{$name}Type{$suf}">
            <xs:annotation>
                <xs:appinfo>
                    <d2t:xsdguide/>
                </xs:appinfo>
            </xs:annotation>
        </xs:complexType>
    </xsl:function>    
</xsl:stylesheet>