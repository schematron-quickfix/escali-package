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

public class QuickFix extends MessageGroup implements _QuickFix {
	private final String[] idSet = new String[ID_COUNT];
	private boolean isDefault;
	private final int role;
	private ArrayList<_QuickFix> fixRelFixes = new ArrayList<_QuickFix>();
	private ArrayList<_QuickFix> msgRelFixes = new ArrayList<_QuickFix>();
	private final String[] baseUris;

	
	
	
	public static ArrayList<_QuickFix> getSubsequence(ArrayList<_ModelNode> nodes){
		ArrayList<_QuickFix> subsequence = new ArrayList<_QuickFix>();
		for (Iterator<_ModelNode> iterator = nodes.iterator(); iterator.hasNext();) {
			_ModelNode modelNode = iterator.next();
			if(modelNode instanceof QuickFix){
				QuickFix subNode = (QuickFix) modelNode;
				subsequence.add(subNode);
			}
		}
		return subsequence;
	}
	
	QuickFix(Node node, int svrlIdx, int index) throws DOMException, URISyntaxException, XPathExpressionException {
		super(node, svrlIdx);
		this.setId(SVRLReport.XPR.getAttributValue(node, "id"));
		this.setIndex(index);
		XPathReader xpathreader = new XPathReader();
		
		if(xpathreader.getBoolean("@default='true'", node)){
			this.setDefault(true);
		}
		
		for (int i = 0; i < idSet.length; i++) {
			idSet[i] = SVRLReport.XPR.getAttributValue(node, _QuickFix.ID_ATTRIBUTES[i]);
		}
		
		
//		S E T   N A M E
		Node nameNode = xpathreader.getNode("sqf:description/sqf:title", node);
//		NodeList texte = xpathreader.getNodeSet("sqf:description/es:text",
//				node);
//		String description = "";
//		for (int i = 0; i < texte.getLength(); i++) {
//			description += texte.item(i).getTextContent();
//			if (i + 1 < texte.getLength())
//				description += " ";
//		}
		this.setName(nameNode.getTextContent());
		
//		S E T   T Y P E
		String type = SVRLReport.XPR.getAttributValue(node, "role");
		int i;
		for (i = 0; i < Role_VALUES.length; i++) {
			if(Role_VALUES[i].equals(type))
				break;
		}
		this.role = i;
		
//		B A S E   U R I S 		
		this.baseUris = xpathreader.getString("@base-uris", node).split("\\s");
		
		
//		U S E R   E N T R I E S
		NodeList userEntryNodes = xpathreader.getNodeSet("sqf:user-entry", node);
		for (int y = 0; y < userEntryNodes.getLength(); y++) 
			this.addChild(ModelNodeFac.nodeFac.getNode(userEntryNodes.item(y), null));
		
		
	}
	
	

	@Override
	public _UserEntry[] getParameter() {
		ArrayList<_ModelNode> children = this.getChildren();
		ArrayList<_UserEntry> entries = UserEntry.getSubsequence(children);
		return entries.toArray(new _UserEntry[entries.size()]);
	}
	
	@Override
	public _UserEntry[] getSettedParameter() {
		_UserEntry[] allParams = getParameter();
		ArrayList<_UserEntry> settedParams = new ArrayList<_UserEntry>();
		for(_UserEntry param : allParams){
			if(param.isValueSet()){
				settedParams.add(param);
			}
		}
		return settedParams.toArray(new _UserEntry[settedParams.size()]);
	}
	
	@Override
	public _UserEntry[] getInvalidParameter() {
		_UserEntry[] allParams = getParameter();
		ArrayList<_UserEntry> invalidParams = new ArrayList<_UserEntry>();
		for(_UserEntry param : allParams){
			if(!param.isValueValid()){
				invalidParams.add(param);
			}
		}
		return invalidParams.toArray(new _UserEntry[invalidParams.size()]);
	}
	
	@Override
	public boolean hasParameter() {
		return getParameter().length > 0;
	}
	
	

	@Override
	public String getFixId() {
		return this.idSet[FIX_ID];
	}

	@Override
	public String getMessageId() {
		return this.idSet[MESSAGE_ID];
	}

	@Override
	public String getContextId() {
		return this.idSet[CONTEXT_ID];
	}

	@Override
	public String getId(int idType) {
		return this.idSet[idType];
	}

	@Override
	public boolean isDefault() {
		return this.isDefault;
	}

	private void setDefault(boolean b) {
		this.isDefault = b;
	}

	@Override
	public int getRole() {
		// TODO Auto-generated method stub
		return this.role;
	}

	@Override
	public void addFixRelFix(_QuickFix relFix) {
		this.fixRelFixes.add(relFix);
	}
	@Override
	public void addMsgRelFix(_QuickFix relFix) {
		this.msgRelFixes.add(relFix);
	}
	
	@Override
	public _QuickFix[] getFixRelFixes() {
		return this.fixRelFixes.toArray(new _QuickFix[fixRelFixes.size()]);
	}

	@Override
	public _QuickFix[] getMsgRelFixes() {
		return this.msgRelFixes.toArray(new _QuickFix[msgRelFixes.size()]);
	}

	@Override
	public String[] getBaseUris() {
		return this.baseUris;
	}
	
}
