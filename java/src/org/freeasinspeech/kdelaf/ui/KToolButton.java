package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.freeasinspeech.kdelaf.*; 
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KToolButton extends JButton //implements MouseListener
{
	public KToolButton() {
		super();
		init();
	}
	
	public KToolButton(Icon icon) {
		super(icon);
		init();
	}
	
	public KToolButton(String text) {
		super(text);
		init();
	}
	
	public KToolButton(Action a) {
		super(a);
		init();
	}
	
	public KToolButton(String text, Icon icon) {
		super(text, icon);
		init();
	}
	
	private void init() {
		/*MouseListener[] listerners = getMouseListeners();
		for (int i = 0; i < listerners.length; i++)
			removeMouseListener(listerners[i]);
		addMouseListener(this);
		for (int i = 0; i < listerners.length; i++)
			addMouseListener(listerners[i]);*/
	}
	/*
	public void mousePressed(MouseEvent e) {
		getModel().setPressed(true);
	}
	
	public void mouseReleased(MouseEvent e) {
		getModel().setPressed(false);
		getModel().setRollover(false);
	}
	
	public void mouseClicked(MouseEvent e) {
	}
	
	public void mouseEntered(MouseEvent e) {
		getModel().setRollover(true);
	}
	
	public void mouseExited(MouseEvent e) {
		getModel().setRollover(false);
	}*/
}
