<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:es="http://www.escali.schematron-quickfix.com/" queryBinding="xslt2">
    <title>Schematron unit test</title>
    <!--    
    Trivial phase reference
    Active patterns:
        - pattern1
        - pattern2 (ref to helper2)
    -->

    <phase id="phase1">
        <active pattern="pattern1"/>
        <es:phase ref="helper2"/>
    </phase>

    <!--    
    Multiple references
    Active patterns:
        - pattern3 (ref to helper3)
        - pattern4 (ref to helper4)
    -->
    <phase id="phase2">
        <es:phase ref="helper3"/>
        <es:phase ref="helper4"/>
    </phase>
    
<!--    
    Nested references
    Active patterns:
    - pattern1 (ref to phase1)
    - pattern2 (ref to phase1 -> helper2)
    - pattern3 (ref to phase2 -> helper3)
    - pattern4 (ref to phase2 -> helper4)
    -->
    <phase id="phase3">
        <es:phase ref="phase1"/>
        <es:phase ref="phase2"/>
    </phase>
    
<!--    
    Recoursive references
    Active patterns:
    - pattern2
    - pattern3
    - pattern1 (ref to helper5)
    -->
    <phase id="phase4">
        <es:phase ref="helper5"/>
        <active pattern="pattern2"/>
        <active pattern="pattern3"/>
    </phase>
    
    <!--    
    Helper phases
    -->
    <phase id="helper1">
        <active pattern="pattern1"/>
    </phase>
    <phase id="helper2">
        <active pattern="pattern2"/>
    </phase>
    <phase id="helper3">
        <active pattern="pattern3"/>
    </phase>
    <phase id="helper4">
        <active pattern="pattern4"/>
    </phase>
    <phase id="helper5">
        <active pattern="pattern1"/>
        <active pattern="pattern2"/>
        <es:phase ref="phase4"/>
    </phase>

    <!--    phase1 phase3 phase4 #ALL-->
    <pattern id="pattern1">
        <rule context="pattern1">
            <assert test="false()" id="t1">Test 1</assert>
        </rule>
    </pattern>
    <!--    phase1 phase2 phase3 #ALL-->
    <pattern id="pattern2">
        <rule context="pattern2">
            <assert test="false()" id="t2">Test 2</assert>
        </rule>
    </pattern>
    <!--    phase3 #ALL-->
    <pattern id="pattern3">
        <rule context="pattern3">
            <assert test="false()" id="t3">Test 3</assert>
        </rule>
    </pattern>
    <!--    #ALL-->
    <pattern id="pattern4">
        <rule context="pattern4">
            <assert test="false()" id="t4">Test 4</assert>
        </rule>
    </pattern>

</schema>
