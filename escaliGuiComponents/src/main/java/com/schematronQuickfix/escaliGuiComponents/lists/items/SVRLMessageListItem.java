package com.schematronQuickfix.escaliGuiComponents.lists.items;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.xml.xpath.XPathExpressionException;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.lists.DefaultTheme;
import com.github.oxygenPlugins.common.gui.lists.items.AbstractListItem;
import com.github.oxygenPlugins.common.gui.process.ProgressListener;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;

public class SVRLMessageListItem extends AbstractListItem<_SVRLMessage> implements Comparable<SVRLMessageListItem> {

	private _QuickFix selectedFix = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4487992173205754784L;
	private final Icon hasQFicon;
	private String toolTipHasQF = "";

	private final EscaliMessangerAdapter ema;

	public SVRLMessageListItem(EscaliMessangerAdapter ema, _SVRLMessage msg) {
		super(msg, SVRLMessageListItem.getDefaultIcon(ema, msg), new DefaultTheme());
		this.ema = ema;
		if (msg.hasQuickFixes()) {
			hasQFicon = ema.getIcon(4, 4);
			toolTipHasQF = "This message has a QuickFix";
		} else {
			hasQFicon = ema.getIcon(IconMap.NOICON);
		}

		this.setControllIcon(hasQFicon, toolTipHasQF);
	}

	private static Icon getDefaultIcon(EscaliMessangerAdapter ema, _SVRLMessage node) {
		int elevel = node.getErrorLevelInt();
		switch (elevel) {
		case _SVRLMessage.LEVEL_FATAL_ERROR:
			return ema.getIcon(IconMap.MESSAGE_FATAL_ERROR);
		case _SVRLMessage.LEVEL_ERROR:
			return ema.getIcon(IconMap.MESSAGE_ERROR);
		case _SVRLMessage.LEVEL_WARNING:
			return ema.getIcon(IconMap.MESSAGE_WARNING);
		case _SVRLMessage.LEVEL_INFO:
			return ema.getIcon(IconMap.MESSAGE_INFO);
		default:
			return ema.getIcon(IconMap.MESSAGE_ERROR);
		}
	}

	public void notifySelectedFix(_QuickFix fix) {
		if (this.selectedFix == null) {
			setControllIcon(ema.getIcon(0, 13), "Execute fix");
		}
		this.selectedFix = fix;

	}

	public void notifyUnSelection() {
		if (this.selectedFix != null) {
			setControllIcon(this.hasQFicon, toolTipHasQF);
		}
		this.selectedFix = null;
	}

	private void setControllIcon(Icon icon, String tooltip) {
		JLabel label = new JLabel(icon);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (selectedFix != null) {
						ProgressListener processLoger = new ProgressListener(EscaliMessangerAdapter.FIX_STEPS,
								"Fix execution", ema.getGui().asJFrame(), ema);
						SVRLMessageListItem.this.ema.executeFix(selectedFix, processLoger).execute();
					} else {
						// ema.viewQuickFixes(SVRLMessageListItem.this.node,
						// selectedFix);
					}

				} catch (CancelException e1) {
					return;
				}
				super.mouseClicked(e);
			}
		});
		label.setToolTipText(tooltip);
		this.controlPanel.add(label);
		this.updateUI();
	}

	public _QuickFix getSelectedFix() {
		return this.selectedFix;
	}

	private int getLineNumber() {
		NodeInfo info = this.node.getLocationInIstance();
		return info.getStart().getLineNumber();
	}

	@Override
	public int compareTo(SVRLMessageListItem otherItem) {
		Integer ln = this.getLineNumber();
		return ln.compareTo(otherItem.getLineNumber());
	}

}
