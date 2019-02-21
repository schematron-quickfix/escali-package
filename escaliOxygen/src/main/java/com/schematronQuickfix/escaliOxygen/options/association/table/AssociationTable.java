package com.schematronQuickfix.escaliOxygen.options.association.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.ext.swing.GridBagConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.gui.swing.SwingUtil;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.schematronQuickfix.escaliOxygen.EscaliPlugin;
import com.schematronQuickfix.escaliOxygen.toolbar.main.OxygenToolbarButton;

import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.documenttype.DocumentTypeInformation;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.util.editorvars.EditorVariables;

public class AssociationTable extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final String NULL_FRAMEWORK_LABEL = "";

	// private ArrayList<AssociationRule> rows = new
	// ArrayList<AssociationRule>();
	private AssociationRuleTable ruleTable;

	String[] columnNames = { "", "Schema", "Match type", "Match Pattern", "Phase", "Language" };

	private JTable table;

	private TableModel tableModel;

	private final SelectionListener selListener = new SelectionListener();

	private final GridBagLayout gbl;

	// private class Row {
	//
	//
	// private File schema = null;
	// private String pattern = "*.*";
	// private String[] phases = new String[]{};
	// private int phaseSel = -1;
	// private String[] langs = new String[]{};
	// private int langSel = -1;
	//
	// File getSchema(){return this.schema;}
	// String getPattern(){return this.pattern;}
	// String[] getPhases(){return this.phases;}
	// String[] getLanguages(){return this.langs;}
	// }

	protected class TableModel extends AbstractTableModel implements TableCellRenderer {
		protected final static int MIN_SIZE = 10;

		private final DefaultTableCellRenderer transRender = new DefaultTableCellRenderer();

		public TableModel() {
			this.transRender.setOpaque(false);
		}

		private boolean isOutOfBound(int row) {
			return row >= ruleTable.size();
		}

		public String getColumnName(int col) {
			return columnNames[col].toString();
		}

		@Override
		public int getRowCount() {
			int size = ruleTable.size();
			if (size < MIN_SIZE)
				return MIN_SIZE;
			return size;
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnNames.length;
		}

		@Override
		public Object getValueAt(final int rowIndex, int columnIndex) {
			if (isOutOfBound(rowIndex)) {
				return null;
			}
			AssociationRule row = ruleTable.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return row.isSelected();
			case 1:
				return new SchemaCell(row.getSchema(), rowIndex);
			case 2:
				final JComboBox<String> boxM = new WideComboBox<>(AssociationRule.MATCH_MODE_LABELS);
				boxM.setSelectedIndex(row.getMatchMode());
				boxM.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						tableModel.fireTableCellUpdated(rowIndex, 3);
					}
				});
				return boxM;
			case 3:
				if (row.getMatchMode() == AssociationRule.FRAMEWORK_MATCH_MODE) {
					JComboBox<Framework> boxP = createFrameworkBox(row.getPattern());
					return boxP;
				} else {
					return row.getPattern();
				}
			case 4:
				JComboBox<String> boxP = new WideComboBox<String>(row.getPhases());
				boxP.setSelectedIndex(row.getPhaseSelection());
				boxP.setEnabled(row.getPhases().length > 1);
				return boxP;
			case 5:
				ArrayList<String> langs = row.getLanguages();
				String[] langsArr = langs.toArray(new String[langs.size()]);
				JComboBox<String> boxL = new WideComboBox<String>(langsArr);
				boxL.setSelectedIndex(row.getLangSelection());
				if(!(langsArr.length > 1)){
					boxL.setEnabled(false);
				}
				return boxL;
			}
			return null;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (isOutOfBound(rowIndex)) {
				return;
			}
			AssociationRule row = ruleTable.get(rowIndex);
			switch (columnIndex) {
			case 0:
				row.setSelected((Boolean) aValue);
				break;
			case 1:
				row.setSchema(aValue);
				break;
			case 2:
				row.setMatchMode(aValue);
				break;
			case 3:
				row.setPattern(aValue);
				break;
			case 4:
				row.setPhaseSelection(aValue);
				break;
			case 5:
				row.setLangueageSelection(aValue);
				break;
			}
			this.fireTableCellUpdated(rowIndex, columnIndex);
		}

		@Override
		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return !(isOutOfBound(rowIndex)) && ruleTable.isActive();
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component comp = null;
			final boolean setSelectionColor = isSelected && column != 0 && ruleTable.isActive();

			if (value == null) {
				// JLabel l = new JLabel("..");
				// comp = l;

				if (column == 0) {
					comp = new JPanel();
				} else {
					comp = new JLabel("") {
						public void setEnabled(boolean enabled) {
							this.setOpaque(!enabled || setSelectionColor);
							super.setEnabled(enabled);
						}
					};
				}

			} else if (value instanceof Component) {
				comp = (Component) value;
			} else if (value instanceof Boolean) {

				JCheckBox jCheckBox = new JCheckBox();
				Boolean selected = (Boolean) value;
				jCheckBox.setSelected(selected);
				comp = jCheckBox;

			} else {
				JLabel jl = new JLabel(value.toString());
				jl.setOpaque(!ruleTable.isActive() || setSelectionColor);
				comp = jl;
			}
			if(comp instanceof JComboBox){
				JComboBox box = (JComboBox) comp;
				comp.setEnabled(ruleTable.isActive() && box.getItemCount() > 1);
			} else {
				comp.setEnabled(ruleTable.isActive());
			}

			if (setSelectionColor) {
				comp.setBackground(table.getSelectionBackground());
				comp.setForeground(table.getSelectionForeground());
				if (comp instanceof SchemaCell)
					((SchemaCell) comp).setOpaque(true);
				if(comp instanceof JComboBox){
					final JComboBox box = (JComboBox) comp;

					box.setBackground(Color.RED);
					box.setOpaque(true);

					if(column == 3){
						box.setBackground(table.getSelectionBackground());
						JComboBox<Framework> boxFw = ((JComboBox<Framework>) box);
						final ListCellRenderer<Framework> defaultRenderer = (ListCellRenderer<Framework>) boxFw.getRenderer();
						boxFw.setRenderer(new ListCellRenderer<Framework>() {
							@Override
							public Component getListCellRendererComponent(JList<? extends Framework> list, Framework value, int index, boolean isSelected, boolean cellHasFocus) {
								UIResource comp = (UIResource) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
								comp.setOpaque(true);
								return comp;
							}
						});

					} else {

						box.setRenderer(new DefaultListCellRenderer(){

							@Override
							public Color getBackground() {
								return table.getSelectionBackground();
							}

							@Override
							public Color getForeground() {
								return box.isEnabled() ? table.getSelectionForeground() : super.getForeground();
							}
						});

					}




				}
			}
			setToolTipText(getColumnName(column));
			return comp;
		}

		TableCellEditor getCellEditor(final int row, final int column) {
			Object value = getValueAt(row, column);
			if (value instanceof Boolean) {
				JCheckBox jcb = new JCheckBox(selListener.createAction(row));
				jcb.setSelected((Boolean) value);

				return new DefaultCellEditor(jcb);
			}
			if (value instanceof JComboBox)
				return new DefaultCellEditor((JComboBox) value);
			if (value instanceof SchemaCell) {
				SchemaCell cell = (SchemaCell) value;
				return cell.getCellEditor();
			}
			return new DefaultCellEditor(new JTextField(value.toString()));
		}

	}

	private class SelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return;
			if (!ruleTable.isActive())
				return;

			final DefaultListSelectionModel target = (DefaultListSelectionModel) e.getSource();
			for (int i = 0; i < ruleTable.size(); i++) {
				ruleTable.get(i).setSelected(target.isSelectedIndex(i));
				tableModel.fireTableCellUpdated(i, 0);
			}
		}

		public AbstractAction createAction(final int row) {
			return new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jbc = (JCheckBox) e.getSource();
					if (!jbc.isSelected()) {
						table.getSelectionModel().removeSelectionInterval(row, row);
					}
				}
			};
		}

	}
	
	
	private void removeRow(ArrayList<AssociationRule> removeRows) {
		ruleTable.removeAll(removeRows);
	}

	private static HashMap<String, String> frameworksByUrl = new HashMap<String, String>();

	public static void addFramework(String frameworkLocation, String name){
		if(!frameworksByUrl.containsKey(frameworkLocation)){
			frameworksByUrl.put(frameworkLocation, name);
		}
	}

	public static void updateFrameworks(){
		updateFrameworks(false);
	}
	@SuppressWarnings("unchecked")
	public static void updateFrameworks(boolean checkFileSystem){
		EscaliPlugin instance = EscaliPlugin.getInstance();
		if(instance == null)
			return;
		
		StandalonePluginWorkspace workspace = instance.getWorkspace();
		URL[] urls = workspace.getAllEditorLocations(StandalonePluginWorkspace.MAIN_EDITING_AREA);
		
		

		for (URL url : urls) {
			WSEditor editor = workspace.getEditorAccess(url, StandalonePluginWorkspace.MAIN_EDITING_AREA);
			if(editor != null){
				DocumentTypeInformation docType = editor.getDocumentTypeInformation();
				if(docType != null){
					String fwLocation = docType.getFrameworkStoreLocation();
					addFramework(fwLocation, docType.getName());
				}
			}
		}
		
		if(!checkFileSystem)
			return;
		
		
		File[] frameworkUrls = EditorVariables.getAllFrameworksDirs();
		Collection<File> allFrameworks = new ArrayList<File>();
		
		for (File file : frameworkUrls) {
			if(file.exists()){
				try {
					allFrameworks.addAll(FileUtils.listFiles(file.getAbsoluteFile(), new WildcardFileFilter("*.framework"), TrueFileFilter.TRUE));
				} catch (IllegalArgumentException e){
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		for (File fw : allFrameworks) {
			if(!frameworksByUrl.containsKey(fw.getAbsolutePath())){
				try {
					StringNode fwSN = new StringNode(fw);
					String fwName = fwSN.getNodeValue("/serialized/serializableOrderedMap/entry"
							+ "/documentTypeDescriptor-array/documentTypeDescriptor/field[@name = 'name']/String");
					addFramework(fw.getAbsolutePath(), fwName);

				} catch (IOException e) {
				} catch (SAXException e) {
				} catch (XMLStreamException e) {
				} catch (XPathExpressionException e) {
				}
			}
		}
		
	}
	
	private class Framework {
		
		String name;
		String url;
		Framework(String name, String url){
			this.name = name;
			this.url = url;
		}
		@Override
		public String toString() {
			return url;
		}
		@Override
		public int hashCode() {
			return url.hashCode();
		}
	}


	public JComboBox<Framework> createFrameworkBox(String selectedFwUrl) {
		StandalonePluginWorkspace workspace = EscaliPlugin.getInstance().getWorkspace();
//		URL[] urls = workspace.getAllEditorLocations(StandalonePluginWorkspace.MAIN_EDITING_AREA);

		JComboBox<Framework> fwBox = new WideComboBox<>();
		final ListCellRenderer<? super Framework> defaultRenderer = fwBox.getRenderer();

		fwBox.setRenderer(new ListCellRenderer<Framework>() {
			public Component getListCellRendererComponent(JList<? extends Framework> list, Framework value, int index,
					boolean isSelected, boolean cellHasFocus) {
				Component comp = defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(comp instanceof UIResource){
					UIResource uiRes = (UIResource) comp;
					uiRes.setText(value.name);
					uiRes.setToolTipText(value.url);
				}
				return comp;
			}
		});
		
		ArrayList<String> urlList = new ArrayList<String>(frameworksByUrl.keySet());
		
		Collections.sort(urlList);
		
		for (String url : urlList) {
			String name = frameworksByUrl.get(url);
			Framework fw = new Framework(name, url);
			fwBox.addItem(fw);
			if(fw.url.equals(selectedFwUrl)){
				fwBox.setSelectedItem(fw);
			}
		}
		
//		String fwDir = workspace.getUtilAccess().expandEditorVariables(EditorVariables.FRAMEWORKS_DIRECTORY, null);
//		Collection<File> allFrameworks = FileUtils.listFiles(new File(fwDir), new WildcardFileFilter("*.framework"),
//				TrueFileFilter.TRUE);
//
//		for (File fw : allFrameworks) {
//			System.out.println(fw.toString());
//		}
//		// workspace.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA).getEditorLocation()
//
//		HashSet<String> fwNames = new HashSet<String>();
//		for (URL url : urls) {
//			DocumentTypeInformation docType = workspace
//					.getEditorAccess(url, StandalonePluginWorkspace.MAIN_EDITING_AREA).getDocumentTypeInformation();
//			String fwName = docType != null ? docType.getName() : NULL_FRAMEWORK_LABEL;
//			fwNames.add(fwName);
//		}
//		for (String fwName : fwNames) {
//			fwBox.addItem(new JLabel(fwName));
//		}
		return fwBox;
	}

	private void removeSelectedRows() {
		removeRow(ruleTable.getSelectedRows());
	}

	private boolean rowJump(AssociationRule row, boolean down) {
		int idx = ruleTable.indexOf(row);
		if (idx >= 0) {
			AssociationRule[] rowArray = ruleTable.toArray(new AssociationRule[ruleTable.size()]);
			int switchRowIdx = down ? idx + 1 : idx - 1;
			if (switchRowIdx >= 0 && switchRowIdx < rowArray.length) {
				rowArray[idx] = rowArray[switchRowIdx];
				rowArray[switchRowIdx] = row;

				Collections.sort(ruleTable, new RowComparator(Arrays.asList(rowArray)));
				updateSelection();
				table.updateUI();
				return true;
			} else
				return false;
		} else {
			return false;
		}
	}

	private void updateSelection() {
		ListSelectionModel sm = table.getSelectionModel();
		ArrayList<AssociationRule> selections = new ArrayList<AssociationRule>();

		for (AssociationRule row : ruleTable) {
			if (row.isSelected())
				selections.add(row);
		}
		sm.clearSelection();
		for (AssociationRule row : selections) {
			int i = ruleTable.indexOf(row);
			sm.addSelectionInterval(i, i);
		}
	}

	private class RowComparator implements Comparator<AssociationRule> {
		private final List order;
		private final boolean reverse;

		public RowComparator(List<AssociationRule> order) {
			this(order, false);
		}

		public RowComparator(List order, boolean reverse) {
			this.order = order;
			this.reverse = reverse;
		}

		@Override
		public int compare(AssociationRule row1, AssociationRule row2) {
			int idx1 = order.indexOf(row1);
			int idx2 = order.indexOf(row2);
			return reverse ? idx2 - idx1 : idx1 - idx2;
		}
	}

	private void rowJump(ArrayList<AssociationRule> rows, final boolean down) {
		Collections.sort(rows, new RowComparator(AssociationTable.this.ruleTable, down));

		for (AssociationRule row : rows) {
			boolean valid = rowJump(row, down);
			if (!valid)
				break;
		}
	}

	public AssociationTable() throws IOException {
		this(AssociationRuleTable.getEmptyRuleTable());
	}

	public AssociationTable(AssociationRuleTable ruleTable) throws IOException {
		this.gbl = new GridBagLayout();
		this.ruleTable = ruleTable;

		this.setLayout(gbl);
		
		updateFrameworks();
		
		tableModel = new TableModel();
		table = new JTable(tableModel) {

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				return tableModel;
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				return tableModel.getCellEditor(row, column);
			}

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Border rightBottom = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY);
				Component c = super.prepareRenderer(renderer, row, column);
				JComponent jc = (JComponent) c;
				if (column != 0)
					jc.setBorder(rightBottom);

				if (!AssociationTable.this.ruleTable.isActive()) {
					jc.setForeground(Color.GRAY);
				}

				return c;
			}

		};

		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);

		table.getSelectionModel().addListSelectionListener(selListener);

		int defaultFontSize = new JLabel().getFont().getSize();
		table.setRowHeight((int) (defaultFontSize * 1.5));

		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		GridBagLayout rightGBL = new GridBagLayout();
		final JPanel rightPanel = new JPanel(rightGBL);
		JPanel southPanel = new JPanel();

		// northPanel.add(table.getTableHeader());
		// this.add(table.getTableHeader(), BorderLayout.PAGE_START);
		SwingUtil.addComponent(this, gbl, northPanel, 1, 0, 1, 1, 1.0, 0.0, GridBagConstants.NORTH,
				GridBagConstants.HORIZONTAL);
		SwingUtil.addComponent(this, gbl, leftPanel, 0, 0, 1, 3, 0.0, 1.0, GridBagConstants.WEST,
				GridBagConstants.VERTICAL);
		SwingUtil.addComponent(this, gbl, centerPanel, 1, 1, 1, 1, 1.0, 1.0, GridBagConstants.CENTER,
				GridBagConstants.BOTH);
		SwingUtil.addComponent(this, gbl, rightPanel, 2, 0, 1, 3, 0.0, 1.0, GridBagConstants.EAST,
				GridBagConstants.VERTICAL);
		SwingUtil.addComponent(this, gbl, southPanel, 1, 2, 1, 1, 1.0, 0.0, GridBagConstants.SOUTH,
				GridBagConstants.HORIZONTAL);

		JViewport jvp = new JViewport();
		jvp.add(table);
		table.setPreferredScrollableViewportSize(
				new Dimension(table.getPreferredSize().width, table.getRowHeight() * TableModel.MIN_SIZE));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewport(jvp);

		JPanel rightTop = new JPanel();
		rightTop.setBorder(BorderFactory.createRaisedBevelBorder());
		scrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, rightTop);

		// scrollPane.setVerticalScrollBarPolicy(JideScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		// this.add(scrollPane, BorderLayout.CENTER);

		final JButton addRow = new OxygenToolbarButton(IconMap.getIcon("/images/Add16.png"), "Add a rule") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnable() {
				return AssociationTable.this.ruleTable.isActive();
			}

			@Override
			public void actionPerformed(ActionEvent ev) {
				AssociationRule preset = null;
				ArrayList<AssociationRule> selection = AssociationTable.this.ruleTable.getSelectedRows();
				if (selection.size() > 0) {
					preset = selection.get(0);
				}
				AssociationTable.this.ruleTable.add(AssociationRule.createRule(preset));
				table.updateUI();
			}
		};
		JButton delRow = new OxygenToolbarButton(IconMap.getIcon("/images/Remove16.png"), "Remove the selected rules") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnable() {
				return AssociationTable.this.ruleTable.isActive();
			}

			@Override
			public void actionPerformed(ActionEvent ev) {
				removeSelectedRows();
				table.updateUI();
			}
		};
		JButton upRow = new OxygenToolbarButton(IconMap.getIcon("/images/UpGray16.png"), "Move the selected rows up") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnable() {
				return AssociationTable.this.ruleTable.isActive();
			}

			@Override
			public void actionPerformed(ActionEvent ev) {
				rowJump(AssociationTable.this.ruleTable.getSelectedRows(), false);
			}
		};
		JButton downRow = new OxygenToolbarButton(IconMap.getIcon("/images/DownGray16.png"),
				"Move the selected rows down") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnable() {
				return AssociationTable.this.ruleTable.isActive();
			}

			@Override
			public void actionPerformed(ActionEvent ev) {
				rowJump(AssociationTable.this.ruleTable.getSelectedRows(), true);
			}
		};

		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));

		specifyColumns();

		// this.add(rightPanel, BorderLayout.EAST);

		SwingUtil.addComponent(rightPanel, rightGBL, addRow, 0, 0, 1, 1, 1.0, 0.0, GridBagConstants.NORTH,
				GridBagConstants.NONE);
		SwingUtil.addComponent(rightPanel, rightGBL, delRow, 0, 1, 1, 1, 1.0, 0.0, GridBagConstants.NORTH,
				GridBagConstants.NONE);
		SwingUtil.addComponent(rightPanel, rightGBL, upRow, 0, 2, 1, 1, 1.0, 0.0, GridBagConstants.NORTH,
				GridBagConstants.NONE);
		SwingUtil.addComponent(rightPanel, rightGBL, downRow, 0, 3, 1, 1, 1.0, 0.0, GridBagConstants.NORTH,
				GridBagConstants.NONE);
		SwingUtil.addComponent(rightPanel, rightGBL, new JPanel(), 0, 4, 1, 1, 1.0, 1.0, GridBagConstants.NORTH,
				GridBagConstants.VERTICAL);

		for (JPanel panel : new JPanel[] { northPanel, centerPanel, leftPanel, rightPanel, southPanel }) {
			// panel.setMinimumSize(new Dimension(0, 0));
			if (panel.getComponentCount() == 0) {
				panel.setPreferredSize(new Dimension(0, 0));
			}
		}
	}

	private void specifyColumns() {
		table.getTableHeader().setReorderingAllowed(false);

		final DefaultTableCellRenderer transRender = new DefaultTableCellRenderer();
		for (int i = 0; i < this.table.getColumnModel().getColumnCount(); i++) {
			TableColumn col = this.table.getColumnModel().getColumn(i);
			if (i == 0) {
				col.setCellRenderer(transRender);
				col.setMaxWidth(20);
				col.setPreferredWidth(20);
				col.setResizable(false);
			}

			col.setHeaderRenderer(new TableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					// if(column == 0)
					// return new JPanel();

					JTableHeader header = table.getTableHeader();
					Component comp = transRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
							column);
					if (comp instanceof JComponent) {
						JComponent jc = (JComponent) comp;
						jc.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
						jc.setBackground(header.getBackground());
						jc.setEnabled(ruleTable.isActive());
					}
					return comp;
				}
			});

		}

	}

	public void update(AssociationRuleTable ruleTable) {
		this.ruleTable = ruleTable;
		table.updateUI();
	}

	public void setActive(boolean isActive) {
		this.ruleTable.setActive(isActive);
		this.table.updateUI();
		this.table.getTableHeader().updateUI();
	}

	public static File askForFile(File currentDir) {
		// File f = spw.chooseFile(null, "Save the file list", new String[]
		// {ext}, null, true);
		final File[] result = new File[1];

		final JFileChooser fileChooser = new JFileChooser(currentDir);
		fileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// result[0] = fileChooser.getSelectedFile();
			}
		});
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			result[0] = fileChooser.getSelectedFile();
		}
		return result[0];
	}

	@Override
	public String toString() {
		return this.ruleTable.toString();
	}
}
