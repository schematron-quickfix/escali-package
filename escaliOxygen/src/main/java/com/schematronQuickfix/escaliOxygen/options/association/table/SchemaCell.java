package com.schematronQuickfix.escaliOxygen.options.association.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.apache.batik.ext.swing.GridBagConstants;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.schematronQuickfix.escaliOxygen.EscaliPlugin;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;


public class SchemaCell extends JPanel {
	
	private final GridBagLayout gbl;
	private URL schemaFile;
	private final FileCellEditor cellEditor = new FileCellEditor();
	private final int rowIndex;
	private JLabel label;
	private JLabel iconLabel;
	
	protected SchemaCell(URL schemaURL, int rowIndex){
		super(new GridBagLayout());
		this.schemaFile = schemaURL;
		this.rowIndex = rowIndex;
		this.gbl = (GridBagLayout) this.getLayout();
		this.setBackground(Color.WHITE);
		
		label = new JLabel("...");
		if(schemaURL != null){
			String path = schemaURL.getPath();
			this.setToolTipText(path);
			path = path.replaceAll(".*/", "");
			label.setText(path);
		}
		Icon openIc = null;
		try {
			openIc = IconMap.getIcon("/images/Open12.gif");
		} catch (IOException e) {
		}
		
		SwingUtil.addComponent(this, gbl, label, 				0, 0, 1, 1, 1.0, 1.0, GridBagConstants.WEST, GridBagConstants.BOTH);
		iconLabel = new JLabel(openIc);
		SwingUtil.addComponent(this, gbl, iconLabel, 	1, 0, 1, 1, 0.0, 1.0, GridBagConstants.EAST, GridBagConstants.VERTICAL);
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cellEditor.mouseClicked(e);
			}
		};
		this.addMouseListener(ma);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		iconLabel.setEnabled(enabled);
		iconLabel.setOpaque(!enabled);
		label.setEnabled(enabled);
		label.setOpaque(!enabled);
		this.setOpaque(!enabled);
		super.setEnabled(enabled);
	}
	private void setSchema(URL schema){
		this.schemaFile = schema;
	}
	public URL getSchema(){
		return this.schemaFile;
	}
	
	TableCellEditor getCellEditor(){
		return cellEditor;
	}
	
	@Override
	public void setForeground(Color fg) {
		if(label != null){
			label.setForeground(fg);
		}
		super.setForeground(fg);
	}
	
	@Override
	public void setBackground(Color bg) {
		if(label != null){
			label.setBackground(bg);
		}
		super.setBackground(bg);
	}
	
	private class FileCellEditor extends AbstractCellEditor implements TableCellEditor{

	    protected static final String EDIT = "edit";
//		private JFileChooser fileChooser;
	    JTable table = null;
		
		@Override
		public Object getCellEditorValue() {
			return SchemaCell.this;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			this.table = table;
			return SchemaCell.this;
		}

		public void mouseClicked(MouseEvent e) {
			StandalonePluginWorkspace workspace = EscaliPlugin.getInstance().getWorkspace();
			URL url = workspace.chooseURL("Select the schema", new String[]{"sch", "sqf"}, "Schematron files");
			
			setSchema(url);
			fireEditingStopped();
		}
		
	}
}
