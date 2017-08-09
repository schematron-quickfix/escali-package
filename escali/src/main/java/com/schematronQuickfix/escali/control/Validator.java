package com.schematronQuickfix.escali.control;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.transform.Source;

import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.xslt.Parameter;
import com.github.oxygenPlugins.common.xml.xslt.XSLTPipe;
import com.schematronQuickfix.escali.resources.EscaliRsourcesInterface;


public class Validator {
	private TextSource svrl;

	private XSLTPipe precompilerPipe;
	private XSLTPipe compilerPipe;
	private XSLTPipe validatorPipe;

	private TextSource schema;

	private final EscaliRsourcesInterface resource;

	private ArrayList<TextSource> fixParts;

	private final Config config;
	
	
	public Validator(EscaliRsourcesInterface resource, Config config)
			throws XSLTErrorListener, FileNotFoundException {
		this.resource = resource;
		this.config = config;
		compilerPipe = new XSLTPipe("Escali compiling");
		Source[] compiler = resource.getCompiler();
		compilerPipe.addStep(compiler);

		precompilerPipe = new XSLTPipe("Escali pre compiling");
		Source[] precompiler = resource.getPreCompiler();
		precompilerPipe.addStep(precompiler);
	}

	protected TextSource preCompileSchema(TextSource schema,
			ProcessLoger loger) {
		loger.log("Create validator");
		return precompilerPipe.pipeMain(schema, config.createCompilerParams(),
				new DefaultProcessLoger());
	}

	protected void compileSchema(TextSource schema,
			ProcessLoger loger) throws XSLTErrorListener, FileNotFoundException {
		this.schema = schema;
		loger.log("Create validator");
		TextSource validator = compilerPipe.pipeMain(schema,
				config.createCompilerParams());
		loger.log("Implement validator");
		createValidatorPipe(validator);

	}

	private void createValidatorPipe(TextSource validator1)
			throws XSLTErrorListener, FileNotFoundException {
		this.validatorPipe = new XSLTPipe("Escali validate");
		validatorPipe.addStep(validator1);
		validatorPipe.addStep(resource.getValidator());

	}

	protected void validateInstance(TextSource xml,
			ArrayList<Parameter> params, ProcessLoger logger) {
		ArrayList<TextSource> pipeMain = validatorPipe.pipeAll(xml, params, logger);
		this.svrl = pipeMain.get(0);
		pipeMain.remove(0);
		this.fixParts = new ArrayList<TextSource>(pipeMain);
		
	}

	protected TextSource getSvrl() {
		return this.svrl;
	}
	protected ArrayList<TextSource> getFixParts(){
		return this.fixParts;
	}

	public TextSource getSchema() {
		// TODO Auto-generated method stub
		return this.schema;
	}

}
