package com.schematronQuickfix.escaliOxygen.options;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.batik.ext.swing.GridBagConstants;

import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.schematronQuickfix.escaliOxygen.options.association.table.AssociationRuleTable;
import com.schematronQuickfix.escaliOxygen.options.association.table.AssociationTable;


public class OptionPage extends JPanel {
	private static final long serialVersionUID = -221418094334720557L;
	// About Escali
	private final GridBagLayout gbl = new GridBagLayout();

	private final JCheckBox escaliPattern = new JCheckBox(
			"Detect schema by processing-instruction <?xml-model?>");
	private final JCheckBox active = new JCheckBox(
			"Use Escali Schematron Plugin for Schematron validation");
	private final JCheckBox xmlModel = new JCheckBox(
			"Detect schema by Escali Pattern");
	private final JCheckBox useRuleTable = new JCheckBox(
			"Detect schema by the following rule table");

	private final JComboBox<String> saxonVersion = new JComboBox<String>(
			EscaliPluginConfig.SAXON_VERSIONS);

	private AssociationTable ruleTableView;

	public OptionPage() {

		final ArrayList<JComponent> components = new ArrayList<JComponent>();
		JLabel saxVersLbl = new JLabel(
				"Select the Saxon version for Schematron validation: ");
		components.add(xmlModel);
		components.add(escaliPattern);
		components.add(saxVersLbl);
		components.add(saxonVersion);
		components.add(useRuleTable);

		this.setOpaque(true);
		this.setLayout(gbl);
		JLabel label = new JLabel("Escali Plugin Options");
		Font f = label.getFont();
		f = new Font(f.getName(), Font.BOLD, f.getSize());
		label.setFont(f);
		active.setSelected(true);
		active.setOpaque(false);

		active.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				for (JComponent comp : components) {
					comp.setEnabled(active.isSelected());
				}
				ruleTableView.setActive(active.isSelected());
			}
		});

		xmlModel.setSelected(true);
		xmlModel.setOpaque(false);

		escaliPattern.setSelected(true);
		escaliPattern.setOpaque(false);

		useRuleTable.setSelected(false);
		useRuleTable.setOpaque(false);

		try {
			ruleTableView = new AssociationTable(new AssociationRuleTable());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		useRuleTable.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				ruleTableView.setActive(useRuleTable.isSelected());
			}
		});

		SwingUtil.addComponent(this, gbl, label, 0, 0, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);
		SwingUtil.addComponent(this, gbl, active, 0, 1, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, saxVersLbl, 0, 2, 1, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, saxonVersion, 1, 2, 1, 1, 1.0, 0.0,
				GridBagConstants.NORTHEAST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, xmlModel, 0, 3, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);
		SwingUtil.addComponent(this, gbl, escaliPattern, 0, 4, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, useRuleTable, 0, 5, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, ruleTableView, 0, 6, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, new JPanel(), 0, 10, 1, 1, 1.0, 1.0,
				GridBagConstants.NORTHWEST, GridBagConstants.BOTH);

	}

	public OptionPage(String xml) {
		this();
		updateOptions(xml);
	}

	public void updateOptions(String xml) {

		EscaliPluginConfig tempConf = EscaliPluginConfig
				.createTempPluginFromXML(xml);

		this.active.setSelected(tempConf.isActive());
		int index = Arrays.asList(EscaliPluginConfig.SAXON_VERSIONS).indexOf(tempConf.getSaxonVersion());
		this.saxonVersion.setSelectedIndex(index);
		this.xmlModel.setSelected(tempConf.useXmlModel());
		this.escaliPattern.setSelected(tempConf.useEscaliPattern());

		this.useRuleTable.setSelected(tempConf.getRuleTable().isActive());
		this.ruleTableView.update(tempConf.getRuleTable());

		this.repaint();
	}
	
	private String getSaxonVersion(){
		int idx = this.saxonVersion.getSelectedIndex();
		if(idx < 0 || idx >= EscaliPluginConfig.SAXON_VERSIONS.length){
			idx = 0;
		}
		return EscaliPluginConfig.SAXON_VERSIONS[idx];
	}

	@Override
	public String toString() {
		String xml = "<es:escaliPluginConfig isActive=\""
				+ this.active.isSelected()
				+ "\" "
				+ "saxonVersion=\"" 
				+ getSaxonVersion()
				+ "\" xmlns:es=\"http://www.escali.schematron-quickfix.com/\">"
				+ "<es:detectSchema " + "pi=\"" + this.xmlModel.isSelected()
				+ "\" " + "esPattern=\"" + this.escaliPattern.isSelected()
				+ "\"/>" + this.ruleTableView.toString()
				+ "</es:escaliPluginConfig>";
		return xml;
	}
}
