package com.schematronQuickfix.escali.schematron;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import com.schematronQuickfix.escali.control.SVRLReport;
import org.junit.Before;

import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.escali.helpers.ResourceHelper;
import com.schematronQuickfix.escali.helpers.EscaliValidationTestPair;
import com.schematronQuickfix.escali.helpers.ValidationTestStrategy;

public abstract class ValidationTestBase {
	
	
	private ValidationTestStrategy tester;
	private ResourceHelper resource;
	protected String phase, lang = null;
	
	
	public abstract String getFolder();
	
	@Before
	public void setup(){
		tester  = new ValidationTestStrategy(new MuteProcessLoger());
		resource = new ResourceHelper(getClass(), getFolder());
	}

	public Config getConfig(){
		Config config = ConfigFactory.createDefaultConfig();
		config.setCompactSVRL(false);
		if(phase != null){
			config.setPhase(phase);
		}
		if(lang != null){
			config.setLanguage(lang);
		}
		return config;
	}

	public String getFormat(){
		return SVRLReport.ESCALI_FORMAT;
	}
	
	public SVRLReport doTest(){

		return doTest(getConfig());
	}
	
	public SVRLReport doTest(Config config){
		String[] lang = config.getLanguage();
		String[] phase = config.getPhase();
		String suffix = phase == null ? "" : "_" + phase[0];
		suffix = suffix + (lang == null ? "" : "_" + lang[0]);

		String ext = SVRLReport.ESCALI_FORMAT.equals(getFormat()) ? "es" : "svrl";
		String svrlPath = "expected/test" + suffix + "." + ext;
		return doTest(config, svrlPath);
	}
	
	public SVRLReport doTest(Config config, String expectedSvrl){
		try {
			return tester.testStandardValidation(new EscaliValidationTestPair(resource, getInstancePath(), getSchemaPath(), expectedSvrl, config));
		} catch (IOException e) {
			fail(e.getMessage());
			return null;
		}
	}

	public String getSchemaPath(){
		return "input/test.sch";
	}

	public String getInstancePath(){
		return "input/test.xml";
	}


	public void expectError(Config config, Class<?> errorClass){
		try {
			tester.executeStandardValidation(new EscaliValidationTestPair(resource, getInstancePath(), getSchemaPath(), getInstancePath(), config));
			fail("Test execution did not failed though error with class " + errorClass.getName() + " expected.");
		} catch (Exception e){
			assertEquals(e.getClass().getName(), errorClass.getName());
		}

	}
}
