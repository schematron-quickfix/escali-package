package com.schematronQuickfix.escaliOxygen.options.association.xmlModel;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xerces.dom.ProcessingInstructionImpl;

import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;

import ro.sync.ecss.dom.wrappers.AuthorPIDomWrapper;




public class XmlModel {
	private static final String HREF = "href";
	private static final String TYPE = "type";
	private static final String SCHEMATYPE_NS = "schematypens";
	private static final String CHARSET = "charset";
	private static final String TITLE = "title";
	private static final String GROUP = "group";
	private static final String PHASE = "phase";
	
	
	public final static Pattern attrPattern = Pattern
			.compile("(^|\\s+)(\\S+?)(\\s+)?=(\\s+)?(\"([^\"]*)\"|'([^']*)')");
	
	private File href = null;
	private String type = null;
	private String schematypens = null;
	private String charset = null;
	private String title = null;
	private String group = null;
	private String phase = "#ALL";
	private String baseURI = null;
	private String value = "";
	
	private XmlModel(){}
	
	private static XmlModel getSchematronModel(String baseURI, EscaliMessanger ema){
		XmlModel model = new XmlModel();
		
		model.baseURI = baseURI;
		model.phase = "#ALL";
		try {
			model.href = new File(ema.getPluginWorkspace().getXMLUtilAccess().resolvePathThroughCatalogs(new URL("http://www.schematron-quickfix.com/escali/schema/SQF/sqf.sch"), "", true, true).toURI());
		} catch (MalformedURLException e) {
			model.href = new File("lib/xml/schema/SQF/sqf.sch");
		} catch (URISyntaxException e) {
			model.href = new File("lib/xml/schema/SQF/sqf.sch");
		}
		model.type = "application/xml";
		model.schematypens = "http://purl.oclc.org/dsdl/schematron";
		
		return model;
	}
	
	public static XmlModel getModel(ProcessingInstructionImpl modelPi) throws URISyntaxException{
		return getModel(modelPi.getNodeValue(), modelPi.getBaseURI());
	}
	
	public static XmlModel getModel(AuthorPIDomWrapper modelPiWrapper) throws URISyntaxException{
		return getModel(modelPiWrapper.getNodeValue(), modelPiWrapper.getWrappedAuthorNode().getXMLBaseURL().toString());
	}
	
	public static XmlModel getModel(String piValue, String baseURI) throws URISyntaxException{
		XmlModel model = new XmlModel();
		
		model.baseURI = baseURI;
		model.value = piValue;
		
		Matcher matcher = attrPattern.matcher(piValue);
		
		
		
		
//		debug-error! can not be debugged...
		while (matcher.find()) {
			String name = matcher.group(2);
			String value = matcher.group(6) != null ? matcher.group(6) : matcher.group(7);
			if (name.equals(HREF)) {
				URI base = URI.create(baseURI);
				URI hrefUri = base.resolve(value);

				model.baseURI = base.toString();
				model.href = new File(hrefUri);
			} else if(name.equals(TYPE)){
				model.type = value;
			} else if(name.equals(SCHEMATYPE_NS)){
				model.schematypens = value;
			} else if(name.equals(CHARSET)){
				model.charset = value;
			} else if(name.equals(TITLE)){
				model.title = value;
			} else if(name.equals(GROUP)){
				model.group = value;
			} else if(name.equals(PHASE)){
				model.phase = value;
			}
		}
		
		return model;
	}

	public File getHref() {
		return href;
	}

	public String getType() {
		return type;
	}

	public String getSchematypens() {
		return schematypens;
	}

	public String getCharset() {
		return charset;
	}

	public String getTitle() {
		return title;
	}

	public String getGroup() {
		return group;
	}

	public String getPhase() {
		return phase;
	}

	public String getBaseURI() {
		return baseURI;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	public void overwritePhase(String phase){
		this.phase = phase;
	}
	
}
