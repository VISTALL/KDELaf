package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KValueSelector extends JPanel implements SwingConstants
{
	protected int orientation;
	protected ImagePanel imagePanel;
	protected CursorPanel cursorPanel;
	
	public KValueSelector() {
		super();
		init(VERTICAL);
	}
	
	public KValueSelector(int orientation) {
		super();
		init((orientation == HORIZONTAL) ? HORIZONTAL : VERTICAL);
	}
	
	protected void init(int orientation) {
		this.orientation = orientation;
		setLayout(new BorderLayout());
		cursorPanel = new CursorPanel();
		imagePanel = new ImagePanel();
		add(imagePanel, BorderLayout.CENTER);
		add(cursorPanel, (orientation == HORIZONTAL) ? BorderLayout.SOUTH : BorderLayout.EAST);
	}
		
	
	
	class CursorPanel extends JPanel implements KColorEventMgr.KColorEventListener
	{
		protected HSV hsv;
		protected BufferedImage cursorImage;
		
		public CursorPanel() {
			super();
			buildImage();
			setColor(KColor.BLACK.hsv);
			KColorEventMgr.addColorEventListener(this);
			addMouseListener(
				new java.awt.event.MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						processClick(e.getPoint());
					}
				}
			);
			addMouseMotionListener(
				new java.awt.event.MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						processClick(e.getPoint());
					}
				}
			);
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			// draw the cursor
			int xCursor, yCursor;
			if (orientation == HORIZONTAL) {
				int range = getWidth() - cursorImage.getWidth();
				xCursor =  (int)((double)range * (double)hsv.v / 255.0);
				yCursor = 0;
			}
			else {
				int range = getHeight() - cursorImage.getHeight();
				xCursor = 0;
				yCursor = (int)((double)range * (double)(255 - hsv.v) / 255.0);
			}
			g.drawImage(cursorImage, xCursor, yCursor, this);
		}
		
		protected void buildImage() {
			if (orientation == HORIZONTAL)
				cursorImage = new BufferedImage(9, 5, BufferedImage.TYPE_INT_ARGB);
			else
				cursorImage = new BufferedImage(5, 9, BufferedImage.TYPE_INT_ARGB);
			Graphics g = cursorImage.getGraphics();
			g.setColor(new Color(255, 255, 255, 0));
			g.fillRect(0, 0, cursorImage.getWidth(), cursorImage.getHeight());
			g.setColor(Color.BLACK);
			if (orientation == HORIZONTAL) {
				int[] xPoints = {0, 5, 9};
				int[] yPoints = {5, 0, 5};
				g.fillPolygon(xPoints, yPoints, 3);
			}
			else {
				int[] xPoints = {0, 5, 5};
				int[] yPoints = {5, 0, 9};
				g.fillPolygon(xPoints, yPoints, 3);
			}
		}
		
		public void setColor(HSV hsv) {
			this.hsv = hsv;
		}
		
		public void onColorSelection(KColor c) {
			setColor(c.hsv);
			repaint();
		}
		
		public Dimension getMaximumSize() {
			Dimension parent = super.getMaximumSize();
			Dimension pref = getPreferredSize();
			if (orientation == HORIZONTAL)
				return new Dimension(parent.width, pref.height);
			return new Dimension(pref.width, parent.height);
		}
		
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
		
		public Dimension getPreferredSize() {
			if (orientation == HORIZONTAL)
				return new Dimension(9, 5);
			return new Dimension(5, 9);
		}
		
		public int getConstraint() {
			return 5;
		}
		
		public void processClick(Point p) {
			int min = getConstraint() / 2;
			if (orientation == HORIZONTAL) {
				int range = getWidth() - cursorImage.getWidth();
				int max = range + min;
				if (p.x <= min)
					hsv.v = 0;
				else if (p.x >= max)
					hsv.v = 255;
				else
					hsv.v = p.x * 255 / getWidth();
			}
			else {
				int range = getHeight() - cursorImage.getHeight();
				int max = range + min;
				if (p.y <= min)
					hsv.v = 255;
				else if (p.y >= max)
					hsv.v = 0;
				else
					hsv.v = 255 - (p.y * 255 / getHeight());
			}
			KColor newKColor = new KColor(hsv);
			KColorEventMgr.sendColorEvent(newKColor, imagePanel);
		}
	}
	
	class ImagePanel extends JPanel implements KColorEventMgr.KColorEventListener
	{
		protected HSV hsv;
		protected BufferedImage image;
		protected Border border;
		
		public ImagePanel() {
			super();
			border = BorderFactory.createLoweredBevelBorder();
			setColor(KColor.BLACK.hsv);
			KColorEventMgr.addColorEventListener(this);
			addComponentListener(
				new java.awt.event.ComponentAdapter() {
					public void componentResized(ComponentEvent e) {
						rebuildImage();
						repaint();
					}
				}
			);
			addMouseListener(
				new java.awt.event.MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						cursorPanel.processClick(e.getPoint());
					}
				}
			);
			addMouseMotionListener(
				new java.awt.event.MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						cursorPanel.processClick(e.getPoint());
					}
				}
			);
		}
	
		protected void rebuildImage() {
			//from http://developer.kde.org/documentation/library/3.5-api/kdelibs-apidocs/kdeui/html/kcolordialog_8cpp-source.html
			//KValueSelector::drawPalette
			int width = getWidth();
			int height = getHeight();
			Insets insets = border.getBorderInsets(this);
			width -= insets.left + insets.right;
			height -= insets.top + insets.bottom;
			if (orientation == HORIZONTAL)
				width -= cursorPanel.getConstraint();
			else
				height -= cursorPanel.getConstraint();
			if ( (width > 0) && (height > 0) ) {			
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				if (orientation == HORIZONTAL) {
					for (int v = 0; v < height; v++) {
						int[] pixels = new int[width];
						for(int x = 0; x < width; x++)
							pixels[x] = HSV.toRGB(hsv.h, hsv.s, 255 * x / (width - 1));
						image.setRGB(0, height - v - 1, width, 1, pixels, 0, width);
					}
				}
				else {
					for (int v = 0; v < height; v++) {
						int[] pixels = new int[width];
						int RGB = HSV.toRGB(hsv.h, hsv.s, 255 * v / (height - 1));
						for(int x = 0; x < width; x++)
							pixels[x] = RGB;
						image.setRGB(0, height - v - 1, width, 1, pixels, 0, width);
					}
				}
			}
			else
				image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
		
		public void setColor(HSV hsv) {
			this.hsv = hsv;
			rebuildImage();
		}
		
		public void onColorSelection(KColor c) {
			setColor(c.hsv);
			repaint();
		}
		
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
		
		public Dimension getPreferredSize() {
			if (orientation == HORIZONTAL)
				return new Dimension(70, 21);
			return new Dimension(21, 70);
		}
	
		public void paint(Graphics g) {
			super.paint(g);
			
			int x = 0;
			int y = 0;
			Insets insets = border.getBorderInsets(this);
			x = insets.left;
			y = insets.top;
			if (orientation == HORIZONTAL)
				x += cursorPanel.getConstraint() / 2;
			else
				y += cursorPanel.getConstraint() / 2;
			g.drawImage(image, x, y, this);
			
			x = 0;
			y = 0;
			int width = getWidth();
			int height = getHeight();
			if (orientation == HORIZONTAL) {
				x += cursorPanel.getConstraint() / 2;
				width -= cursorPanel.getConstraint();
			}
			else {
				y += cursorPanel.getConstraint() / 2;
				height -= cursorPanel.getConstraint();
			}
			border.paintBorder(this, g, x, y, width, height);
		}
	}
}
