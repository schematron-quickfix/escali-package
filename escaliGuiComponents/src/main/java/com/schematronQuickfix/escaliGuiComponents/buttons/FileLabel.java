package com.schematronQuickfix.escaliGuiComponents.buttons;

import java.awt.Font;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class FileLabel extends JPanel implements FileChooserConnectComponent {
	private static final long serialVersionUID = -7595714479168655353L;
	private final JLabel label;
	
	public FileLabel(String defaultText){
		this.label = new JLabel(defaultText);
		setItalic(true);
		this.add(label);
	}
	
	@Override
	public void setText(File file) {
		setItalic(false);
		this.label.setText(file.getName());
	}
	
	private void setItalic(boolean isItalic){
		Font f = label.getFont();
		this.label.setFont(new Font(f.getFontName(), isItalic ? Font.ITALIC : Font.PLAIN, f.getSize()));
	}
}