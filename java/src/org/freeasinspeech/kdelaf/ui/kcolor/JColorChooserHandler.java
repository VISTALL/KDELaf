package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class JColorChooserHandler implements KColorEventMgr.KColorEventListener
{
	protected JColorChooser chooser;
		
	public JColorChooserHandler(JColorChooser chooser) {
		this.chooser = chooser;
		Color c = this.chooser.getColor();
		KColorEventMgr.addColorEventListener(this);
		KColorEventMgr.sendColorEvent((c == null) ? KColor.BLACK : new KColor(c));
	}
	
	public void onColorSelection(KColor c) {
		chooser.setColor(c.color);
	}
}	
