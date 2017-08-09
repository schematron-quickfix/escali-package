package com.schematronQuickfix.xsm.operations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.github.oxygenPlugins.common.xml.staxParser.ElementInfo;
import com.github.oxygenPlugins.common.xml.staxParser.NodeInfo;
import com.github.oxygenPlugins.common.xml.staxParser.PositionalXMLReader;
import com.github.oxygenPlugins.common.xml.staxParser.StringNode;
import com.github.oxygenPlugins.common.xml.xpath.NodeUtils;
import com.github.oxygenPlugins.common.xml.xpath.XPathReader;

public class OperationsSet extends ArrayList<_Operation> {
	private static final long serialVersionUID = 2596362096996568212L;

	private ArrayList<_Operation> getWrapper(_Operation op) {
		ArrayList<_Operation> wrappers = new ArrayList<_Operation>();
		for (Iterator<_Operation> iterator = this.iterator(); iterator
				.hasNext();) {
			_Operation wrapper = iterator.next();
			if (wrapper.getCorrectionId().equals(op.getCorrectionId())) {
				wrappers.add(wrapper);
			} else if (wrapper.getStart() < op.getStart()
					&& wrapper.getEnd() > op.getEnd()) {
				wrappers.add(wrapper);
			} else if (wrapper.getStart() == wrapper.getEnd()
					&& op.getStart() == op.getEnd()) {
				// both are add operations on the same node.
				continue;
			} else if (wrapper.getStart() == op.getStart()
					&& wrapper.getEnd() == op.getEnd()) {
				if (wrapper.getPrio() > op.getPrio()) {
					wrappers.add(wrapper);
				}
			}

		}
		return wrappers;
	}

	private ArrayList<_Operation> getIncludes(_Operation op) {
		ArrayList<_Operation> includes = new ArrayList<_Operation>();
		for (Iterator<_Operation> iterator = this.iterator(); iterator
				.hasNext();) {
			_Operation include = iterator.next();
			if (include.getStart() > op.getStart()
					&& include.getEnd() < op.getEnd()) {
				includes.add(include);
			} else if (include.getStart() == include.getEnd()
					&& op.getStart() == op.getEnd()) {
				// both are add operations on the same node.
				continue;
			} else if (include.getStart() == op.getStart()
					&& include.getEnd() == op.getEnd()) {
				if (include.getCorrectionId().equals(op.getCorrectionId())) {
					continue;
				} else if (op.getPrio() > include.getPrio()) {
					includes.add(include);
				}
			}

		}
		return includes;
	}

	@Override
	public boolean add(_Operation op) {
		ArrayList<_Operation> wrapper = getWrapper(op);
		ArrayList<_Operation> includes = getIncludes(op);
		for (Iterator<_Operation> iterator = includes.iterator(); iterator
				.hasNext();) {
			_Operation includeOp = iterator.next();
			this.remove(includeOp);
		}
		if (wrapper.size() > 0) {
			return false;
		} else {
			this.addAll(op.getCorrectionOperations());
			boolean ret = super.add(op);
			sort(this);
			return ret;
		}
	}

	public boolean addAll(OperationsSet c) {
		for (Iterator<_Operation> iterator = c.iterator(); iterator.hasNext();) {
			_Operation op = iterator.next();
			this.add(op);
		}
		return true;
	};

	public static OperationsSet createOperations(StringNode sourceDoc,
			StringNode sheetStringNode, NodeList sheetNodes)
			throws XPathExpressionException, IOException, SAXException {
		OperationsSet ops = new OperationsSet();
		for (int i = 0; i < sheetNodes.getLength(); i++) {
			ops.addAll(createOperation(sourceDoc, sheetStringNode,
					sheetNodes.item(i), i * 10));
		}
		sort(ops);
		return ops;
	}

	private static void sort(OperationsSet operations) {
		Collections.sort(operations, new Comparator<_Operation>() {
			@Override
			public int compare(_Operation o1, _Operation o2) {
				if (o1.getStart() == o2.getStart()) {
					return o1.getPrio() < o2.getPrio() ? -1 : 1;
				}
				return o1.getStart() < o2.getStart() ? -1 : 1;
			}
		});
	}

	private static XPathReader xpr = new XPathReader();

	public static OperationsSet createOperation(StringNode sourceDoc,
			StringNode sheetDocString, Node sheetNode, int prio)
			throws XPathExpressionException {

		String xpath = sheetNode.getAttributes().getNamedItem("node")
				.getNodeValue();
		// @SuppressWarnings("unchecked")
		final NamespaceContext nsc = (NamespaceContext) sheetNode
				.getUserData(PositionalXMLReader.NAMESPACE_CONTEXT);
		NodeList sourceNodes;
		try {
			sourceNodes = xpr.getNodeSet(xpath, sourceDoc.getDocument(), nsc);
		} catch (XPathExpressionException e) {
			int ln = PositionalXMLReader.getLine(sheetNode,
					PositionalXMLReader.NODE_LOCATION_START);
			throw new XPathExpressionException(
					"XPath expression error in line " + ln + ": "
							+ e.getMessage());
		}
		OperationsSet operations = new OperationsSet();

		if (xpr.getBoolean("self::xsm:delete", sheetNode)) {
			operations.addAll(createDeleteOperations(sourceNodes, prio, sheetNode));
		} else if (xpr.getBoolean("self::xsm:replace", sheetNode)) {
			operations.addAll(createReplaceOperations(sourceDoc,
					sheetDocString, sourceNodes, sheetNode, prio));
		} else if (xpr.getBoolean("self::xsm:add", sheetNode)) {
			operations.addAll(createAddeOperations(sourceDoc, sheetDocString,
					sourceNodes, sheetNode, prio));
			// String axis = sheetNode.getAttributes().getNamedItem("axis")
			// .getNodeValue();
			// String replaceString = "";
			// if (axis.equals("attribute") || axis.equals("@")) {
			// Node xsmContent = xpr.getNode("xsm:content", sheetNode);
			//
			// int start = (Integer) xsmContent
			// .getUserData(PositionalXMLReader.ATTRIBUTE_REGION_POSITION);
			// int end = (Integer) xsmContent
			// .getUserData(PositionalXMLReader.ATTRIBUTE_REGION_POSITION_LAST);
			// replaceString = sheetDocString.substring(start, end);
			//
			// for (int i = 0; i < sourceNodes.getLength(); i++) {
			// NodeList addAttrList = xpr.getNodeSet("./@*", xsmContent);
			// for (int j = 0; j < addAttrList.getLength(); j++) {
			// String attrXpath = "@*[namespace-uri()='"
			// + xpr.getString("namespace-uri()",
			// addAttrList.item(j))
			// + "'][local-name()='"
			// + xpr.getString("local-name()",
			// addAttrList.item(j)) + "']";
			// Node attrToReplace = xpr.getNode(attrXpath,
			// sourceNodes.item(i));
			// if (attrToReplace != null) {
			// operations.add(new DeleteOp(attrToReplace));
			// }
			// }
			// operations.add(new AddOp(sourceNodes.item(i),
			// replaceString, axis));
			// }
			// } else {
			// Node contentNode = xpr.getNode("xsm:content", sheetNode);
			// int start = (Integer) contentNode
			// .getUserData(PositionalXMLReader.INNER_POSITION_KEY_NAME);
			// int end = (Integer) contentNode
			// .getUserData(PositionalXMLReader.INNER_POSITION_LAST_KEY_NAME);
			// replaceString = sheetDocString.substring(start, end);
			//
			// for (int i = 0; i < sourceNodes.getLength(); i++) {
			// operations.add(new AddOp(sourceNodes.item(i),
			// replaceString, axis));
			// }
			// }

		} else {

		}
		sort(operations);
		return operations;
	}

	private static OperationsSet createDeleteOperations(NodeList sourceNodes,
			int prio, Node xsmDelete) throws XPathExpressionException {
		OperationsSet operations = new OperationsSet();
		boolean substring = xpr.getBoolean("@start-position and @end-position",
				xsmDelete);
		
		int startOffset = substring ? Integer.parseInt(xpr.getString(
				"@start-position", xsmDelete)) : -1;
		int endOffset = substring ? Integer.parseInt(xpr.getString(
				"@end-position", xsmDelete)) : -1;
		
		for (int i = 0; i < sourceNodes.getLength(); i++) {
			Node srcNode = sourceNodes.item(i);
			ReplaceOp dop;
			
			if (substring && srcNode.getNodeType() == Node.TEXT_NODE) {
				dop = new ReplaceOp((Text) srcNode, "", prio,
						startOffset, endOffset);
			} else if (substring
					&& srcNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				dop = new ReplaceOp((Attr) srcNode, "", prio,
						startOffset, endOffset);
			} else if (substring
					&& srcNode.getNodeType() == Node.COMMENT_NODE) {
				dop = new ReplaceOp((Comment) srcNode, "", prio,
						startOffset, endOffset);
			} else if (substring
					&& srcNode.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
				dop = new ReplaceOp((ProcessingInstruction) srcNode, "", prio,
						startOffset, endOffset);
			} else {
				dop = new DeleteOp(srcNode, prio);
			}
			operations.add(dop);
		}
		return operations;
	}

	private static OperationsSet createReplaceOperations(StringNode sourceDoc,
			StringNode sheetDocString, NodeList sourceNodes, Node xsmReplace,
			int prio) throws XPathExpressionException {
		OperationsSet operations = new OperationsSet();

		Node contentNode = xpr.getNode("xsm:content", xsmReplace);
		boolean substring = xpr.getBoolean("@start-position and @end-position",
				xsmReplace);
		NodeList xsmCopies = xpr.getNodeSet(".//xsm:copy", xsmReplace);

		CopyOpSet xsmCopy = CopyOpSet.createCopyOpSet(xsmCopies, sourceDoc);

		int startOffset = substring ? Integer.parseInt(xpr.getString(
				"@start-position", xsmReplace)) : -1;
		int endOffset = substring ? Integer.parseInt(xpr.getString(
				"@end-position", xsmReplace)) : -1;

		// in case for non attributes
		ElementInfo contentInfo = (ElementInfo) PositionalXMLReader
				.getNodeInfo(contentNode);
		String contString = sheetDocString
				.getTextSource()
				.toString()
				.substring(contentInfo.getInnerStartOffset(),
						contentInfo.getInnerEndOffset());

		// replaceContString = xsmCopy(xsmCopies, replaceContString,
		// contentInfo.getInnerStartOffset(), sourceDoc);
		String replaceContString = xsmCopy.applyContentCopy(contString,
				contentInfo.getInnerStartOffset());

		// in case for attributes
		String replaceAttrString = sheetDocString
				.getTextSource()
				.toString()
				.substring(contentInfo.getAttributRegionStartOffset(),
						contentInfo.getAttributRegionEndOffset());

		replaceAttrString += xsmCopy.applyTopLevelAttributeCopy(contString,
				contentInfo.getInnerStartOffset());

		// xsmCopy.applyContentCopy(replace, offset)

		for (int i = 0; i < sourceNodes.getLength(); i++) {
			Node srcNode = sourceNodes.item(i);
			if (srcNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				ArrayList<Node> tlas = xsmCopy.getTLAttributes();
				tlas.addAll(NodeUtils.toArrayList(xpr.getNodeSet("@*",
						contentNode)));
				operations.addAll(createAttribteDeletion(tlas,
						xpr.getNode("..", srcNode), prio - 1));
			}

			String replaceString = (srcNode.getNodeType() == Node.ATTRIBUTE_NODE && !substring) ? replaceAttrString
					: replaceContString;
			ReplaceOp rop;
			if (substring && srcNode.getNodeType() == Node.TEXT_NODE) {
				rop = new ReplaceOp((Text) srcNode, replaceString, prio,
						startOffset, endOffset);
			} else if (substring
					&& srcNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				rop = new ReplaceOp((Attr) srcNode, replaceString, prio,
						startOffset, endOffset);
			} else if (substring
					&& srcNode.getNodeType() == Node.COMMENT_NODE) {
				rop = new ReplaceOp((Comment) srcNode, replaceString, prio,
						startOffset, endOffset);
			} else if (substring
					&& srcNode.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
				rop = new ReplaceOp((ProcessingInstruction) srcNode, replaceString, prio,
						startOffset, endOffset);
			} else {
				rop = new ReplaceOp(sourceNodes.item(i), replaceString, prio);
			}
			operations.add(rop);
		}
		return operations;
	}

	private static OperationsSet createAddeOperations(StringNode sourceDoc,
			StringNode sheetDocString, NodeList sourceNodes, Node xsmAdd,
			int prio) throws XPathExpressionException {
		OperationsSet operations = new OperationsSet();
		String position = xsmAdd.getAttributes().getNamedItem("position")
				.getNodeValue();
		String addString = "";
		Node xsmContent = xpr.getNode("xsm:content", xsmAdd);
		NodeList xsmCopies = xpr.getNodeSet(".//xsm:copy", xsmContent);

		boolean substring = xpr.getBoolean("@start-position and @end-position",
				xsmAdd);

		int startOffset = substring ? Integer.parseInt(xpr.getString(
				"@start-position", xsmAdd)) : -1;
		int endOffset = substring ? Integer.parseInt(xpr.getString(
				"@end-position", xsmAdd)) : -1;

		CopyOpSet xsmCopy = CopyOpSet.createCopyOpSet(xsmCopies, sourceDoc);

		ElementInfo info = (ElementInfo) PositionalXMLReader
				.getNodeInfo(xsmContent);

		ArrayList<Node> tlas = xsmCopy.getTLAttributes();
		tlas.addAll(NodeUtils.toArrayList(xpr.getNodeSet("./@*", xsmContent)));

		String replace = sheetDocString.toString().substring(
				info.getInnerStartOffset(), info.getInnerEndOffset());

		if (tlas.size() > 0) {
			addString = sheetDocString.toString().substring(
					info.getAttributRegionStartOffset(),
					info.getAttributRegionEndOffset());

			addString += xsmCopy.applyTopLevelAttributeCopy(replace,
					info.getAttributRegionStartOffset());

			for (int i = 0; i < sourceNodes.getLength(); i++) {

				operations.addAll(createAttribteDeletion(tlas,
						sourceNodes.item(i), prio));

				operations.add(new AddOp(sourceNodes.item(i), addString, "@",
						prio));
			}
		}
		if (xpr.getBoolean("node()", xsmContent)) {

			addString = replace;

			addString = xsmCopy.applyContentCopy(addString,
					info.getInnerStartOffset());

			// addString = xsmCopy(xsmCopies, addString,
			// info.getInnerStartOffset(), sourceDoc, position.equals("@") ||
			// position.equals("attribute"));

			for (int i = 0; i < sourceNodes.getLength(); i++) {
				Node srcNode = sourceNodes.item(i);

				boolean isTextNode = srcNode.getNodeType() == Node.TEXT_NODE;

				AddOp aop;
				if (substring && isTextNode) {
					aop = new AddOp((Text) srcNode, addString, position, prio,
							startOffset, endOffset);
				} else if (substring
						&& srcNode.getNodeType() == Node.ATTRIBUTE_NODE) {
					aop = new AddOp((Attr) srcNode, addString, position, prio,
							startOffset, endOffset);
				} else if (substring
						&& srcNode.getNodeType() == Node.COMMENT_NODE) {
					aop = new AddOp((Comment) srcNode, addString, position, prio,
							startOffset, endOffset);
				} else if (substring
						&& srcNode.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
					aop = new AddOp((ProcessingInstruction) srcNode, addString, position, prio,
							startOffset, endOffset);
				} else {
					aop = new AddOp(srcNode, addString, position, prio);
				}

				operations.add(aop);
			}
		}
		// if (position.equals("attribute") || position.equals("@")) {
		//
		// addString =
		// sheetDocString.toString().substring(info.getAttributRegionStartOffset(),
		// info.getAttributRegionEndOffset());
		//
		// for (int i = 0; i < sourceNodes.getLength(); i++) {
		// NodeList addAttrList = xpr.getNodeSet("./@*", xsmContent);
		// for (int j = 0; j < addAttrList.getLength(); j++) {
		// String attrXpath = "@*[namespace-uri()='"
		// + xpr.getString("namespace-uri()",
		// addAttrList.item(j))
		// + "'][local-name()='"
		// + xpr.getString("local-name()",
		// addAttrList.item(j)) + "']";
		// Node attrToReplace = xpr.getNode(attrXpath,
		// sourceNodes.item(i));
		// if (attrToReplace != null) {
		// PositionalXMLReader.getNodeInfo(attrToReplace);
		// operations.add(new DeleteOp(attrToReplace, prio));
		// }
		// }
		// operations.add(new AddOp(sourceNodes.item(i),
		// addString, position, prio));
		// }
		// } else {
		//
		// addString =
		// sheetDocString.toString().substring(info.getInnerStartOffset(),
		// info.getInnerEndOffset());
		//
		// for (int i = 0; i < sourceNodes.getLength(); i++) {
		// operations.add(new AddOp(sourceNodes.item(i),
		// addString, position, prio));
		// }
		// }
		return operations;
	}

	private static OperationsSet createAttribteDeletion(NodeList[] addAttrList,
			Node sourceNode, int prio) throws XPathExpressionException {
		ArrayList<Node> attributes = new ArrayList<Node>();

		for (NodeList attrList : addAttrList) {
			for (int i = 0; i < attrList.getLength(); i++) {
				attributes.add(attrList.item(i));
			}
		}

		return createAttribteDeletion(attributes, sourceNode, prio);
	}

	private static OperationsSet createAttribteDeletion(NodeList addAttrList,
			Node sourceNode, int prio) throws XPathExpressionException {
		ArrayList<Node> attrList = new ArrayList<Node>();

		for (int i = 0; i < addAttrList.getLength(); i++) {
			attrList.add(addAttrList.item(i));
		}

		return createAttribteDeletion(attrList, sourceNode, prio);
	}

	private static OperationsSet createAttribteDeletion(
			ArrayList<Node> addAttrList, Node sourceNode, int prio)
			throws XPathExpressionException {
		OperationsSet operations = new OperationsSet();
		for (Node attr : addAttrList) {
			String attrXpath = "@*[namespace-uri()='"
					+ xpr.getString("namespace-uri()", attr)
					+ "'][local-name()='" + xpr.getString("local-name()", attr)
					+ "']";
			Node attrToReplace = xpr.getNode(attrXpath, sourceNode);
			if (attrToReplace != null) {
				PositionalXMLReader.getNodeInfo(attrToReplace);
				operations.add(new DeleteOp(attrToReplace, prio));
			}
		}
		return operations;
	}

	private static String xsmCopy(NodeList xsmCopies, String replaceString,
			int offset, StringNode source) throws XPathExpressionException {
		return xsmCopy(xsmCopies, replaceString, offset, source, false);
	}

	private static String xsmCopy(NodeList xsmCopies, String replaceString,
			int offset, StringNode source, boolean ignoreAttributes)
			throws XPathExpressionException {
		for (int i = 0; i < xsmCopies.getLength(); i++) {
			String afterReplace = xsmCopy(xsmCopies.item(i), replaceString,
					offset, source, ignoreAttributes);
			offset -= afterReplace.length() - replaceString.length();
			replaceString = afterReplace;
		}
		return replaceString;
	}

	private static String xsmCopy(Node xsmCopy, String replaceString,
			int offset, StringNode source, boolean ignoreAttributes)
			throws XPathExpressionException {
		Node selectAttr = xpr.getNode("@select", xsmCopy);
		NodeInfo selectNI = PositionalXMLReader.getNodeInfo(selectAttr);
		String xpath = selectAttr.getNodeValue();

		NodeInfo copyNI = source.getNodeInfo(xpath);
		String copyCode = source.getCode(copyNI);

		short nodeType = copyNI.getNode().getNodeType();

		ElementInfo xsmNodeInfo = PositionalXMLReader
				.getNodeInfo((Element) xsmCopy);

		int start = xsmNodeInfo.getStartOffset() - offset;
		int end = xsmNodeInfo.getEndOffset() - offset;

		if (nodeType == Node.ELEMENT_NODE) {
			ElementInfo copyEI = (ElementInfo) copyNI;

			int startAttrReg = xsmNodeInfo.getAttributRegionStartOffset()
					- offset;
			int endAttrReg = xsmNodeInfo.getAttributRegionEndOffset() - offset;
			int startSelect = selectNI.getStartOffset() - offset;
			int endSelect = selectNI.getEndOffset() - offset;

			int endAttrRegCopyCode = copyEI.getAttributRegionEndOffset()
					- copyEI.getStartOffset();

			String attrReg = replaceString.substring(startAttrReg, startSelect)
					+ replaceString.substring(endSelect, endAttrReg);

			copyCode = copyCode.substring(0, endAttrRegCopyCode) + attrReg
					+ copyCode.substring(endAttrRegCopyCode);
		} else if (nodeType == Node.ATTRIBUTE_NODE && !ignoreAttributes) {
			Element parent = (Element) xpr.getNode("..", xsmCopy);
			ElementInfo parentEI = PositionalXMLReader.getNodeInfo(parent);

			replaceString = replaceString.substring(0, start)
					+ replaceString.substring(end);

			start = parentEI.getAttributRegionEndOffset() - offset;
			end = start;

		}

		if (start >= 0) {
			replaceString = replaceString.substring(0, start) + copyCode
					+ replaceString.substring(end);
		}

		return replaceString;
	}

}
