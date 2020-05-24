package org.freeasinspeech.kdelaf.ui;

import java.util.*;
import java.io.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.View;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.freeasinspeech.kdelaf.*;
//[!] todo a new combobox editor + completition
public class KIconTextField extends JTextField implements KeyListener
{
	protected Icon icon;
	protected int iconLeft = 4;
	protected int iconTextGap = 4;
	
	public KIconTextField() {
		super();
		init();
	}
	
	public KIconTextField(javax.swing.text.Document doc, String text, int columns) {
		super(doc, text, columns);
		init();
	}
	
	public KIconTextField(int columns) {
		super(columns);
		init();
	}
	
	public KIconTextField(String text) {
		super(text);
		init();
	}
	
	public KIconTextField(String text, int columns) {
		super(text, columns);
		init();
	}
	
	public void init() {
		addKeyListener(this);
	}
		
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	public Icon getIcon() {
		return icon;
	}
	
	public void setIconTextGap(int iconTextGap) {
		this.iconTextGap = iconTextGap;
	}
	
	public Insets getInsets() {
	  Insets insets = super.getInsets();
		if (icon != null)
			insets.left += icon.getIconWidth() + iconLeft + iconTextGap;
		return insets;
	}
	
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
		if (icon != null)
			icon.paintIcon(this, g,  iconLeft, (getHeight() - icon.getIconHeight()) / 2);
	}
	public void setBorder(javax.swing.border.Border b) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			fireActionPerformed();
	}
}
