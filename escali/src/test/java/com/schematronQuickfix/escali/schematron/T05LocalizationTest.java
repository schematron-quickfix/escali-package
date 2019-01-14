package com.schematronQuickfix.escali.schematron;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class T05LocalizationTest extends ValidationTestBase {
	
	@Parameterized.Parameters(name = "{0}")
    public static List<String[]> baseLang() {
        return Arrays.asList(
                new String[]{"en"},
                new String[]{"de"}
        );
    }
	
    public T05LocalizationTest(String lang) {
		this.lang = lang;
    }
    
    @Override
	public String getFolder() {
		return "test05-localization";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}

	
	
}
