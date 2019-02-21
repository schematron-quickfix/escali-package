package com.schematronQuickfix.escaliOxygen;

import com.schematronQuickfix.escaliOxygen.options.OptionPage;
import com.schematronQuickfix.escaliOxygen.options.association.table.AssociationTable;
import ro.sync.exml.plugin.PluginDescriptor;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class OptionPageTester {


    public static void main(String[] args){
        final File sch = new File("escaliOxygen/src/test/resources/optionPageTest/sch/phase1NoDefault.sch");
        EscaliPlugin plugin = new EscaliPlugin(new PluginDescriptor());
        plugin.setWorkspace(new DummyPluginWorkspace() {
            @Override
            public URL chooseURL(String title, String[] allowedExtensions, String filterDescr) {
                try {
                    return sch.toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            }

        });

        AssociationTable.addFramework(new File("test.framework").getAbsolutePath(), "Test Framework");
        AssociationTable.addFramework(new File("test2.framework").getAbsolutePath(), "Test 2 Framework");

        ViewTester tester = new ViewTester();
        final OptionPage page = new OptionPage("<es:escaliPluginConfig isActive=\"true\" saxonVersion=\"Home Edition (HE)\" preferedLang=\"USE_SPEC_LANGUAGE\" specLang=\"en de fr\"  xmlns:es=\"http://www.escali.schematron-quickfix.com/\"><es:detectSchema pi=\"true\" esPattern=\"true\"/><es:rules xmlns:es=\"http://www.escali.schematron-quickfix.com/\" active=\"true\"><es:rule  schema=\"file:/C:/Users/Nico/Work/Java2/escali-main-package/escaliOxygen/src/test/resources/optionPageTest/sch/with2Lang.sch\" matchMode=\"0\" pattern=\"*.*\" phase=\"#ALL\"/></es:rules></es:escaliPluginConfig>");
        tester.test(page);

        tester.addCloseListener(new ViewTester.AfterClose() {
            @Override
            public void doOnClose() {
                System.out.println(page.toString());
            }
        });
    }

}
