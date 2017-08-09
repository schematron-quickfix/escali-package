package com.schematronQuickfix.xsm.operations;

import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.oxygenPlugins.common.xml.staxParser.ElementInfo;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.github.oxygenPlugins.common.xml.staxParser.PositionalXMLReader;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;

public class CopyOpSet {

	private static XPathReader xpr = new XPathReader();
	
	
	public class CopyOp {
		
		
		private final Node xsmCopy;
		private final ElementInfo xsmCopyInfo;
		private final String copyCode;
		
		private final boolean isTopLevel;
		
		private final NodeInfo selectNodeInfo;
		private NodeInfo copyNodeInfo;

		protected CopyOp(Node xsmCopy, StringNode source) throws XPathExpressionException{
			this.xsmCopy = xsmCopy;
			Node selectAttr = xpr.getNode("@select", xsmCopy);
			
			this.isTopLevel = xpr.getBoolean("parent::xsm:content", xsmCopy);
			
			selectNodeInfo = PositionalXMLReader.getNodeInfo(selectAttr);
			String xpath = selectAttr.getNodeValue();
			
			copyNodeInfo = source.getNodeInfo(xpath);
			copyCode = source.getCode(copyNodeInfo);
			
			
			xsmCopyInfo = PositionalXMLReader.getNodeInfo((Element)xsmCopy);
			
		}
		
		private boolean isAttribute(){
			return copyNodeInfo.getNode().getNodeType() == Node.ATTRIBUTE_NODE;
		}
		
		private boolean isTopLevel(){
			return this.isTopLevel;
		}
		
		private boolean isTopLevelAttribute(){
			return this.isTopLevel() && this.isAttribute();
		}
		
		private String applyCopy(String replace, int offset) throws XPathExpressionException{
			String copyCode = this.copyCode;

			ElementInfo xsmNodeInfo = PositionalXMLReader.getNodeInfo((Element)xsmCopy);
			
			int start = xsmNodeInfo.getStartOffset() - offset;
			int end = xsmNodeInfo.getEndOffset() - offset;
			
			String attrReg = getAttributeRegion(xsmNodeInfo, replace, offset);
			
			if(copyNodeInfo.getNode().getNodeType() == Node.ELEMENT_NODE){
				ElementInfo copyEI = (ElementInfo) copyNodeInfo;
				
				int endAttrRegCopyCode = copyEI.getAttributRegionEndOffset() - copyEI.getStartOffset();
				
				copyCode = copyCode.substring(0, endAttrRegCopyCode) + attrReg + copyCode.substring(endAttrRegCopyCode);
			} else if(isAttribute()){
				Element parent = (Element) xpr.getNode("..", xsmCopy);
				ElementInfo parentEI = PositionalXMLReader.getNodeInfo(parent);
				
				Node copyNode = this.copyNodeInfo.getNode();
				QName copyNodeQName = new QName(copyNode.getNamespaceURI(), copyNode.getLocalName() == null ? copyNode.getNodeName() : copyNode.getLocalName());

				replace = replace.substring(0, start) + replace.substring(end);
				
				replace = deleteAttribute(replace, offset, parent, copyNodeQName);
				
				copyCode = copyCode + attrReg;
				
				start = parentEI.getAttributRegionStartOffset() - offset;
				end = start;
				
			}
			
			if(start >= 0){
				replace = replace.substring(0, start) + copyCode + replace.substring(end);
			}
			
			return replace;
		}
		
		private String deleteAttribute(String replace, int offset, Element element, QName deleteAttribute) throws XPathExpressionException{
			String ns = deleteAttribute.getNamespaceURI();
			String name = deleteAttribute.getLocalPart();
			String attrCheckXPath = "@*[local-name() = '" + name +
					"'][namespace-uri()='" + ns +
					"']";
			Node deleteAttr = xpr.getNode(attrCheckXPath, element);
			if(deleteAttr != null){
				NodeInfo attrInfo = PositionalXMLReader.getNodeInfo(deleteAttr);
				replace = replace.substring(0, attrInfo.getStartOffset() - offset) + replace.substring(attrInfo.getEndOffset() - offset);
			}
			return replace;
		}

		public String applyTopLevelAttribute(String replace, int offset) {
			return this.copyCode + getAttributeRegion(replace, offset);
		}
		

		private String getAttributeRegion(String replace, int offset){
			return getAttributeRegion(PositionalXMLReader.getNodeInfo((Element)xsmCopy), replace, offset);
		}
		private String getAttributeRegion(ElementInfo xsmNodeInfo, String replace, int offset){

			int startAttrReg = xsmNodeInfo.getAttributRegionStartOffset() - offset;
			int endAttrReg = xsmNodeInfo.getAttributRegionEndOffset() - offset;
			int startSelect = selectNodeInfo.getStartOffset() - offset;
			int endSelect = selectNodeInfo.getEndOffset() - offset;
			String attrReg = replace.substring(startAttrReg, startSelect) + replace.substring(endSelect, endAttrReg);
			return attrReg;
		}
		
	}
	
	private final ArrayList<CopyOp> copyList = new ArrayList<CopyOpSet.CopyOp>();
	private final ArrayList<CopyOp> topLevelAttributeCopyList = new ArrayList<CopyOpSet.CopyOp>();
	
	private CopyOpSet(NodeList xsmCopies, StringNode source) throws XPathExpressionException{
		for (int i = 0; i < xsmCopies.getLength(); i++) {
			CopyOp co = new CopyOp(xsmCopies.item(i), source);
			if(co.isTopLevelAttribute()){
				this.topLevelAttributeCopyList.add(co);
			}
			this.copyList.add(co);
		}
	}
	
	
	public String applyContentCopy(String replace, int offset) throws XPathExpressionException{
		
		for (CopyOp copy : this.copyList) {
			String afterReplace = copy.applyCopy(replace, offset);
			offset -= afterReplace.length() - replace.length();
			replace = afterReplace;
		}
		
		return replace;
	}
	
	public ArrayList<Node> getTLAttributes(){
		ArrayList<Node> tlaList = new ArrayList<Node>();
		for (CopyOp tla : this.topLevelAttributeCopyList) {
			tlaList.add(tla.copyNodeInfo.getNode());
		}
		return tlaList;
	}
	
	public String applyTopLevelAttributeCopy(String replace, int offset){
		String returnVal = "";
		for (CopyOp topLevelAttr : this.topLevelAttributeCopyList) {
			returnVal += topLevelAttr.applyTopLevelAttribute(replace, offset);
		}
		return returnVal;
	}
	
	public static CopyOpSet createCopyOpSet(NodeList xsmCopies, StringNode source) throws XPathExpressionException{
		return new CopyOpSet(xsmCopies, source);
	}
	
	
}
