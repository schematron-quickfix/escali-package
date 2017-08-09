package com.schematronQuickfix.escali.control.report;

import java.net.URISyntaxException;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;

public class Phase extends Report implements _Phase {
	public static final int STATE_ACTIVE = 0;
	public static final int STATE_INACTIVE = 1;
	public static final int STATE_INCLUDED = 2;
	
	private final int state;
	private final String phaseId;
	private final boolean isDefault;
	
	protected Phase(Node phase, _Report report, int svrlIdx, StringNode instance)
			throws DOMException, URISyntaxException, XPathExpressionException {
		super(phase, svrlIdx, instance, report);
		
		XPathReader xpath = new XPathReader();
		
		if(xpath.getBoolean("@isActive = 'yes' ", phase)){
			state = STATE_ACTIVE;
		} else if(xpath.getBoolean("@isActive = 'included' ", phase)){
			state = STATE_INCLUDED;
		} else {
			state = STATE_INACTIVE;
		}
		
		isDefault = xpath.getBoolean("@isDefault = 'true'", phase);
		phaseId = xpath.getAttributValue(phase, "id");
		this.setId(phaseId);
		this.setName(xpath.getAttributValue(phase, "title", ProcessNamespaces.ES_NS, phaseId));
		
		for (_Pattern pattern : report.getPattern(phaseId)) {
			this.addChild(pattern);
		}
	}
	
	public int getState() {
		return state;
	}
	
	public String getPhaseId(){
		return phaseId;
	}
	
	@Override
	public boolean isDefault(){
		return this.isDefault;
	}
}
