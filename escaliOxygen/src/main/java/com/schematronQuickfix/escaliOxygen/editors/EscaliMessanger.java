package com.schematronQuickfix.escaliOxygen.editors;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.github.oxygenPlugins.common.collections.MultiValueHashMap;
import com.github.oxygenPlugins.common.gui.buttons.AbstractDropDownButton;
import com.github.oxygenPlugins.common.gui.buttons._DropDownButtonAction;
import com.github.oxygenPlugins.common.oxygen.adapter.ToolbarMenuAdapter;
import com.github.oxygenPlugins.common.oxygen.buttons.DropDownButton;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.process.queues.VoidWorker;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.schematronQuickfix.escali.control.report._ModelNode;
import com.schematronQuickfix.escali.control.report._Phase;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escali.resources.EscaliArchiveResources;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliMessangerAdapter;
import com.schematronQuickfix.escaliOxygen.EscaliPlugin;
import com.schematronQuickfix.escaliOxygen.EscaliPluginExtension;
import com.schematronQuickfix.escaliOxygen.EscaliPluginOptions;
import com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig;
import com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig.ConfigChangeListener;
import com.schematronQuickfix.escaliOxygen.tools.WSPageAdapter;
import com.schematronQuickfix.escaliOxygen.options.OxygenOptionDialog;
import com.schematronQuickfix.escaliOxygen.validation.ValidationAdapter;

import ro.sync.exml.workspace.api.PluginWorkspace;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;


public class EscaliMessanger extends EscaliMessangerAdapter {
	private final StandalonePluginWorkspace spw;
	private EditorChangeListener editorListener;
	private final ToolbarMenuAdapter tma;
	

	public EscaliMessanger(OxygenEscaliGui gui, StandalonePluginWorkspace spw,
			ToolbarMenuAdapter tma) throws XSLTErrorListener, IOException {
//		new EscaliFileResources(new File(EscaliPlugin.descriptor.getBaseDir(), "lib"))
		super(gui, new EscaliArchiveResources(), false);
		this.spw = spw;
		this.tma = tma;
		this.editorListener = new EditorChangeListener(this);
		
//		JComponent emptyLabel = new JLabel("Open a XML document or connect the\n currently opened with a Schematron schema.");
//		JComponent emptyLabel = new JPanel();
		final String activeText = "Open a XML document or connect the\n currently opened with a Schematron schema.";
		final String inactiveText = "The plugin is not active!\nYou need to activate the plugin via options to use it.";
		final JTextPane emptyText = new JTextPane();
		emptyText.setText(EscaliPluginConfig.config.isActive() ? activeText : inactiveText);
		StyledDocument doc = emptyText.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setForeground(center, Color.GRAY);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		this.getMessageList().setEmptyLabel(emptyText);
		clearList();
		
		EscaliPluginConfig.config.addChangeListener(new ConfigChangeListener() {
			
			@Override
			public boolean configChanging(EscaliPluginConfig newConfig,
					EscaliPluginConfig oldConfig) {
				if(newConfig.isActive()){
					emptyText.setText(activeText);
					emptyText.repaint();
				} else {
					emptyText.setText(inactiveText);
					emptyText.repaint();
				}
				return true;
			}

			@Override
			public void configChanged(EscaliPluginConfig newConfig) {
				if(newConfig.isActive()){
					editorListener.checkForSQFValidations();
				} else {
					editorListener.removeAllAdapter();
				}
			}
		});
	}

	public void clearList(){
		this.getMessageList().removeAllItems();
	}

	@Override
	public void showQuickFixAndUserEntryViewer() {
		showQuickFixViewer();
		showUserEntryViewer();
	}

	@Override
	public void showUserEntryViewer() {
		spw.showView(EscaliViewer.UELIST_ID, true);
	}

	@Override
	public void hideQuickFixAndUserEntryViewer() {
		// spw.hideView(EscaliViewer.QUICK_FIX_LIST_ID);
		this.getQuickFixList().removeAllItems();
		hideUserEntryViewer();
	}

	@Override
	public void hideUserEntryViewer() {
		// spw.hideView(EscaliViewer.UELIST_ID);
		this.getUeList().removeAllItems();
	}

	@Override
	public void showQuickFixViewer() {
		spw.showView(EscaliViewer.QUICK_FIX_LIST_ID, true);
	}

	@Override
	public VoidWorker validate(_Phase phase, ProcessLoger logger)
			throws CancelException {
		// empty void worker, don't do anything
		VoidWorker vw = new VoidWorker(logger) {
			@Override
			protected Void doInBackground() throws Exception {
				return null;
			}
		};
		try {
			ValidationAdapter va = this.getEditor(phase);
			if (va != null) {
				va.viewPhase(phase);
				vw = new VoidWorker(logger) {
					@Override
					protected Void doInBackground() throws Exception {
						tma.validationButton();
						return null;
					}
				};
			}
		} catch (MalformedURLException e) {
		}

		return vw;
	}

	@Override
	public VoidWorker validate(ProcessLoger loger) throws CancelException {
		VoidWorker worker = new VoidWorker(loger) {

			@Override
			protected Void doInBackground() throws Exception {
				tma.validationButton();
				return null;
			}
		};
		return worker;
	}

	@Override
	public VoidWorker executeFix(final _QuickFix fix, ProcessLoger loger)
			throws CancelException {
		VoidWorker worker = new VoidWorker(loger) {

			@Override
			protected Void doInBackground() throws Exception {
				String baseUri = fix.getBaseUri();
				URL fixURL = new URL(baseUri);
				ValidationAdapter adapter = editorListener.getAdapter(fixURL);
				adapter.executeFix(fix);

				return null;
			}
		};

		return worker;
	}

	@Override
	public VoidWorker executeFix(final List<_QuickFix> fixList,
			ProcessLoger loger) throws CancelException {
		VoidWorker worker = new VoidWorker(loger) {
			@Override
			protected Void doInBackground() throws Exception {
				MultiValueHashMap<URL, _QuickFix> fixByURL = new MultiValueHashMap<URL, _QuickFix>();

				for (_QuickFix fix : fixList) {
					URL fixURL = new URL(fix.getBaseUri());
					fixByURL.put(fixURL, fix);
				}

				for (URL url : fixByURL.keySet()) {
					ArrayList<_QuickFix> fixes = fixByURL.getAll(url);
					ValidationAdapter adapter = editorListener.getAdapter(url);
					adapter.executeFix(fixes);
				}

				return null;
			}
		};
		return worker;
	}

	@Override
	public void showMessage(_SVRLMessage msg) {
		showMessage(msg, false);
	}

	@Override
	public void showMessage(_SVRLMessage msg, boolean forceFocus) {
		try {
			
			URL url = new URL(msg.getBaseUri());
			StandalonePluginWorkspace workspace = EscaliPlugin.getInstance().getWorkspace();
			
			if(forceFocus){
				workspace.open(url);
			} 
			WSEditor wsEditor = workspace.getEditorAccess(url, PluginWorkspace.MAIN_EDITING_AREA);
			
			if(wsEditor == null){
				return;
			}
			
			
			WSPageAdapter page = WSPageAdapter.getWSEditorAdapter(wsEditor.getCurrentPage());
			NodeInfo loc = msg.getLocationInIstance();
			int start = loc.getMarkStartLocation().getCharacterOffset();
			int end = loc.getMarkEndLocation().getCharacterOffset();
			
			page.select(start, end);
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void viewException(Exception e) throws CancelException {
		this.spw.showErrorMessage(e.getLocalizedMessage());
	}

	@Override
	public ImageIcon getIcon(int x, int y) {
		return EscaliPluginExtension.ICONS.getIcon(x, y);
	}

	@Override
	public ImageIcon getIcon(Point p) {
		return EscaliPluginExtension.ICONS.getIcon(p);
	}

	public StandalonePluginWorkspace getPluginWorkspace() {
		return this.spw;
	}

	public ValidationAdapter getEditor(_ModelNode node)
			throws MalformedURLException {
		URL url = new URL(node.getBaseUri());
		return this.editorListener.getAdapter(url);
	}

	@Override
	public boolean isStandalon() {
		return false;
	}

	@Override
	protected JButton getToolbarBtn() {
		return new ToolbarButton(null, true);
	}

	@Override
	public AbstractDropDownButton getDropDownButton(_DropDownButtonAction action) {
		return new DropDownButton(action);
	}

	public HashMap<String, WSEditor> getEditorsByFix(_QuickFix fix) {
		return getEditorsByFix(new _QuickFix[] { fix });
	}

	public HashMap<String, WSEditor> getEditorsByFix(_QuickFix[] fixArr) {
		HashMap<String, WSEditor> editorByBaseUri = new HashMap<String, WSEditor>();
		for (_QuickFix fix : fixArr) {
			for (String baseUri : fix.getBaseUris()) {
				URL url;
				try {
					url = new URL(baseUri);
					this.getPluginWorkspace().open(url);
					WSEditor editor = this
							.getPluginWorkspace()
							.getEditorAccess(url,
									StandalonePluginWorkspace.MAIN_EDITING_AREA);
					editorByBaseUri.put(baseUri, editor);
				} catch (MalformedURLException e) {
				}
			}
		}
		return editorByBaseUri;
	}
	
	@Override
	public boolean isOptionAvailable() {
		return true;
	}
	
	@Override
	public void showOptions() {
		new OxygenOptionDialog(new EscaliPluginOptions(), this.spw);
	}
}
