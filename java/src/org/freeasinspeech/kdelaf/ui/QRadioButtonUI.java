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
 * Class QRadioButtonUI
 * RadioButtonUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QRadioButtonUI extends BasicRadioButtonUI {
	/**
	 * Returns the RadioButton icon.
	 * @return the RadioButton icon.
	 */
	public static Icon getIcon() {
		return new QRadioButtonIcon();
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
 * Class QRadioButtonIcon
 * An icon displayed for RadioButton components.
 * 
 * @author sekou.diakite@fais.org
 */
class QRadioButtonIcon implements Icon
{
  /** The RadioButton dimension. */
  protected static Dimension dim = null;
    
  /**
   * Returns the width of the icon, in pixels.
   * @return The width of the icon.
   */
  public int getIconWidth() {
    if (dim == null) {
      try {
        dim = KdeLAF.getClient().getRadioButtonDimension();
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
        dim = KdeLAF.getClient().getRadioButtonDimension();
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
      JRadioButton rb = (JRadioButton)c;
      ButtonModel model = rb.getModel();
      BufferedImage img = KdeLAF.getClient().getRadioButtonImage(model.isEnabled(), rb.isSelected(), model.isRollover());
      g.drawImage(img, 0, 0, c);
    }
    catch (Exception e) {
      KdeLAF.onError(e);
    }
  }
}
