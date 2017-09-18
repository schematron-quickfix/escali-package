package com.schematronQuickfix.escaliOxygen.toolbar;

import javax.swing.JComponent;
import javax.swing.JMenuBar;

import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;
import com.schematronQuickfix.escaliOxygen.toolbar.main.CommitChanges;
import com.schematronQuickfix.escaliOxygen.toolbar.main.OpenSchemaAssociation;
import com.schematronQuickfix.escaliOxygen.toolbar.main.ShowViewButtons;

import ro.sync.exml.workspace.api.standalone.ToolbarInfo;

public class MainToolbar {
	private final static String title = "Escali tools";
	private JComponent[] components;
	private JMenuBar menuInfo;
	
	public MainToolbar(EscaliMessanger ema){
		this.components = new JComponent[]{
				new CommitChanges(ema), 
				new OpenSchemaAssociation(ema, this),
				new ShowViewButtons(ema)
		};
	}
	
	private JComponent[] getComponents(){
		return this.components;
	}
	
	
	public void customizeToolbars(ToolbarInfo barInfo){
		barInfo.setTitle(title);
		barInfo.setComponents(getComponents());
	}
	
	public void implementBarInfo(JMenuBar menuInfo){
		this.menuInfo = menuInfo;
	}
	
	public JMenuBar getMenuInfo(){
		return this.menuInfo;
	}
}
