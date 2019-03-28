package com.schematronQuickfix.escali.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
import com.github.oxygenPlugins.common.xml.xslt.Parameter;

public class Config {
	
	private static XPathReader xpr = new XPathReader();
	
	private StringNode doc;
	
	private File tempFolder;
	private String[] language = null;
	private String[] phase = null;
	private boolean xmlSaveMode = true;
	private boolean compactSVRL = true;
	
	private String changePrefix = "sqfc";
	private boolean supressSQF = false;
	private boolean xinclude = true;
	private boolean internalValidation = true;
	
	private ArrayList<Parameter> validationParams = new ArrayList<Parameter>();
	
	
	protected Config(){
	}
	
	protected Config(TextSource conf) throws IOException, SAXException, XMLStreamException, XPathExpressionException{
		this.doc = new StringNode(conf);
		
		this.tempFolder = new File(this.doc.getFile(), "../" + xpr.getString("/es:config/es:tempFolder", doc.getDocument()));
		
		String phase = xpr.getString("/es:config/es:phase", doc.getDocument());
		this.phase = phase.equals("") || phase.equals("#DEFAULT") ? null : phase.split("\\s");
		
		this.xmlSaveMode = xpr.getBoolean("/es:config/es:output/es:xml-save-mode = 'true'", doc.getDocument());
		this.compactSVRL = xpr.getBoolean("not(/es:config/es:output/es:compact-svrl = 'false')", doc.getDocument());
		
		this.changePrefix = xpr.getString("/es:config/es:output/es:changePrefix", doc.getDocument());
	}
	

	public File getTempFolder(){
		return this.tempFolder;
	}
	
	public boolean hasPhase(){
		return this.phase != null;
	}
	public boolean hasLanguage(){
		return this.language != null;
	}
	
	public String[] getPhase(){
		return this.phase;
	}
	public String[] getLanguage(){
		return this.language;
	}
	public boolean isXmlSaveMode(){
		return this.xmlSaveMode;
	}
	public boolean isCompactSVRL(){
		return this.compactSVRL;
	}
	public boolean resolvesXInclude(){
		return this.xinclude;
	}
	
	public void setPhase(String phase){
		this.phase = new String[]{phase};
	}
	public void setPhase(String[] phases){
		this.phase = phases;
	}
	
	public void setChangePrefix(String prefix){
		this.changePrefix = prefix;
	}

	public void setLanguage(String language){
		this.language = new String[]{language};
	}
	
	public void setLanguage(String[] language){
		this.language = language;
	}
	
	public void setXmlSaveMode(Boolean xmlSaveMode){
		this.xmlSaveMode = xmlSaveMode;
	}
	
	public void setCompactSVRL(boolean compactSVRL){
		this.compactSVRL = compactSVRL;
	}
	
	public void setTempFolder(File tempFolder){
		this.tempFolder = tempFolder;
	}
	public void setResolvesXIncludes(boolean xinclude){
		this.xinclude = xinclude;
	}

	public String getChangePrefix() {
		return this.changePrefix;
	}
	
	public ArrayList<Parameter> createCompilerParams(){
		ArrayList<Parameter> compileParams = new ArrayList<Parameter>();
		if(hasLanguage()){
			compileParams.add(new Parameter("lang", ProcessNamespaces.ES_NS, 0, getLanguage()));
			}
		if(hasPhase()){
			compileParams.add(new Parameter("phase", 2, getPhase()));
		}
		
		compileParams.add(new Parameter("changePrefix", ProcessNamespaces.SQF_NS, 2, getChangePrefix()));
		compileParams.add(new Parameter("xml-save-mode", ProcessNamespaces.XSM_NS, 2, this.xmlSaveMode));
		compileParams.add(new Parameter("compact-svrl", ProcessNamespaces.ES_NS, 2, this.compactSVRL));
		if(this.supressSQF){
			compileParams.add(new Parameter("useSQF", ProcessNamespaces.SQF_NS, 2, false));
		}
		
		
		return compileParams;
	}

	public ArrayList<Parameter> createManipulatorParams() {
		ArrayList<Parameter> manipulatorParams = new ArrayList<Parameter>();
		manipulatorParams.add(new Parameter("xml-save-mode", ProcessNamespaces.XSM_NS, this.xmlSaveMode));
		return manipulatorParams;
	}
	
	public void addValidationParam(String name, Object value) {
		validationParams.add(new Parameter(name, value));
	}
	
	public ArrayList<Parameter> createValidationParams() {
		return validationParams;
	}

    public void setInternalValidation(boolean internalValidation) {
		this.internalValidation = internalValidation;
    }
    public boolean isInternalValidation(){return this.internalValidation;}
}
