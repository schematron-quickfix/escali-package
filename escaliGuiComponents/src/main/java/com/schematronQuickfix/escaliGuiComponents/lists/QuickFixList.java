package com.schematronQuickfix.escaliGuiComponents.lists;

import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.lists.AbstractList;
import com.github.oxygenPlugins.common.gui.lists.items.AbstractItemMouseListener;
import com.github.oxygenPlugins.common.gui.process.ProgressListener;
import com.github.oxygenPlugins.common.process.exceptions.ErrorViewer;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;
import com.schematronQuickfix.escaliGuiComponents.lists.items.QuickFixListItem;
import com.schematronQuickfix.escaliGuiComponents.toolbar.QuickFixToolbar;



public class QuickFixList extends AbstractList<_QuickFix, QuickFixListItem> {
	
	private final EscaliMessangerAdapter ema;
	private _QuickFix choosenFix = null;
	
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private class QuickFixListener extends AbstractItemMouseListener<_QuickFix, QuickFixListItem> {
		
		public QuickFixListener(QuickFixListItem item) {
			super(item, QuickFixList.this);
		}
		
		@Override
		public void oneClick(MouseEvent e, boolean isSelected) {
			if(isSelected){
				ema.getUeList().showUserEntries(item.getModelNode());
			} else {
//				ema.hideUserEntryViewer();
			}
		}
		@Override
		public void doubleClick(MouseEvent e) {
			try {
				ProgressListener processLoger = new ProgressListener(EscaliMessangerAdapter.FIX_STEPS, "Fix execution", ema.getGui().asJFrame(), ema);
				ema.executeFix(this.item.getModelNode(), processLoger).execute();
//				processLoger.end("Execute fix completed");
			} catch (CancelException e1) {
				return;
			}
		}
	}

	public QuickFixList(EscaliMessangerAdapter ema) {
		super(ema.isStandalon() ? "QuickFixes" : null);
		this.isMultiSelectable = false;
		this.ema = ema;
		int i = 0;
		for (JButton btn : QuickFixToolbar.getToolbarButtons(ema, this)) {
			this.addComponentToToolbar(btn, i++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
		}
		this.addComponentToToolbar(new JPanel(), i++, 0, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3491850582096741424L;

	private _SVRLMessage msg;

	public void showQuickFixes(_SVRLMessage msg, _QuickFix selectedFix) {
		this.msg = msg;
		resetList();
		for (_QuickFix fix : msg.getQuickFixes()) {
			QuickFixListItem qfli = new QuickFixListItem(ema, this, fix, this.buttonGroup, fix == selectedFix);
			this.addListItem(qfli);
			qfli.addMouseListener(new QuickFixListener(qfli));
		}
		this.updateUI();
	}
	
	private void resetList(){
		this.removeAllItems();
		this.choosenFix = null;
	}
	
	public _SVRLMessage getCurrentMessage(){
		return this.msg;
	}
	
	public ButtonGroup getButtonGroup(){
		return this.buttonGroup;
	}
	
	public void setChoosenFix(_QuickFix fix){
		QuickFixListItem item = this.getListItemByNode(fix);
		item.setAsChoosen();
	}
	
	public _QuickFix getChoosenQuickFix(){
		return this.choosenFix;
	}

	public boolean hasChoosenFix() {
		return this.choosenFix != null;
	}

	
	public void forceUnchoose(){
		this.buttonGroup.clearSelection();
	}
	
	public void notifyUnselection(){
		this.choosenFix = null;
		this.ema.hideUserEntryViewer();
	}

	public void notifySelectedFix(_QuickFix fix) {
		this.choosenFix = fix;
		this.ema.notifySelectedFix(fix);
	}


	
}
