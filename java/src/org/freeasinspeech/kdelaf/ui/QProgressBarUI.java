package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.KDEConstants;

/**
 * Class QProgressBarUI
 * ProgressBarUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QProgressBarUI extends BasicProgressBarUI {
	/**
	 * Returns an instance of <code>QProgressBarUI</code>.
	 * @return an instance of <code>QProgressBarUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QProgressBarUI();
	}
	
	/**
	 * This method changes the settings for the progressBar to
	 * the defaults provided by the current Look and Feel.
	 */
	protected void installDefaults() {
		super.installDefaults();
		progressBar.setStringPainted(true);
	}

	/**
	 * This method returns the preferred size of the given JComponent. If it returns null, then it is up to the LayoutManager to give it a size.
	 * @param c The component to find the preferred size for.
	 * @return The preferred size of the component.
	 */	
	public Dimension getPreferredSize(JComponent c) {
		Dimension dim = super.getPreferredSize(c);
		try {
			int key = (progressBar.getOrientation() == JProgressBar.HORIZONTAL) ? KDEConstants.SZ_HPROGRESSBAR : KDEConstants.SZ_VPROGRESSBAR;
			Dimension dim2 = KdeLAF.getClient().getSimplePreferredSize(key);
			if (progressBar.getOrientation() == JProgressBar.HORIZONTAL)
				dim.height = Math.max(dim.height, dim2.height);
			else
				dim.width = Math.max(dim.width, dim2.width);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
		return dim;
	}
	
	/**
	 * This method is called if the painting to be done is for a determinate progressBar.
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	protected void paintDeterminate(Graphics g, JComponent c) {
		try {
			int min = progressBar.getMinimum();
			int value = progressBar.getValue() - min;
			int length = progressBar.getMaximum() - min;
			boolean horizonal = (progressBar.getOrientation() == JProgressBar.HORIZONTAL);
			boolean isEnabled = progressBar.isEnabled();
			boolean hasFocus = progressBar.hasFocus();
			int width = c.getWidth();
			int height = c.getHeight();
			BufferedImage img = KdeLAF.getClient().getProgressBarImage(width, height, isEnabled, horizonal, hasFocus, value, length);
			g.drawImage(img, 0, 0, c);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
		if (progressBar.isStringPainted() && !progressBar.getString().equals("")) {
			Rectangle or = progressBar.getBounds();
			Insets insets = c.getInsets();
			int amountFull = getAmountFull(insets, or.width, or.height);
			paintString(g, 0, 0, or.width, or.height, amountFull, insets);
		}
	}
	
	/**
	 * This method is called if the painting to be done is for an indeterminate progressBar.
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	protected void paintIndeterminate(Graphics g, JComponent c) {
		try {
			int min = progressBar.getMinimum();
			int value = progressBar.getValue() - min;
			boolean horizonal = (progressBar.getOrientation() == JProgressBar.HORIZONTAL);
			boolean isEnabled = progressBar.isEnabled();
			boolean hasFocus = progressBar.hasFocus();
			int width = c.getWidth();
			int height = c.getHeight();
			BufferedImage img = KdeLAF.getClient().getProgressBarImage(width, height, isEnabled, horizonal, hasFocus, value, 0);
			g.drawImage(img, 0, 0, c);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
		if (progressBar.isStringPainted() && !progressBar.getString().equals("")) {
			Rectangle or = progressBar.getBounds();
			Insets insets = c.getInsets();
			int amountFull = getAmountFull(insets, or.width, or.height);
			paintString(g, 0, 0, or.width, or.height, amountFull, insets);
		}
	}
}
