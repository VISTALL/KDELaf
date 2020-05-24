package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

import java.awt.event.*;
import javax.swing.event.*;


import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
 
 
/*
 * @author sekou.diakite@fais.org
 */
public class QToolBarUI extends BasicToolBarUI
{
 protected QToolBarHandle toolBarHandle = null;
		
	public static ComponentUI createUI( JComponent c) {
		return new QToolBarUI();
	}
	
	/**
	 * This method installs the needed components for the JToolBar.
	 */
	protected void installComponents()
	{
		super.installComponents();
		
		if (toolBar.isFloatable()) {
			//toolBarHandle.setVisible(false);
			toolBarHandle = new QToolBarHandle(toolBar);
			toolBar.add(toolBarHandle, 0);
		}
	}

	/**
	 * This method installs listeners for the JToolBar.
	 */
	protected void installListeners()
	{
		super.installListeners();
		toolBar.removeMouseListener(dockingListener);
		toolBar.removeMouseMotionListener(dockingListener);
	}
	
	public void paint(Graphics g, JComponent c) {
		Utilities.configure(g);
		try {
			BufferedImage img = KdeLAF.getClient().getToolbarBackgroundImage(c.getWidth(), c.getHeight(), (((JToolBar)c).getOrientation() == SwingConstants.HORIZONTAL));
			FillWithImage.fillWithImage(g, img, c, c.getWidth(), c.getHeight());
		}
		catch (Exception e) {
		}
    }
 protected RootPaneContainer createFloatingWindow(JToolBar toolbar) {
  return new MyFloatingWindow(toolbar);
 }
	/**
	 * This method paints the border around the given Component.
	 *
	 * @param c The Component whose border is being painted.
	 * @param g The Graphics object to paint with.
	 * @param x The x coordinate of the component.
	 * @param y The y coordinate of the component.
	 * @param width The width of the component.
	 * @param height The height of the component.
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}
	
	protected static int qHandleExtends = -1;
	class QToolBarHandle extends JButton
	{		 
		QToolBarHandle(JToolBar t) {
			super("");
			MouseInputListener l = createDockingListener();
			addMouseListener(l);
			addMouseMotionListener(l);
			setVisible(toolBar.isFloatable());
		}
		
		public Dimension getMinimumSize() {
			int width = 36;
			int height = 36;
			if (toolBar.isFloatable()) {
				if (qHandleExtends < 0) {
					try {
						qHandleExtends = KdeLAF.getClient().getPixelMetric(KDEConstants.JPM_DockWindowHandleExtent);
					}
					catch (Exception e) {
						qHandleExtends = 8;
					}
				}
				if (toolBar.getOrientation() == SwingConstants.HORIZONTAL)
					width = qHandleExtends;
				else
					height = qHandleExtends;
			}
			else {
				width = 0;
				height = 0;
			}
			return new Dimension(width, height);
		}
		
		public void paint(Graphics g) {
			//if (toolBar.isFloatable()) {
				try {
					BufferedImage img = KdeLAF.getClient().getToolbarHandleImage(getWidth(), getHeight(), (toolBar.getOrientation() == SwingConstants.HORIZONTAL));
					g.drawImage(img, 0, 0, this);
				}
				catch (Exception e) {
				}
			//}
		}
	}
 class MyFloatingWindow extends JWindow implements MouseListener, WindowFocusListener
 {
  JLabel title;
  MyFloatingWindow(JToolBar toolbar) {
   super();
   getContentPane().setLayout(new BorderLayout());
   title = null;
   if (toolbar.getName() != null)
    title = new JLabel(toolbar.getName());
   else
    title = new JLabel("Toolbar");
   
   //[!] todo readcolors from kde
   title.setBackground(Color.BLACK);
   title.setForeground(Color.WHITE);
   title.setOpaque(true);
   MouseInputListener lL = createDockingListener();
   title.addMouseListener(lL);
   title.addMouseListener(this);
   title.addMouseMotionListener(lL);
   addWindowListener(createFrameListener());
   addWindowFocusListener(this);
   getContentPane().add(title, BorderLayout.NORTH);
	if (toolBarHandle != null)
		toolBarHandle.setVisible(false);
  }
  public void mouseClicked(MouseEvent e) {
   if (e.getClickCount() > 1) {
   toolBarHandle.setVisible(true);
    setVisible(false);
    WindowListener[] listeners = getWindowListeners();
    for (int i = 0; i < listeners.length; i++) {
     listeners[i].windowClosing(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
   }
  }
  public void windowGainedFocus(WindowEvent e) {
   //[!] todo readcolors from kde
   title.setBackground(Color.BLACK);
   title.setForeground(Color.WHITE);
  }
  public void windowLostFocus(WindowEvent e) {
   //[!] todo readcolors from kde
   title.setBackground(Color.BLACK);
   title.setForeground(Color.WHITE);
  }
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
 }
	
}

class FillWithImage
{
	public static void fillWithImage(Graphics g, BufferedImage img, ImageObserver o, int width, int height)
	{
		int paintedX = 0;
		while (paintedX < width) {
			g.drawImage(img, paintedX, 0, o);
			paintedX += img.getWidth();
			
			int paintedY = img.getHeight();
			while (paintedY < height) {
				g.drawImage(img, paintedX, paintedY, o);
				paintedY += img.getHeight();
			}
		}
	}
}
