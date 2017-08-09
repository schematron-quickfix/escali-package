package com.schematronQuickfix.escali.control.report;

import java.util.ArrayList;

import com.github.oxygenPlugins.common.gui.lists.items._ListGroupNode;



public interface _MessageGroup extends _ModelNode, _ListGroupNode{

	public abstract int[] getLevelCounts();

	public abstract ArrayList<_SVRLMessage> getMessages();
	
	public abstract ArrayList<_SVRLMessage> getMessages(int minErrorLevel, int maxErrorLevel);

	public static final int SVRL_SORTING = 0;
	public static final int SOURCE_SORTING = 1;
	public static final int ERROR_UP_SORTING = 2;
	public static final int ERROR_DOWN_SORTING = 3;
	public static final int HIERARCHY_SORTING = 4;
	
	public abstract ArrayList<_SVRLMessage> getSortedMessages(int mode);

	public abstract int getMessageCount();

	public abstract double getErrorLevel();

	public abstract double getMaxErrorLevel();

	public abstract int getMaxErrorLevelInt();
	

	public abstract String[] getPhases();

	boolean isPhase(String phase);


}