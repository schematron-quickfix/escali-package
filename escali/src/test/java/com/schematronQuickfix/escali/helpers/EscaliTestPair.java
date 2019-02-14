package com.schematronQuickfix.escali.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.escali.control.Config;
import static com.schematronQuickfix.escali.control.ConfigFactory.createDefaultConfig;

public class EscaliTestPair {
	
	private SchematronInstancePair inputPair;
	private TextSource[] expectedResult;
	private Config escaliConfig;
	private Properties testProperties = new Properties();
	
	public EscaliTestPair(SchematronInstancePair inputPair, ResourceHelper res, String[] expectedResult, Config escaliConfig) throws IOException {
		this(
				inputPair,
				res.textSource(expectedResult),
				escaliConfig
		);
	}
	public EscaliTestPair(SchematronInstancePair inputPair, TextSource[] expectedResult, Config escaliConfig) {
		this.inputPair = inputPair;
		this.expectedResult = expectedResult;
		this.escaliConfig = escaliConfig;
	}
	
	public EscaliTestPair(ResourceHelper res, String instancePath, String schemaPath, String[] svrlPath, Config escaliConfig) throws IOException {
		this(
				new SchematronInstancePair(res, instancePath, schemaPath),
				res.textSource(svrlPath),
				escaliConfig
				);
	}

	public EscaliTestPair(ResourceHelper res, String[] expectPath, Config escaliConfig) throws IOException {
		this(
				new SchematronInstancePair(res),
				res.textSource(expectPath),
				escaliConfig
				);
	}

	public EscaliTestPair(ResourceHelper res, String expectPath, Config escaliConfig) throws IOException {
		this(res, new String[]{expectPath}, escaliConfig);
	}

	public EscaliTestPair(ResourceHelper res, String[] expectPath) throws IOException {
		this(
				new SchematronInstancePair(res),
				res.textSource(expectPath),
				createDefaultConfig()
				);
	}
	public EscaliTestPair(ResourceHelper res, String expectPath) throws IOException {
		this(res, new String[]{expectPath});
	}

	public SchematronInstancePair getInputPair() {
		return inputPair;
	}
	
	public ArrayList<TextSource> getExpected() {
		ArrayList<TextSource> expectList = new ArrayList<>();
		for (TextSource result: expectedResult
			 ) {
			expectList.add(result);
		}
		return expectList;
	}
	
	public Config getEscaliConfig() {
		return escaliConfig;
	}

	public EscaliTestPair addTestProperty(String key, String value){
		this.testProperties.setProperty(key, value);
		return this;
	}

	public String getTestProperty(String key){
		return this.testProperties.getProperty(key);
	}
	
}
