package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import javax.swing.text.*;
import org.freeasinspeech.kdelaf.Logger;

 
/**
 * Class QTextFieldUI
 * TextFieldUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QTextFieldUI extends BasicTextFieldUI implements KeyListener
{
	/** True when the SHIFT key is pressed. */
	protected boolean isSelecting = false;
	
	/** The characters that stops the control+(left|right) movement. */
	protected final char[] CONTROL_STOP_CHARS = {
		':',
		' ',
		'/',
		'.'
	};
		
	/**
	 * Returns an instance of <code>QTextFieldUI</code>.
	 * @return an instance of <code>QTextFieldUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QTextFieldUI();
	}
	
	/**
	* Installs listeners for the UI.
	*/
	protected void installListeners() {
		super.installListeners();
		JTextComponent tc = getComponent();
		tc.addKeyListener(this);
	}
	
	/**
	* Uninstalls listeners for the UI.
	*/
	protected void uninstallListeners() {
		super.uninstallListeners();
		JTextComponent tc = getComponent();
		tc.removeKeyListener(this);
	}
	
	// ************ KEY ACTIONS ***********
	protected void setCaretRelativePosition(int offset) {
		setCaretPosition(getComponent().getCaretPosition() + offset);
	}
	
	protected void setCaretPosition(int position) {
		JTextComponent tc = getComponent();
		try {
			if (isSelecting)
				tc.moveCaretPosition(position);
			else
				tc.setCaretPosition(position);
		}
		catch (Exception e) {
			Logger.error(e);
		}
	}
	
	protected int numberOfDeplacementWithControl(int direction) {
		if (direction < 0)
			direction = -1;
		else
			direction = 1;
		JTextComponent tc = getComponent();
		boolean stop = false;
		int caretPos = tc.getCaretPosition() + direction;
		int dep = 1;
		String text = tc.getText();
		
		while ( (!stop) && (caretPos >= 0) && (caretPos < text.length()) ) {
			char charCaret = text.charAt(caretPos);
			for (int i = 0; (i < CONTROL_STOP_CHARS.length) && (!stop); i++) {
				stop = (CONTROL_STOP_CHARS[i] == charCaret);
			}
			caretPos += direction;
			dep++;
		}
		if (direction < 0) {
			dep -= stop ? 2 : 1;
			if (dep == 0)
				dep = 1;
			if ( (tc.getCaretPosition() + direction * dep) < 0 )
				dep = tc.getCaretPosition();
		} else if ((tc.getCaretPosition() + direction * dep) > text.length())
			dep = text.length() - tc.getCaretPosition();
	
		return dep;
	}
	
	protected void moveLeftWithControl() {
		setCaretRelativePosition(-numberOfDeplacementWithControl(-1));
	}
	
	protected void moveRightWithControl() {
		setCaretRelativePosition(numberOfDeplacementWithControl(1));
	}
	
	protected void deleteLeft() {
		JTextComponent tc = getComponent();
		try {
			tc.getDocument().remove(tc.getCaretPosition() - 1, 1) ;
		}
		catch (Exception e) {
			Logger.error(e);
		}
	}
	
	protected void deleteLeftWithControl() {
		int dep = numberOfDeplacementWithControl(-1);
		while (dep-- > 0)
			deleteLeft();
	}
	
	protected void deleteRight() {
		JTextComponent tc = getComponent();
		try {
			tc.getDocument().remove(tc.getCaretPosition(), 1);
		}
		catch (Exception e) {
			Logger.error(e);
		}
	}
	
	protected void deleteRightWithControl() {
		int dep = numberOfDeplacementWithControl(1);
		while (dep-- > 0)
			deleteRight();
	}
	
	protected void removeSelection() {
		JTextComponent tc = getComponent();
		try {
			int length = Math.abs(tc.getSelectionStart() - tc.getSelectionEnd());
			int start = Math.min(tc.getSelectionStart(), tc.getSelectionEnd());
			tc.getDocument().remove(start, length);
		}
		catch (Exception e) {
			Logger.error(e);
		}
	}
	
	// ************ KEY LISTENERS ***********
	public void keyTyped(KeyEvent e) { }
	public void keyPressed(KeyEvent e) {
		JTextComponent tc = getComponent();
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (e.isControlDown())
					moveLeftWithControl();
				else
					setCaretRelativePosition(-1);
				break;
			case KeyEvent.VK_RIGHT:
				if (e.isControlDown())
					moveRightWithControl();
				else
					setCaretRelativePosition(1);
				break;
			case KeyEvent.VK_SHIFT:
				isSelecting = true;
				break;
			case KeyEvent.VK_BACK_SPACE:
				if (tc.getSelectionStart() != tc.getSelectionEnd())
					removeSelection();
				else if (e.isControlDown())
					deleteLeftWithControl();
				else
					deleteLeft();
				break;
			case KeyEvent.VK_DELETE:
				if (tc.getSelectionStart() != tc.getSelectionEnd())
					removeSelection();
				else if (e.isControlDown())
					deleteRightWithControl();
				else
					deleteRight();
				break;
			case KeyEvent.VK_A:
				if (e.isControlDown())
					tc.select(0, tc.getText().length());
				break;
			case KeyEvent.VK_C:
				if (e.isControlDown())
					tc.copy();
				break;
			case KeyEvent.VK_X:
				if (e.isControlDown())
					tc.cut();
				break;
			case KeyEvent.VK_V:
				if (e.isControlDown())
					tc.paste();
				break;
			case KeyEvent.VK_END:
				setCaretPosition(tc.getText().length());
				break;
			case KeyEvent.VK_HOME:
				setCaretPosition(0);
				break;
		}
	}
	public void keyReleased(KeyEvent e) {
		JTextComponent tc = getComponent();
		switch (e.getKeyCode()) {
			case KeyEvent.VK_SHIFT:
				isSelecting = false;
				break;
		}
	}


}

