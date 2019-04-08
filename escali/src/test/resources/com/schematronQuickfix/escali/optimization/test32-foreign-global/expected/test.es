<?xml version="1.0" encoding="UTF-8"?>
<es:escali-reports xmlns:es="http://www.escali.schematron-quickfix.com/"
    xml:base="file:/C:/Users/Nico/Work/Java2/escali-main-package/escali/target/test-classes/com/schematronQuickfix/escali/optimization/test01-trivial-delete/input/trivial-delete.xml" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:sch="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <es:meta activePatterns="pattern1"
        instance="#IGNORED#"
        phase="#ALL"
        schema=""
        title="Schematron unit test - foreign global">
        <es:title>Schematron unit test - foreign global</es:title>
        <es:schema>#IGNORED#</es:schema>
        <es:instance>#IGNORED#</es:instance>
        <es:phase id="#ALL" isActive="yes" isDefault="true"/>
        <es:ns-prefix-in-attribute-values prefix="foo" uri="foo.com"/>
        
        <xsl:key name="foo-id" match="foo" use="@id"/>
        
        <xsl:function name="foo:id">
            <xsl:param name="foo"/>
            <xsl:sequence select="$foo/(@id, concat('id_', count($foo/preceding::foo) + 1))[1]"/>
        </xsl:function>
        
        <sch:let name="allFooBar" value="//(foo|bar)"/>
        <xsl:variable name="root" select="/"/>
        <xsl:template name="createBar">
            <xsl:param name="id"/>
            <xsl:param name="counter"/>
            
            <xsl:variable name="isGeneric" select="not(key('foo-id', $id, $root))"/>
            
            <xsl:for-each select="1 to $counter">
                <bar barId="{$id}_{.}" generic="{$isGeneric}"/>
            </xsl:for-each>
        </xsl:template>
        
    </es:meta>
    <es:pattern>
        <es:meta id="pattern1" phases="#ALL"/>
        <es:rule>
            <es:meta context="foo" id="w20aab3b1" roleLabel="error">
                <sqf:fix id="replaceBar">
                    <sqf:replace match="bar">
                        <xsl:call-template name="createBar">
                            <xsl:with-param name="id" select="foo:id(..)"/>
                            <xsl:with-param name="counter" select="count($allFooBar)"/>
                        </xsl:call-template>
                    </sqf:replace>
                </sqf:fix>
            </es:meta>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][1]"
                roleLabel="error"
                test="bar/@id[contains(., foo:id(current()))]">
                <es:text>Bar should contain id of foo.</es:text>
                <sqf:fix fixId="replaceBar" title="Replace bar with for each foo one bar." id="replaceBar_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="replaceBar"/>
                </sqf:fix>
            </es:assert>
            <es:assert id="w41aab1_w20aab3b1b1"
                location="/*:root[namespace-uri()=''][1]/*:foo[namespace-uri()=''][2]"
                roleLabel="error"
                test="bar/@id[contains(., foo:id(current()))]">
                <es:text>Bar should contain id of foo.</es:text>
                <sqf:fix fixId="replaceBar" title="Replace bar with for each foo one bar." id="replaceBar_w41aab1_w20aab3b1b1">
                    <sqf:call-fix ref="replaceBar"/>
                </sqf:fix>
            </es:assert>
        </es:rule>
    </es:pattern>
</es:escali-reports>