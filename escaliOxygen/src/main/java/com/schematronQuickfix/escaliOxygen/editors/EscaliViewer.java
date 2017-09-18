package com.schematronQuickfix.escaliOxygen.editors;

import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.github.oxygenPlugins.common.oxygen.adapter.ToolbarMenuAdapter;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escaliOxygen.options.AboutEscaliPluginMenuItem;
import com.schematronQuickfix.escaliOxygen.toolbar.MainToolbar;

import ro.sync.exml.workspace.api.standalone.MenuBarCustomizer;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer;
import ro.sync.exml.workspace.api.standalone.ToolbarInfo;
import ro.sync.exml.workspace.api.standalone.ViewComponentCustomizer;
import ro.sync.exml.workspace.api.standalone.ViewInfo;

public class EscaliViewer implements ViewComponentCustomizer, ToolbarComponentsCustomizer, MenuBarCustomizer {
	
	public final static String MESSANGER_ID = "EscaliView";
	public final static String QUICK_FIX_LIST_ID = "QuickFixView";
	public final static String UELIST_ID = "UserEntryView";
	public final static String MAIN_TOOLBAR_ID = "EscaliToolbar";
	


	private final EscaliMessanger oxygenMessanger;
	private MainToolbar mainToolBar;
	private static AbstractButton validationBtn;
	private final ToolbarMenuAdapter tma;
	
	public EscaliViewer(ToolbarMenuAdapter tma, final StandalonePluginWorkspace pwa) throws XSLTErrorListener, IOException{
		this.tma = tma;
		OxygenEscaliGui gui = new OxygenEscaliGui(pwa);
		this.oxygenMessanger = new EscaliMessanger(gui, pwa, tma);
		this.mainToolBar = new MainToolbar(oxygenMessanger);
		
	}

	@Override
	public void customizeView(ViewInfo viewInfo) {
		// TODO Auto-generated method stub
		if(viewInfo.getViewID().equals(MESSANGER_ID)){
			viewInfo.setTitle("Escali Schematron");
			viewInfo.setComponent(oxygenMessanger.getMessageList());
		} else if(viewInfo.getViewID().equals(QUICK_FIX_LIST_ID)){
			viewInfo.setTitle("Escali QuickFixes");
			viewInfo.setComponent(oxygenMessanger.getQuickFixList());
		} else if(viewInfo.getViewID().equals(UELIST_ID)){
			viewInfo.setTitle("Escali UserEntries");
			viewInfo.setComponent(oxygenMessanger.getUeList());
		}
	}

	@Override
	public void customizeToolbar(ToolbarInfo barInfo) {
		tma.customizeToolbar(barInfo);
		if(barInfo.getToolbarID().equals(MAIN_TOOLBAR_ID)){
			this.mainToolBar.customizeToolbars(barInfo);
		}
	}
	

	@Override
	public void customizeMainMenu(JMenuBar menuInfo) {
		tma.customizeMainMenu(menuInfo);
		this.mainToolBar.implementBarInfo(menuInfo);
		
		Object frameObj = oxygenMessanger.getPluginWorkspace().getParentFrame();
		JFrame frame =  frameObj instanceof JFrame ? (JFrame) frameObj : null;

		menuInfo.getMenu(menuInfo.getMenuCount() - 1).add(new AboutEscaliPluginMenuItem(frame));
	}

}