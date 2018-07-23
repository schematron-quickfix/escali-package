package com.schematronQuickfix.escaliOxygen;

import javax.swing.JComponent;

import com.github.oxygenPlugins.common.process.log.DefaultProcessLoger;
import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig;
import com.schematronQuickfix.escaliOxygen.options.OptionPage;

import ro.sync.exml.plugin.option.OptionPagePluginExtension;
import ro.sync.exml.workspace.api.PluginWorkspace;

public class EscaliPluginOptions extends OptionPagePluginExtension {
	
	static {
		DefaultProcessLoger.setDefaultProcessLogger(new MuteProcessLoger());
	}
	
	private OptionPage page = new OptionPage();

	@Override
	public void apply(PluginWorkspace pw) {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			// This is the implementation of the
			// WorkspaceAccessPluginExtension plugin interface.
			Thread.currentThread().setContextClassLoader(EscaliPluginOptions.this.getClass().getClassLoader());
			pw.getOptionsStorage().setOption(EscaliPluginConfig.ESCALI_PLUGIN_OPTION_KEY, page.toString());

		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Escali Plugin Options ";
	}

	@Override
	public JComponent init(PluginWorkspace pw) {

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			// This is the implementation of the
			// WorkspaceAccessPluginExtension plugin interface.
			Thread.currentThread().setContextClassLoader(EscaliPluginOptions.this.getClass().getClassLoader());
			
			String config = pw.getOptionsStorage().getOption(EscaliPluginConfig.ESCALI_PLUGIN_OPTION_KEY, EscaliPluginConfig.DEFAULT_CONFIG);
			page = new OptionPage(config);
			return page;

		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	@Override
	public void restoreDefaults() {
		page.updateOptions(EscaliPluginConfig.DEFAULT_CONFIG);
	}

}
