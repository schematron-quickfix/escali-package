package com.schematronQuickfix.xsm.operations;



import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.github.oxygenPlugins.common.xml.staxParser.PositionalXMLReader;
import com.github.oxygenPlugins.common.xml.staxParser.TextInfo;
import com.github.oxygenPlugins.common.xml.staxParser.TextInfo.JumpInfo;

public class ReplaceOp implements _Operation {
	final private int start;
	final private int end;
	final private String replace;
	final private int prio;
	private String correctionId = "" + this.hashCode();
	
	protected ReplaceOp(int start, int end, String replace, int prio, String correctionId){
		this.start = start;
		this.end = end;
		this.replace = replace;
		this.prio = prio;
		this.correctionId = correctionId;
	}
	
	public ReplaceOp(Node node, String replace, int prio) {
		
		NodeInfo nodeInfo = PositionalXMLReader.getNodeInfo(node);
		start = nodeInfo.getStartOffset();
		end = nodeInfo.getEndOffset();
		this.replace = replace;
		this.prio = prio;
	}
	
	

	public ReplaceOp(Attr node, String replace, int prio, int startOffset, int endOffset){
		this(PositionalXMLReader.getNodeInfo(node), replace, prio, startOffset, endOffset);
	}
	
	public ReplaceOp(Text node, String replace, int prio, int startOffset, int endOffset){
		this(PositionalXMLReader.getNodeInfo(node), replace, prio, startOffset, endOffset);
	}
	

	public ReplaceOp(Comment node, String replace, int prio, int startOffset, int endOffset) {
		NodeInfo nodeInfo = PositionalXMLReader.getNodeInfo(node);
		int valueStart = nodeInfo.getStartOffset() + 4;
		start = valueStart + startOffset;
		end = valueStart + endOffset;
		this.replace = replace;
		this.prio = prio;
	}
	
	public ReplaceOp(ProcessingInstruction node, String replace, int prio, int startOffset, int endOffset) {
		NodeInfo nodeInfo = PositionalXMLReader.getNodeInfo(node);
		int valueStart = nodeInfo.getEndOffset() - 2 - node.getNodeValue().length();
		start = valueStart + startOffset;
		end = valueStart + endOffset;
		this.replace = replace;
		this.prio = prio;
	}
	
	private ReplaceOp(TextInfo nodeInfo, String replace, int prio, int startOffset, int endOffset) {
		
//		START
		if(nodeInfo.isInJump(startOffset + 1)){
			JumpInfo jumpInfo = nodeInfo.getJumpInfo(startOffset + 1);
			
			int startPosInJump = startOffset - jumpInfo.startNodeOffset;
			String prefix = jumpInfo.replacement.substring(0, startPosInJump);
			
			replace = prefix + replace;
			
			start = jumpInfo.startOffset + nodeInfo.getValueStartOffset();
		} else {
			start = nodeInfo.getValueStartOffset() + nodeInfo.transformNodeToCode(startOffset);
		}
		
//		END
		if(nodeInfo.isInJump(endOffset)){
			JumpInfo jumpInfo = nodeInfo.getJumpInfo(endOffset);
			
			int endPosInJump = endOffset - jumpInfo.startNodeOffset;
			String suffix = jumpInfo.replacement.substring(endPosInJump);
			
			replace = replace + suffix ;
			
			end = jumpInfo.endOffset + nodeInfo.getValueStartOffset();
		} else {
			end = nodeInfo.getValueStartOffset() + nodeInfo.transformNodeToCode(endOffset);
		}
		
		this.replace = replace;
		this.prio = prio;
	}
	
	/* (non-Javadoc)
	 * @see net.sqf.tester.fixer._Operations#getStart()
	 */
	@Override
	public int getStart(){
		return this.start;
	}
	/* (non-Javadoc)
	 * @see net.sqf.tester.fixer._Operations#getEnd()
	 */
	@Override
	public int getEnd(){
		return this.end;
	}
	/* (non-Javadoc)
	 * @see net.sqf.tester.fixer._Operations#getReplace()
	 */
	@Override
	public String getReplace(){
		return this.replace;
	}
	
	@Override
	public int getPrio(){
		return this.prio;
	}
	
	@Override
	public String toString() {
		return "[" + getStart() + ":" + getEnd() + "] " + getReplace();
	}

	@Override
	public OperationsSet getCorrectionOperations() {
		return new OperationsSet();
	}

	@Override
	public String getCorrectionId() {
		return this.correctionId;
	}
	
}
