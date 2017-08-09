package com.schematronQuickfix.xsm.operations;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.github.oxygenPlugins.common.xml.staxParser.ElementInfo;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.github.oxygenPlugins.common.xml.staxParser.PositionalXMLReader;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;

public class CopyOp {
	
	private static XPathReader xpr = new XPathReader();
	
	private final Node xsmCopy;
	private final ElementInfo xsmCopyInfo;
	private final String copyCode;
	private final short nodeType;
	
	private final boolean isTopLevel;
	
	private final NodeInfo selectNodeInfo;

	protected CopyOp(Node xsmCopy, StringNode source) throws XPathExpressionException{
		this.xsmCopy = xsmCopy;
		Node selectAttr = xpr.getNode("@select", xsmCopy);
		
		this.isTopLevel = xpr.getBoolean("parent::xsm:content", xsmCopy);
		
		selectNodeInfo = PositionalXMLReader.getNodeInfo(selectAttr);
		String xpath = selectAttr.getNodeValue();
		
		NodeInfo copyNI = source.getNodeInfo(xpath);
		copyCode = source.getCode(copyNI);
		
		nodeType = copyNI.getNode().getNodeType();
		
		xsmCopyInfo = PositionalXMLReader.getNodeInfo((Element)xsmCopy);
		
	}
	
	protected boolean isAttribute(){
		return nodeType == Node.ATTRIBUTE_NODE;
	}
	
	protected boolean isTopLevel(){
		return this.isTopLevel;
	}
	
	protected String replace(String replace, int offset){
		
		
		
		return replace;
	}
	
	
}
