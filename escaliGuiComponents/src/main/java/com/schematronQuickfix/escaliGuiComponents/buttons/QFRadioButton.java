package com.schematronQuickfix.escaliGuiComponents.buttons;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escaliGuiComponents.lists.QuickFixList;


public class QFRadioButton extends JRadioButton {
	private static final long serialVersionUID = -5244033865831897940L;
	private final QuickFixList qfList;
	private final _QuickFix fix;

	public QFRadioButton(QuickFixList qfList, final _QuickFix fix){
		this.qfList = qfList;
		this.fix = fix;
		setHorizontalAlignment(SwingConstants.CENTER);
		setContentAreaFilled(false);
		
		addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				if(isSelected()){
					QFRadioButton.this.qfList.notifySelectedFix(fix);
				} else {
					QFRadioButton.this.qfList.notifyUnselection();
				}
			}
		});
	}
	
	public _QuickFix getQuickFix(){
		return this.fix;
	}
	
}
