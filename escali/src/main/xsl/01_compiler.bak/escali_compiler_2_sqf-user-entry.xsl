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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" exclude-result-prefixes="xs xd" version="2.0">

    <xd:doc scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Nov 19, 2013</xd:p>
            <xd:p><xd:b>Author:</xd:b> Nico Kutscherauer</xd:p>
        </xd:desc>
    </xd:doc>

    <xd:doc scope="version">
        <xd:desc>
            <xd:p>Version information</xd:p>
            <xd:ul>
                <xd:li>
                    <xd:p>2014-03-14</xd:p>
                    <xd:ul>
                        <xd:li>
                            <xd:p>publishing version</xd:p>
                        </xd:li>
                    </xd:ul>
                </xd:li>
            </xd:ul>
        </xd:desc>
    </xd:doc>

    <xsl:key name="globalFixById" match="sqf:fixes//sqf:fix|sqf:fixes/sqf:group" use="@id"/>

    <xsl:variable name="rule.id.separator" select="'.'"/>

    <!--    <!-\-  
    marks as an user-entry-parameter
    -\->
    <xsl:template match="sqf:fix/sqf:param[@name=parent::sqf:fix/sqf:user-entry/@ref]" mode="#all">
        <xsl:copy>
            <xsl:attribute name="user-entry">yes</xsl:attribute>
            <xsl:apply-templates select="node() | @*" mode="#current"/>
        </xsl:copy>
    </xsl:template>-->
    <xsl:key name="ruleByExtendedRule" match="sch:rule" use="./sch:extends/@rule"/>

    <xsl:function name="sqf:createAbstractFixId" as="xs:string">
        <xsl:param name="abstractRuleIds" as="xs:string*"/>
        <xsl:param name="id" as="attribute(id)"/>
        <xsl:sequence select="
                string-join(($abstractRuleIds,
                $id), $rule.id.separator)"/>
    </xsl:function>
    <xsl:function name="sqf:createAbstractFixIdUnique" as="xs:string">
        <xsl:param name="abstractRuleIds" as="xs:string*"/>
        <xsl:param name="id" as="attribute(id)"/>
        <xsl:choose>
            <xsl:when test="count($abstractRuleIds) gt 0">
                <xsl:variable name="abstractId" select="sqf:createAbstractFixId($abstractRuleIds, $id)"/>
                <xsl:variable name="sameId" select="
                    for $i in $abstractRuleIds return (
                    key('ruleByExtendedRule', $i, root($id))/
                    sqf:fix[sqf:createAbstractFixId($abstractRuleIds[position() lt index-of($abstractRuleIds, $i)], @id) = $abstractId])"/>
                <xsl:value-of select=" if (count($sameId) gt 0) then (concat($abstractId, count($sameId) + 1)) else ($abstractId)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:sequence select="string($id)"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>
    
    <xsl:template match="sqf:fix/@id" mode="#all" priority="1000">
        <xsl:param name="abstractRuleId" tunnel="yes" select="()"/>
        <xsl:attribute name="id" select="sqf:createAbstractFixIdUnique($abstractRuleId, .)"/>
    </xsl:template>

    <xsl:template match="sch:*/@sqf:fix" mode="#all" priority="1000">
        <xsl:param name="abstractRuleId" tunnel="yes" select="()"/>
        <xsl:variable name="ids" select="tokenize(., '\s')"/>
        <xsl:variable name="local-fixes" select="ancestor::sch:rule[1]//(sqf:fix | sqf:group)"/>
        <xsl:attribute name="sqf:fix" select="
                for $i in $ids
                return
                    ($local-fixes[@id = $i],
                    key('globalFixById', $i))[1]/sqf:createAbstractFixIdUnique($abstractRuleId, @id)" separator=" "/>
    </xsl:template>



    <xsl:template match="sch:assert[@sqf:fix] | sch:report[@sqf:fix]" mode="#all" priority="1000">
        <xsl:variable name="local-fix" select="../sqf:fix"/>
        <xsl:variable name="prec-called-fixes" select="preceding-sibling::*/@sqf:fix/tokenize(., '\s')" as="xs:string*"/>
        <xsl:variable name="global-fix-ids" select="
                tokenize(@sqf:fix, '\s')[not(. = ($local-fix/@id,
                $prec-called-fixes))]" as="xs:string*"/>
        <xsl:next-match/>
        <xsl:apply-templates select="key('globalFixById', $global-fix-ids)" mode="#current"/>
    </xsl:template>

    <xsl:template match="sqf:fixes" mode="#all" priority="1000"/>
    
    
    <xsl:template match="sqf:param[@abstract = 'true']" mode="resolvePattern" priority="100"/>
        
    
    

</xsl:stylesheet>
