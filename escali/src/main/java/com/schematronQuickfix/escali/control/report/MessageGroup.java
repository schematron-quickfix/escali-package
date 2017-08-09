package com.schematronQuickfix.escali.control.report;

import java.awt.List;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class MessageGroup extends ModelNode implements _MessageGroup {

	MessageGroup(Node node, int svrlIdx) throws DOMException, URISyntaxException {
		super(node, svrlIdx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.messages.nodes._MessageGroup#getLevelCounts()
	 */
	@Override
	public int[] getLevelCounts() {
		int[] levelCounts = new int[_SVRLMessage.LEVEL_COUNT];
		ArrayList<_SVRLMessage> children = this.getMessages();
		for (Iterator<_SVRLMessage> iterator = children.iterator(); iterator.hasNext();) {
			_SVRLMessage modelNode = iterator.next();
			if (modelNode instanceof SVRLMessage) {
				SVRLMessage msg = (SVRLMessage) modelNode;
				int level = (int) msg.getErrorLevel();
				levelCounts[level]++;
			}
		}
		return levelCounts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.messages.nodes._MessageGroup#getMessages()
	 */
	@Override
	public ArrayList<_SVRLMessage> getMessages(int minErrorLevel, int maxErrorLevel) {
		ArrayList<_SVRLMessage> messages = new ArrayList<_SVRLMessage>();
		ArrayList<_ModelNode> children = this.getChildren();
		for (Iterator<_ModelNode> iterator = children.iterator(); iterator.hasNext();) {
			_ModelNode modelNode = iterator.next();
			if (modelNode instanceof MessageGroup) {
				_MessageGroup group = (_MessageGroup) modelNode;
				messages.addAll(group.getMessages(minErrorLevel, maxErrorLevel));

			} else if (modelNode instanceof SVRLMessage) {
				SVRLMessage msg = (SVRLMessage) modelNode;
				if (msg.getErrorLevel() >= minErrorLevel && msg.getErrorLevel() <= maxErrorLevel)
					messages.add(msg);
			}
		}
		return messages;
	}

	@Override
	public ArrayList<_SVRLMessage> getMessages() {
		return getMessages(0, SVRLMessage.LEVEL_COUNT);
	}

	@Override
	public ArrayList<_SVRLMessage> getSortedMessages(final int mode) {

		ArrayList<_SVRLMessage> unSortedMessages = getMessages();
		Collections.sort(unSortedMessages, new Comparator<_SVRLMessage>() {

			@Override
			public int compare(_SVRLMessage m1, _SVRLMessage m2) {
				
				switch (mode) {
				case ERROR_UP_SORTING:
					return m1.getErrorLevelInt() > m2.getErrorLevelInt() ? 1 : -1;
				case ERROR_DOWN_SORTING:
					return m1.getErrorLevelInt() < m2.getErrorLevelInt() ? 1 : -1;
				case SVRL_SORTING:
					// get index of pattern
					int p1 = m1.getParent().getParent().getSvrlIndex();
					int p2 = m2.getParent().getParent().getSvrlIndex();
					if(p1 != p2){
						// if pattern unequal, compare them
						return p1 > p2 ? 1 : -1;
					}
					// else sourt by location (SOURCE_SORTING)

				case SOURCE_SORTING:
					int l1 = m1.getLocationInIstance().getStartOffset();
					int l2 = m2.getLocationInIstance().getStartOffset();
					if(l1 != l2){
						return l1 > l2 ? 1 : -1;
					}
				default:
					return m1.getSvrlIndex() > m2.getSvrlIndex() ? 1 : -1;
				}
			}
		});
		return unSortedMessages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.messages.nodes._MessageGroup#getMessageCount()
	 */
	@Override
	public int getMessageCount() {
		return this.getMessages().size();
	}

	@Override
	public double getMaxErrorLevel() {
		ArrayList<_SVRLMessage> messages = getMessages();
		double maxLevel = -1.0;
		for (Iterator<_SVRLMessage> iterator = messages.iterator(); iterator.hasNext();) {
			double msgLevel = iterator.next().getErrorLevel();
			maxLevel = msgLevel > maxLevel ? msgLevel : maxLevel;
		}
		return maxLevel;
	}

	@Override
	public int getMaxErrorLevelInt() {
		ArrayList<_SVRLMessage> messages = getMessages();
		int maxLevel = -1;
		for (Iterator<_SVRLMessage> iterator = messages.iterator(); iterator.hasNext();) {
			int msgLevel = iterator.next().getErrorLevelInt();
			maxLevel = msgLevel > maxLevel ? msgLevel : maxLevel;
		}
		return maxLevel;
	}

	@Override
	public double getErrorLevel() {
		ArrayList<_SVRLMessage> messages = getMessages();
		double sumWeight = 0.0;
		for (Iterator<_SVRLMessage> iterator = messages.iterator(); iterator.hasNext();) {
			_SVRLMessage msg = iterator.next();
			sumWeight += msg.getErrorWeight();
		}
		return sumWeight / messages.size();
	}

	@Override
	public String[] getPhases() {
		_ModelNode parent = this.getParent();
		if (parent instanceof MessageGroup) {
			return ((MessageGroup) parent).getPhases();
		} else {
			return null;
		}
	}

	@Override
	public boolean isPhase(String phase) {
		String[] phases = getPhases();
		if (phases != null) {
			for (String ph : getPhases()) {
				if (ph.equals(phase)) {
					return true;
				}
			}
		}
		return false;
	}
}
