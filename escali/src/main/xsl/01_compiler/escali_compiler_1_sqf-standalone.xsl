<?xml version="1.0" encoding="UTF-8"?>
<!--  
    Copyright (c) 2014 Nico Kutscherauer
        
    This file is part of Escali Schematron.
    
    Escali Schematron is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    Escali Schematron is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with Escali Schematron.  If not, see http://www.gnu.org/licenses/gpl-3.0.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" exclude-result-prefixes="es xs xd" version="2.0">

    <xsl:template name="sqf:transformStandalone">
        <xsl:param name="sqf:fixes" required="yes" as="node()?"/>
        <sch:schema>
            <xsl:attribute name="xml:base" select="$sqf:fixes/base-uri(.)"/>
            <xsl:attribute name="queryBinding" select="
                if ($sqf:fixes/@es:queryBinding) then
                ($sqf:fixes/@es:queryBinding)
                else
                ('xslt2')"/>
            
            <xsl:sequence select="$sqf:fixes/(* except sqf:*)"/>
            
            <sch:pattern id="generated" es:matchType="all">
                <xsl:for-each select="$sqf:fixes/(sqf:fix | sqf:group)[@es:context]">
                    <sch:rule context="{@es:context}">
                        <sch:assert test="false()" role="sqf-standalone" sqf:fix="{@id}"/>
                        <xsl:sequence select="."/>
                    </sch:rule>
                </xsl:for-each>
            </sch:pattern>
            
            <sqf:fixes>
                <xsl:apply-templates select="$sqf:fixes/(sqf:fix | sqf:group)[@id]" mode="es:resolveStandalone"/>
            </sqf:fixes>
        </sch:schema>
    </xsl:template>
    
    <xsl:template match="sqf:*[@es:context]" mode="es:resolveStandalone"/>
        
    
    
</xsl:stylesheet>
