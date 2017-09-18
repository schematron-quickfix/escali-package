package com.schematronQuickfix.escaliGuiComponents.toolbar;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.schematronQuickfix.escali.control.report.Phase;
import com.schematronQuickfix.escali.control.report._Phase;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;


public class PhaseMenuItem extends JMenuItem {

	private static final long serialVersionUID = 1L;
	private final EscaliMessangerAdapter ema;
	private final _Phase phase;
	
	
	private final static Font BOLD_FONT;
	
	static {
		Font f = new JMenuItem().getFont();
		BOLD_FONT = new Font(f.getName(), Font.BOLD, f.getSize());
	}
	
	public PhaseMenuItem(final _Phase phase, EscaliMessangerAdapter ema) {
		super(phase.getName() + (phase.isDefault() ? "*" : ""));
		
		if(phase.getState() == Phase.STATE_ACTIVE){
			this.setFont(BOLD_FONT);
		}
		
		this.phase = phase;
		this.ema = ema;
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action();
			}
		});
	}
	
	private void action(){
		try {
			ema.validate(phase, new DefaultProcessLoger()).execute();
		} catch (CancelException e) {
		}
	}

}
