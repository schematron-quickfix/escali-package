package com.schematronQuickfix.escaliOxygen.toolbar.main;

import java.awt.event.ActionEvent;

import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;
import com.schematronQuickfix.escaliOxygen.editors.EscaliViewer;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;


public class ShowViewButtons extends OxygenToolbarButton {

	private final EscaliMessanger ema;

	public ShowViewButtons(final EscaliMessanger ema) {
		super(ema.getIcon(15, 6), "Open QuickFix views");
		this.ema = ema;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		ema.getPluginWorkspace().showView(EscaliViewer.MESSANGER_ID, true);
		ema.showQuickFixAndUserEntryViewer();
	}

	@Override
	public boolean isEnable() {
		StandalonePluginWorkspace pw = ema.getPluginWorkspace();
		boolean isMsgShow = pw.isViewShowing(EscaliViewer.MESSANGER_ID);
		boolean isQFShow = pw.isViewShowing(EscaliViewer.QUICK_FIX_LIST_ID);
		boolean isUEShow = pw.isViewShowing(EscaliViewer.UELIST_ID);
		return !(isMsgShow && isQFShow && isUEShow);
	}

}
