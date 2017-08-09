package com.schematronQuickfix.xsm.operations;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.github.oxygenPlugins.common.xml.staxParser.ElementInfo;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.github.oxygenPlugins.common.xml.staxParser.PositionalXMLReader;
import com.github.oxygenPlugins.common.xml.staxParser.TextInfo;
import com.github.oxygenPlugins.common.xml.staxParser.TextInfo.JumpInfo;

public class AddOp implements _Operation {

	final private int start;
	final private int end;
	final private String replace;
	final private int prio;
	final private OperationsSet corrections = new OperationsSet();
	private String correctionId = "" + this.hashCode();

	public AddOp(Node context, String replace, String position, int prio,
			String correctionId) {
		this(context, replace, position, prio);
		this.correctionId = correctionId;
	}

	public AddOp(Node context, String replace, String position, int prio) {
		boolean addChild = position.equals("first-child")
				|| position.equals("last-child");

		NodeInfo ni = PositionalXMLReader.getNodeInfo(context);

		boolean contextIsEmpty = PositionalXMLReader.getPosition(context,
				PositionalXMLReader.NODE_INNER_LOCATION_START) == PositionalXMLReader
				.getPosition(context, PositionalXMLReader.NODE_LOCATION_END);

		if (addChild && contextIsEmpty && ni instanceof ElementInfo) {
			ElementInfo ei = (ElementInfo) ni;
			ReplaceOp startTag = new ReplaceOp(ei.getAttributRegionEndOffset(),
					ei.getInnerStartOffset(), ">", 1000, "openStartTag#"
							+ context.hashCode());
			String prefix = context.getPrefix() == null ? "" : context
					.getPrefix() + ":";
			String qname = prefix + context.getLocalName();
			AddOp endTag = new AddOp(context, "</" + qname + ">", "after",
					1000, "closeEndTag#" + context.hashCode());
			this.corrections.add(startTag);
			this.corrections.add(endTag);
		}
		// if (addChild && contextIsEmpty && ni instanceof ElementInfo) {
		// ElementInfo ei = (ElementInfo) ni;
		// start = ei.getAttributRegionEndOffset();
		// end = ei.getInnerStartOffset();
		//
		// String prefix = context.getPrefix() == null ? "" : context
		// .getPrefix() + ":";
		// String qname = prefix + context.getLocalName();
		// replace = ">" + replace + "</" + qname + ">";
		// } else {

		start = getStartByPosition(ni, position);
		end = start;
		// }
		this.replace = replace;
		this.prio = prio;

	}


	public AddOp(Attr context, String replace, String position, int prio,
			int startOffset, int endOffset) {
		this(PositionalXMLReader.getNodeInfo(context), replace, position, prio, startOffset, endOffset);
	}
	public AddOp(Text context, String replace, String position, int prio,
			int startOffset, int endOffset) {
		this(PositionalXMLReader.getNodeInfo(context), replace, position, prio, startOffset, endOffset);
		
	}
	public AddOp(Comment context, String replace, String position, int prio,
			int startOffset, int endOffset) {
		NodeInfo nodeInfo = PositionalXMLReader.getNodeInfo(context);
		int offset = position.equals("after") ? endOffset : (startOffset);
		int valueStart = nodeInfo.getStartOffset() + 4;
		start = valueStart + offset;
		
		end = start;
		
		
		this.replace = replace;
		this.prio = prio;
	}
	

	public AddOp(ProcessingInstruction context, String replace, String position, int prio,
			int startOffset, int endOffset) {
		NodeInfo nodeInfo = PositionalXMLReader.getNodeInfo(context);
		int offset = position.equals("after") ? endOffset : (startOffset);
		int valueStart = nodeInfo.getEndOffset() - 2 - context.getNodeValue().length();
		start = valueStart + offset;
		
		end = start;
		
		
		this.replace = replace;
		this.prio = prio;
	}
	
	private AddOp(TextInfo nodeInfo, String replace, String position, int prio,
			int startOffset, int endOffset) {

		int offset = position.equals("after") ? endOffset : startOffset;
		
//		START
		if(nodeInfo.isInJump(offset)){
			JumpInfo jumpInfo = nodeInfo.getJumpInfo(offset);
			
			int startPosInJump = offset - jumpInfo.startNodeOffset;
			String prefix = jumpInfo.replacement.substring(0, startPosInJump);
			
			replace = prefix + replace;
			
			start = jumpInfo.startOffset + nodeInfo.getValueStartOffset();
		} else {
			start = nodeInfo.getValueStartOffset() + nodeInfo.transformNodeToCode(offset);
		}
		
		end = start;
		
		
		this.replace = replace;
		this.prio = prio;

	}

	private static int getStartByPosition(NodeInfo nodeInfo, String position) {

		if (position.equals("before")) {
			return nodeInfo.getStartOffset();
		}
		if (position.equals("after")) {
			return nodeInfo.getEndOffset();
		}
		if (nodeInfo instanceof ElementInfo) {
			if (position.equals("first-child")) {
				return ((ElementInfo) nodeInfo).getInnerStartOffset();
			}
			if (position.equals("last-child")) {
				return ((ElementInfo) nodeInfo).getInnerEndOffset();
			}
			if (position.equals("attribute") || position.equals("@")) {
				return ((ElementInfo) nodeInfo).getAttributRegionEndOffset();
			}
		}

		return nodeInfo.getStartOffset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sqf.tester.fixer._Operations#getStart()
	 */
	@Override
	public int getStart() {
		return this.start;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sqf.tester.fixer._Operations#getEnd()
	 */
	@Override
	public int getEnd() {
		return this.end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sqf.tester.fixer._Operations#getReplace()
	 */
	@Override
	public String getReplace() {
		return this.replace;
	}

	@Override
	public int getPrio() {
		// TODO Auto-generated method stub
		return prio;
	}

	@Override
	public String toString() {
		return "[" + getStart() + ":" + getEnd() + "] " + getReplace();
	}

	@Override
	public OperationsSet getCorrectionOperations() {
		return this.corrections;
	}

	@Override
	public String getCorrectionId() {
		// TODO Auto-generated method stub
		return this.correctionId;
	}
}
