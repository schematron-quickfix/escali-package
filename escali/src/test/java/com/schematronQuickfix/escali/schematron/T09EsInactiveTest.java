package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T09EsInactiveTest extends ValidationTestBase {
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> basePhase() {
        return Arrays.asList(
                new String[]{"phase1"},
                new String[]{"phase2"},
                new String[]{"phase3"},
                new String[]{null}
        );
    }
	
    public T09EsInactiveTest(String phase) {
		this.phase = phase;
    }
    
    @Override
	public String getFolder() {
		return "test09-es-inactive";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}

	
	
}
