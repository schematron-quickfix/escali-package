package com.schematronQuickfix.escali.helpers;

import com.github.oxygenPlugins.common.process.log.MuteProcessLoger;
import com.github.oxygenPlugins.common.text.TextSource;
import com.github.oxygenPlugins.common.xml.exceptions.XSLTErrorListener;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;
import com.schematronQuickfix.escali.control.SVRLReport;
import com.schematronQuickfix.escali.resources.EscaliArchiveResources;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.github.oxygenPlugins.common.xml.xpath.ProcessNamespaces.ES_NS;
import static com.schematronQuickfix.escali.control.SVRLReport.ESCALI_FORMAT;

public class ExpectedReportData {

    // meta
    protected String title = null;
    protected String phase = null;
    protected String queryBinding = null;

//    tests
    protected ArrayList<Test> asserts = null;
    protected ArrayList<Test> reports = null;

    public int getMessageCount() {
        int assertCount = asserts != null ? asserts.size() : 0;
        int reportCount = reports != null ? reports.size() : 0;

        return assertCount + reportCount;
    }


    public static class Test {
        private String message;
        private String location;
        private String label;
        private String id;

        public Test(String message) {
            this(message, null, null, null);
        }

        public Test(String message, String location, String label, String id){
            this.message = message;
            this.location = location;
            this.label = label;
            this.id = id;
        }


        public String getMessage() {
            return message;
        }

        public String getLocation() {
            return location;
        }

        public String getLabel() {
            return label;
        }

        public String getId() {
            return id;
        }
    }

    public Test addReport(){
        if(this.reports == null){
            this.reports = new ArrayList<>();
        }
        Test report = new Test(null);
        this.reports.add(report);
        return report;
    }

    public Test addAssert(){
        if(this.asserts == null){
            this.asserts = new ArrayList<>();
        }
        Test azzert = new Test(null);
        this.asserts.add(azzert);
        return azzert;
    }

    public void addEsTest(Element testEl) throws XPathExpressionException {
        Test test = "assert".equals(testEl.getLocalName()) ? addAssert() : addReport();

        test.message = XPR.getAttributValue(testEl,"message", "", null);
        test.id = XPR.getAttributValue(testEl,"base-id", "", null);
        test.label = XPR.getAttributValue(testEl,"roleLabel", "", null);
        test.location = XPR.getAttributValue(testEl,"location", "", null);


        if(test.message == null && XPR.getBoolean("es:text", testEl)){
            test.message = XPR.getString("es:text", testEl);
        }

    }

    public void addSVRLTest(Element testEl) throws XPathExpressionException {
        Test test = "failed-assert".equals(testEl.getLocalName()) ? addAssert() : addReport();

        test.id = XPR.getAttributValue(testEl,"base-id", ES_NS, null);
        test.label = XPR.getAttributValue(testEl,"roleLabel", ES_NS, null);
        test.location = XPR.getAttributValue(testEl,"location", "", null);


        if(XPR.getBoolean("svrl:text", testEl)){
            test.message = XPR.getString("svrl:text", testEl);
        }

    }

    private static XPathReader XPR = new XPathReader();


    public static ExpectedReportData createSVRLExpectReportData(Document doc){
        ExpectedReportData expData = new ExpectedReportData();
        try {
            Node root = XPR.getNode("/svrl:schematron-output", doc);

            expData.title = XPR.getAttributValue(root, "title", "", null);
            expData.queryBinding = XPR.getAttributValue(root, "queryBinding", ES_NS, null);
            expData.phase = XPR.getAttributValue(root, "phase", "", null);


            NodeList tests = XPR.getNodeSet("//(svrl:failed-assert|svrl:successful-report)", doc);
            for (int i = 0; i < tests.getLength(); i++) {
                expData.addSVRLTest((Element) tests.item(i));
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return expData;
    }
    public static ExpectedReportData createEscaliExpectReportData(Document doc){
        ExpectedReportData expData = new ExpectedReportData();
        try {
            Node meta = XPR.getNode("/es:escali-reports/es:meta", doc);

            expData.title = XPR.getAttributValue(meta, "title", "", null);
            expData.queryBinding = XPR.getAttributValue(meta, "queryBinding", "", null);
            expData.phase = XPR.getAttributValue(meta, "phase", "", null);


            NodeList tests = XPR.getNodeSet("//(es:assert|es:report)", doc);
            for (int i = 0; i < tests.getLength(); i++) {
                expData.addEsTest((Element) tests.item(i));
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return expData;
    }

    public static ExpectedReportData createExportReportData(TextSource report){
        try {
            StringNode sn = new StringNode(report, new MuteProcessLoger());
            return createEscaliExpectReportData(sn.getDocument());

//            if(XPR.getBoolean("/es:escali-reports", sn.getDocument())){
//            } else {
//                TextSource dummyTS = TextSource.createVirtualTextSource(new File("dummy.xml"));
//                dummyTS.setData("<dummy/>");
//                SVRLReport svrlReport = new SVRLReport(report, new ArrayList<TextSource>(), dummyTS, dummyTS, new EscaliArchiveResources());
//
//                TextSource esReport = svrlReport.getFormatetReport(ESCALI_FORMAT);
//
//                return createEscaliExpectReportData(new StringNode(esReport).getDocument());
//            }

        } catch (IOException | SAXException | XMLStreamException e) {
            e.printStackTrace();
            return new ExpectedReportData();
        }

    }

}
