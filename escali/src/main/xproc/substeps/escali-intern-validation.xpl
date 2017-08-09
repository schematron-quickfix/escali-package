<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:es="http://www.escali.schematron-quickfix.com/"
    xmlns:c="http://www.w3.org/ns/xproc-step" version="1.0">
    <p:input port="schema" primary="true"/>
    <p:output port="result" primary="true"/>
    <p:output port="report">
        <p:pipe port="report" step="es_intern-validation"/>
    </p:output>
    <p:import href="../escali-schematron-lib.xpl"/>
    <es:intern-validation name="es_intern-validation"/>
</p:declare-step>