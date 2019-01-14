package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T03EsPhaseInactiveTest extends ValidationTestBase {
	
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> basePhases() {
        return Arrays.asList(
                new String[]{"phase1"},
                new String[]{"phase3"},
                new String[]{"phase4"}
        );
    }
	
    public T03EsPhaseInactiveTest(String phase) {
    	this.phase = phase;
    }
	
    
    @Override
	public String getFolder() {
		return "test03-es-phase-inactive";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}

}
