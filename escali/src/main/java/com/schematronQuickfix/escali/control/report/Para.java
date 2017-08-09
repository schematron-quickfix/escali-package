package com.schematronQuickfix.escali.control.report;

import java.net.URISyntaxException;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
import com.schematronQuickfix.escali.control.SVRLReport;

public class Para extends ModelNode implements _Para {
	
	private String groupId;
	private _Report report;
	private boolean isPattern;

	protected Para(Node node, _Report report, int svrlIdx, int index) throws DOMException, URISyntaxException {
		super(node, svrlIdx);
		setId("para-" + index);
		setIndex(index);
		setName(node.getTextContent());
		this.report = report;

		XPathReader xpathreader = new XPathReader();
		String ref = SVRLReport.XPR.getAttributValue(node, "ref", ProcessNamespaces.ES_NS);
		this.isPattern = true;
		try {
			this.isPattern = xpathreader.getBoolean("parent::meta/parent::pattern", node);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		this.groupId = ref;
	}

	@Override
	public double getErrorLevel() {
		_MessageGroup group = report.getGroup(groupId);
		if(group == null)
			return 0;
		return group.getErrorLevel();
	}
	

	@Override
	public boolean isPatternPara() {
		// TODO Auto-generated method stub
		return this.isPattern;
	}

}
