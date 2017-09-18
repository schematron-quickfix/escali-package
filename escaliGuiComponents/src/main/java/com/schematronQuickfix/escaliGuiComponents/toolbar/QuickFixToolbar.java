package com.schematronQuickfix.escaliGuiComponents.toolbar;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.github.oxygenPlugins.common.gui.process.ProgressListener;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;
import com.schematronQuickfix.escaliGuiComponents.buttons._ToolbarButtonAction;
import com.schematronQuickfix.escaliGuiComponents.lists.QuickFixList;
import com.schematronQuickfix.escaliGuiComponents.lists.items.QuickFixListItem;


public class QuickFixToolbar extends JToolBar {

	private static final long serialVersionUID = -303895131784287317L;
	
	public static ArrayList<JButton> getToolbarButtons(final EscaliMessangerAdapter ema, final QuickFixList qfList){
		ArrayList<JButton> buttons = new ArrayList<JButton>();
		buttons.add(ema.createToolbarBtn(ema.getIcon(10, 11), "Deselect fix", new _ToolbarButtonAction() {


			@Override
			public void actionPerformed(ActionEvent ev) {
				qfList.forceUnchoose();
				ema.notifyUnselcted(qfList.getCurrentMessage());
			}

			@Override
			public boolean isEnable() {
				// TODO Auto-generated method stub
				return qfList.hasChoosenFix();
			}
		}));
		
		
		buttons.add(ema.createToolbarBtn(ema.getIcon(11, 19), "Select default fixes", new _ToolbarButtonAction() {

			
			private _QuickFix getDefaultFix(){
				_QuickFix defaultFix = null;
				for(QuickFixListItem qfli : qfList.getAllItems()){
					_QuickFix fix = qfli.getModelNode();
					if(fix.isDefault()){
						defaultFix = fix;
						break;
					}
				}
				return defaultFix;
			}
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				// TODO Auto-generated method stub
				_QuickFix defaultFix = getDefaultFix();
				if(defaultFix != null){
					qfList.setChoosenFix(defaultFix);
				}
			}

			@Override
			public boolean isEnable() {
				return getDefaultFix() != null;
			}
		}));
		
		buttons.add(ema.createToolbarBtn(ema.getIcon(0, 13), "Execute QuickFix & Validate", new _ToolbarButtonAction() {


			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					ProgressListener processLoger = new ProgressListener(EscaliMessangerAdapter.FIX_STEPS, "Fix execution", ema.getGui().asJFrame(), ema);
					ema.executeFix(qfList.getChoosenQuickFix(), processLoger).execute();
				} catch (CancelException e) {
					return;
				}
			}

			@Override
			public boolean isEnable() {
				// TODO Auto-generated method stub
				return qfList.hasChoosenFix();
			}
		}));
		
		return buttons;
	}

	public QuickFixToolbar(final EscaliMessangerAdapter ema, final QuickFixList qfList){
		for (JButton btn : getToolbarButtons(ema, qfList)) {
			this.add(btn);
		}
		
	}
}
