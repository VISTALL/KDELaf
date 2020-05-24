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
 * Class QSpinnerUI
 * SpinnerUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QSpinnerUI extends BasicSpinnerUI
{	
	protected static QSpinnerStatics images = new QSpinnerStatics();
	
	/**
	 * Returns an instance of <code>QSpinnerUI</code>.
	 * @return an instance of <code>QSpinnerUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QSpinnerUI();
	}
	
	/**
	 * Creates the "Next" button
	 * @return the next button component
	 */
	protected Component createNextButton() {
		Component button = new QSpinnerButton(
			new ImageIcon(images.getNextImage(false, false)),
			new ImageIcon(images.getNextImage(true, false)),
			new ImageIcon(images.getNextImage(false, true))
		);
		installNextButtonListeners(button);
		return button;
    }
	
	/**
	 * Creates the "Previous" button
	 * @return the previous button component
	 */
	protected Component createPreviousButton() {
		Component button = new QSpinnerButton(
			new ImageIcon(images.getPrevImage(false, false)),
			new ImageIcon(images.getPrevImage(true, false)),
			new ImageIcon(images.getPrevImage(false, true))
		);
		installPreviousButtonListeners(button);
		return button;
	}
	
	class QSpinnerButton extends JButton
	{
		protected int w;
		protected int h;
			
		public QSpinnerButton(Icon defaultIcon, Icon pressedIcon, Icon disabledIcon) {
			super();
			setIcon(defaultIcon);
			setPressedIcon(pressedIcon);
			setDisabledIcon(disabledIcon);
			setBorderPainted(false);
			setContentAreaFilled(false);
			w = Math.max(defaultIcon.getIconWidth(), pressedIcon.getIconWidth());
			h = Math.max(defaultIcon.getIconHeight(), pressedIcon.getIconHeight());
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(w, h);
		}
		
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
		
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}
	}
}

class QSpinnerStatics
{
	protected BufferedImage[] nextButton = null;
	protected BufferedImage[] prevButton = null;
		
	public QSpinnerStatics() {
	}
	
	public BufferedImage getNextImage(boolean clicked, boolean disabled) {
		int index = 0;
		if (disabled)
			index = 2;
		else if (clicked)
			index = 1;
		//else index = 0;
		return getNextImage(index);
	}
	
	public BufferedImage getPrevImage(boolean clicked, boolean disabled) {
		int index = 0;
		if (disabled)
			index = 2;
		else if (clicked)
			index = 1;
		//else index = 0;
		return getPrevImage(index);
	}
	
	protected BufferedImage getNextImage(int index) {
		if ( (nextButton == null) || (nextButton[index] == null) ) {
			if (nextButton == null)
				nextButton = new BufferedImage[3];
			try {
				nextButton[index] = KdeLAF.getClient().getSpinnerImage(index, true);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				nextButton[index] = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
			}
		}
		return nextButton[index];
	}
	
	protected BufferedImage getPrevImage(int index) {
		if ( (prevButton== null) || (prevButton[index] == null) ) {
			if (prevButton == null)
				prevButton = new BufferedImage[3];
			try {
				prevButton[index] = KdeLAF.getClient().getSpinnerImage(index, false);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				prevButton[index] = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
			}
		}
		return prevButton[index];
	}
}