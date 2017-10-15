package com.schematronQuickfix.escaliGuiComponents.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import com.github.oxygenPlugins.common.gui.buttons.AbstractDropDownButton;
import com.github.oxygenPlugins.common.gui.buttons._DropDownButtonAction;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.schematronQuickfix.escali.control.report._Phase;
import com.schematronQuickfix.escali.control.report._Report;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;
import com.schematronQuickfix.escaliGuiComponents.buttons._ToolbarButtonAction;
import com.schematronQuickfix.escaliGuiComponents.lists.MessageList;


public class ButtonFactory {
	public static class PhaseButton {
		private AbstractDropDownButton btn;
		private final EscaliMessangerAdapter ema;

		public PhaseButton(final EscaliMessangerAdapter ema) {
			this.ema = ema;
			this.btn = ema.getDropDownButton(ema.getIcon(12, 21), "Validate",
					new _DropDownButtonAction() {
						@Override
						public void action(MouseEvent e) {
							try {
								ema.validate(DefaultProcessLoger.getDefaultProccessLogger())
										.execute();
							} catch (CancelException e1) {
							}
						}
					});
		}

		public void implementReport(ArrayList<_Report> reports) {
			btn.removeAllMenuItems();
			int i = 0;
			for (_Report report : reports) {
				String[] phasesIds = report.getPhases();
				if (phasesIds != null) {
					for (String phaseId : phasesIds) {
						_Phase phaseReport = report.getPhaseReport(phaseId);
						PhaseMenuItem phaseItem = new PhaseMenuItem(
								phaseReport, ema);
						btn.addMenuItem(phaseItem);
					}
				}
				if (++i < reports.size()) {
					this.btn.addSeparator();
				}
			}
		}

		public void implementReport(_Report report) {
			ArrayList<_Report> reports = new ArrayList<_Report>();
			reports.add(report);
			implementReport(reports);
		}

		public JPanel getButton() {
			return this.btn;
		}
	}

	public static PhaseButton getPhaseButton(final EscaliMessangerAdapter ema,
			final MessageList messageList) {
		return new PhaseButton(ema);
	}

	public static class OptionButton {

		private AbstractButton btn;
		private final EscaliMessangerAdapter ema;

		public OptionButton(final EscaliMessangerAdapter ema) {
			this.ema = ema;
			this.btn = ema.createToolbarBtn(ema.getIcon(0, 4), "Options",
					new _ToolbarButtonAction() {

						@Override
						public boolean isEnable() {
							return ema.isOptionAvailable();
						}

						@Override
						public void actionPerformed(ActionEvent ev) {
							ema.showOptions();
						}
					});
		}
		

		public AbstractButton getButton() {
			return this.btn;
		}
	}
	
	public static OptionButton getOptionButton(final EscaliMessangerAdapter ema,
			final MessageList messageList) {
		return new OptionButton(ema);
	}
}
