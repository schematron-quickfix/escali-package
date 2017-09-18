package com.schematronQuickfix.escaliOxygen.toolbar.main;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;

import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;
import com.schematronQuickfix.escaliOxygen.toolbar.MainToolbar;


public class OpenSchemaAssociation extends OxygenToolbarButton {

	private final EscaliMessanger ema;
	private final MainToolbar toolbar;

	public OpenSchemaAssociation(final EscaliMessanger ema, MainToolbar toolbar) {
		super(ema.getIcon(5, 17), "Associate to schema...");
		this.ema = ema;
		this.toolbar = toolbar;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		JMenuItem toolbarItem = getMenuItem();
		if(toolbarItem != null){
			toolbarItem.doClick();
		}
	}
	
	
	private JMenuItem getMenuItem(){
		JMenuBar menu = toolbar.getMenuInfo();
		if(menu == null){
			return null;
		} else {
			JMenu subMenu1 = menu.getMenuCount() > 9 ? menu.getMenu(9) : null;
			if(subMenu1 == null)
				return null;
			
			JMenuItem subMenu2 = subMenu1.getItemCount() > 8 ? subMenu1.getItem(8) : null;
			if(subMenu2 == null)
				return null;
			
			MenuElement me = subMenu2.getSubElements()[0].getSubElements().length > 1 ? subMenu2.getSubElements()[0].getSubElements()[1] : null;
			if(me instanceof JMenuItem){
				return (JMenuItem) me;
			} else {
				return null;
			}
		}
//		for (int i = 0; i < menu.getMenuCount(); i++) {
//			subMenu1 = menu.getMenu(i);
//			if(subMenu1.getText().equals("Document")){
//				break;
//			}
//		}
//		if(subMenu1 == null){
//			return null;
//		}
//		if(!subMenu1.getText().equals("Document")){
//			return null;
//		}
//		JMenuItem subMenu2 = null;
//		for (int i = 0; i < subMenu1.getItemCount(); i++) {
//			subMenu2 = subMenu1.getItem(i);
//			if(subMenu2.getText().equals("Schema")){
//				break;
//			}
//		}
//		if(subMenu2 == null){
//			return null;
//		}
//		if(!subMenu2.getText().equals("Schema")){
//			return null;
//		}
//		MenuElement subMenu3 = subMenu2.getSubElements()[0];
//		for (int i = 0; i < subMenu3.getSubElements().length; i++) {
//			if(subMenu3.getSubElements()[i] instanceof JMenuItem){
//				JMenuItem subMenu4 = (JMenuItem) subMenu3.getSubElements()[i];
//				if(subMenu4.getText().equals("Associate Schema...")){
//					return subMenu4;
//				}
//			}
//		}
//		return null;
	}

	@Override
	public boolean isEnable() {
		JMenuItem toolbarItem = getMenuItem();
		if (toolbarItem == null) {
			return false;
		}
		return toolbarItem.isEnabled();
	}

}
