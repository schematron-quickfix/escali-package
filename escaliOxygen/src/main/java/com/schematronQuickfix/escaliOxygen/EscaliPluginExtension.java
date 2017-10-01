package com.schematronQuickfix.escaliOxygen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.oxygen.adapter.ToolbarMenuAdapter;
import com.github.oxygenPlugins.common.reflect.ReflectionUtil;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.xslt.MyTransformerFactory;
import com.github.oxygenPlugins.common.xml.xslt.XSLTStep;
import com.schematronQuickfix.escaliOxygen.editors.EscaliViewer;
import com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig;
import com.schematronQuickfix.escaliOxygen.tools.OxygenResolver;
import com.schematronQuickfix.xsm.operations.PositionalReplace;

import net.sf.saxon.lib.OutputURIResolver;
import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

public class EscaliPluginExtension implements WorkspaceAccessPluginExtension {
	public static IconMap ICONS;

	private EscaliViewer viewer;

	@Override
	public boolean applicationClosing() {
		// TODO Auto-generated method stub
		TextSource.resetResolver();
		return true;
	}

	@Override
	public void applicationStarted(final StandalonePluginWorkspace pluginWorkspaceAccess) {
		// TODO Auto-generated method stub

		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			// This is the implementation of the
			// WorkspaceAccessPluginExtension plugin interface.
			Thread.currentThread().setContextClassLoader(EscaliPluginExtension.this.getClass().getClassLoader());
			
			EscaliPlugin.getInstance().setWorkspace(pluginWorkspaceAccess);
			
			// XSLTStep.transfac = new T
			EscaliPluginConfig.implementConfig(pluginWorkspaceAccess);

			try {
				TextSource.implementResolver(new OxygenResolver(pluginWorkspaceAccess));

				XSLTStep.transfac = new MyTransformerFactory() {

					private OutputURIResolver outputResolver;

					@Override
					public void setOutputUriResolver(OutputURIResolver outputUriResolver) {
						this.outputResolver = outputUriResolver;

					}

					@Override
					public void setErrorListener(ErrorListener errorListener) {

					}

					@Override
					public void setAttribute(String key, Object value) {

					}

					@Override
					public Transformer newTransformer(Source xsl) throws TransformerConfigurationException {

						// if(useHe){
						// TransformerFactory transfacHe =
						// TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",
						// null);
						// return transfacHe.newTransformer(xsl);
						// }

						Transformer transformer = pluginWorkspaceAccess.getXMLUtilAccess().createXSLTTransformer(xsl,
								null, EscaliPluginConfig.config.getSaxonVersionForOxy());
						if (outputResolver != null) {
							try {

								Class<? extends Transformer> transformerClass = transformer.getClass();
								Object controler = null;
								if (ReflectionUtil.hasMethod(transformerClass, "getUnderlyingController")) {
									Method getUnderlyingControler = transformerClass
											.getMethod("getUnderlyingController", null);
									controler = getUnderlyingControler.invoke(transformer, null);
								} else if (ReflectionUtil.hasMethod(transformerClass, "setOutputURIResolver")) {
									controler = transformer;
								}
								if (controler != null) {
									Method setOutputURIResolver = controler.getClass().getMethod("setOutputURIResolver",
											OutputURIResolver.class);
									setOutputURIResolver.invoke(controler, new Object[] { outputResolver });
								}
								// for (Method m :
								// controler.getClass().getMethods()) {
								// if(m.getName().equals("setOutputURIResolver")){
								// m.invoke(controler, new
								// Object[]{outputResolver});
								// }
								// }

							} catch (NoSuchMethodException e) {
								System.err.println(e.getLocalizedMessage());
							} catch (SecurityException e) {
							} catch (IllegalAccessException e) {
							} catch (IllegalArgumentException e) {
								System.err.println(e.getLocalizedMessage());
							} catch (InvocationTargetException e) {
							}
							// if(transformer instanceof TransformerImpl){
							// TransformerImpl transImpl = (TransformerImpl)
							// transformer;
							// transImpl.getUnderlyingController().setOutputURIResolver(outputResolver);
							// }
						}

						return transformer;
					}
				};

				ICONS = new IconMap(new File(EscaliPlugin.descriptor.getBaseDir(), "icons/diagona_my.gif"));
				IconMap.ICONS = ICONS;

				// WSOptionsStorage options =
				// pluginWorkspaceAccess.getOptionsStorage();

				PositionalReplace.fastMode = false;

				ToolbarMenuAdapter tma = new ToolbarMenuAdapter();
				viewer = new EscaliViewer(tma, pluginWorkspaceAccess);
				pluginWorkspaceAccess.addViewComponentCustomizer(viewer);
				pluginWorkspaceAccess.addToolbarComponentsCustomizer(viewer);
				pluginWorkspaceAccess.addMenuBarCustomizer(viewer);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XSLTErrorListener e) {
				e.printStackTrace();
			}
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

}
