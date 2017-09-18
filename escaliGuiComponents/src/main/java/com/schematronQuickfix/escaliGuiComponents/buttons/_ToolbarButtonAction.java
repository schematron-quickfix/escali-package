package com.schematronQuickfix.escaliGuiComponents.buttons;

import java.awt.event.ActionEvent;

public interface _ToolbarButtonAction {

	public abstract void actionPerformed(ActionEvent ev);
	
	public abstract boolean isEnable();
}
