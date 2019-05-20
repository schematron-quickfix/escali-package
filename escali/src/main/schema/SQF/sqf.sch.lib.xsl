<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:sqf="http://www.schematron-quickfix.com/validator/process"
    xmlns:sch="http://purl.oclc.org/dsdl/schematron"
    xmlns:es="http://www.escali.schematron-quickfix.com/"
    exclude-result-prefixes="xs"
    version="2.0">
    <!--  
    Functions
    -->
    <xsl:variable name="LOCAL_ONLY" select="'LOCAL_ONLY'"/>
    <xsl:variable name="GLOBAL_ONLY" select="'GLOBAL_ONLY'"/>
    <xsl:variable name="GLOBAL_AND_LOCAL" select="'GLOBAL_AND_LOCAL'"/>

<!--
    sqf:getRoots
    
    returns a sequence of document nodes which are the schema root nodes
    and all recurse included or imported root nodes of the schema.
    
    $context = an arbitrary element, inside of a Schematron schema.
    
    return value = all root nodes of the context Schematron schema.
    
    -->
    <xsl:function name="sqf:getRoots" as="document-node()*">
        <xsl:param name="context" as="element()*"/>
        <xsl:variable name="contextRoot" select="$context/root(.)"/>
        <xsl:variable name="includeImports" select="$contextRoot/(sch:schema/sch:extends | sch:schema/es:import | .//sch:include)/doc(resolve-uri(@href, base-uri(.)))/*"/>
        <xsl:sequence select="$contextRoot, $includeImports/sqf:getRoots(.)"/>
    </xsl:function>
    
<!--    
    sqf:getAvailableFixOrGroups
    
    see sqf:getAvailableFixOrGroups#2
    
    set $localOrGlobal to $GLOBAL_AND_LOCAL
    -->
    <xsl:function name="sqf:getAvailableFixOrGroups" as="element()*">
        <xsl:param name="context" as="element()*"/>
        <xsl:sequence select="sqf:getAvailableFixOrGroups($context, $GLOBAL_AND_LOCAL)"/>
    </xsl:function>
    
<!--    
    sqf:getAvailableFixOrGroups
    
    Function to return all available sqf:fix and sqf:group elements
    for a Schematron $context.
    
    $context = an arbitrary element, inside of a Schematron schema.
    $localOrGlobal = Indicator to return only global, only local or 
                     all sqf:group or sqf:fix elements in scope. 
                     Available values are 'LOCAL_ONLY', 'GLOBAL_ONLY', 'GLOBAL_AND_LOCAL'
    
    -->
    <xsl:function name="sqf:getAvailableFixOrGroups" as="element()*">
        <xsl:param name="context" as="element()*"/>
        <xsl:param name="localOrGlobal" as="xs:string"/>
        <xsl:variable name="rule" select="$context/ancestor-or-self::sch:rule"/>
        <xsl:variable name="local" select="
            if (not($localOrGlobal = $GLOBAL_ONLY))
            then
            ($rule)
            else
            ()"/>
        <xsl:variable name="global" select="
            if (not($localOrGlobal = $LOCAL_ONLY))
            then
            (sqf:getRoots($context)/(sch:schema | .)/sqf:fixes)
            else
            ()"/>
        
        <xsl:variable name="localIds" select="
            $local/(sqf:fix|sqf:group/(.|sqf:fix))/@id"/>
        <xsl:variable name="availableFixes" select="
            $local/sqf:fix | $global/sqf:fix[not(@id = $localIds)]"/>
        
        <xsl:variable name="availableGroups" select="
            ($global/sqf:group)/(. | ./sqf:fix)[not(@id = $localIds)]"/>
        <xsl:variable name="availableGroups" select="
            $local/sqf:group/(. | ./sqf:fix), $availableGroups"/>
        <xsl:sequence select="
            $availableFixes,
            $availableGroups"/>
    </xsl:function>
    
    <xsl:function name="sqf:getSQFDescrLangs" as="xs:string*">
        <xsl:param name="descriptionEl" as="element()"/>
        <xsl:variable name="elementLang" select="sqf:getLang($descriptionEl)"/>
        <xsl:variable name="refs" select="$descriptionEl/@ref/tokenize(., '\s')"/>
        
        <xsl:variable name="diagnostics" select="root($descriptionEl)/sch:schema/sch:diagnostics/sch:diagnostic[@id = $refs]"/>
        
        <xsl:variable name="unknownRefs" select="$refs[not($diagnostics/@id = .)]"/>
        
        <xsl:sequence select="
            $elementLang,
            $diagnostics/sqf:getLang(.),
            '#UNKNOWN'[count($unknownRefs) gt 0]
            "/>
        
    </xsl:function>
    
    <xsl:function name="sqf:getLang" as="xs:string">
        <xsl:param name="node" as="node()"/>
        <xsl:variable name="lang" select="($node/ancestor-or-self::*/@xml:lang)[last()]"/>
        <xsl:value-of select="
            if ($lang) then
            ($lang)
            else
            ('#DEFAULT')"/>
    </xsl:function>
    
    
    
    <xsl:function name="sqf:getRefFix" as="element(sqf:fix)*">
        <xsl:param name="call-fix" as="element(sqf:call-fix)"/>
        <xsl:variable name="fix" select="$call-fix/parent::sqf:fix"/>
        <xsl:variable name="localAvailableFix" select="sqf:getAvailableFixOrGroups($fix, $LOCAL_ONLY)"/>
        <xsl:variable name="refFix" select="
            for $ref in $call-fix/@ref
            return
            (
            if ($localAvailableFix[@id = $ref] except $fix)
            then
            ($localAvailableFix[@id = $ref])
            else
            (sqf:getAvailableFixOrGroups($fix, $GLOBAL_ONLY)[@id = $ref])
            )[self::sqf:fix]
            "/>
        <xsl:sequence select="$refFix"/>
    </xsl:function>
    <xsl:function name="sqf:hasDescription" as="xs:boolean">
        <xsl:param name="fix" as="element(sqf:fix)"/>
        <xsl:variable name="refFix" select="$fix/sqf:call-fix/sqf:getRefFix(.)"/>
        <xsl:sequence select="
            if ($fix/sqf:description)
            then
            true()
            else
            if (count($refFix) gt 1)
            then
            false()
            else
            if ($refFix[sqf:hasDescription(.)])
            then
            true()
            else
            false()
            "/>
        
    </xsl:function>
    
    <xsl:key name="asserts-fixIds" match="sch:assert | sch:report" use="@sqf:fix/tokenize(., '\s')"/>
    
    <xsl:function name="sqf:isReferered" as="xs:boolean">
        <xsl:param name="fix" as="element(sqf:fix)"/>
        <xsl:variable name="id" select="$fix/@id, $fix/parent::sqf:group/@id"/>
        <xsl:variable name="localAsserts" select="$fix/ancestor::sch:rule/(sch:assert | sch:report)"/>
        <xsl:variable name="referedAsserts" select="key('asserts-fixIds', $id, root($fix))"/>
        <xsl:sequence select="
            exists(if ($localAsserts) then
            ($localAsserts intersect $referedAsserts)
            else
            ($referedAsserts/(sqf:getAvailableFixOrGroups(.) intersect $fix)))"/>
    </xsl:function>
</xsl:stylesheet>