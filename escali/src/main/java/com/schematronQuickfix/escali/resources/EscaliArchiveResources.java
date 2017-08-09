package com.schematronQuickfix.escali.resources;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class EscaliArchiveResources implements EscaliRsourcesInterface {
	private final String escaliFolder;
	private final String valFolder;
	private final String extractorFolder;
	private final String path;
	private String manipulatorFolder;

	private Source getInputStream(String dir, String path) throws FileNotFoundException {
		String systemId = dir + path;
		Class<? extends EscaliArchiveResources> cl = this.getClass();
		InputStream ss = cl.getResourceAsStream(systemId);
		Source src = new StreamSource(ss);
		URL url = cl.getResource(systemId);
		src.setSystemId(url.toExternalForm());
		return src;
	}

	private Source[] getInputStream(String dir, String[] path) throws FileNotFoundException {
		Source[] isArr = new Source[path.length];
		for (int i = 0; i < path.length; i++) {
			if (path[i] != null) {
				isArr[i] = getInputStream(dir, path[i]);
			} else {
				isArr[i] = null;
			}
		}
		return isArr;
	}

	public EscaliArchiveResources(String path) {
		this.path = path;
		this.escaliFolder = path + "xsl/";
		this.valFolder = escaliFolder + "02_validator/";
		this.extractorFolder = escaliFolder + "03_extractor/";
		this.manipulatorFolder = escaliFolder + "04_manipulator/";
	}

	public EscaliArchiveResources() {
		this("/");
	}

	public Source getConfig() throws FileNotFoundException {
		return getInputStream(path, "config.xml");
	}

	public Source getSchemaInfo() throws FileNotFoundException {
		String compFolder = escaliFolder + "01_compiler/";
		return getInputStream(compFolder, "escali_compiler_0_getSchemaInfo.xsl");
	}

	//
	// Compiler
	//

	public Source[] getCompiler() throws FileNotFoundException {
		String compFolder = escaliFolder + "01_compiler/";
		String[] paths = { "escali_compiler_1_include.xsl", "escali_compiler_2_abstract-patterns.xsl",
				"escali_compiler_3_main.xsl" };
		return getInputStream(compFolder, paths);
	}

	public Source[] getPreCompiler() throws FileNotFoundException {
		String compFolder = escaliFolder + "01_compiler/";
		String[] paths = { "escali_compiler_0_validate.xsl" };
		return getInputStream(compFolder, paths);
	}

	//
	// Validator
	//

	public Source[] getValidator() throws FileNotFoundException {
		return getInputStream(valFolder, new String[] { "escali_validator_2_postprocess.xsl" });
	}

	public Source getSvrlPrinter(String type) throws FileNotFoundException {
		String prx = type.toLowerCase();
		return getInputStream(valFolder, "escali_validator_3_" + prx + "-report.xsl");
	}

	//
	// Resolver
	//

	public Source getExtractor() throws FileNotFoundException {
		return getInputStream(extractorFolder, "escali_extractor_1_main.xsl");
	}

	@Override
	public Source[] getManipulator() throws FileNotFoundException {
		return getInputStream(manipulatorFolder, new String[] {"escali_manipulator_2_postprocess.xsl" });
	}

	@Override
	public Source getSchematronSchema() throws FileNotFoundException {
		return getInputStream(this.path + "schema/SQF/", "iso-schematron.xsd");
	}

	@Override
	public Source getSchematronForSchematron() throws FileNotFoundException {
		return getInputStream(this.path + "schema/SQF/", "sqf.sch");
	}

}
