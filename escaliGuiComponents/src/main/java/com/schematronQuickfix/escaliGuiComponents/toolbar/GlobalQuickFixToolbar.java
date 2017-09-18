package com.schematronQuickfix.escaliGuiComponents.toolbar;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.github.oxygenPlugins.common.gui.process.ProgressListener;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;
import com.schematronQuickfix.escaliGuiComponents.buttons._ToolbarButtonAction;
import com.schematronQuickfix.escaliGuiComponents.lists.MessageList;
import com.schematronQuickfix.escaliGuiComponents.lists.items.SVRLMessageListItem;


public class GlobalQuickFixToolbar extends JToolBar {

	
	private static final long serialVersionUID = -8730664898925056875L;
	
	
	
	public static ArrayList<JButton> getToolbarButtons(final EscaliMessangerAdapter ema, final MessageList messageList){
		ArrayList<JButton> buttons = new ArrayList<JButton>();
		buttons.add(ema.createToolbarBtn(ema.getIcon(10, 11), "Deselect all fix", new _ToolbarButtonAction() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				ArrayList<SVRLMessageListItem> items = messageList.getAllItems();
				for (SVRLMessageListItem item : items) {
					ema.notifyUnselcted(item.getModelNode());
				}
			}

			@Override
			public boolean isEnable() {
				// TODO Auto-generated method stub
				return ema.hasSelectedFix();
			}
		}));

		buttons.add(ema.createToolbarBtn(ema.getIcon(4, 19), "Select identical fixes (from identical tests)", new _ToolbarButtonAction() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				
			}

			@Override
			public boolean isEnable() {
				// TODO Auto-generated method stub
				return ema.hasSelectedFix();
			}
		}));
		
		buttons.add(ema.createToolbarBtn(ema.getIcon(12, 10), "Select identical fixes", new _ToolbarButtonAction() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				
			}

			@Override
			public boolean isEnable() {
				// TODO Auto-generated method stub
				return ema.hasSelectedFix();
			}
		}));
		
		
		buttons.add(ema.createToolbarBtn(ema.getIcon(11, 19), "Select all default fixes", new _ToolbarButtonAction() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				for(SVRLMessageListItem item : messageList.getAllItems()){
					_SVRLMessage msg = item.getModelNode();
					if(msg.hasDefaultFix()){
						ema.notifySelectedFix(msg.getDefaultFix());
					}
				}
			}

			@Override
			public boolean isEnable() {
				for(SVRLMessageListItem item : messageList.getAllItems()){
					if(item.getModelNode().hasDefaultFix())
						return true;
				}
				return false;
			}
		}));

		buttons.add(ema.createToolbarBtn(ema.getIcon(8, 13), "Execute all QuickFixes", new _ToolbarButtonAction() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				ArrayList<_QuickFix> fixList = new ArrayList<_QuickFix>(); 
				
				for(SVRLMessageListItem item : messageList.getAllItems()){
					_QuickFix fix = item.getSelectedFix();
					if(fix != null){
						fixList.add(fix);
					}
				}
				try {
					ProgressListener processLoger = new ProgressListener(EscaliMessangerAdapter.FIX_STEPS, "Fix execution", ema.getGui().asJFrame(), ema);
					ema.executeFix(fixList, processLoger).execute();
//					processLoger.end("Execute fix completed");
				} catch (CancelException e) {
					return;
				}
			}

			@Override
			public boolean isEnable() {
				// TODO Auto-generated method stub
				return ema.hasSelectedFix();
			}
		}));
		
		return buttons;
	}

	public GlobalQuickFixToolbar(final EscaliMessangerAdapter ema, final MessageList messageList){
		for (JButton btn : getToolbarButtons(ema, messageList)) {
			this.add(btn);
		}
	}
	
	
	
}
