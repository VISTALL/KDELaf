package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
 
import org.freeasinspeech.kdelaf.*;
  
/**
 * Class QTabbedPaneUI
 * TabbedPaneUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QTabbedPaneUI extends BasicTabbedPaneUI
{
	/**
	* Returns an instance of <code>QTabbedPaneUI</code>.
	* @return an instance of <code>QTabbedPaneUI</code>.
	*/
	public static ComponentUI createUI(final JComponent c)  {
		return new QTabbedPaneUI();
	}

	/**
	* Calculate the preferred size of this component
	* @param c The component to measure
	* @return The preferred dimensions of the component
	*/
	public Dimension getPreferredSize(JComponent c) {
		KdeLAF.declareRoot(c);
		return super.getPreferredSize(c);
	}

	/**
	 * This method paints the JTabbedPane.
	 *
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	public void paint(Graphics g, JComponent c)
	{
		Utilities.configure(g);
		super.paint(g, c);
	}
	
	/**
	 * This method paints the background for an individual tab.
	 *
	 * @param g The Graphics object to paint with.
	 * @param tabPlacement The JTabbedPane's tab placement.
	 * @param tabIndex The tab index.
	 * @param x The x position of the tab.
	 * @param y The y position of the tab.
	 * @param w The width of the tab.
	 * @param h The height of the tab.
	 * @param isSelected Whether the tab is selected.
	 */
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		try {
			int __tabIndex = 0;
			if (tabIndex == (tabPane.getTabCount() - 1))
				__tabIndex = 2;
			else if (tabIndex != 0)
				__tabIndex = 1;
			BufferedImage img = KdeLAF.getClient().getTabImage(tabPlacement, __tabIndex, w, h, tabPane.isEnabledAt(tabIndex), isSelected);
			g.drawImage(img, x, y, tabPane);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
	}
	
	/**
	 * This method paints the border for an individual tab.
	 * Do nothing
	 * @param g The Graphics object to paint with.
	 * @param tabPlacement The JTabbedPane's tab placement.
	 * @param tabIndex The tab index.
	 * @param x The x position of the tab.
	 * @param y The y position of the tab.
	 * @param w The width of the tab.
	 * @param h The height of the tab.
	 * @param isSelected Whether the tab is selected.
	 */
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
	}
}
