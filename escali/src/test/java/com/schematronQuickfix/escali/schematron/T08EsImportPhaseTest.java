package com.schematronQuickfix.escali.schematron;

import org.junit.Test;

public class T08EsImportPhaseTest extends ValidationTestBase {
	
	
    public T08EsImportPhaseTest() {
		this.phase = "phase1";
    }
    
    @Override
	public String getFolder() {
		return "test08-es-import-phase";
	}
    
	
	@Test
	public void test(){
		doTest();
		
	}

	
	
}
