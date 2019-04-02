package com.schematronQuickfix.escali.optimization.schematron;

import com.github.oxygenPlugins.common.gui.types.converter.EnumTypeConverter;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.control.report._UserEntry;
import com.schematronQuickfix.escali.schematron.ValidationTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class T24UserEntryDefaultSequence extends ValidationTestBase {


	@Override
	public String getFolder() {
		return "../test24-user-entry-default-sequence";
	}


	@Override
	public String getSchemaPath() {
		return "input/user-entry-default-sequence.sch";
	}
	@Override
	public String getInstancePath() {
		return "input/user-entry-default-sequence.xml";
	}

	@Override
	public String getFormat() {
		return SVRLReport.ESCALI_FORMAT;
	}

	@Override
	public Config getConfig() {
		Config config = super.getConfig();
		config.setInternalValidation(false);
		return config;
	}

	@Test
	public void test(){
		SVRLReport report = doTest();
		_UserEntry ue = report.getReport().getMessages().get(0).getQuickFixes()[0].getParameter()[0];

		if(ue.getTypeConverter() instanceof EnumTypeConverter){
			EnumTypeConverter typeConverter = (EnumTypeConverter) ue.getTypeConverter();
			assertArrayEquals(typeConverter.getEnumValuesAsString(), new String[]{"bar1", "bar2", "bar3", "bar4"});
		} else {
			fail("UE should have an enumeration type");
		}
		
	}
	
}
