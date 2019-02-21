package com.schematronQuickfix.escaliOxygen;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.github.oxygenPlugins.common.gui.swing.SwingUtil;




public class ViewTester extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6005442275173572213L;
	private ArrayList<AfterClose> closeListener = new ArrayList<ViewTester.AfterClose>();
	public ViewTester() {
		// TODO Auto-generated constructor stub
		try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ex){
            //ignore;
        }

		this.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				for (AfterClose ac : closeListener) {
					ac.doOnClose();
				}
			}
			public void windowClosed(WindowEvent e) {
				for (AfterClose ac : closeListener) {
					ac.doOnClose();
				}
			}
			public void windowActivated(WindowEvent e) {}
		});
	}
	
	public void test(JComponent comp){
		JPanel panel = new JPanel();
		panel.add(comp);
		test((Container)panel);
	}
	
	public void test(Container panel){
		test(panel, 0, 0);
	}
	
	public void test(Container panel, int minWidth, int minHeight){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel);
		
		this.setMinimumSize(new Dimension(minWidth, minHeight));
		
		this.pack();
		SwingUtil.centerFrame(this);
		this.setVisible(true);
	}
	
	public void addCloseListener(AfterClose closelistener){
		this.closeListener.add(closelistener);
	}
	
	
	
	public interface AfterClose{
		void doOnClose();
	}
}
