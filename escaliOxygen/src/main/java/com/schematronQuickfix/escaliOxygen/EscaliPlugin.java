package com.schematronQuickfix.escaliOxygen;
import ro.sync.exml.plugin.Plugin;
import ro.sync.exml.plugin.PluginDescriptor;


public class EscaliPlugin extends Plugin{
	/**
	 * The static plugin instance.
	 */
	private static EscaliPlugin instance = null;
	public static PluginDescriptor descriptor;

	public EscaliPlugin(PluginDescriptor descriptor) {
		super(descriptor);
		EscaliPlugin.descriptor = descriptor;
		if (instance != null) {
			throw new IllegalStateException("Already instantiated!");
		}
		instance = this;
	}
	
	/**
	 * Get the plugin instance.
	 * 
	 * @return the shared plugin instance.
	 */
	public static EscaliPlugin getInstance() {
		return instance;
	}
}
