package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;


import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
 
/**
 * Class QInternalFrameUI
 * InternalFrameUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QInternalFrameUI extends BasicInternalFrameUI {
	
	/**
	 * Constructor.
	 *
	 * @param c The JInternalFrame this UI will represent.
	 */
	public QInternalFrameUI(JInternalFrame c) {
		super(c);
	}
	
	/**
	 * Returns an instance of <code>QInternalFrameUI</code>.
	 * @return an instance of <code>QInternalFrameUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new QInternalFrameUI((JInternalFrame)c);
	}
	
	/**
	 * This method creates the north pane used in the JInternalFrame.
	 *
	 * @param w The JInternalFrame to create a north pane for.
	 *
	 * @return The north pane.
	 */
	protected JComponent createNorthPane(JInternalFrame w) {
		titlePane = new QInternalFrameTitlePane(w);
		return titlePane;
	}
	
	/**
	 * This method installs the defaults specified by the look and feel.
	 */
	protected void installDefaults() {
		super.installDefaults();
		KIcon kIcon = KIconFactory.getIconByName("xapp");
		frame.setFrameIcon(kIcon.load(16, 16));
	}
}
