package com.schematronQuickfix.escaliGuiComponents.buttons;


import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.exceptions.ErrorViewer;


public abstract class FileChooser extends ToolbarButton {
	private static final long serialVersionUID = -7882552876752059198L;
	public final int openMode;
	private FileChooserConnectComponent conectedField;
	private File instance;
	private final ErrorViewer ema;
	private final int dialogType;
	private final ArrayList<FileNameExtensionFilter> extFilter = new ArrayList<FileNameExtensionFilter>();
	
	
	public FileChooser(Icon icon, String label, ErrorViewer ema) {
		this(icon, label, ema, JFileChooser.FILES_AND_DIRECTORIES);
	}
	
	public FileChooser(Icon icon, String label, ErrorViewer ema, int openMode) {
		this(icon, label, ema, openMode, JFileChooser.OPEN_DIALOG);
	}
	
	public FileChooser(Icon icon, String label, ErrorViewer ema, int openMode, int dialogType) {
		this(icon, label, ema, openMode, dialogType, null);
	}
	public FileChooser(Icon icon, String label, ErrorViewer ema, int openMode, int dialogType, FileChooserConnectComponent conectedField) {
		this(icon, label, ema, openMode, dialogType, conectedField, new FileNameExtensionFilter[]{});
	}
	
	public FileChooser(Icon icon, String label, ErrorViewer ema, int openMode, int dialogType, FileChooserConnectComponent conectedField, FileNameExtensionFilter[] extensions) {
		super(icon, label);
		this.dialogType = dialogType;
		this.openMode = openMode;
		this.ema = ema;
		this.conectedField = conectedField;
		for (FileNameExtensionFilter filter : extensions) {
			extFilter.add(filter);
		}
	}
	
	public FileChooser(Icon icon, String label, ErrorViewer ema, FileChooserConnectComponent conectedField) {
		this(icon, label, ema, JFileChooser.FILES_AND_DIRECTORIES, JFileChooser.OPEN_DIALOG, conectedField);
	}
	
//	private FileChooser(Icon icon, String label, int openMode, JTextField conectedField, ErrorViewer ema, FileNameExtensionFilter[] extensions){
//		this(icon, label, JFileChooser.OPEN_DIALOG, openMode, conectedField, ema);
//		for (FileNameExtensionFilter filter : extensions) {
//			extFilter.add(filter);
//		}
//	}
//	
//	private FileChooser(Icon icon, String label, int openMode, JTextField conectedField, ErrorViewer ema){
//		this(icon, label, JFileChooser.OPEN_DIALOG, openMode, conectedField, ema);
//	}
//	private FileChooser(Icon icon, String label, int dialogType, int openMode, JTextField conectedField, ErrorViewer ema){
//		this(icon, label, openMode, ema);
//		this.conectedField = conectedField;
//		
//	}


	
	
//	public FileChooser(String label){
//		this(IconMap.getIcon(10, 4), label);
//	}


	abstract public void loadFile(File file) throws CancelException;
	abstract public File getWorkspace();
	
	
	@Override
	public void actionPerformed(ActionEvent ev)  {
		try {
			action();
		} catch (Exception e) {
			try {
				this.ema.viewException(e);
			} catch (CancelException e1) {}
			return;
		}
	}
	
	
	
	public void action() throws CancelException{
		instance = oeffnen();
		if (instance == null)
			return;
		if(conectedField != null)
			conectedField.setText(instance);
		loadFile(instance);
	}

	public File oeffnen() {
		final JFileChooser chooser = new JFileChooser("Choose file");

		chooser.setDialogType(dialogType);
		chooser.setFileSelectionMode(openMode);

		final File file = getWorkspace();

		chooser.setCurrentDirectory(file);
		for (FileNameExtensionFilter filter : extFilter) {
			chooser.setFileFilter(filter);
		}
		

		chooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals(
						JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
						|| e.getPropertyName().equals(
								JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
					@SuppressWarnings("unused")
					final File f = (File) e.getNewValue();
				}
			}
		});

		chooser.setVisible(true);
		final int result = chooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			File inputVerzFile = chooser.getSelectedFile();
			return inputVerzFile;
		}
		chooser.setVisible(false);
		return null;
	}
	public File getFile(){
		return instance;
	}
//	public FileChooser getCopy(){
//		FileChooser copy = new FileChooser(this.getIcon(), this.getText(), this.ema, this.openMode, this.dialogType, this.extFilter.toArray(new FileNameExtensionFilter[extFilter.size()])) {
//			private static final long serialVersionUID = 2422744704559847666L;
//			@Override
//			public void loadFile(File file) throws CancelException {
//				FileChooser.this.loadFile(file);
//			}
//			@Override
//			public File getWorkspace() {
//				// TODO Auto-generated method stub
//				return FileChooser.this.getWorkspace();
//			}
//			@Override
//			public boolean isEnable() {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		};
//		copy.setIcon(this.getIcon());
//		return copy;
//	}
}
