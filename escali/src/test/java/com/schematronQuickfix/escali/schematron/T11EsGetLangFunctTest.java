package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T11EsGetLangFunctTest extends ValidationTestBase {
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> baseLang() {
        return Arrays.asList(
                new String[]{"en"},
                new String[]{"#ALL"},
                new String[]{null}
        );
    }
	
    public T11EsGetLangFunctTest(String lang) {
		this.lang = lang;
    }
    
    @Override
	public String getFolder() {
		return "test11-es-getLang-funct";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}

	
	
}
