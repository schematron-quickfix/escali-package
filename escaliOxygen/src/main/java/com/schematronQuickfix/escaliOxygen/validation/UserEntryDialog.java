package com.schematronQuickfix.escaliOxygen.validation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escaliGuiComponents.buttons.ToolbarButton;
import com.schematronQuickfix.escaliGuiComponents.lists.UserEntryList;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;


public abstract class UserEntryDialog extends JDialog {
	private static final long serialVersionUID = -6111053496870017022L;
	private final EscaliMessanger ema;

	public UserEntryDialog(final _QuickFix[] fixes, EscaliMessanger ema){
		this.ema = ema;
		this.setModal(true);
		
		JPanel contentPane = new JPanel();
		
		GridBagLayout gblCP = new GridBagLayout();
		
		contentPane.setLayout(gblCP);
		

		
		ToolbarButton okBtn = new ToolbarButton("OK") {
			
			private static final long serialVersionUID = -617878593527408928L;

			@Override
			public boolean isEnable() {
				for (_QuickFix fix : fixes) {
					if(fix.getInvalidParameter().length > 0){
						return false;
					}
				}
				return true;
			}
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				UserEntryDialog.this.setVisible(false);
				executeFix(fixes);
			}
		};
		

		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				UserEntryDialog.this.setVisible(false);
			}
		});
		int i = 0;
		
		UserEntryList uel = new UserEntryList(ema, this);
		uel.showUserEntries(fixes, okBtn, cancelBtn);
//		for (_QuickFix fix : fixes) {
//			
//			UserEntryList uel = new UserEntryList(ema, this);
//			uel.showUserEntries(fix);
//
//		}
		SwingUtil.addComponent(contentPane, gblCP, uel, 		0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3));
		

		
		SwingUtil.addComponent(contentPane, gblCP, okBtn, 			0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(0, 3, 3, 0));
		SwingUtil.addComponent(contentPane, gblCP, cancelBtn, 		1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(0, 3, 3, 3));
		
		contentPane.setBorder(BorderFactory.createEtchedBorder());
		
		this.setContentPane(contentPane);
		this.setUndecorated(true);
		this.pack();

		
	}
	
	public void showDialog(){
		showDialog(ema.getGui().asJFrame());
	}
	public void showDialog(JFrame frame){
		this.pack();
		SwingUtil.centerFrame(this, frame);
		this.setVisible(true);
	}
	
	public abstract void executeFix(_QuickFix[] fix);
}
