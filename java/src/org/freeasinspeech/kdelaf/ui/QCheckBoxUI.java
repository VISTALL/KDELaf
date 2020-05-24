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
 
/**
 * Class QCheckBoxUI
 * CheckBoxUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class QCheckBoxUI extends BasicCheckBoxUI {  
	/**
	 * Returns the CheckBox icon.
	 * @return the CheckBox icon.
	 */
	public static Icon getIcon() {
		return new QCheckBoxIcon();
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
}

/**
 * Class QCheckBoxIcon
 * An icon displayed for CheckBox components.
 * 
 * @author sekou.diakite@fais.org
 */
class QCheckBoxIcon implements Icon
{
  /** The checkbox dimension. */
  protected static Dimension dim = null;
    
  /**
   * Returns the width of the icon, in pixels.
   * @return The width of the icon.
   */
  public int getIconWidth() {
    if (dim == null) {
      try {
        dim = KdeLAF.getClient().getCheckBoxDimension();
      }
      catch (Exception e) {
        KdeLAF.onError(e);
        dim = new Dimension(20, 20);
      }
    }
    return dim.width;
  }
  
  /**
   * Returns the height of the icon, in pixels.
   * @return The height of the icon.
   */
  public int getIconHeight() {
    if (dim == null) {
      try {
        dim = KdeLAF.getClient().getCheckBoxDimension();
      }
      catch (Exception e) {
        KdeLAF.onError(e);
        dim = new Dimension(20, 20);
      }
    }
    return dim.height;
  }
  
  /**
   * Paints the icon.
   * @param c  the component.
   * @param g  the graphics device.
   * @param x  the x-coordinate.
   * @param y  the y-coordinate.
   */
  public void paintIcon(Component c, Graphics g, int x, int y) {
    try {
      JCheckBox cb = (JCheckBox)c;
      ButtonModel model = cb.getModel();
      BufferedImage img = KdeLAF.getClient().getCheckBoxImage(model.isEnabled(), cb.isSelected(), model.isRollover());
      g.drawImage(img, 0, 0, c);
    }
    catch (Exception e) {
      KdeLAF.onError(e);
    }
  }
}
