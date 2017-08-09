package com.schematronQuickfix.escali.control.report;

import com.github.oxygenPlugins.common.gui.lists.items._ListGroupNode;

public interface _QuickFix extends _ModelNode, _ListGroupNode {
	public static final int ROLE_MIX = 0;
	public static final int ROLE_ADD = 1;
	public static final int ROLE_DELETE = 2;
	public static final int ROLE_REPLACE = 3;
	public static final int ROLE_STRINGREPLACE = 4;

	public static final int Role_COUNT = 5;
	public static final String[] Role_VALUES = new String[]{"mix","add","delete","replace","stringReplace"};
	
	static final int ID = 0;
	static final int FIX_ID = 1;
	static final int MESSAGE_ID = 2;
	static final int CONTEXT_ID = 3;
	static final int ID_COUNT = 4;
	String[] ID_ATTRIBUTES = new String[]{"id", "fixId", "messageId", "contextId"};
	
	String toString();
    String getFixId();
    String getMessageId();
    String getContextId();
    String getId(int idType);
    String getId();
    boolean isDefault();
    
    _UserEntry[] getParameter();
    _UserEntry[] getSettedParameter();
    _UserEntry[] getInvalidParameter();

    boolean hasParameter();
    int getRole();
	_QuickFix[] getFixRelFixes();
	_QuickFix[] getMsgRelFixes();
	void addFixRelFix(_QuickFix relFix);
	void addMsgRelFix(_QuickFix relFix);
	
	String[] getBaseUris(); 
}
