package com.schematronQuickfix.escali.helpers;

import java.io.IOException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.escali.control.Config;
import static com.schematronQuickfix.escali.control.ConfigFactory.createDefaultConfig;

public class ValidationTestPair {
	
	private SchematronInstancePair inputPair;
	private TextSource expectedSvrl;
	private Config escaliConfig;
	
	private ValidationTestPair(SchematronInstancePair inputPair, TextSource expectedSvrl, Config escaliConfig) {
		this.inputPair = inputPair;
		this.expectedSvrl = expectedSvrl;
		this.escaliConfig = escaliConfig;
	}
	
	public ValidationTestPair(ResourceHelper res, String instancePath, String schemaPath, String svrlPath, Config escaliConfig) throws IOException {
		this(
				new SchematronInstancePair(res, instancePath, schemaPath),
				res.textSource(svrlPath),
				escaliConfig
				);
	}
	
	public ValidationTestPair(ResourceHelper res, String svrlPath, Config escaliConfig) throws IOException {
		this(
				new SchematronInstancePair(res),
				res.textSource(svrlPath),
				escaliConfig
				);
	}
	
	public ValidationTestPair(ResourceHelper res, String svrlPath) throws IOException {
		this(
				new SchematronInstancePair(res),
				res.textSource(svrlPath),
				createDefaultConfig()
				);
	}
	
	
	
	public SchematronInstancePair getInputPair() {
		return inputPair;
	}
	
	public TextSource getExpectedSvrl() {
		return expectedSvrl;
	}
	
	public Config getEscaliConfig() {
		return escaliConfig;
	}
	
}
