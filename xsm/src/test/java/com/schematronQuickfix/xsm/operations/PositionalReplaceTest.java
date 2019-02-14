package com.schematronQuickfix.xsm.operations;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.process.exceptions.CancelException;
import com.github.oxygenPlugins.common.text.TextSource;
import com.schematronQuickfix.xsm.*;


public class PositionalReplaceTest {
	
	private static class PositionalReplaceTestCase extends PositionalReplace {
		
		public PositionalReplaceTestCase(File source, String deleteNode) throws XPathExpressionException, IOException, SAXException, XMLStreamException, CancelException {
			super(source, false);
			this.deleteNode(deleteNode);
		}
		public PositionalReplaceTestCase(File document, boolean isSheet) throws XPathExpressionException, IOException, SAXException, XMLStreamException, CancelException{
			super(document, isSheet);
		}
		public PositionalReplaceTestCase(File document, File sheet) throws XPathExpressionException, IOException, SAXException, XMLStreamException, CancelException{
			super(document, sheet);
		}
		
		@Override
		public String toString() {
			return this.getSource().toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			return this.toString().equals(obj.toString());
		}
		
		public static PositionalReplace createTestCase(File source, String deleteNode){
			try {
				return new PositionalReplaceTestCase(source, deleteNode);
			} catch (XPathExpressionException | IOException | SAXException | XMLStreamException | CancelException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static PositionalReplace createTestCase(File source, boolean isSheet){
			try {
				return new PositionalReplaceTestCase(source, isSheet);
			} catch (XPathExpressionException | IOException | SAXException | XMLStreamException | CancelException e) {
				e.printStackTrace();
				return null;
			}
		}
		public static PositionalReplace createTestCase(File source, File sheet){
			try {
				return new PositionalReplaceTestCase(source, sheet);
			} catch (XPathExpressionException | IOException | SAXException | XMLStreamException | CancelException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	@Test
	public void test01WithExternDTD() {
		File source = new File(STRING_MAIN_FOLDER + "test1/base.xml");
		
		
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/comment()[1]"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test1/expected-1.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/comment()[2]"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test1/expected-2.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test1/expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(source, "/root/a[@id='sqf-examples-download']"));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/b[1]/text()"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test1/expected-4.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/d[1]/@def"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test1/expected-5.xml"), false));

		
	}
	@Test
	public void test02WithInternalDTD(){
		File source = new File(STRING_MAIN_FOLDER + "test2/base.xml");
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/comment()[1]").toString(), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test2/expected-1.xml"), false).toString());
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/comment()[2]").toString(), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test2/expected-2.xml"), false).toString());
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/b[1]/text()"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test2/expected-3.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/text()[1]"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test2/expected-4.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/text()[2]"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test2/expected-5.xml"), false));
	}
	
	@Test
	public void test03WithInternalAndExternalDTD(){
		File source = new File(STRING_MAIN_FOLDER + "test3/base.xml");
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/comment()[1]"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test3/expected-1.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/comment()[2]"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test3/expected-2.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/a[1]/text()"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test3/expected-3.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test3/expected-4.xml"), false), PositionalReplaceTestCase.createTestCase(source, "/root/d/@def"));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/text()[1]"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test3/expected-5.xml"), false));
	}

	@Test
	public void test04DeleteMoreNodes(){
		File source = new File(STRING_MAIN_FOLDER + "test4/base.xml");
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/comment()"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test4/expected-1.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/node()"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test4/expected-2.xml"), false));
		assertEquals(PositionalReplaceTestCase.createTestCase(source, "/root/*"), PositionalReplaceTestCase.createTestCase(new File(STRING_MAIN_FOLDER + "test4/expected-3.xml"), false));
	}
	
	@Test
	public void test05ReplacerSheet(){
		String folder = STRING_MAIN_FOLDER + "test5/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-4.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-4.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-5.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-5.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-6.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-6.xsm"), true));
	}
	@Test
	public void test06ReplacerSheetHtmlLang(){
		String folder = STRING_MAIN_FOLDER + "test6/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true));
	}
	@Test
	public void test07ReplacerSheetHtmlHierarchy(){
		String folder = STRING_MAIN_FOLDER + "test7/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-4.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-4.xsm"), true));
	}
	@Test
	public void test08ReplacerSheetHtmlTitle(){
		String folder = STRING_MAIN_FOLDER + "test8/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-4.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-4.xsm"), true));
	}
	@Test
	public void test09ReplacerSheetHtmlTable1(){
		String folder = STRING_MAIN_FOLDER + "test9/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.sqf"), true));
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.sqf"), true));
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-4.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-4.sqf"), true));
	}
	@Test
	public void test10Encoding(){
		String folder = STRING_MAIN_FOLDER + "test10/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-ansi.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer.xsm"), new File(folder + "base-ansi.xml")));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-utf8-with.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer.xsm"), new File(folder + "base-utf8-with.xml")));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-utf8-without.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer.xsm"), new File(folder + "base-utf8-without.xml")));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-ucs2-big.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer.xsm"), new File(folder + "base-ucs2-big.xml")));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-ucs2-litle.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer.xsm"), new File(folder + "base-ucs2-litle.xml")));
	}
	@Test
	public void test11SpecialCases(){
		String folder = STRING_MAIN_FOLDER + "test11/";
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true));
	}
	@Test
	public void test12Namespaces(){
		String folder = STRING_MAIN_FOLDER + "test12/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true));
	}
//	@Test
//	public void test13XpathBug(){
//		String folder = "resources/test13/";
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "downloads_de.html"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "adapter-download.xsm"), true));
////		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
////		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true));
//	}
	@Test
	public void test14Conflicts(){
		String folder = STRING_MAIN_FOLDER + "test14/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true));
	}
	@Test
	public void test15LargeFiles(){
		String folder = STRING_MAIN_FOLDER + "test15/";
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
	}
		
	@Test
	public void test16QuoteForPatterns(){
		String folder = STRING_MAIN_FOLDER + "test16/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
	}	

	@Test
	public void test17AddAfterPI(){
		String folder = STRING_MAIN_FOLDER + "test17/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true));
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
	}
	
	@Test
	public void test18ConflictAdds(){
		String folder = STRING_MAIN_FOLDER + "test18/";
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true).toString());
		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true));
	}

	@Test
	public void test19SubstringReplace(){
		String folder = STRING_MAIN_FOLDER + "test19/";
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-4.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-4.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-5.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-5.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-6.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-6.xsm"), true).toString());
		testFolder(folder);
	}
	

	@Test
	public void test20CopyReplace(){
		String folder = STRING_MAIN_FOLDER + "test20/";
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-1.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-1.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-2.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-2.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-3.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-3.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-4.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-4.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-5.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-5.xsm"), true).toString());
//		assertEquals(PositionalReplaceTestCase.createTestCase(new File(folder + "expected-6.xml"), false).toString(), PositionalReplaceTestCase.createTestCase(new File(folder + "replacer-6.xsm"), true).toString());
		testFolder(folder);
	}
	
	private static String STRING_MAIN_FOLDER = "src/test/resources/";

	@Test
	public void test21CopyAdd(){
//		String folder = "resources/test21/";
		testFolder(STRING_MAIN_FOLDER + "test21/");
	}


	@Test
	public void test23XmlResolver(){
//		String folder = "resources/test21/";
		final String dtd = STRING_MAIN_FOLDER + "test23/test.dtd";

		TextSource.implementEntityResolver(new XMLResolver() {
			@Override
			public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace) throws XMLStreamException {

				if(systemID.equals("http://www.schematron-quickfix.com/test.dtd")){

					try {
						return new File(dtd).toURI().toURL().openStream();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}
				return null;
			}
		});
		testFolder(STRING_MAIN_FOLDER + "test23/");

		TextSource.implementEntityResolver(null);
	}
	
	private static FilenameFilter fnf = new FilenameFilter() {
		
		@Override
		public boolean accept(File file, String name) {
			return name.matches(".+\\.xsm$");
		}
	};
	
	private void testFolder(String folder){
		testFolder(new File(folder));
	}
	private void testFolder(File folder){
		
		for (String name : folder.list(fnf)) {
			File xsmFile = new File(folder, name);
			name = name.replaceAll("\\.xsm$", ".xml");
			name = new File(folder, name).exists() ? name : name.replaceAll("^replacer-", "expected-");
			File expectedFile = new File(folder, name);
			String info = "\n" + xsmFile.toString() + " / " + expectedFile.toString() + "\n\n";
			
			assertEquals(info + PositionalReplaceTestCase.createTestCase(expectedFile, false).toString(), info + PositionalReplaceTestCase.createTestCase(xsmFile, true).toString());
		}
	}
}
