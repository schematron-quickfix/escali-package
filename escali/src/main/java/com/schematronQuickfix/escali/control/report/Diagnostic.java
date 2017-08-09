package com.schematronQuickfix.escali.control.report;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
import com.schematronQuickfix.escali.control.SVRLReport;

public class Diagnostic extends ModelNode implements _Diagnostic {

	public static ArrayList<Diagnostic> getSubsequence(
			ArrayList<_ModelNode> nodes) {
		ArrayList<Diagnostic> subsequence = new ArrayList<Diagnostic>();
		for (Iterator<_ModelNode> iterator = nodes.iterator(); iterator
				.hasNext();) {
			_ModelNode modelNode = iterator.next();
			if (modelNode instanceof Diagnostic) {
				Diagnostic subNode = (Diagnostic) modelNode;
				subsequence.add(subNode);
			}
		}
		return subsequence;
	}

	Diagnostic(Node node, int svrlIdx, int index) throws DOMException, URISyntaxException, XPathExpressionException {
		super(node, svrlIdx);
		this.setId(SVRLReport.XPR.getAttributValue(node, "diagnostic"));
		this.setIndex(index);
		// S E T N A M E
		XPathReader xpathreader = new XPathReader();
		NodeList texte = xpathreader.getNodeSet("svrl:text", node);
		String description = "";
		for (int i = 0; i < texte.getLength(); i++) {
			description += texte.item(i).getTextContent();
			if (i + 1 < texte.getLength())
				description += " ";
		}
		this.setName(description);
	}

	@Override
	public double getErrorLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
}
