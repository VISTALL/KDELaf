package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import javax.swing.colorchooser.*;
import javax.swing.event.*;

import org.freeasinspeech.kdelaf.ui.kcolor.*;
import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
 
/**
 * Class KColorChooserUI
 * ColorChooserUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class KColorChooserUI extends ColorChooserUI
{
	protected JColorChooser chooser;
	protected KColorChooserPanel mainPanel;
	protected JColorChooserHandler mainHandler;
	
	/**
	 * Constructor.
	 */
	public KColorChooserUI() {
		super();
	}
	
	/**
	 * Returns an instance of <code>KColorChooserUI</code>.
	 * @return an instance of <code>KColorChooserUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new KColorChooserUI();
	}
	/**
    * This method creates the default chooser panels for the JColorChooser.
    *
    * @return The default chooser panels.
    */
	protected AbstractColorChooserPanel[] createDefaultChoosers() {
		AbstractColorChooserPanel[] result = {
			mainPanel = new KColorChooserPanel(chooser)
		};
		return result;
	}

	/**
    * This method installs the UI Component for the given JComponenorg.freeasinspeech.kdelaf.ui.kcolort.
    *
    * @param c The JComponent to install this UI for.
    */
	public void installUI(JComponent c) {
		if (c instanceof JColorChooser) {
			chooser = (JColorChooser) c;
			chooser.setLayout(new BorderLayout());
			AbstractColorChooserPanel[] panels = createDefaultChoosers();
			chooser.setChooserPanels(panels);
			final JPanel pane = new JPanel(new BorderLayout());
			chooser.add(panels[0], BorderLayout.CENTER);
			mainHandler = new JColorChooserHandler(chooser);
		}
	}
	/**
	 * This method uninstalls this UI for the given JComponent.
	 *
	 * @param c The JComponent that will have this UI removed.
	 */
	public void uninstallUI(JComponent c) {
		chooser.remove(mainPanel);
		mainPanel = null;
		mainHandler = null;
		chooser = null;
	}
}
