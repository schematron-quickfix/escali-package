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

        private ArrayList<QF> quickFixes = new ArrayList<>();
        private boolean isSubstring = false;
        private int[] substrings = new int[2];

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

        public ArrayList<QF> getQuickFixes() {
            return quickFixes;
        }

        public QF addQuickFix(){
            QF fix = new QF();
            quickFixes.add(fix);
            return fix;
        }

        public boolean isSubstring() {
            return this.isSubstring;
        }

        public int getSubstringStart(){
            if(isSubstring){
                return substrings[0];
            } else {
                return -1;
            }
        }

        public int getSubstringEnd(){
            if(isSubstring){
                return substrings[1];
            } else {
                return -1;
            }
        }
    }

    public static class QF {
        private String description = null;
        private String role = null;
        private String fixId = null;

        public String getDescription() {
            return description;
        }
        public String getFixId() {
            return fixId;
        }
        public String getRole() {
            return role;
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
        String substring = XPR.getAttributValue(testEl,"substring", "", null);
        if (substring != null){
            test.isSubstring = true;
            test.substrings[0] = Integer.parseInt(substring.split("\\s")[0]);
            test.substrings[1] = Integer.parseInt(substring.split("\\s")[1]);
        }


        if(test.message == null && XPR.getBoolean("es:text", testEl)){
            test.message = XPR.getString("es:text", testEl);
        }

        NodeList fixes = XPR.getNodeSet("sqf:fix", testEl);

        for (int i = 0; i < fixes.getLength(); i++) {
            Node fixEl = fixes.item(i);
            QF qf = test.addQuickFix();
            qf.description = XPR.getAttributValue(fixEl, "title", "", null);
            qf.role = XPR.getAttributValue(fixEl, "role", "", null);
            qf.fixId = XPR.getAttributValue(fixEl, "fixId", "", null);
        }

    }



    private static XPathReader XPR = new XPathReader();


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
