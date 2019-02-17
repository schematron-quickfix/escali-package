package com.schematronQuickfix.escaliOxygen.validation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escaliOxygen.EscaliPluginExtension;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;

import ro.sync.exml.workspace.api.editor.WSEditor;

public class QuickFixMenuItem extends JMenuItem implements ActionListener{

	private static final long serialVersionUID = -599766027317795498L;
	private final WSEditor editor;
	private final _QuickFix fix;
	private final SVRLReport svrlReport;
	private final EscaliMessanger ema;

	public QuickFixMenuItem(_QuickFix fix, WSEditor editor, SVRLReport svrlReport, EscaliMessanger ema, String key) {
		super(fix.toString());
		this.fix = fix;
		this.editor = editor;
		this.svrlReport = svrlReport;
		this.ema = ema;
		
		ImageIcon icon;
		
		switch (fix.getRole()) {
			case _QuickFix.ROLE_DELETE:
				icon = EscaliPluginExtension.ICONS.getIcon(IconMap.QUICKFIX_DELETE);
				break;
			case _QuickFix.ROLE_ADD:
				icon = EscaliPluginExtension.ICONS.getIcon(IconMap.QUICKFIX_ADD);
				break;
			case _QuickFix.ROLE_REPLACE:
				icon = EscaliPluginExtension.ICONS.getIcon(IconMap.QUICKFIX_REPLACE);
				break;
			case _QuickFix.ROLE_STRINGREPLACE:
				icon = EscaliPluginExtension.ICONS.getIcon(IconMap.QUICKFIX_STRING_REPLACE);
				break;
	
			default:
				icon = EscaliPluginExtension.ICONS.getIcon(IconMap.QUICKFIX_REPLACE);
				break;
		}
		
		this.setIcon(icon);
		this.setToolTipText(fix.getDescription());
		if(!key.equals("")){
			this.setAccelerator(KeyStroke.getKeyStroke(key));
		}
//		this.add(new JLabel("test"));
		this.addActionListener(this);
	}

	@Override
	public Point getToolTipLocation(MouseEvent event) {
		return new Point(this.getWidth() + 10, 0);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(fix.hasParameter()){
			
			UserEntryDialog ueDialog = new UserEntryDialog(new _QuickFix[]{fix}, ema) {
				
				private static final long serialVersionUID = 6126451967389308039L;

				@Override
				public void executeFix(_QuickFix[] fix) {
					QuickFixMenuItem.this.executeFix();
				}
			};
			
			ueDialog.showDialog();
			
			
		} else {
			executeFix();
			
		}
	}
	
	private void executeFix(){
		try {
			HashMap<String, WSEditor> editors = ema.getEditorsByFix(new _QuickFix[]{fix});
			ValidationAdapter.valEngine.fix(editors, new _QuickFix[]{fix}, this.svrlReport);
		} catch (Exception e) {
			e.printStackTrace();
			ema.getPluginWorkspace().showErrorMessage(e.getMessage().equals("") ? e.toString() : e.getMessage());
			return;
		}
		
	}
}
