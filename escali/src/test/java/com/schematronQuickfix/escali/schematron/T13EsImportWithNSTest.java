package com.schematronQuickfix.escali.schematron;

import org.junit.Test;

public class T13EsImportWithNSTest extends ValidationTestBase {
	
	
    
    @Override
	public String getFolder() {
		return "test13-es-import-with-ns";
	}
    
	
	@Test
	public void test(){
		doTest();
	}

	
	
}
