package com.schematronQuickfix.escaliOxygen;

import ro.sync.diff.merge.api.MergeFilesException;
import ro.sync.diff.merge.api.MergedFileState;
import ro.sync.ecss.extensions.api.OptionListener;
import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.exml.workspace.api.Platform;
import ro.sync.exml.workspace.api.PluginResourceBundle;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.author.css.AuthorCSSAlternativesCustomizer;
import ro.sync.exml.workspace.api.editor.page.ditamap.keys.KeyDefinitionManager;
import ro.sync.exml.workspace.api.images.ImageUtilities;
import ro.sync.exml.workspace.api.license.LicenseInformationProvider;
import ro.sync.exml.workspace.api.listeners.BatchOperationsListener;
import ro.sync.exml.workspace.api.listeners.WSEditorChangeListener;
import ro.sync.exml.workspace.api.options.DataSourceAccess;
import ro.sync.exml.workspace.api.options.WSOptionsStorage;
import ro.sync.exml.workspace.api.process.ProcessController;
import ro.sync.exml.workspace.api.process.ProcessListener;
import ro.sync.exml.workspace.api.standalone.*;
import ro.sync.exml.workspace.api.standalone.actions.ActionsProvider;
import ro.sync.exml.workspace.api.standalone.actions.MenusAndToolbarsContributorCustomizer;
import ro.sync.exml.workspace.api.standalone.ditamap.TopicRefTargetInfoProvider;
import ro.sync.exml.workspace.api.templates.TemplateManager;
import ro.sync.exml.workspace.api.util.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class DummyPluginWorkspace implements StandalonePluginWorkspace {
    @Override
    public void addToolbarComponentsCustomizer(ToolbarComponentsCustomizer componentsCustomizer) {

    }

    @Override
    public void addViewComponentCustomizer(ViewComponentCustomizer viewComponentCustomizer) {

    }

    @Override
    public void addMenuBarCustomizer(MenuBarCustomizer menuBarCustomizer) {

    }

    @Override
    public void addTopicRefTargetInfoProvider(String protocol, TopicRefTargetInfoProvider targetInfoProvider) {

    }

    @Override
    public void showView(String viewID, boolean requestFocus) {

    }

    @Override
    public void hideView(String viewID) {

    }

    @Override
    public boolean isViewShowing(String viewID) {
        return false;
    }

    @Override
    public void hideToolbar(String toolbarID) {

    }

    @Override
    public void showToolbar(String toolbarID) {

    }

    @Override
    public boolean isToolbarShowing(String toolbarID) {
        return false;
    }

    @Override
    public String getOxygenActionID(Action action) {
        return null;
    }

    @Override
    public ActionsProvider getActionsProvider() {
        return null;
    }

    @Override
    public void addMenusAndToolbarsContributorCustomizer(MenusAndToolbarsContributorCustomizer customizer) {

    }

    @Override
    public void removeMenusAndToolbarsContributorCustomizer(MenusAndToolbarsContributorCustomizer customizer) {

    }

    @Override
    public EditorComponentProvider createEditorComponentProvider(String[] allowedPages, String initialPage) throws AuthorComponentException {
        return null;
    }

    @Override
    public PluginResourceBundle getResourceBundle() {
        return null;
    }

    @Override
    public URL[] getAllEditorLocations(int editingArea) {
        return new URL[0];
    }

    @Override
    public WSEditor getEditorAccess(URL location, int editingArea) {
        return null;
    }

    @Override
    public WSEditor getCurrentEditorAccess(int editingArea) {
        return null;
    }

    @Override
    public XMLUtilAccess getXMLUtilAccess() {
        return null;
    }

    @Override
    public UtilAccess getUtilAccess() {
        return null;
    }

    @Override
    public void addEditorChangeListener(WSEditorChangeListener editorListener, int editingArea) {

    }

    @Override
    public void removeEditorChangeListener(WSEditorChangeListener editorListener, int editingArea) {

    }

    @Override
    public WSEditorChangeListener[] getEditorChangeListeners(int editingArea) {
        return new WSEditorChangeListener[0];
    }

    @Override
    public WSOptionsStorage getOptionsStorage() {
        return null;
    }

    @Override
    public void setDITAKeyDefinitionManager(KeyDefinitionManager keyDefitionManager) {

    }

    @Override
    public void addAuthorCSSAlternativesCustomizer(AuthorCSSAlternativesCustomizer cssAlternativesCustomizer) {

    }

    @Override
    public void removeAuthorCSSAlternativesCustomizer(AuthorCSSAlternativesCustomizer cssAlternativesCustomizer) {

    }

    @Override
    public void showPreferencesPages(String[] pagesToShowKeys, String pageToSelectKey, boolean showChildrenOfPages) {

    }

    @Override
    public void addBatchOperationsListener(BatchOperationsListener listener) {

    }

    @Override
    public void removeBatchOperationsListener(BatchOperationsListener listener) {

    }

    @Override
    public boolean open(URL url) {
        return false;
    }

    @Override
    public boolean open(URL url, String imposedPage) {
        return false;
    }

    @Override
    public boolean open(URL url, String imposedPage, String imposedContentType) {
        return false;
    }

    @Override
    public void saveAll() {

    }

    @Override
    public boolean close(URL url) {
        return false;
    }

    @Override
    public boolean closeAll() {
        return false;
    }

    @Override
    public void delete(URL url) throws IOException {

    }

    @Override
    public void refreshInProject(URL url) {

    }

    @Override
    public URL createNewEditor(String extension, String contentType, String content) {
        return null;
    }

    @Override
    public boolean isStandalone() {
        return false;
    }

    @Override
    public Platform getPlatform() {
        return null;
    }

    @Override
    public void setParentFrameTitle(String parentFrameTitle) {

    }

    @Override
    public Object getParentFrame() {
        return null;
    }

    @Override
    public File chooseFile(String title, String[] allowedExtensions, String filterDescr, boolean openForSave) {
        return null;
    }

    @Override
    public File chooseFile(File currentFileContext, String title, String[] allowedExtensions, String filterDescr, boolean usedForSave) {
        return null;
    }

    @Override
    public File chooseFile(String title, String[] allowedExtensions, String filterDescr) {
        return null;
    }

    @Override
    public File[] chooseFiles(File currentFileContext, String title, String[] allowedExtensions, String filterDescr) {
        return new File[0];
    }

    @Override
    public File chooseDirectory() {
        return null;
    }

    @Override
    public URL chooseURL(String title, String[] allowedExtensions, String filterDescr) {
        return null;
    }

    @Override
    public URL chooseURL(String title, String[] allowedExtensions, String filterDescr, String initialURL) {
        return null;
    }

    @Override
    public String chooseURLPath(String title, String[] allowedExtensions, String filterDescr) {
        return null;
    }

    @Override
    public String chooseURLPath(String title, String[] allowedExtensions, String filterDescr, String initialURL) {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public String getVersionBuildID() {
        return null;
    }

    @Override
    public String getUserInterfaceLanguage() {
        return null;
    }

    @Override
    public String getPreferencesDirectory() {
        return null;
    }

    @Override
    public int showConfirmDialog(String title, String message, String[] buttonNames, int[] buttonIds) {
        return 0;
    }

    @Override
    public int showConfirmDialog(String title, String message, String[] buttonNames, int[] buttonIds, int initialSelectedIndex) {
        return 0;
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void showWarningMessage(String message) {

    }

    @Override
    public void showInformationMessage(String message) {

    }

    @Override
    public void showStatusMessage(String statusMessage) {

    }

    @Override
    public void openInExternalApplication(URL url, boolean preferAssociatedApplication) {

    }

    @Override
    public void openInExternalApplication(URL url, boolean preferAssociatedApplication, String mediaType) {

    }

    @Override
    public ProcessController createJavaProcess(String additionalJavaArguments, String[] classpath, String mainClass, String additionalArguments, Map<String, String> environmentalVariables, File startDirectory, ProcessListener processListener) {
        return null;
    }

    @Override
    public void startProcess(String name, File workingDirectory, String cmdLine, boolean showConsole) {

    }

    @Override
    public LicenseInformationProvider getLicenseInformationProvider() {
        return null;
    }

    @Override
    public void clearImageCache() {

    }

    @Override
    public DataSourceAccess getDataSourceAccess() {
        return null;
    }

    @Override
    public ImageUtilities getImageUtilities() {
        return null;
    }

    @Override
    public TemplateManager getTemplateManager() {
        return null;
    }

    @Override
    public void setMathFlowFixedLicenseKeyForEditor(String fixedKey) {

    }

    @Override
    public void setMathFlowFixedLicenseKeyForComposer(String fixedKey) {

    }

    @Override
    public void setMathFlowFixedLicenseFile(File licenseFile) {

    }

    @Override
    public void setMathFlowInstallationFolder(File installationFolder) {

    }

    @Override
    public void addGlobalOptionListener(OptionListener listener) {

    }

    @Override
    public void removeGlobalOptionListener(OptionListener listener) {

    }

    @Override
    public Object getGlobalObjectProperty(String key) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void setGlobalObjectProperty(String key, Object value) {

    }

    @Override
    public void importGlobalOptions(File optionsFile) {

    }

    @Override
    public void importGlobalOptions(File optionsFile, boolean preserveExistingOptionKeys) throws IOException {

    }

    @Override
    public void saveGlobalOptions() throws IOException {

    }

    @Override
    public Object openDiffFilesApplication(URL leftURL, URL rightURL) {
        return null;
    }

    @Override
    public Object openDiffFilesApplication(URL leftURL, URL rightURL, URL ancestorURL) {
        return null;
    }

    @Override
    public List<MergedFileState> openMergeApplication(File baseDir, File personalModifiedFilesDir, File externalModifiedFilesDir, Map<String, String> mergeOptions) throws MergeFilesException {
        return null;
    }

    @Override
    public void addInputURLChooserCustomizer(InputURLChooserCustomizer inputURLChooserCustomizer) {

    }

    @Override
    public void addRelativeReferencesResolver(String protocol, RelativeReferenceResolver resolver) {

    }

    @Override
    public ColorTheme getColorTheme() {
        return null;
    }

    @Override
    public ImageInverter getImageInverter() {
        return null;
    }
}
