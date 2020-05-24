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

import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
 
 /**
 * Class KOptionPaneUI
 * OptionPaneUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KOptionPaneUI extends BasicOptionPaneUI
{
	protected static Icon kErrorIcon = null;
	protected static Icon kInfoIcon = null;
	protected static Icon kWarningIcon = null;
	protected static Icon kQuestionIcon = null;
		
	/**
	 * Returns an instance of <code>KOptionPaneUI</code>.
	 * @return an instance of <code>KOptionPaneUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new KOptionPaneUI();
	}
	
	/**
	 * Creates and returns the separator between contents and icons.
	 * @return The separator between contents and icons.
	 */
	protected Container createSeparator() {
		return new JSeparator(SwingConstants.HORIZONTAL);
	}
	
	/**
	 * Creates and returns the message area.
	 * Actually gets the message area from parent and adds a border.
	 * @return The message area.
	 */
	protected Container createMessageArea() {
		Container _messageArea = super.createMessageArea();
		if (_messageArea instanceof JComponent)
			((JComponent)_messageArea).setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		return _messageArea;
	}


    /**
     * Creates and returns a Container containing the buttons. The buttons
     * are created by calling <code>getButtons</code>.
     */
   /* protected Container createButtonArea() {
        JPanel bottom = new JPanel();
        Border border = (Border)DefaultLookup.get(optionPane, this,
                                          "OptionPane.buttonAreaBorder");
        bottom.setName("OptionPane.buttonArea");
        if (border != null) {
            bottom.setBorder(border);
        }
	bottom.setLayout(new ButtonAreaLayout(
           DefaultLookup.getBoolean(optionPane, this,
                                    "OptionPane.sameSizeButtons", true),
           DefaultLookup.getInt(optionPane, this, "OptionPane.buttonPadding",
                                6),
           DefaultLookup.getInt(optionPane, this,
                        "OptionPane.buttonOrientation", SwingConstants.CENTER),
           DefaultLookup.getBoolean(optionPane, this, "OptionPane.isYesLast",
                                    false)));
	addButtonComponents(bottom, getButtons(), getInitialValueIndex());
	return bottom;
    }*/
	
	/**
	 * This method returns the icon for the given messageType.
	 * @param messageType The type of message.
	 * @return The icon for the given messageType.
	 */
	protected Icon getIconForType(int messageType) {
		Icon tmp = null;
		switch (messageType) {
			case JOptionPane.ERROR_MESSAGE:
				tmp = getErrorIcon();
				break;
			case JOptionPane.INFORMATION_MESSAGE:
				tmp = getInfoIcon();
				break;
			case JOptionPane.WARNING_MESSAGE:
				tmp = getWarningIcon();
				break;
			case JOptionPane.QUESTION_MESSAGE:
				tmp = getQuestionIcon();
				break;
		}
		return tmp;
	}
	
	protected Icon getErrorIcon() {
		if (kErrorIcon == null) {
			try {
				kErrorIcon = new ImageIcon(KdeLAF.getClient().getCommonImage(KDEConstants.CI_ERROR_ICON));
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				kErrorIcon = new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
			}
		}
		return kErrorIcon;
	}
	
	protected Icon getInfoIcon() {
		if (kInfoIcon == null) {
			try {
				kInfoIcon = new ImageIcon(KdeLAF.getClient().getCommonImage(KDEConstants.CI_INFO_ICON));
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				kInfoIcon = new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
			}
		}
		return kInfoIcon;
	}
	
	protected Icon getWarningIcon() {
		if (kWarningIcon == null) {
			try {
				kWarningIcon = new ImageIcon(KdeLAF.getClient().getCommonImage(KDEConstants.CI_WARNING_ICON));
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				kWarningIcon = new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
			}
		}
		return kWarningIcon;
	}
	
	protected Icon getQuestionIcon() {
		if (kQuestionIcon == null) {
			try {
				kQuestionIcon = new ImageIcon(KdeLAF.getClient().getCommonImage(KDEConstants.CI_QUESTION_ICON));
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				kQuestionIcon = new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
			}
		}
		return kQuestionIcon;
	}
}
/*
new ButtonAreaLayout(
           DefaultLookup.getBoolean(optionPane, this, "OptionPane.sameSizeButtons", true),
           DefaultLookup.getInt(optionPane, this, "OptionPane.buttonPadding", 6),
           DefaultLookup.getInt(optionPane, this, "OptionPane.buttonOrientation", SwingConstants.CENTER),
           DefaultLookup.getBoolean(optionPane, this, "OptionPane.isYesLast", false)
)
*/