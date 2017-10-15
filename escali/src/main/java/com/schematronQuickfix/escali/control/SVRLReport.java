package com.schematronQuickfix.escali.control;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
import com.github.oxygenPlugins.common.xml.xslt.Parameter;
import com.github.oxygenPlugins.common.xml.xslt.XSLTPipe;
import com.schematronQuickfix.escali.control.report.ModelNodeFac;
import com.schematronQuickfix.escali.control.report.SVRLMessage;
import com.schematronQuickfix.escali.control.report._Report;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escali.resources.EscaliResourcesInterface;

public class SVRLReport {
	public static String HTML_FORMAT = "html";
	public static String ESCALI_FORMAT = "escali";
	public static String TEXT_FORMAT = "text";
	public static String OXYGEN_FORMAT = "oxygen";
	public static String SVRL_FORMAT = "svrl";
	
	public static XPathReader XPR = new XPathReader();

	private XSLTPipe htmlPrinter = new XSLTPipe("Escali HTML output");
	private XSLTPipe textPrinter = new XSLTPipe("Escali text output");
	private XSLTPipe escaliPrinter = new XSLTPipe("Escali SVRL output");
	
	private final StringNode svrl;
	
	
	private File sourceFile;
	
	private _Report report;
	private final HashMap<String, TextSource> fixPartsBySystemId = new HashMap<String, TextSource>();

	
	public SVRLReport(TextSource svrl, ArrayList<TextSource> fixParts, TextSource input, TextSource schema, EscaliResourcesInterface resource) throws XSLTErrorListener, IOException, SAXException, XMLStreamException, XPathExpressionException, DOMException, URISyntaxException{
		
		
		for (TextSource fixPart : fixParts) {
			String systemId = fixPart.getFile().toURI().toString();
			fixPartsBySystemId.put(systemId, fixPart);
		}
		
		htmlPrinter.addStep(resource.getSvrlPrinter("html"));
		textPrinter.addStep(resource.getSvrlPrinter("text"));
		escaliPrinter.addStep(resource.getSvrlPrinter("escali"));
		
		StringNode source = new StringNode(input);
		
		this.sourceFile = input.getFile();
		this.svrl = new StringNode(svrl);
		
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("schema", schema.getFile().toURI()));
		params.add(new Parameter("instance", input.getFile().toURI()));
		StringNode escaliReport = new StringNode(escaliPrinter.pipeMain(svrl, params));
		
		this.report = ModelNodeFac.nodeFac.getReport(XPR.getNode("/es:escali-reports", escaliReport.getDocument()), source);
	}
	
	
	private TextSource getReportAsHTML(){
		return htmlPrinter.pipeMain(this.svrl.getTextSource(), DefaultProcessLoger.getDefaultProccessLogger());
	}
	
	private TextSource getReportAsText(){
		return textPrinter.pipeMain(this.svrl.getTextSource(), DefaultProcessLoger.getDefaultProccessLogger());
	}
	
	private TextSource getReportEscali(){
		return escaliPrinter.pipeMain(this.svrl.getTextSource(), DefaultProcessLoger.getDefaultProccessLogger());
	}
	
	private TextSource getReportOxygen() throws IOException, SAXException, XMLStreamException, XPathExpressionException{
		TextSource oxygenReport = TextSource.createVirtualTextSource(this.svrl.getFile());
		ArrayList<_SVRLMessage> messages = this.report.getMessages();
		for (_SVRLMessage message : messages) {
			String report = oxygenReport.toString() + getOxygenReportEntry(message) + "\n\n";
			oxygenReport.setData(report);
		}
		return oxygenReport;
	}
	
	private String getOxygenReportEntry(_SVRLMessage message) throws XPathExpressionException{
		NodeInfo ni = message.getLocationInIstance();
		Location location = ni.getMarkStartLocation();
	
		String entry = "";
		String level = SVRLMessage.levelToString(message.getErrorLevelInt());
		entry += "Type: " + level.substring(0, 1).toUpperCase() +  "\n";
		entry += "SystemID: " + sourceFile.getAbsolutePath() +  "\n";
		entry += "Line: " + location.getLineNumber() +  "\n";
		entry += "Column: " + (location.getColumnNumber() - 1) +   "\n";
		if(message.hasLink()){
			entry += "AdditionalInfoURL: " + message.getLink();
		}
		entry += "Description: " + message.getName();
		return entry;
	}
	
	public TextSource getSVRL(){
		return this.svrl.getTextSource();
	}
	
	public TextSource getFormatetReport(String type){
		if(type.equals(HTML_FORMAT)){
			return getReportAsHTML();
		} else if(type.equals(TEXT_FORMAT)){
			return getReportAsText();
		} else if(type.equals(ESCALI_FORMAT)){
			return getReportEscali();
		} else if(type.equals(OXYGEN_FORMAT)) {
			try {
				return getReportOxygen();
			} catch (XPathExpressionException | IOException | SAXException
					| XMLStreamException e) {
				return getSVRL();
			}
		} else{
			return getSVRL();
		}
	}
	
	
//	public TextSource getInput(){
//		return this.source.getTextSource();
//	}
	public File getSourceFile(){
		return this.sourceFile;
	}
	
	public _Report getReport(){
		return this.report;
	}


	public HashMap<String, TextSource> getFixParts() {
		return this.fixPartsBySystemId;
	}
}
