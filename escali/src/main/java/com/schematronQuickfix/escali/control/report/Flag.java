package com.schematronQuickfix.escali.control.report;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class Flag extends ModelNode implements _Flag{

	public static ArrayList<_Flag> getSubsequence(ArrayList<_ModelNode> nodes){
		ArrayList<_Flag> subsequence = new ArrayList<_Flag>();
		for (Iterator<_ModelNode> iterator = nodes.iterator(); iterator.hasNext();) {
			_ModelNode modelNode = iterator.next();
			if(modelNode instanceof Flag){
				Flag subNode = (Flag) modelNode;
				subsequence.add(subNode);
			}
		}
		return subsequence;
	}
	
	Flag(Node node, int svrlIdx, int index) throws DOMException, URISyntaxException {
		super(svrlIdx, node.getBaseURI());
		this.setId(node.getNodeValue());
		this.setName(node.getNodeValue());
		this.setIndex(index);
	}

	@Override
	public double getErrorLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

}
