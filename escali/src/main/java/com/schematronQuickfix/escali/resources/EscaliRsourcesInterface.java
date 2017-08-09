package com.schematronQuickfix.escali.resources;

import java.io.FileNotFoundException;

import javax.xml.transform.Source;

public interface EscaliRsourcesInterface {
	public Source getConfig() throws FileNotFoundException;
	public Source getSchemaInfo() throws FileNotFoundException;
	public Source[] getCompiler() throws FileNotFoundException;
	public Source[] getPreCompiler() throws FileNotFoundException;
	public Source[] getValidator() throws FileNotFoundException;
	public Source getSvrlPrinter(String type) throws FileNotFoundException;
	public Source getExtractor() throws FileNotFoundException;
	public Source[] getManipulator() throws FileNotFoundException;
	public Source getSchematronSchema() throws FileNotFoundException;
	Source getSchematronForSchematron() throws FileNotFoundException;
}
