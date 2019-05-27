package com.schematronQuickfix.escali.helpers;

import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.escali.control.Config;

import java.io.IOException;
import java.util.Properties;

import static com.schematronQuickfix.escali.control.ConfigFactory.createDefaultConfig;

public class EscaliValidationTestPair {

	private SchematronInstancePair inputPair;
	private ExpectedReportData expectedResult;
	private Config escaliConfig;
	private Properties testProperties = new Properties();

	public EscaliValidationTestPair(SchematronInstancePair inputPair, ResourceHelper res, String expectedResult, Config escaliConfig) throws IOException {
		this(
				inputPair,
				res.textSource(expectedResult),
				escaliConfig
		);
	}

	public EscaliValidationTestPair(SchematronInstancePair inputPair, ExpectedReportData expectedResult, Config escaliConfig) {
		this.inputPair = inputPair;
		this.expectedResult = expectedResult;
		this.escaliConfig = escaliConfig;
	}
	public EscaliValidationTestPair(SchematronInstancePair inputPair, TextSource expectedResult, Config escaliConfig) {
		this(
				inputPair,
				ExpectedReportData.createExportReportData(expectedResult),
				escaliConfig
		);
	}

	public EscaliValidationTestPair(ResourceHelper res, String instancePath, String schemaPath, String svrlPath, Config escaliConfig) throws IOException {
		this(
				new SchematronInstancePair(res, instancePath, schemaPath),
				res.textSource(svrlPath),
				escaliConfig
				);
	}

	public EscaliValidationTestPair(ResourceHelper res, String expectPath, Config escaliConfig) throws IOException {
		this(
				new SchematronInstancePair(res),
				res.textSource(expectPath),
				escaliConfig
				);
	}


	public EscaliValidationTestPair(ResourceHelper res, String expectPath) throws IOException {
		this(
				new SchematronInstancePair(res),
				res.textSource(expectPath),
				createDefaultConfig()
				);
	}

	public SchematronInstancePair getInputPair() {
		return inputPair;
	}
	
//	public ArrayList<TextSource> getExpected() {
//		ArrayList<TextSource> expectList = new ArrayList<>();
//		for (TextSource result: expectedResult
//			 ) {
//			expectList.add(result);
//		}
//		return expectList;
//	}

	public ExpectedReportData getExpected(){
		return this.expectedResult;
	}
	
	public Config getEscaliConfig() {
		return escaliConfig;
	}

	public EscaliValidationTestPair addTestProperty(String key, String value){
		this.testProperties.setProperty(key, value);
		return this;
	}

	public String getTestProperty(String key){
		return this.testProperties.getProperty(key);
	}
	
}
