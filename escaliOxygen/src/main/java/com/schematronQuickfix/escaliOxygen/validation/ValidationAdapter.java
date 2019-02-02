package com.schematronQuickfix.escaliOxygen.validation;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.xml.xpath.XPathExpressionException;

import com.github.oxygenPlugins.common.collections.MultiValueHashMap;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.process.queues.VoidWorker;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.ValidationException;
import com.github.oxygenPlugins.common.xml.exceptions.ValidationSummaryException;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.control.report._Phase;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._Report;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escaliGuiComponents.adapter.EscaliEditorAdapter;
import com.schematronQuickfix.escaliOxygen.editors.EscaliMessanger;
import com.schematronQuickfix.escaliOxygen.options.EscaliPluginConfig;
import com.schematronQuickfix.escaliOxygen.options.association.ValidationInfoSet;
import com.schematronQuickfix.escaliOxygen.options.association.ValidationInfoSet.ValidationInfo;
import com.schematronQuickfix.escaliOxygen.toolbar.main.CommitChanges;
import com.schematronQuickfix.escaliOxygen.tools.PopupMenuCustomizer;
import com.schematronQuickfix.escaliOxygen.tools.ReadWrite;
import com.schematronQuickfix.escaliOxygen.tools.WSPageAdapter;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.exml.editor.EditorPageConstants;
import ro.sync.exml.workspace.api.PluginWorkspace;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.WSEditorPage;
import ro.sync.exml.workspace.api.editor.page.WSTextBasedEditorPage;
import ro.sync.exml.workspace.api.editor.page.text.xml.WSXMLTextEditorPage;
import ro.sync.exml.workspace.api.editor.validation.ValidationProblems;
import ro.sync.exml.workspace.api.editor.validation.ValidationProblemsFilter;
import ro.sync.exml.workspace.api.listeners.WSEditorListener;

public class ValidationAdapter extends ValidationProblemsFilter implements
		KeyListener, EscaliEditorAdapter {
	private final static String OXYGEN_SCH_ENGINE_NAME = "ISO Schematron";
	public final static String ESCALI_SCH_ENGINE_NAME = "Escali Schematron";

	private ValidationInfoSet validationInfo = null;

	private final WSEditor editor;
	private WSPageAdapter page;

	private final PopupMenuCustomizer popupCustomizer;

	private ArrayList<SVRLReport> reports = new ArrayList<SVRLReport>();

	public static ValidationEngine valEngine;

	private final EscaliMessanger ema;
	
	private String rememberPhase = null;

	private boolean isValidating;
	

	// private boolean isValidating = false;

	static {
		try {
			valEngine = new ValidationEngine();
		} catch (XSLTErrorListener e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private ValidationAdapter(WSEditor editor, EscaliMessanger ea) {
		this.editor = editor;
		this.validationInfo = ValidationInfoSet.createValidationInfo(editor.getEditorLocation());
		page = WSPageAdapter.getWSEditorAdapter(editor.getCurrentPage());
		this.ema = ea;
		this.popupCustomizer = new PopupMenuCustomizer(this, ea);
		customizePopup();
		checkForValidation();

		this.editor.addEditorListener(new WSEditorListener() {
			@Override
			public void editorPageChanged() {
				super.editorPageChanged();
				page = WSPageAdapter
						.getWSEditorAdapter(ValidationAdapter.this.editor
								.getCurrentPage());
				customizePopup();
			}
		});

	}

	private void checkForValidation() {
		
		this.validationInfo = ValidationInfoSet.createValidationInfo(editor, ema);
		
//		if (editor.getEditorLocation().toString().endsWith(".sch")) {
//			this.validationInfo = XmlModelSet.getSchematronModelSet(editor
//					.getEditorLocation().toString(), ema);
//		} else {
//			if(EscaliPluginConfig.config.useXmlModel()){
//				this.validationInfo = XmlModelSet.getXmlModelSet(editor, ema);
//			} else {
//				this.validationInfo = new XmlModelSet();
//			}
//		}
	}
	
	private static URL schemaIdUrl;
	static {
		try {
			schemaIdUrl = new URL("http://www.schematron-quickfix.com/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void filterValidationProblems(ValidationProblems problems) {
		EscaliPluginConfig config = EscaliPluginConfig.config;
		if(!config.isActive()){
			super.filterValidationProblems(problems);
			return;
		}
		
		if(this.isValidating)
			return;
		
		this.isValidating = true;
		ValidationProcessLogger oxygenLogger  = new ValidationProcessLogger();
		

		ArrayList<DocumentPositionedInfo> schProblems = new ArrayList<DocumentPositionedInfo>();
		
		List<DocumentPositionedInfo> problemList = problems
				.getProblemsList();
		problemList = problemList == null ? new ArrayList<DocumentPositionedInfo>()
				: problemList;

		String oxySchema = null;
		
		for (DocumentPositionedInfo posInfo : problemList) {
			
			String engineName = posInfo.getEngineName();
			if (engineName.equals(OXYGEN_SCH_ENGINE_NAME) || engineName.equals(ESCALI_SCH_ENGINE_NAME)) {
				schProblems.add(posInfo);
				if(schemaIdUrl.equals(posInfo.getAdditionalInfo())){
					oxySchema = posInfo.getMessage();
					
				} else {
//					oxySchema = posInfo.getSystemID();
				}
			}
		}
		
		checkForValidation();
		
//		implementation for Escali Pattern
//		if(oxySchema != null && config.useEscaliPattern()){
//			String piValue = "href=\"" + oxySchema + "\" schematypens=\"" + ProcessNamespaces.SCH_NS + "\"  type=\"application/xml\"";
//			validationInfo.createVirtualModel(piValue, editor.getEditorLocation().toString());
//		}
		
		
		if (validationInfo.size() > 0) {


			problemList.removeAll(schProblems);

			reports = new ArrayList<SVRLReport>();

			try {
				for (ValidationInfo vi : validationInfo) {
					if(!vi.isValid()){
						oxygenLogger.log(vi.getError());
						continue;
					}
					if(this.rememberPhase != null){
						vi.setPhase(rememberPhase);
					}
					addReport(valEngine.validate(vi, editor, oxygenLogger));
				}
				problemList.addAll(viewMessages(oxygenLogger));
			} catch (CancelException e) {
				problemList.addAll(viewErrorLog(oxygenLogger));
			}
			problems.setProblemsList(problemList);
		} else {
			ema.clearList();

		}
		

		super.filterValidationProblems(problems);

		this.isValidating = false;
		this.rememberPhase = null;
		this.notifySelection();
	}

	private ArrayList<DocumentPositionedInfo> viewErrorLog(
			ValidationProcessLogger logger) {
		return viewErrorLog(logger.getAllExceptions());
	}

	private ArrayList<DocumentPositionedInfo> viewErrorLog(
			ArrayList<Exception> allExceptions) {
		ArrayList<DocumentPositionedInfo> posInfos = new ArrayList<DocumentPositionedInfo>();
		for (Exception exc : allExceptions) {
			if (exc instanceof ValidationSummaryException) {
				for (ValidationException ve : ((ValidationSummaryException) exc)
						.getExceptionList()) {
					ve.printStackTrace();
					posInfos.add(valEngine.convertForOxygen(ve));
				}
			} else {
				exc.printStackTrace();
				posInfos.add(valEngine.convertForOxygen(exc));
			}
		}
		return posInfos;
	}

	private ArrayList<DocumentPositionedInfo> viewMessages(
			ValidationProcessLogger logger) throws CancelException {
		ArrayList<DocumentPositionedInfo> posInfos = new ArrayList<DocumentPositionedInfo>();
		posInfoByMessage = new HashMap<_SVRLMessage, DocumentPositionedInfo>();

		for (_SVRLMessage msg : getMessages()) {
			DocumentPositionedInfo posInfo;
			try {
				posInfo = valEngine.convertForOxygen(msg);
				posInfoByMessage.put(msg, posInfo);
				posInfos.add(posInfo);
			} catch (XPathExpressionException e) {
				logger.log(e);
			}
		}

		return posInfos;
	}

	private ArrayList<_SVRLMessage> getMessages() {
		ArrayList<_SVRLMessage> messages = new ArrayList<_SVRLMessage>();

		for (SVRLReport report : this.reports) {
			messages.addAll(report.getReport().getMessages());
		}

		return messages;
	}

	private boolean customizePopup = false;

	private final HashMap<_SVRLMessage, SVRLReport> reportByMessage = new HashMap<_SVRLMessage, SVRLReport>();
	private HashMap<_SVRLMessage, DocumentPositionedInfo> posInfoByMessage = new HashMap<_SVRLMessage, DocumentPositionedInfo>();

	private void customizePopup() {
		if (page != null) {
			ema.getGui().asJFrame().addKeyListener(this);

			page.removeKeyListener(this);
			page.addKeyListener(this);

			addShortCut(page.getComponent());

			page.removePopUpMenuCustomizer(popupCustomizer);
			page.addPopUpMenuCustomizer(popupCustomizer);

		}
	}

	// , InputEvent.ALT_GRAPH_DOWN_MASK
	private final KeyStroke contextMenue = KeyStroke.getKeyStroke('1',
			InputEvent.ALT_DOWN_MASK);


	private void addShortCut(JComponent comp) {

		comp.getInputMap().put(contextMenue, "escali_quickFix_menu");
		comp.getActionMap().put("escali_quickFix_menu", this.popupCustomizer);

	}

	public void executeFix(ArrayList<_QuickFix> fixes) {

		ema.getPluginWorkspace().open(editor.getEditorLocation());

		MultiValueHashMap<SVRLReport, _QuickFix> fixesBySVRLReport = new MultiValueHashMap<SVRLReport, _QuickFix>();
		for (_QuickFix fix : fixes) {
			fixesBySVRLReport.put(reportByMessage.get(fix.getParent()), fix);
		}
		for (final SVRLReport report : fixesBySVRLReport.keySet()) {
			ArrayList<_QuickFix> fixesOfReport = fixesBySVRLReport
					.getAll(report);
			final _QuickFix[] fixArr = fixesOfReport
					.toArray(new _QuickFix[fixesOfReport.size()]);
			boolean hasInvParams = false;
			for (_QuickFix fix : fixesOfReport) {
				if (fix.getInvalidParameter().length > 0) {
					hasInvParams = true;
					break;
				}
			}
			final HashMap<String, WSEditor> editors = ema.getEditorsByFix(fixArr);

			if (hasInvParams) {
				UserEntryDialog ued = new UserEntryDialog(fixArr, ema) {

					private static final long serialVersionUID = 3505130114101875020L;

					@Override
					public void executeFix(_QuickFix[] fix) {
						try {
							valEngine.fix(editors, fixArr, report);
							refreshMemory();
						} catch (XSLTErrorListener e) {
							ema.getPluginWorkspace().showErrorMessage(
									e.getLocalizedMessage());
							return;
						} catch (Exception e) {
							ema.getPluginWorkspace().showErrorMessage(
									e.getLocalizedMessage());
							return;
						}
					}
				};
				ued.showDialog();
			} else {
				try {
					valEngine.fix(editors, fixArr, report);
					refreshMemory();
				} catch (XSLTErrorListener e) {
					ema.getPluginWorkspace().showErrorMessage(
							e.getLocalizedMessage());
					return;
				} catch (Exception e) {
					ema.getPluginWorkspace().showErrorMessage(
							e.getLocalizedMessage());
					return;
				}
			}

		}
	}

	private void refreshMemory() {
		this.reports = new ArrayList<SVRLReport>();
		this.ema.getMessageList().removeAllItems();
	}

	public void executeFix(_QuickFix fix) {
		final HashMap<String, WSEditor> editorByBaseUri = ema.getEditorsByFix(fix);

		if (fix.getInvalidParameter().length > 0) {
			UserEntryDialog ued = new UserEntryDialog(new _QuickFix[] { fix },
					ema) {

				private static final long serialVersionUID = 5781457448605119796L;

				@Override
				public void executeFix(_QuickFix[] fix) {
					try {
						valEngine.fix(editorByBaseUri, fix,
								reportByMessage.get(fix[0].getParent()));
						refreshMemory();
					} catch (Exception e) {
						ema.getPluginWorkspace().showErrorMessage(
								e.getLocalizedMessage());
						return;
					}
				}
			};
			ued.showDialog();
		} else {
			try {
				valEngine.fix(editorByBaseUri, new _QuickFix[] { fix },
						reportByMessage.get(fix.getParent()));
				refreshMemory();
			} catch (Exception e) {
				ema.getPluginWorkspace().showErrorMessage(
						e.getLocalizedMessage());
				return;
			}
		}

	}

//	private HashMap<String, WSEditor> getEditorsByFix(_QuickFix fix) {
//		return getEditorsByFix(new _QuickFix[] { fix });
//	}
//
//	private HashMap<String, WSEditor> getEditorsByFix(_QuickFix[] fixArr) {
//		HashMap<String, WSEditor> editorByBaseUri = new HashMap<String, WSEditor>();
//		for (_QuickFix fix : fixArr) {
//			for (String baseUri : fix.getBaseUris()) {
//				URL url;
//				try {
//					url = new URL(baseUri);
//					ema.getPluginWorkspace().open(url);
//					WSEditor editor = ema.getPluginWorkspace().getEditorAccess(
//							url, StandalonePluginWorkspace.MAIN_EDITING_AREA);
//					editorByBaseUri.put(baseUri, editor);
//				} catch (MalformedURLException e) {
//				}
//			}
//		}
//		return editorByBaseUri;
//	}

	public ArrayList<_SVRLMessage> getMessageByCaret(WSTextBasedEditorPage page) {
		ArrayList<_SVRLMessage> messages = new ArrayList<_SVRLMessage>();
		int offset = page.getCaretOffset();

		for (_SVRLMessage msg : getMessages()) {
			DocumentPositionedInfo posInfo = posInfoByMessage.get(msg);
			int[] startEnd;
			try {
				startEnd = page.getStartEndOffsets(posInfo);
				if (startEnd[0] <= offset && startEnd[1] >= offset
						&& msg.hasQuickFixes()) {
					messages.add(msg);
				}
			} catch (BadLocationException e) {
			}
		}
		return messages;
	}

	private void addReport(SVRLReport report) {
		this.reports.add(report);
		for (_SVRLMessage msg : report.getReport().getMessages()) {
			this.reportByMessage.put(msg, report);
		}
	}

	public static boolean isSQFevailable(WSEditor editor) {
		EscaliPluginConfig config = EscaliPluginConfig.config;
		if(!config.isActive()){
			return false;
		}
		if (editor == null) {
			return false;
		}

		WSEditorPage page = editor.getCurrentPage();
		if (page == null)
			return false;

		if (editor.getCurrentPageID().equals(EditorPageConstants.PAGE_AUTHOR)) {
			return true;
		}

		if (editor.getCurrentPageID().equals(EditorPageConstants.PAGE_TEXT)) {
			if (page instanceof WSXMLTextEditorPage) {
				return true;
			}
			return false;
		}
		return false;
	}

	public static ValidationAdapter checkForSQFValidation(WSEditor editor,
			EscaliMessanger ema) {
		ValidationAdapter valAdap = new ValidationAdapter(editor, ema);

		editor.addValidationProblemsFilter(valAdap);
		
		return valAdap;
	}

	//
	// KEY LISTENER
	//

	@Override
	public void keyTyped(KeyEvent ke) {

	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (KeyEvent.VK_CONTROL == ke.getExtendedKeyCode()) {
			customizePopup = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (KeyEvent.VK_CONTROL == ke.getExtendedKeyCode()) {
			customizePopup = true;
		}
	}

	public void notifySelection() {
		ArrayList<_Report> reports = new ArrayList<_Report>();
		for (SVRLReport svrlRep : this.reports) {
			reports.add(svrlRep.getReport());
		}
		WSEditor curEditor = this.ema.getPluginWorkspace().getCurrentEditorAccess(PluginWorkspace.MAIN_EDITING_AREA);
		if(this.editor == curEditor){
			this.ema.viewReport(reports);
		}
	}

	@Override
	public File getInstanceFile() {
		try {
			return new File(editor.getEditorLocation().toURI());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	@Override
	public TextSource getInstance() {
		try {
			return TextSource.readTextFile(getInstanceFile());
//			return ReadWrite.createTextSource(editor, getInstanceFile());
		} catch (IOException e) {
			return null;
		}

	}

	@Override
	public String[] askForPhase(String[] phases, String defaultPhase) {
		return new String[] { defaultPhase };
	}

	@Override
	public String[] askForLanguage(String[] languages, String defaultLang) {
		// TODO Auto-generated method stub
		return new String[] { defaultLang };
	}

	@Override
	public void markMessages(_Report report) {
	}

	@Override
	public void markMessages(ArrayList<_Report> report) {
	}

	@Override
	public void selectMessage(_SVRLMessage msg, boolean forceFocus)
			throws CancelException {
		if (forceFocus) {
			open();
		}
		DocumentPositionedInfo posInfo = posInfoByMessage.get(msg);
		try {
			int[] startEnd = page.getStartEndOffsets(posInfo);
			page.select(startEnd[0], startEnd[1]);
		} catch (BadLocationException e) {
		}
	}

	public void open() {
		this.ema.getPluginWorkspace().open(editor.getEditorLocation());
	}

	@Override
	public VoidWorker setInstance(final TextSource instance, ProcessLoger loger) {
		return new VoidWorker(loger) {

			@Override
			protected Void doInBackground() throws Exception {
				ReadWrite.setEditorContent(editor, instance);
				return null;
			}
		};
	}

	@Override
	public int getInstanceSteps() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasChanges() {

		WSPageAdapter pageAdap = WSPageAdapter.getWSEditorAdapter(this.editor
				.getCurrentPage());
		if (pageAdap.getPageType() == WSPageAdapter.AUTHOR_PAGE) {
			return true;
		}
		Object[] changeMarker = pageAdap
				.evaluateXPath(CommitChanges.OXY_CHANGE_MARKER_XPATH);
		if (changeMarker == null)
			return false;
		return changeMarker.length > 0;
	}

	public boolean isPopupCustomizing() {
		// TODO Auto-generated method stub
		return this.customizePopup;
	}

	public void setPupupCustomizing(boolean customizing) {
		this.customizePopup = customizing;
	}

	public SVRLReport getReportByMessage(_SVRLMessage msg) {
		// TODO Auto-generated method stub
		return this.reportByMessage.get(msg);
	}

	public WSEditor getEditor() {
		// TODO Auto-generated method stub
		return this.editor;
	}

	public void viewPhase(_Phase phase) {
		this.rememberPhase = phase.getId();
	}

}
