package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

import org.freeasinspeech.kdelaf.*;
 
/**
 * Class QToolTipUI
 * ToolTipUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class QToolTipUI extends BasicToolTipUI
{
	/** The shared instance. */
	protected static QToolTipUI _sharedInstance = null;
		
   /**
    * Returns an instance of <code>QToolTipUI</code>.
    * @return an instance of <code>QToolTipUI</code>.
    */
	public static ComponentUI createUI( JComponent c) {
		if (_sharedInstance == null)
			_sharedInstance = new QToolTipUI();
		return _sharedInstance;
	}
}
