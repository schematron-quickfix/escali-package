package com.schematronQuickfix.escaliGuiComponents.lists;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JDialog;

import com.github.oxygenPlugins.common.gui.lists.AbstractList;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._UserEntry;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;
import com.schematronQuickfix.escaliGuiComponents.lists.items.UserEntryListItem;




public class UserEntryList extends AbstractList<_UserEntry, UserEntryListItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6909034934599995416L;
	private final JDialog parentDialog;
	private final EscaliMessangerAdapter ema;
	
	
	
	public UserEntryList(EscaliMessangerAdapter ema){
		this(ema, null);
	}

	public UserEntryList(EscaliMessangerAdapter ema, JDialog userEntryDialog) {
		super(ema.isStandalon() ? "User entries" : null);
		this.isMultiSelectable = false;
		this.ema = ema;
		this.parentDialog = userEntryDialog;
	}

	public void showUserEntries(_QuickFix fix) {
		showUserEntries(fix, null);
	}
	public void showUserEntries(_QuickFix fix, JComponent nextAndPrevComponent) {
		showUserEntries(fix, nextAndPrevComponent, nextAndPrevComponent);
	}
	
	public void showUserEntries(_QuickFix fix, JComponent nextComponent, JComponent prevComponent) {
		showUserEntries(new _QuickFix[]{fix}, false, nextComponent, prevComponent);
	}
	
	public void showUserEntries(_QuickFix[] fixes) {
		showUserEntries(fixes, null);
	}
	
	public void showUserEntries(_QuickFix[] fixes, boolean asGroup) {
		showUserEntries(fixes, asGroup, null);
	}
	
	public void showUserEntries(_QuickFix[] fixes, JComponent nextAndPrevComponent) {
		showUserEntries(fixes, nextAndPrevComponent, nextAndPrevComponent);
	}
	
	public void showUserEntries(_QuickFix[] fixes, boolean asGroup, JComponent nextAndPrevComponent) {
		showUserEntries(fixes, asGroup, nextAndPrevComponent, nextAndPrevComponent);
	}
	
	public void showUserEntries(_QuickFix[] fixes, JComponent nextComponent, JComponent prevComponent){
		showUserEntries(fixes, true, nextComponent, prevComponent);
	}

	
	public void showUserEntries(_QuickFix[] fixes, boolean asGroups, JComponent nextComponent, JComponent prevComponent){
		this.removeAllItems();
		JComponent firstItem = null;
		UserEntryListItem lastItem = null;
		
		for (_QuickFix fix : fixes) {
			ArrayList<UserEntryListItem> items = new ArrayList<UserEntryListItem>();
			for (_UserEntry param : fix.getParameter()) {
				UserEntryListItem ueli = new UserEntryListItem(ema, param, parentDialog);
				items.add(ueli);
				if(lastItem != null){
					lastItem.setNextComponent(ueli);
					ueli.setPrevComponent(lastItem);
				} else {
					firstItem = ueli;
				}
				lastItem = ueli;
			}
			if (asGroups) {
				JComponent group = this.addListItemAsGroup(items, fix);
				lastItem.setNextComponent(group);
			} else {
				for (UserEntryListItem ueli : items) {
					this.addListItem(ueli);
				}
			}
			
		}
		
		if(nextComponent != null){
			if (firstItem instanceof UserEntryListItem) {
				UserEntryListItem firstUE = (UserEntryListItem) firstItem;
				firstUE.setPrevComponent(lastItem);
			}
//			firstItem.setPrevComponent(prevComponent);
			lastItem.setNextComponent(nextComponent);
		} else {
			lastItem.setNextComponent(firstItem);
		}
		
		this.updateUI();
	}

	

}
