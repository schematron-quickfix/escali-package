package com.schematronQuickfix.escali.control.report;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.schematronQuickfix.escali.control.SVRLReport;

public class Rule extends MessageGroup implements _Rule {

	public static ArrayList<_Rule> getSubsequence(ArrayList<_ModelNode> nodes) {
		ArrayList<_Rule> subsequence = new ArrayList<_Rule>();
		for (Iterator<_ModelNode> iterator = nodes.iterator(); iterator
				.hasNext();) {
			_ModelNode modelNode = iterator.next();
			if (modelNode instanceof Rule) {
				Rule subNode = (Rule) modelNode;
				subsequence.add(subNode);
			}
		}
		return subsequence;
	}

	Rule(Node ruleNode, int svrlIdx, int Index, StringNode instance) throws DOMException,
			URISyntaxException, XPathExpressionException {
		super(ruleNode, svrlIdx);
		Node meta = SVRLReport.XPR.getNode("es:meta", ruleNode);
		this.setId(SVRLReport.XPR.getAttributValue(meta, "id"));
		this.setIndex(Index);

		// S E T N A M E
		String title = "";
		title = SVRLReport.XPR.getAttributValue(meta, "id", "", "Rule " +  (this.getIndex() + 1)) + " " + SVRLReport.XPR.getAttributValue(meta, "context");
		this.setName(title);
		
		//
		// S E T M E S S A G E S
		
		NodeList messageNodes = SVRLReport.XPR.getNodeSet("es:assert|es:report", ruleNode);
		for (int i = 0; i < messageNodes.getLength(); i++) {
			Node message = messageNodes.item(i);
			this.addChild(ModelNodeFac.nodeFac.getNode(message, instance));
		}
		
		
		
	}

	

	public ArrayList<_SVRLMessage> getMessages() {
		ArrayList<_ModelNode> children = this.getChildren();
		ArrayList<_SVRLMessage> messages = SVRLMessage.getSubsequence(children);
		return messages;
	}

}
