package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T02NestedEsImportTest extends ValidationTestBase {
	
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> basePhases() {
        return Arrays.asList(
                new String[]{"#ALL"},
                new String[]{"importPhase"}
        );
    }
	
    public T02NestedEsImportTest(String phase) {
    	this.phase = phase;
    }
	
    
    @Override
	public String getFolder() {
		return "test02-nested-es-import";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}


	
}
