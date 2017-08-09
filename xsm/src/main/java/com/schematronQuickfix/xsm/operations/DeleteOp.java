package com.schematronQuickfix.xsm.operations;


import org.w3c.dom.Node;

public class DeleteOp extends ReplaceOp implements _Operation {
	public DeleteOp(Node node, int prio){
		super(node, "", prio);
	}
}
