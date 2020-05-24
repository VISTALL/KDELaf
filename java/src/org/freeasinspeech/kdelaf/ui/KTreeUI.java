package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import java.awt.event.*;
import javax.swing.tree.TreePath;

import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.KDEConstants;


/**
 * Class KTreeUI
 * TreeUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KTreeUI extends BasicTreeUI
{
	/** The static images for KTreeUI. */
	protected static KTreeStatics images = new KTreeStatics();
		
	/**
	 * Returns an instance of <code>KTreeUI</code>.
	 * @return an instance of <code>KTreeUI</code>.
	 */
	public static ComponentUI createUI(JComponent c)  {
		return new KTreeUI();
	}

	/**
	 * This method paints the Component.
	 *
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	public void paint(Graphics g, JComponent c) {
		Utilities.configure(g);
		super.paint(g, c);
	}

	/**
	 * Returns control icon. It is null if the LookAndFeel does not implements the control icons. Package private for use in inner classes.
	 * From GNU CLASSPATH - BasicTreeUI.
	 * @return control icon if it exists.
	 */
	Icon getCurrentControlIcon(TreePath path) {
		return images.getExpander(!tree.isExpanded(path));
	}
	
	/**
	 * Paints the expand (toggle) part of a row. The receiver should NOT modify clipBounds, or insets.
	 * Adapted from GNU CLASSPATH - BasicTreeUI.
	 * @param g the graphics configuration
	 * @param clipBounds 
	 * @param insets 
	 * @param bounds bounds of expand control
	 * @param path path to draw control for
	 * @param row row to draw control for
	 * @param isExpanded is the row expanded
	 * @param hasBeenExpanded has the row already been expanded
	 * @param isLeaf is the path a leaf
	 */
	protected void paintExpandControl(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
		if (shouldPaintExpandControl(path, row, isExpanded, hasBeenExpanded, isLeaf)) {
			Icon icon = getCurrentControlIcon(path);
			int iconW = icon.getIconWidth();
			int x = bounds.x - rightChildIndent - iconW / 2;
			icon.paintIcon(tree, g, x, bounds.y + bounds.height / 2 - icon.getIconHeight() / 2);
		}
	}

	/**
	 * Draws a vertical line using the given graphic context
	 * @param g is the graphic context
	 * @param c is the component the new line will belong to
	 * @param x is the horizonal position
	 * @param top specifies the top of the line
	 * @param bottom specifies the bottom of the line
	 */
	protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom) {
		BufferedImage img = images.getBranch(false);
		int height = Math.abs(bottom - top);
		if (img.getHeight() == height)
			g.drawImage(img, x, top, c);
		else if (img.getHeight() > height)
			g.drawImage(img.getSubimage(0, 0, img.getWidth(), height), x, top, c);
		else {
			int draw = 0;
			while (draw < height) {
				int toDraw = Math.min(img.getHeight(), height - draw);
				g.drawImage(img.getSubimage(0, 0, img.getWidth(), toDraw), x, top + draw, c);
				draw += toDraw;
			}
		}
	}

	/**
	 * Draws a horizontal line using the given graphic context
	 * @param g is the graphic context
	 * @param c is the component the new line will belong to
	 * @param y is the vertical position
	 * @param left specifies the left point of the line
	 * @param right specifies the right point of the line
	 */
	protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
		BufferedImage img = images.getBranch(true);
		int width = Math.abs(left - right);
		if (img.getWidth() == width)
			g.drawImage(img, left, y, c);
		else if (img.getWidth() > width)
			g.drawImage(img.getSubimage(0, 0, width, img.getHeight()), left, y, c);
		else {
			int draw = 0;
			while (draw < width) {
				int toDraw = Math.min(img.getWidth(), width - draw);
				g.drawImage(img.getSubimage(0, 0, toDraw, img.getHeight()), left + draw, y, c);
				draw += toDraw;
			}
		}
	}
}

class KTreeStatics
{
	protected Icon[] expander = null;
	protected BufferedImage[] branch = null;
	
	public Icon getExpander(boolean on) {
		int index = on ? 0 : 1;
		int key = on ? KDEConstants.CI_TREEEXPANDER_ON : KDEConstants.CI_TREEEXPANDER_OFF;
		if (expander == null)
			expander = new Icon[2];
		if (expander[index] == null) {
			try {
				expander[index] = new ImageIcon(KdeLAF.getClient().getCommonImage(key));
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				expander[index] = new ImageIcon(new BufferedImage(9, 9, BufferedImage.TYPE_INT_ARGB));
			}
		}
		return expander[index];
	}
	
	public BufferedImage getBranch(boolean horizontal) {
		int index = horizontal ? 0 : 1;
		int key = horizontal ? KDEConstants.CI_TREEBRANCH_H : KDEConstants.CI_TREEBRANCH_V;
		if (branch == null)
			branch = new BufferedImage[2];
		if (branch[index] == null) {
			try {
				branch[index] = KdeLAF.getClient().getCommonImage(key);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				branch[index] = new BufferedImage(horizontal ? 128 : 1, horizontal ? 1 : 129, BufferedImage.TYPE_INT_ARGB);
			}
		}
		return branch[index];
	}
}