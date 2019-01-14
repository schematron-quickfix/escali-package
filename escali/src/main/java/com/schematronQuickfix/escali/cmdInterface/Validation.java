package com.schematronQuickfix.escali.cmdInterface;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.escali.control.Escali;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.resources.EscaliArchiveResources;

public class Validation {
	private Escali escali;
//	private Scanner cmdInput = new Scanner(System.in);
	
	public Validation(File schema, ProcessLoger logger) throws XSLTErrorListener, IOException, CancelException{
		this(schema, ConfigFactory.createDefaultConfig(), logger);
	}
	
	public Validation(File schema, Config config, ProcessLoger logger) throws XSLTErrorListener, IOException, CancelException{
		this(TextSource.readTextFile(schema), config, logger);
	}
	
	public Validation(TextSource schema, Config config, ProcessLoger logger) throws XSLTErrorListener, IOException, CancelException{
		this.escali = new Escali(config, new EscaliArchiveResources());
		this.escali.compileSchema(schema, logger);
	}
	
	public SVRLReport validate(File instance) throws IOException, XPathExpressionException, XSLTErrorListener, SAXException, URISyntaxException, XMLStreamException{
		TextSource textSource = TextSource.readTextFile(instance);
		return validate(textSource);
	}
	
	public SVRLReport validate(TextSource source) throws XPathExpressionException, XSLTErrorListener, IOException, SAXException, URISyntaxException, XMLStreamException{
		return this.escali.validate(source, DefaultProcessLoger.getDefaultProccessLogger());
	}
	
	public void executeFix(String fixId) throws XSLTErrorListener, IOException {
//		_Report reportObj = this.escali.getReport().getReport();
//		_ModelNode node = reportObj.getChildById(fixId);
//		_QuickFix[] fixes;
//		if(node != null && node instanceof _QuickFix){
//			fixes = new _QuickFix[]{(_QuickFix) node};
//		} else {
//			fixes = new _QuickFix[]{};
//		}
//		escali.executeFix(fixes);
	}
	
	public void interactive() {
		SVRLReport report = this.escali.getReport();
		TextSource ts = report.getFormatetReport(SVRLReport.TEXT_FORMAT);
		System.out.println(ts.toString());
		System.out.println("Choose your quickfix:");
		
		
		
	}
	
}
