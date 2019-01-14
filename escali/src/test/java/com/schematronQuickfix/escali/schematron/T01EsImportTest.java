package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T01EsImportTest extends ValidationTestBase {
	
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> basePhases() {
        return Arrays.asList(
                new String[]{"#ALL"},
                new String[]{null}
        );
    }
	
    
	public T01EsImportTest(String phase) {
		this.phase = phase;
    }
	
    
    @Override
	public String getFolder() {
		return "test01-es-import";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}
	
}
