<?xml version="1.0" encoding="UTF-8"?>
<declare-step xmlns="http://www.w3.org/ns/xproc" version="1.0">
    <input port="source" sequence="true" primary="true"/>
    <input port="parameters" kind="parameter" primary="true"/>
    
    <output port="validator">
        <pipe step="compiler" port="validator"/>
    </output>
    <import href="escali-schematron-lib.xpl"/>
    <es:compile xmlns:es="http://www.escali.schematron-quickfix.com/" name="compiler"/>
</declare-step>