package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T10EsGetPhaseFunctTest extends ValidationTestBase {
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> basePhase() {
        return Arrays.asList(
                new String[]{"phase1"},
                new String[]{"#ALL"},
                new String[]{null}
        );
    }
	
    public T10EsGetPhaseFunctTest(String phase) {
		this.phase = phase;
    }
    
    @Override
	public String getFolder() {
		return "test10-es-getPhase-funct";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}

	
	
}
