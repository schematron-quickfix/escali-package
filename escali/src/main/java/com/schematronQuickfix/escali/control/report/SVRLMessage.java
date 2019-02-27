package com.schematronQuickfix.escali.control.report;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
import com.schematronQuickfix.escali.control.SVRLReport;

public class SVRLMessage extends ModelNode implements _SVRLMessage {

	private String location;
	private double errorLevel;
	private _QuickFix defaultFix;
	private _Flag flag;
	private _Report report;
	// private final StringNode instance;
	private final NodeInfo locationInfo;
	private final File instanceFile;
	private int[] offsets = new int[2]; 

	public static ArrayList<_SVRLMessage> getSubsequence(
			ArrayList<_ModelNode> nodes) {
		ArrayList<_SVRLMessage> subsequence = new ArrayList<_SVRLMessage>();
		for (Iterator<_ModelNode> iterator = nodes.iterator(); iterator
				.hasNext();) {
			_ModelNode modelNode = iterator.next();
			if (modelNode instanceof SVRLMessage) {
				SVRLMessage subNode = (SVRLMessage) modelNode;
				subsequence.add(subNode);
			}
		}
		return subsequence;
	}

	SVRLMessage(Node messageNode, _Report report, int svrlIdx, int Index,
			StringNode instance) throws DOMException, URISyntaxException,
			XPathExpressionException {
		super(messageNode, svrlIdx);
		this.instanceFile = instance.getFile();
		this.setId(SVRLReport.XPR.getString("@id", messageNode));
		this.setIndex(Index);
		this.report = report;


		// S E T N A M E
		XPathReader xpathreader = new XPathReader();
		NodeList texte = xpathreader.getNodeSet("es:text", messageNode);
		String message = "";
		for (int i = 0; i < texte.getLength(); i++) {
			message += texte.item(i).getTextContent();
			if (i + 1 < texte.getLength())
				message += " ";
		}
		this.setName(message);

		// L O C A T I O N
		this.location = SVRLReport.XPR
				.getAttributValue(messageNode, "location");
		NodeInfo locInfo = instance.getNodeInfo(this.location);
		
		// L O C A T I O N - O F F S E T
		if(SVRLReport.XPR.getBoolean("@substring", messageNode)){
			String substring = SVRLReport.XPR
					.getAttributValue(messageNode, "substring");
			String[] substringSplit = substring.split("\\s");
			if(substringSplit.length == 2){
				this.offsets[0] = Integer.parseInt(substringSplit[0]) - 1;
				this.offsets[1] = Integer.parseInt(substringSplit[1]) - 1;
			}
			locInfo = locInfo.nodeInfoWithOffset(this.offsets[0], this.offsets[1], instance.getLineColumnInfo());
		}

		this.locationInfo = locInfo;
		
				
		// E R R O R L E V E L (@role)
		String levelValue = SVRLReport.XPR
				.getAttributValue(messageNode, "role");
		this.errorLevel = levelValue.equals("") ? _SVRLMessage.LEVEL_DEFAULT
				: Double.parseDouble(levelValue);

		// D I A G N O S T I C S
		NodeList diagnNodes = xpathreader.getNodeSet("es:diagnostics",
				messageNode);

		for (int i = 0; i < diagnNodes.getLength(); i++) {
			this.addChild(ModelNodeFac.nodeFac.getNode(diagnNodes.item(i),
					instance));
		}

		// Q U I C K F I X E S
		NodeList fixNodes = xpathreader.getNodeSet("sqf:fix", messageNode);
		Node defFixNode = xpathreader.getNode("sqf:fix[@default='true']",
				messageNode);

		for (int i = 0; i < fixNodes.getLength(); i++) {
			Node fixNode = fixNodes.item(i);
			_QuickFix fix = (_QuickFix) ModelNodeFac.nodeFac.getNode(fixNode,
					instance);
			this.addChild(fix);

			if (defFixNode == fixNode) {
				this.defaultFix = fix;
			}
		}

		// F L A G S
		Node flagNode = messageNode.getAttributes().getNamedItem("flag");
		if (flagNode != null) {
			this.flag = ModelNodeFac.nodeFac.getFlag(flagNode);
			flag.addChild(this);
		}

		this.setName(this.getName());
	}

	@Override
	public void addChild(_ModelNode child) {
		if (child instanceof _Flag) {
			this.flag = (_Flag) child;
		} else {
			super.addChild(child);
			if (child instanceof QuickFix) {
				report.addChild(child);
			}
		}
	}

	@Override
	public boolean hasQuickFixes() {
		return getQuickFixes().length > 0;
	}

	@Override
	public _QuickFix[] getQuickFixes() {
		ArrayList<_ModelNode> children = this.getChildren();
		ArrayList<_QuickFix> fixes = QuickFix.getSubsequence(children);
		return fixes.toArray(new _QuickFix[fixes.size()]);
	}

	@Override
	public _QuickFix getQuickFix(String fixId) {
		ArrayList<_QuickFix> fixList = QuickFix.getSubsequence(this
				.getChildById(new String[] { fixId }));
		return fixList.get(0);
	}

	@Override
	public String getPatternId() {
		// TODO Auto-generated method stub
		return this.getParent().getParent().getId();
	}

	@Override
	public String getRuleId() {
		// TODO Auto-generated method stub
		return this.getParent().getId();
	}

	@Override
	public _QuickFix getDefaultFix() {
		return this.defaultFix;
	}

	@Override
	public boolean hasDefaultFix() {
		return this.defaultFix != null;
	}

	@Override
	public String getLocation() {
		return this.location;
	}

	@Override
	public NodeInfo getLocationInIstance() {
		return this.locationInfo;
	}
	@Override
	public String getBaseUri() {
		
		String baseURI = this.locationInfo.getNode().getBaseURI();
		if(baseURI != null)
			return baseURI;
		
		return super.getBaseUri();
	}
	@Override
	public File getInstanceFile() {
		return this.instanceFile;
	}

	@Override
	public double getErrorLevel() {
		return doubleToLevel(this.errorLevel);
	}

	@Override
	public int getErrorLevelInt() {
		return doubleToLevel(this.errorLevel);
	}

	@Override
	public boolean isHidden() {
		return this.getErrorLevelInt() == _SVRLMessage.LEVEL_STANDALONE;
	}

	@Override
	public double getErrorWeight() {
		return this.errorLevel;
	}

	@Override
	public _Flag getFlag() {
		return this.flag;
	}

	@Override
	public ArrayList<Diagnostic> getDiagnostics() {
		ArrayList<_ModelNode> children = this.getChildren();
		ArrayList<Diagnostic> diagn = Diagnostic.getSubsequence(children);
		return diagn;
	}

	static public int doubleToLevel(double value) {
		int levelCount = _SVRLMessage.LEVEL_COUNT;
		value = value * levelCount;
		value = Math.floor(value);
		return (int) value;
	}

	public static String levelToString(int i) {
		return _SVRLMessage.LEVEL_NAMES[i];
	}

	public static String levelToString(int[] levels) {

		String summary = "";
		for (int i = levels.length - 1; i >= 0; i--) {
			if (levels[i] > 0) {
				summary += levels[i] + " " + levelToString(i);
				summary += levels[i] > 1 ? "s" : "";
				summary += i > 0 ? ", " : "";
			}
		}
		return summary;
	}

	@Override
	public String toString() {
		return this.getLocationInIstance().getStart().getLineNumber() + ": "
				+ this.getName();
	}
	
	@Override
	public String[] getPhases() {
		_ModelNode parent = this.getParent();
		if(parent instanceof MessageGroup){
			return ((MessageGroup)parent).getPhases();
		} else {
			return null;
		}
	}
}
