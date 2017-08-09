<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc" xmlns:es="http://www.escali.schematron-quickfix.com/"
    xmlns:c="http://www.w3.org/ns/xproc-step" version="1.0">
    <p:output port="result" primary="true"/>
    <p:import href="../escali-schematron-lib.xpl"/>
    <es:validateAndFix phase="#ALL" msgPos="1" fixName="add2bodyH1" name="es_intern-validation">
        <p:input port="schema">
            <p:document href="../../../../net.sqf.website/in/files/examples/example2/quickFix2.sch"/>
        </p:input>
        <p:input port="source">
            <p:document href="../../../../net.sqf.website/in/files/examples/example2/instance2.xml"/>
        </p:input>
        <p:input port="params">
            <p:inline>
                <c:param-set>
                    <c:param name="h1" value="This is a new headline"/>
                </c:param-set>
            </p:inline>
        </p:input>
    </es:validateAndFix>
</p:declare-step>