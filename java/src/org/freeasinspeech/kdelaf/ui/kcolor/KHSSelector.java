package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KHSSelector extends JPanel implements MouseListener, MouseMotionListener, KColorEventMgr.KColorEventListener
{
	protected BufferedImage image;
	protected Point cursor;
	protected KColor current;
	
	public KHSSelector() {
		super();
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		setColor(KColor.BLACK);
		addComponentListener(
			new java.awt.event.ComponentAdapter() {
				public void componentResized(ComponentEvent e) {
					rebuildImage();
					setColor(current);
				}
			}
		);
		addMouseMotionListener(this);
		addMouseListener(this);
		KColorEventMgr.addColorEventListener(this);
	}
	
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(70, 140);
	}
	
	protected Point positionCorrection(Point p) {
		Point corrected = new Point(p.x, p.y);
		if (corrected.x < 0)
			corrected.x = 0;
		else if (corrected.x >= image.getWidth())
			corrected.x = image.getWidth() - 1;
		if (corrected.y < 0)
			corrected.y = 0;
		else if (corrected.y >= image.getHeight())
			corrected.y = image.getHeight() - 1;
		return corrected;
	}
	
	protected KColor pointToColor(Point p) {
		p = positionCorrection(p);
		int h = p.x * 359 / (image.getWidth() - 1);
		int s = p.y * 255 / (image.getHeight() - 1);
		s = 255 - s;
		return new KColor(new HSV(h, s, 0));
	}
	
	protected Point hsvToPoint(HSV hsv) {
		Point p = new Point(hsv.h * (image.getWidth() - 1) / 359, image.getHeight() - (hsv.s * (image.getHeight() - 1) / 255));
		return positionCorrection(p);
	}
	
	protected void rebuildImage() {
		//from http://developer.kde.org/documentation/library/3.5-api/kdelibs-apidocs/kdeui/html/kcolordialog_8cpp-source.html
		//KHSSelector::drawPalette
		int width = getWidth();
		int height = getHeight();
		if ( (width > 0) && (height > 0) ) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			int[] pixels = new int[width];
			int h, s;
			double hFactor = 359.0 / ((double)width - 1.0);
			double sFactor = 255.0 / ((double)height - 1.0);
			for (s = height - 1; s >= 0; s--) {
				for (h = 0; h < width; h++)
					pixels[h] = HSV.toRGB((int)(h * hFactor), (int)(s * sFactor), 192);
				image.setRGB(0, height - s - 1, width, 1, pixels, 0, width);
			}
		}
		else
			image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setColor(KColor c) {
		setColor(c, true);
	}
	public void setColor(KColor c, boolean repaint) {
		cursor = hsvToPoint(c.hsv);
		current = c;
		if (repaint)
			repaint();
	}
	
	public void onColorSelection(KColor c) {
		setColor(c);
	}
	
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, this);
		if ( (cursor.x >= 0) && (cursor.y >= 0) && (cursor.x <= getWidth()) && (cursor.y <= getHeight()) ) {
			Color saved = g.getColor();
			g.setColor(Color.WHITE);
			int numPoints = 5;
			int blankPoints = 2;
			drawCursorPart(g, 1, 1, blankPoints, numPoints);
			drawCursorPart(g, -1, 1, blankPoints, numPoints);
			drawCursorPart(g, -1, -1, blankPoints, numPoints);
			drawCursorPart(g, 1, -1, blankPoints, numPoints);
			g.setColor(saved);
		}
	}
	
	protected void drawCursorPart(Graphics g, int xInc, int yInc, int blankPoints, int numPoints) {
		int x = cursor.x + xInc * blankPoints;
		int y = cursor.y + yInc * blankPoints;
		numPoints--;
		int endX = x + xInc * numPoints;
		int endY = y + yInc * numPoints;
		g.drawLine(x, y, endX, endY);
	}
	
	public void onMouse(Point p) {
		setColor(pointToColor(p), false);
		KColorEventMgr.sendColorEvent(current, this);
		repaint();
	}
	public void mousePressed(MouseEvent e) {
		onMouse(e.getPoint());
	}
	public void mouseDragged(MouseEvent e) {
		onMouse(e.getPoint());
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}
