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
public class KFileFavorites extends KFileBase
{
	protected JPanel content;
	public KFileFavorites(JFileChooser filechooser) {
		super(filechooser);
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		KFileConfig.SpeedBarEntry[] speedBarEntries = KFileConfig.getSpeedBarEntries();
		for (int i = 0; i < speedBarEntries.length; i++) {
			content.add(speedBarEntries[i].getButton());
		}
		setLayout(new BorderLayout());
		add(content, BorderLayout.CENTER);
		setVisible(KFileConfig.getShowSpeedbar());
	}
	
	public void setVisible(boolean aFlag) {
		setVisible(aFlag, false);
	}
	
	public void setVisible(boolean aFlag, boolean updateConfig) {
		if (updateConfig)
			KFileConfig.setShowSpeedbar(aFlag);
		super.setVisible(aFlag);
	}
}