package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import javax.swing.event.*;
import javax.swing.table.*;

import org.freeasinspeech.kdelaf.*; 

/**
 * Class QTableHeaderUI
 * TableHeaderUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class QTableHeaderUI extends BasicTableHeaderUI
{	
	protected QTableHeaderUIRenderer hRenderer = new QTableHeaderUIRenderer();
	protected static int preferredHeight = -1;
		
	/**
	 * Returns an instance of <code>QTableHeaderUI</code>.
	 * @return an instance of <code>QTableHeaderUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QTableHeaderUI();
	}
	
	public void installUI(JComponent c) {
		super.installUI(c);
		header.setDefaultRenderer(hRenderer);
	}
	
	protected MouseInputListener createMouseInputListener() {
		return new KMouseInputHandler();
	}
	
	public Dimension getPreferredSize(JComponent c) {
		Dimension dim = super.getPreferredSize(c);
		setHeight(dim);
		return dim;
	}
	public Dimension getMinimumSize(JComponent c) {
		Dimension dim = super.getPreferredSize(c);
		setHeight(dim);
		return dim;
	}
	public Dimension getMaximumSize(JComponent c) {
		Dimension dim = super.getPreferredSize(c);
		setHeight(dim);
		return dim;
	}
	public void setHeight(Dimension dim) {
		if (preferredHeight == -1) {
			try {
				preferredHeight = KdeLAF.getClient().getPixelMetric(org.freeasinspeech.kdelaf.client.KDEConstants.JPM_TableHeaderH) ;
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				preferredHeight = 0;
			}
		}
		dim.height = Math.max(dim.height, preferredHeight);
	}
	
	class KMouseInputHandler extends BasicTableHeaderUI.MouseInputHandler
	{
		protected int col;
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			hRenderer.setPressed(col, false);
			header.repaint();
		}
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			col = header.columnAtPoint(e.getPoint());
			if (col > -1) {
				hRenderer.setPressed(col, true);
				header.repaint();
			}
		}
	}
	
	class QTableHeaderUIRenderer extends JLabel implements TableCellRenderer
	{
		protected Hashtable pressed = new Hashtable();
		protected int column = -1;
			
		public void paint(Graphics g) {
			Utilities.configure(g);
			try {
				BufferedImage img = KdeLAF.getClient().getTableHeaderImage(getWidth(), getHeight(), pressed.containsKey(String.valueOf(column)), false, false);
				g.drawImage(img, 0, 0, this);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
			}

			setOpaque(false);
			super.paint(g);
			setOpaque(true);
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			this.column = column;
			setText( (value == null) ? "" : value.toString() );
			return this;
		}
		public void setPressed(int column, boolean isPressed) {
			if (isPressed)
				pressed.put(String.valueOf(column), String.valueOf(isPressed));
			else
				pressed.remove(String.valueOf(column));
		}
	}
}
