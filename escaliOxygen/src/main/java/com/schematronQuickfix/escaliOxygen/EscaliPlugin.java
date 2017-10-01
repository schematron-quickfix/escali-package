package com.schematronQuickfix.escaliOxygen;
import ro.sync.exml.plugin.Plugin;
import ro.sync.exml.plugin.PluginDescriptor;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;


public class EscaliPlugin extends Plugin{
	/**
	 * The static plugin instance.
	 */
	private static EscaliPlugin instance = null;
	public static PluginDescriptor descriptor;
	private StandalonePluginWorkspace workspace;

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
	
	public void setWorkspace(StandalonePluginWorkspace workspace){
		if (this.workspace != null) {
			throw new IllegalStateException("Already instantiated!");
		}
		this.workspace =workspace;
	}
	
	public StandalonePluginWorkspace getWorkspace(){
		if (workspace == null) {
			throw new IllegalStateException("Not instantiated!");
		}
		return this.workspace;
	}
}
