package com.schematronQuickfix.escali.control.report;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.schematronQuickfix.escali.control.SVRLReport;

public class Pattern extends MessageGroup implements _Pattern {
	private String isA;

	private final HashMap<String, _ModelNode> rules = new HashMap<String, _ModelNode>();

	private final String[] phases;

	public static ArrayList<_Pattern> getSubsequence(ArrayList<_ModelNode> nodes) {
		ArrayList<_Pattern> subsequence = new ArrayList<_Pattern>();
		for (Iterator<_ModelNode> iterator = nodes.iterator(); iterator
				.hasNext();) {
			_ModelNode modelNode = iterator.next();
			if (modelNode instanceof Pattern) {
				Pattern subNode = (Pattern) modelNode;
				subsequence.add(subNode);
			}
		}
		return subsequence;
	}

	Pattern(Node patternNode, int svrlIdx, int Index, StringNode instance) throws DOMException,
			URISyntaxException, XPathExpressionException {
		super(patternNode, svrlIdx);
		
		Node meta = SVRLReport.XPR.getNode("es:meta", patternNode);
		this.setId(SVRLReport.XPR.getAttributValue(meta, "id"));
		this.setIndex(Index);

		// S E T N A M E
		String title = SVRLReport.XPR.getAttributValue(meta, "name", "", "Pattern " + (this.getIndex() + 1));
		phases = SVRLReport.XPR.getAttributValue(meta, "phases", "", "#ALL").split("\\s");
		
		isA = SVRLReport.XPR.getAttributValue(meta, "is-a");
		this.setName(title);

		// S E T R U L E S
		NodeList ruleNodes = SVRLReport.XPR.getNodeSet("es:rule", patternNode);
		for (int i = 0; i < ruleNodes.getLength(); i++) {
			Node rule = ruleNodes.item(i);
			NodeList paras = SVRLReport.XPR.getNodeSet("es:meta/es:text", rule);
			
			for (int j = 0; j < paras.getLength(); j++) {
				this.addChild(ModelNodeFac.nodeFac.getNode(paras.item(j), instance));
			}
			this.addChild(ModelNodeFac.nodeFac.getNode(rule, instance));
		}
		
// validate succesfull
	}



	@Override
	public void addChild(_ModelNode child) {
		if (child instanceof Rule) {
			if (!rules.containsKey(child.getId()))
				rules.put(child.getId(), child);
		}
		super.addChild(child);
	}

	@Override
	public ArrayList<_Rule> getRules() {
		ArrayList<_ModelNode> children = this.getChildren();
		ArrayList<_Rule> rules = Rule.getSubsequence(children);
		return rules;
	}

	@Override
	public String getName() {
		if (isA.equals(""))
			return super.getName();
		return super.getName() + " (is a " + isA + ")";
	}
	
	@Override
	public String[] getPhases(){
		return this.phases;
	}
	


	@Override
	public _MessageGroup getGroup(String id) {
		ArrayList<_Rule> rules = this.getRules();
		for (Iterator<_Rule> iterator = rules.iterator(); iterator.hasNext();) {
			_Rule rule = iterator.next();
			if (rule.getId().equals(id)) {
				return rule;
			}
		}
		return null;
	}

}
