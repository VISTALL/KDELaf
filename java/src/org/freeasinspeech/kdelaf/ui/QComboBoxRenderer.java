package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
 
/**
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QComboBoxRenderer extends BasicComboBoxRenderer implements ListCellRenderer//, Serializable
{
	/**
	 * Returns a component that has been configured to display the given
	 * <code>value</code>.
	 *
	 * @param list List of items for which to the background and foreground
	 *        colors
	 * @param value object that should be rendered in the cell
	 * @param index index of the cell in the list of items.
	 * @param isSelected draw cell highlighted if isSelected is true
	 * @param cellHasFocus draw focus rectangle around cell if the cell has
	 *        focus
	 *
	 * @return Component that will be used to draw the desired cell.
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (comp instanceof JComponent)
			((JComponent)comp).setOpaque((index < 0) ? false : true);
		if (comp instanceof JLabel) {
			if (value instanceof QComboBoxUI.WithIcon) {
				((JLabel)comp).setIcon(((QComboBoxUI.WithIcon)value).getIcon());
				((JLabel)comp).setIconTextGap(((QComboBoxUI.WithIcon)value).getIconTextGap());
				String tmpText = ((QComboBoxUI.WithIcon)value).getPrettyText();
				if (tmpText != null)
					((JLabel)comp).setText(tmpText);
			}
			else
				((JLabel)comp).setIcon(null);
		}
		return comp;
	}
	
	/**
	 * A subclass of QComboBoxRenderer that implements UIResource.
	 * QComboBoxRenderer doesn't implement UIResource
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
	public static class UIResource extends QComboBoxRenderer implements javax.swing.plaf.UIResource {
	}
}	
