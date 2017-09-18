package com.schematronQuickfix.escaliOxygen;

import javax.swing.JComponent;

import com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig;
import com.schematronQuickfix.escaliOxygen.options.OptionPage;

import ro.sync.exml.plugin.option.OptionPagePluginExtension;
import ro.sync.exml.workspace.api.PluginWorkspace;

public class EscaliPluginOptions extends OptionPagePluginExtension {
	
	private OptionPage page = new OptionPage();

	@Override
	public void apply(PluginWorkspace pw) {
		pw.getOptionsStorage().setOption(EscaliPluginConfig.ESCALI_PLUGIN_OPTION_KEY, page.toString());
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Escali Plugin Options";
	}

	@Override
	public JComponent init(PluginWorkspace pw) {
		String config = pw.getOptionsStorage().getOption(EscaliPluginConfig.ESCALI_PLUGIN_OPTION_KEY, EscaliPluginConfig.DEFAULT_CONFIG);
		page = new OptionPage(config);
		return page;
	}

	@Override
	public void restoreDefaults() {
		page.updateOptions(EscaliPluginConfig.DEFAULT_CONFIG);
	}

}
