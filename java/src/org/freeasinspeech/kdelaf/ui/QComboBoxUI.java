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
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QComboBoxUI extends BasicComboBoxUI 
{
	/** True when the mouse is over the combobox. */
	protected boolean mouseOver = false;
		
	/**
	* A factory method to create a UI delegate for the given 
	* {@link JComponent}, which should be a {@link JComboBox}.
	*
	* @param c The {@link JComponent} a UI is being created for.
	*
	* @return A UI delegate for the {@link JComponent}.
	*/
	public static ComponentUI createUI(JComponent c) {
		return new QComboBoxUI();
	}
	
  /**
   * Creates a component that will be responsible for rendering the
   * selected component in the combo box.
   *
   * @return A renderer for the combo box.
   */
	protected ListCellRenderer createRenderer() {
		return new QComboBoxRenderer.UIResource();
	}

	/**
	 * Creates the default editor that will be used in editable combo boxes.  
	 * A default editor will be used only if an editor has not been 
	 * explicitly set with <code>setEditor</code>.
	 *
	 * @return a <code>ComboBoxEditor</code> used for the combo box
	 * @see javax.swing.JComboBox#setEditor
	 */
	protected ComboBoxEditor createEditor() {
	  return new QComboBoxEditor.UIResource();
	}
	 
	/**
	 * Installs the components for this JComboBox. ArrowButton, main
	 * part of combo box (upper part) and popup list of items are created and
	 * configured here.
	 */
	protected void installComponents() {
		super.installComponents();
		comboBox.remove(arrowButton);
		comboBox.revalidate();
	}
	
	/**
	* Creates and installs the listeners for this UI.
	* 
	* @see #uninstallListeners()
	*/
	protected void installListeners() {
		super.installListeners();
		comboBox.addMouseListener(new QComboBoxMouseListener(this, comboBox));
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}
	
	/**
	 * This method paints the Component.
	 *
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	public void paint(Graphics g, JComponent c)
	{
		Utilities.configure(g);
		hasFocus = comboBox.hasFocus();
		if ( !comboBox.isEditable() ) {
			Rectangle r = rectangleForCurrentValue();
			paintBackground(g, hasFocus);
			paintCurrentValue(g, r, hasFocus);
		}
		else {
			Rectangle r = rectangleForCurrentValue();
			paintBackground(g, hasFocus);
			paintCurrentValue(g,r,hasFocus);
		}
	}
	
	protected String getSelectedText() {
		Object selectedItem = comboBox.getSelectedItem();
		return (selectedItem != null) ? selectedItem.toString() : "";
	}
	
	/**
	* Paints the background of the combo box.
	*
	* @param g graphics context
	* @param hasFocus true if combo box has fox and false otherwise
	*/
	public void paintBackground(Graphics g, boolean hasFocus) {
		try {
			Object selectedItem = comboBox.getSelectedItem();
			BufferedImage img = KdeLAF.getClient().getComboBoxImage(
				comboBox.getWidth(),
				comboBox.getHeight(),
				comboBox.isEditable(),
				getSelectedText(),
				comboBox.isEnabled(),
				hasFocus,
				mouseOver
			);
			g.drawImage(img, 0, 0, comboBox);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
	}

	/**
	* Returns preferred size for the combo box.
	*
	* @param c comboBox for which to get preferred size
	*
	* @return The preferred size for the given combo box
	*/
	public Dimension getPreferredSize(JComponent c) {
		return getMinimumSize(c);
	}

	/**
	* Returns the minimum size for this {@link JComboBox} for this
	* look and feel. Also makes sure cachedMinimimSize is setup correctly.
	*
	* @param c The {@link JComponent} to find the minimum size for.
	*
	* @return The dimensions of the minimum size.
	*/
	public Dimension getMinimumSize(JComponent c) {
		if (isMinimumSizeDirty) {
			cachedMinimumSize = new Dimension(getDisplaySize());
			isMinimumSizeDirty = false;
		}
		return new Dimension(cachedMinimumSize);
	}

	/**
	* Returns the size of the display area for the combo box. This size will be 
	* the size of the combo box, not including the arrowButton.
	*
	* @return The size of the display area for the combo box.
	*/
	protected Dimension getDisplaySize() {
		Dimension displaySize = null;
		try {
			String maxLengthText = "";
			for (int i = 0; i < comboBox.getItemCount(); i++) {
				String currentText = comboBox.getItemAt(i).toString();
				if (currentText.length() > maxLengthText.length())
					maxLengthText = currentText;
			}
			displaySize = KdeLAF.getClient().getComboDisplaySize(comboBox.isEditable(), maxLengthText);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
			displaySize = super.getDisplaySize();
		}
		return displaySize;
  }
	
	/**
	* Returns the bounds in which comboBox's selected item will be
	* displayed.
	*
	* @return rectangle bounds in which comboBox's selected Item will be
	*         displayed
	*/
	protected Rectangle rectangleForCurrentValue() {
		Rectangle rectangle = null;
		try {
			rectangle = KdeLAF.getClient().getComboRectangleForCurrentValue(comboBox.getWidth(), comboBox.getHeight(), comboBox.isEditable(), getSelectedText());
		}
		catch (Exception e) {
			KdeLAF.onError(e);
			rectangle = super.rectangleForCurrentValue();
		}
		return rectangle;
	}
	
	class QComboBoxMouseListener extends java.awt.event.MouseAdapter {
		protected JComboBox comboBox;
		protected QComboBoxUI parent;
		QComboBoxMouseListener(QComboBoxUI parent, JComboBox comboBox) {
			this.comboBox = comboBox;
			this.parent = parent;
		}
		public void mouseEntered(java.awt.event.MouseEvent e) {
			parent.setMouseOver(true);
			comboBox.repaint(); 
		}
		public void mouseExited(java.awt.event.MouseEvent e) {
			parent.setMouseOver(false);
			comboBox.repaint(); 
		}
	}
	public interface WithIcon
	{
		public Icon getIcon();
		public int getIconTextGap();
		public String getPrettyText();
		public String getText();
	}
}
	
