package com.schematronQuickfix.escaliOxygen.options;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.batik.ext.swing.GridBagConstants;

import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.github.oxygenPlugins.common.text.StringUtil;
import com.schematronQuickfix.escaliOxygen.options.association.table.AssociationRuleTable;
import com.schematronQuickfix.escaliOxygen.options.association.table.AssociationTable;

import static com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig.Language_Option.*;


public class OptionPage extends JPanel {
	private static final long serialVersionUID = -221418094334720557L;
	// About Escali
	private final GridBagLayout gbl = new GridBagLayout();

	private final JCheckBox escaliPattern = new JCheckBox(
			"Detect schema by Escali Pattern");
	private final JCheckBox active = new JCheckBox(
			"Use Escali Schematron Plugin for Schematron validation");
	private final JCheckBox xmlModel = new JCheckBox(
			"Detect schema by processing-instruction <?xml-model?>");
	private final JCheckBox useRuleTable = new JCheckBox(
			"Detect schema by the following rule table");

	private final JComboBox<String> saxonVersion = new JComboBox<String>(
			EscaliPluginConfig.SAXON_VERSIONS);
	
	private final JRadioButton useDefLanguage = new JRadioButton("Use the default language defined in the schema");
	private final JRadioButton useOxyLanguage = new JRadioButton("Use the Oxygen interface language (if available)");
	private final JRadioButton useSpecLanguage = new JRadioButton("Use one of the following languages (if available): ");
	private final ButtonGroup selectLangGroup = new ButtonGroup();
	private final JFormattedTextField languageField = new JFormattedTextField();
	


	private AssociationTable ruleTableView;

	public OptionPage() {

		final ArrayList<JComponent> components = new ArrayList<JComponent>();
		JLabel saxVersLbl = new JLabel(
				"Select the Saxon version for Schematron validation: ");
		JLabel langFieldLbl = new JLabel(
				"Prefered languages (codes in whitespace separated list): ");
		
		selectLangGroup.add(useDefLanguage);
		selectLangGroup.add(useOxyLanguage);
		selectLangGroup.add(useSpecLanguage);
		
		useSpecLanguage.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				languageField.setEnabled(useSpecLanguage.isSelected());
			}
		});
		
		components.add(xmlModel);
//		components.add(escaliPattern);
		components.add(saxVersLbl);
		components.add(saxonVersion);
		
		components.add(langFieldLbl);
		components.add(useOxyLanguage);
		components.add(useSpecLanguage);
		components.add(languageField);
		
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
		
		languageField.setText("");
		selectLangGroup.setSelected(useDefLanguage.getModel(), true);
		

		try {
			ruleTableView = new AssociationTable(AssociationRuleTable.getEmptyRuleTable());
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

		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
//		separator.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		SwingUtil.addComponent(this, gbl, separator, 0, 10, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.HORIZONTAL, new Insets(1, 0, 2, 0));
		
		SwingUtil.addComponent(this, gbl, active, 0, 20, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, saxVersLbl, 0, 30, 1, 1, 1.0, 0.0,
				GridBagConstants.WEST, GridBagConstants.NONE, new Insets(3, 0, 3, 0));

		SwingUtil.addComponent(this, gbl, saxonVersion, 1, 30, 1, 1, 1.0, 0.0,
				GridBagConstants.EAST, GridBagConstants.HORIZONTAL);
		
		SwingUtil.addComponent(this, gbl, new JLabel("Localization rules:"), 0, 40, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE, new Insets(7, 0, 2, 0));
		
		SwingUtil.addComponent(this, gbl, useDefLanguage, 0, 41, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);
		
		SwingUtil.addComponent(this, gbl, useOxyLanguage, 0, 42, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);
		
		SwingUtil.addComponent(this, gbl, useSpecLanguage, 0, 43, 1, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, languageField, 1, 43, 1, 1, 1.0, 0.0,
				GridBagConstants.NORTHEAST, GridBagConstants.HORIZONTAL);

		SwingUtil.addComponent(this, gbl, xmlModel, 0, 50, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE, new Insets(7, 0, 2, 0));
//		SwingUtil.addComponent(this, gbl, escaliPattern, 0, 4, 2, 1, 1.0, 0.0,
//				GridBagConstants.NORTHWEST, GridBagConstants.NONE);

		SwingUtil.addComponent(this, gbl, useRuleTable, 0, 60, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.NONE, new Insets(5, 0, 2, 0));

		SwingUtil.addComponent(this, gbl, ruleTableView, 0, 70, 2, 1, 1.0, 0.0,
				GridBagConstants.NORTHWEST, GridBagConstants.BOTH);

		SwingUtil.addComponent(this, gbl, new JPanel(), 0, 100, 1, 1, 1.0, 1.0,
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


		this.languageField.setText(StringUtil.stringJoin(tempConf.getSpecLanguage(), " "));
		this.languageField.setEnabled(false);
		this.useDefLanguage.setSelected(true);

		switch (tempConf.getPreferedLanguageOption()){
			case USE_OXYGEN_LANGUAGE:
				this.useOxyLanguage.setSelected(true);
				break;
			case USE_DEFAULT_LANGUAGE:
				this.useDefLanguage.setSelected(true);
				break;
			case USE_SPEC_LANGUAGE:
				this.useSpecLanguage.setSelected(true);
				this.languageField.setEnabled(true);
				break;
		}

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
	
	private String getSpecLang(){
		return this.languageField.getText();
	}

	private EscaliPluginConfig.Language_Option getLanguageOption(){
		if(useDefLanguage.isSelected()){
			return USE_DEFAULT_LANGUAGE;
		} else if(useOxyLanguage.isSelected()){
			return USE_OXYGEN_LANGUAGE;
		} else if(useSpecLanguage.isSelected()){
			return USE_SPEC_LANGUAGE;
		}
		return USE_DEFAULT_LANGUAGE;
	}



	@Override
	public String toString() {
		String xml = "<es:escaliPluginConfig isActive=\""
				+ this.active.isSelected()
				+ "\" "
				+ "saxonVersion=\"" 
				+ getSaxonVersion()
				+ "\" "
				+ "preferedLang=\""
				+ getLanguageOption()
				+ "\" "
				+ "specLang=\""
				+ getSpecLang()
				+ "\" "
				+ " xmlns:es=\"http://www.escali.schematron-quickfix.com/\">"
				+ "<es:detectSchema " + "pi=\"" + this.xmlModel.isSelected()
				+ "\" " + "esPattern=\"" + this.escaliPattern.isSelected()
				+ "\"/>" + this.ruleTableView.toString()
				+ "</es:escaliPluginConfig>";
		return xml;
	}
}
