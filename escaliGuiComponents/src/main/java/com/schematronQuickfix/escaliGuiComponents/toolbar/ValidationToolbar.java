package com.schematronQuickfix.escaliGuiComponents.toolbar;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JToolBar;

import com.github.oxygenPlugins.common.gui.process.ProgressListener;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;
import com.schematronQuickfix.escaliGuiComponents.buttons.FileChooser;
import com.schematronQuickfix.escaliGuiComponents.buttons.ToolbarButton;

//import net.sqf.utils.process.exceptions.CancelException;
//import net.sqf.view.utils.images.IconMap;
//import net.sqf.view.utils.process.ProgressListener;

public class ValidationToolbar extends JToolBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -419340173250800844L;
	private final EscaliMessangerAdapter ema;

	public ValidationToolbar(final EscaliMessangerAdapter ema){
		this.ema = ema;
		
		this.add(new FileChooser(ema.getIcon(10, 4), "Open Schematron Schema", ema) {
			
			private static final long serialVersionUID = 4752611618633546190L;

			@Override
			public void loadFile(File file) throws CancelException {
				ProgressListener processLoger = new ProgressListener(EscaliMessangerAdapter.OPEN_SCHEMA_STEPS, "Open Schematron Schema", ema.getGui().asJFrame(), ema);
				ValidationToolbar.this.ema.setSchema(file, processLoger).execute();
//				processLoger.end("Finished open Schematron Schema");
			}

			@Override
			public File getWorkspace() {
				return ema.getEditor().getInstanceFile();
			}

			@Override
			public boolean isEnable() {
				return true;
			}
		});
		
		
		this.add(new ToolbarButton(ema.getIcon(0, 12), "Update schema") {
			private static final long serialVersionUID = 455218777966237493L;

			@Override
			public void actionPerformed(ActionEvent ev) {
					try {
						ProgressListener processLoger = new ProgressListener(EscaliMessangerAdapter.COMPILE_SCHEMA_STEPS, "Compile Schematron Schema", ema.getGui().asJFrame(), ema);
						ema.compileSchema(processLoger).execute();
//						processLoger.end("Finished compiling Schematron Schema");
					} catch (Exception e) {
						try {
							ema.viewException(e);
						} catch (CancelException e1) {}
					}
			}

			@Override
			public boolean isEnable() {
				return ema.hasSchema();
			}
		});
		
		this.add(new ToolbarButton(ema.getIcon(12, 21), "Validate") {

			private static final long serialVersionUID = -8421882868455703156L;

			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					ProgressListener processLoger = new ProgressListener(EscaliMessangerAdapter.COMPILE_SCHEMA_STEPS, "Validate instance", ema.getGui().asJFrame(), ema);
					ema.validate(processLoger).execute();
//					processLoger.end("Finished validation");
				} catch (CancelException e) {
					try {
						ema.viewException(e);
					} catch (CancelException e1) {}
				}
			}

			@Override
			public boolean isEnable() {
				return ema.hasSchema();
			}
		});
		
	}
}
