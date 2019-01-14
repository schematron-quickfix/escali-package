package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T06RuleExtendsTest extends ValidationTestBase {
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> basePhase() {
        return Arrays.asList(
                new String[]{"phase1"},
                new String[]{"phase2"},
                new String[]{"phase3"}
        );
    }
	
    public T06RuleExtendsTest(String phase) {
		this.phase = phase;
    }
    
    @Override
	public String getFolder() {
		return "test06-rule-extends-test";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}

	
	
}
