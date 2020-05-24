package org.freeasinspeech.kdelaf.ui.kfile;

import java.util.*;
import java.io.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KFilePreview extends KFileBase
{
	public KFilePreview(JFileChooser filechooser) {
		super(filechooser);
		add(new JLabel("KFilePreview"));
		setVisible(KFileConfig.getShowPreview());
	}
	
	public void setVisible(boolean aFlag) {
		setVisible(aFlag, false);
	}
	
	public void setVisible(boolean aFlag, boolean updateConfig) {
		if (updateConfig)
			KFileConfig.setShowPreview(aFlag);
		super.setVisible(aFlag);
	}
}
