package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
 
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QComboBoxEditor extends BasicComboBoxEditor// implements ListCellRenderer//, Serializable
{	
  /**
   * Creates a new <code>BasicComboBoxEditor</code> instance.
   */
  public QComboBoxEditor()
  {
    editor = new KIconTextField();
    editor.setBorder(null);
    editor.setColumns(9);
  }
  /**
   * Sets item that should be edited when any editing operation is performed
   * by the user. The value is always equal to the currently selected value
   * in the combo box. Thus whenever a different value is selected from the
   * combo box list then this method should be  called to change editing
   * item to the new selected item.
   *
   * @param item item that is currently selected in the combo box
   */
  public void setItem(Object item)
  {
		super.setItem(item);
		if (item instanceof QComboBoxUI.WithIcon) {
			((KIconTextField)editor).setIcon(((QComboBoxUI.WithIcon)item).getIcon());
			((KIconTextField)editor).setIconTextGap(((QComboBoxUI.WithIcon)item).getIconTextGap());
			String tmpText = ((QComboBoxUI.WithIcon)item).getText();
			if (tmpText != null)
				((KIconTextField)editor).setText(tmpText);
		}
		else
			((KIconTextField)editor).setIcon(null);
  }

	/**
	 * A subclass of QComboBoxEditor that implements UIResource.
	 * QComboBoxEditor doesn't implement UIResource
	 * directly so that applications can safely override the
	 * cellRenderer property with BasicListCellRenderer subclasses.
	 * <p>
	 * <strong>Warning:</strong>
	 * Serialized objects of this class will not be compatible with
	 * future Swing releases. The current serialization support is
	 * appropriate for short term storage or RMI between applications running
	 * the same version of Swing.  As of 1.4, support for long term storage
	 * of all JavaBeans<sup><font size="-2">TM</font></sup>
	 * has been added to the <code>java.beans</code> package.
	 * Please see {@link java.beans.XMLEncoder}.
	 */
	public static class UIResource extends QComboBoxEditor implements javax.swing.plaf.UIResource {
	}
	
}	
 
