package com.schematronQuickfix.escaliGuiComponents.adapter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.github.oxygenPlugins.common.gui.buttons.AbstractDropDownButton;
import com.github.oxygenPlugins.common.gui.buttons.DropDownButton;
import com.github.oxygenPlugins.common.gui.buttons._DropDownButtonAction;
import com.github.oxygenPlugins.common.gui.images.IconMap;
import com.github.oxygenPlugins.common.process.exceptions.CancelException;

import com.github.oxygenPlugins.common.process.exceptions.ErrorViewer;
import com.github.oxygenPlugins.common.process.log.ProcessLoger;
import com.github.oxygenPlugins.common.process.queues.VoidWorker;
import com.github.oxygenPlugins.common.process.queues.WatchFactory;
import com.github.oxygenPlugins.common.process.queues.WatchTask;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.schematronQuickfix.escali.control.Config;
import com.schematronQuickfix.escali.control.ConfigFactory;
import com.schematronQuickfix.escali.control.Escali;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.control.SchemaInfo;
import com.schematronQuickfix.escali.control.report._Phase;
import com.schematronQuickfix.escali.control.report._QuickFix;
import com.schematronQuickfix.escali.control.report._Report;
import com.schematronQuickfix.escali.control.report._SVRLMessage;
import com.schematronQuickfix.escali.resources.EscaliArchiveResources;
import com.schematronQuickfix.escali.resources.EscaliResourcesInterface;
import com.schematronQuickfix.escaliGuiComponents.buttons._ToolbarButtonAction;
import com.schematronQuickfix.escaliGuiComponents.lists.MessageList;
import com.schematronQuickfix.escaliGuiComponents.lists.QuickFixList;
import com.schematronQuickfix.escaliGuiComponents.lists.UserEntryList;
import com.schematronQuickfix.escaliGuiComponents.lists.items.QuickFixListItem;
import com.schematronQuickfix.escaliGuiComponents.lists.items.SVRLMessageListItem;


public abstract class EscaliMessangerAdapter implements ErrorViewer {
	
	private final Escali escali;
	private final IconMap iconMap = new IconMap();
	private final MessageList messageList;
	private final QuickFixList quickFixList;
	private final UserEntryList ueList;
	protected EscaliEditorAdapter editor;
	private SchemaInfo schemaInfo;
	protected final EscaliGuiAdapter gui;
	
	
//	Abstract methods

	public abstract void showQuickFixAndUserEntryViewer();
	public abstract void showUserEntryViewer();
	
	public abstract void hideQuickFixAndUserEntryViewer();
	public abstract void hideUserEntryViewer();

	public abstract void showQuickFixViewer();
	
	public void showMessage(_SVRLMessage modelNode){
		showMessage(modelNode, false);
	};
	public abstract void showMessage(_SVRLMessage modelNode, boolean forceFocus);
	
	public MessageList getMessageList() {
		return messageList;
	}
	public QuickFixList getQuickFixList() {
		return quickFixList;
	}
	public UserEntryList getUeList() {
		return ueList;
	}
	public abstract void viewException(Exception e) throws CancelException;
	
	public EscaliMessangerAdapter(EscaliGuiAdapter gui, EscaliResourcesInterface resources, boolean standalone) throws XSLTErrorListener, IOException{
		this.gui = gui;
		this.escali = new Escali(resources);
		
		messageList = new MessageList(this);
		quickFixList = new QuickFixList(this);
		ueList = new UserEntryList(this);
	}
	
	public boolean isStandalon(){return true;}
	
	public EscaliMessangerAdapter(EscaliGuiAdapter gui, boolean standalone) throws XSLTErrorListener, IOException{
		this( gui, new EscaliArchiveResources(), standalone);
	}
	
	public void setEditor(EscaliEditorAdapter editor){
		this.editor = editor;
		FIX_STEPS += editor.getInstanceSteps();
	}
	
	public EscaliGuiAdapter getGui() {
		return gui;
	}
	public EscaliEditorAdapter getEditor(){
		return editor;
	}
	

	public void viewReport(ArrayList<_Report> reports) {
		this.getMessageList().viewReport(reports);
		this.editor.markMessages(reports);
		this.hideQuickFixAndUserEntryViewer();
	}
	
	public void viewReport(_Report report) {
		// TODO Auto-generated method stub
		this.getMessageList().viewReport(report);
		this.editor.markMessages(report);
		this.hideQuickFixAndUserEntryViewer();
	}


	public void viewQuickFixes(_SVRLMessage msg, _QuickFix selectedFix) {
		if(msg.hasQuickFixes()){
			showQuickFixViewer();
			this.getQuickFixList().showQuickFixes(msg, selectedFix);
		} else {
			hideQuickFixAndUserEntryViewer();
		}
	}
	
	public void notifySelectedFix(_QuickFix fix) {
		this.getMessageList().notifySelectedFix(fix);
		QuickFixListItem item = this.getQuickFixList().getListItemByNode(fix);
		if(item != null){
			this.getQuickFixList().setChoosenFix(fix);
			if(fix.hasParameter()){
				showUserEntryViewer();
				this.getUeList().showUserEntries(fix);
			} else {
				hideUserEntryViewer();
			}
		}
	}
	
	public void notifyUnselcted(_SVRLMessage currentMessage) {
		this.getMessageList().notifyUnSelection(currentMessage);
		if(this.getQuickFixList().hasChoosenFix()){
			this.getQuickFixList().forceUnchoose();
		}
	}
	
	public boolean hasSchema(){
		return this.schemaInfo != null;
	}


	
	

	public static final int VALIDATE_STEPS = 2;
	public VoidWorker validate(final ProcessLoger loger) throws CancelException {
		VoidWorker worker = new VoidWorker(loger){
			@Override
			protected Void doInBackground() throws Exception {
				try {
					loger.log("Start validate instance");
					SVRLReport report = EscaliMessangerAdapter.this.escali.validate(editor.getInstance(), loger);
					loger.log("View error report");
					viewReport(report.getReport());
				} catch (Exception e) {
					EscaliMessangerAdapter.this.gui.viewException(e);
				}
				return null;
			}
		};
		
		return worker;
//		System.out.println(report.getFormatetReport(SVRLReport.TEXT_FORMAT));
	}
	
	
	public static final int COMPILE_SCHEMA_STEPS = 5 + VALIDATE_STEPS;
	public VoidWorker compileSchema(final ProcessLoger loger) throws CancelException {
		VoidWorker worker = new VoidWorker(loger){
			@Override
			protected Void doInBackground() throws Exception {
				try {
					if(!hasSchema()){
						loger.log(new Exception("No schema was setted"));
					}
					loger.log("Ask for phase/language", true);
					String[] phase = askForPhase(schemaInfo);
					String[] lang = askForLanguage(schemaInfo);
					loger.log("Start compiling");
					Config config = EscaliMessangerAdapter.this.escali.getConfig();
					config.setPhase(phase);
					config.setLanguage(lang);
					config.setXmlSaveMode(true);
					config.setCompactSVRL(true);
					
					EscaliMessangerAdapter.this.escali.compileSchema(schemaInfo.getSchema(), loger);
					this.setNextWorker(validate(loger));
				} catch (Exception e) {
					EscaliMessangerAdapter.this.gui.viewException(e);
				}
				return null;
			}
		};
		return worker;
	}
	
	
	private String[] askForPhase(SchemaInfo info){
		String[] phases = info.getPhases();
		String defPhase = info.getDefaultPhase();
		switch (phases.length) {
		case 0:
			return new String[]{defPhase};
		case 1:
			return phases;
		default:
			return this.editor.askForPhase(phases, defPhase);
		}
	}
		
	private String[] askForLanguage(SchemaInfo info){
		String[] languages = info.getLanguages();
		String defLang = info.getDefaultLanguage();
		switch (languages.length) {
		case 0:
			return new String[]{defLang};
		case 1:
			return languages;
		case 2:
			return languages;
		default:
			return this.editor.askForLanguage(languages, defLang);
		}
	}
	
	public static final int OPEN_SCHEMA_STEPS = 2 + COMPILE_SCHEMA_STEPS;
	public VoidWorker setSchema(final File schema, final ProcessLoger loger) throws CancelException {
		VoidWorker worker = new VoidWorker(loger){
			@Override
			protected Void doInBackground() throws Exception {
				TextSource tsSchema;
				try {
					loger.log("Read schema " + schema.getName());
					tsSchema = TextSource.readTextFile(schema);
					loger.log("Analyze schema");
					EscaliMessangerAdapter.this.schemaInfo = EscaliMessangerAdapter.this.escali.getSchemaInfo(tsSchema);
					this.setNextWorker(compileSchema(loger));
				} catch (Exception e) {
					EscaliMessangerAdapter.this.gui.viewException(e);
					
				}
				return null;
			}
			
		};
		return worker;
		
	}
	
	
	
	public boolean hasSelectedFix(){
		if(messageList == null){
			return false;
		}
		for (SVRLMessageListItem item : messageList.getAllItems()) {
			_QuickFix fix = item.getSelectedFix();
			if (fix != null) {
				return true;
			}
		}
		return false;
	}

	
//	setInstance steps of the editor will be added, when method setEditor() used
	public static int FIX_STEPS = 2 + VALIDATE_STEPS;
	public VoidWorker executeFix(_QuickFix fix, ProcessLoger loger) throws CancelException {
		return this.executeFix(new _QuickFix[]{fix}, loger);
	}
	public VoidWorker executeFix(List<_QuickFix> fixList, ProcessLoger loger) throws CancelException {
		return this.executeFix(fixList.toArray(new _QuickFix[fixList.size()]), loger);
	}
	public VoidWorker executeFix(final _QuickFix[] fixArray, final ProcessLoger loger) throws CancelException{
		VoidWorker worker = new VoidWorker(loger) {
			
			@Override
			protected Void doInBackground() throws Exception {
				// TODO Auto-generated method stub
//				String[] ids = new String[fixList.size()];
//				int i = 0;
//				for (_QuickFix fix : fixList) {
//					ids[i++] = fix.getId();
//				}
				
				try {
					loger.log("Start execute fix");
					TextSource newInstance = EscaliMessangerAdapter.this.escali.executeFix(fixArray, editor.getInstance()).get(0);
					loger.log("View fixed instance");
					VoidWorker instanceViewer = EscaliMessangerAdapter.this.editor.setInstance(newInstance, loger);
					instanceViewer.setNextWorker(EscaliMessangerAdapter.this.validate(loger));
					this.setNextWorker(instanceViewer);
				} catch (Exception e) {
					EscaliMessangerAdapter.this.gui.viewException(e);
				}
				return null;
			}
		};
		
		return worker;
	}
	
	public ImageIcon getIcon(int x, int y){
		return this.getIcon(new Point(x, y));
	}
	
	public ImageIcon getIcon(Point p){
		return this.iconMap.getIcon(p);
	}
	
	protected JButton getToolbarBtn(){
		return new JButton();
	}
	
	
	public AbstractDropDownButton getDropDownButton(Icon icon, String tooltip, _DropDownButtonAction action){
		AbstractDropDownButton btn = getDropDownButton(action);
		btn.setIcon(icon);
		btn.setToolTipText(tooltip);
		return btn;
		
	}
	public AbstractDropDownButton getDropDownButton(_DropDownButtonAction action){
		return new DropDownButton(action);
	}

	public JButton createToolbarBtn(Icon icon, String tooltip, final _ToolbarButtonAction action) {
		final JButton button = getToolbarBtn();
		button.setIcon(icon);
		button.setToolTipText(tooltip);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(e);
			}
		});
		WatchFactory.addWatchTask(new WatchTask() {
			@Override
			public void watch() {
				button.setEnabled(action.isEnable());
			}
		});
		return button;
	}
	
	public VoidWorker validate(_Phase phase, ProcessLoger logger) throws CancelException {
		this.escali.getConfig().setPhase(phase.getId());
		return validate(logger);
	}
	
	public boolean isOptionAvailable(){
		return false;
	}
	public void showOptions() {
		
	}

	

	
}
