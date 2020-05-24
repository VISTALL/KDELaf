package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
 
/*
 * @author sekou.diakite@fais.org
 */
public class QToolBarSeparatorUI extends BasicToolBarSeparatorUI
{	 
	protected static int qextends = -1;
		
	public static ComponentUI createUI( JComponent c) {
		return new QToolBarSeparatorUI();
	}
	
	protected void installDefaults(JSeparator s) {
		((JToolBar.Separator)s).setSeparatorSize(getPreferredSize(s));
	}
	
	public Dimension getPreferredSize( JComponent c) {
		int width, height;
		if (qextends < 0) {
			try {
				qextends = KdeLAF.getClient().getPixelMetric(KDEConstants.JPM_DockWindowSeparatorExtent);
			}
			catch (Exception e) {
				qextends = 6;
			}
		}
		if (((JSeparator)c).getOrientation() == SwingConstants.HORIZONTAL) {
			width = qextends;
			height = 40; //[!] We must get the real size of the toolbar
		}
		else {
			height = qextends;
			width = 40; //[!] We must get the real size of the toolbar
		}
		return new Dimension(width, height);
	}
	
	public void paint(Graphics g, JComponent c) {
		try {
			BufferedImage img = KdeLAF.getClient().getToolBarSeparatorImage(c.getWidth(), c.getHeight(), (((JSeparator)c).getOrientation() == SwingConstants.VERTICAL));
			g.drawImage(img, 0, 0, c);
		}
		catch (Exception e) {
		}
	}
}
