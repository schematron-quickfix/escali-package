package com.schematronQuickfix.escali.control.report;

import java.util.ArrayList;

public interface _Pattern extends _MessageGroup{

	ArrayList<_Rule> getRules();

	_MessageGroup getGroup(String id);


}
