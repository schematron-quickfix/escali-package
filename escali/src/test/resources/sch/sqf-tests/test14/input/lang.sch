<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" icon="../../../icons/sqf-logo-tiny.ico" xmlns:html="http://www.w3.org/1999/xhtml" xml:lang="en">
    <ns uri="http://www.w3.org/1999/xhtml" prefix="html"/>
    <title>Two different language attributes.</title>
    <pattern id="pattern1">
        <title>Pattern 1</title>
        <p>para of a rule</p>
        <rule context="html:html" role="info">
            <assert test="@lang" diagnostics="lang_miss">The attribute "lang" is missing.</assert>
            <assert test="@xml:lang" diagnostics="xml_lang_miss">The attribute "xml:lang" is missing.</assert>
            <assert test="@lang = @xml:lang" diagnostics="lang_xml_lang_miss"> The attributes "lang" and "xml:lang" should have the same value.</assert>
        </rule>
    </pattern>
    <diagnostics>
        <diagnostic id="lang_miss" xml:lang="de">Das Attribut lang fehlt.</diagnostic>
        <diagnostic id="xml_lang_miss" xml:lang="de">Das Attribut xml:lang fehlt.</diagnostic>
        <diagnostic id="lang_xml_lang_miss" xml:lang="de">Das Attribut lang und xml:lang fehlt.</diagnostic>
    </diagnostics>
</schema>