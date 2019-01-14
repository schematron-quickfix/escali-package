package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T04AbstractPatternsTest extends ValidationTestBase {
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> basePhases() {
        return Arrays.asList(
                new String[]{"#ALL"},
                new String[]{"phase1"},
                new String[]{"phase2"}
        );
    }
	
    public T04AbstractPatternsTest(String phase) {
		this.phase = phase;
    }
    
    @Override
	public String getFolder() {
		return "test04-abstract-patterns";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}

	
	
}
