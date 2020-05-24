package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.Color;
import java.util.ArrayList;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KPalette
{
	protected String name;
	protected ArrayList colors;
		
	public KPalette(String name) {
		this.name = name;
		colors = new ArrayList();
	}
	
	public String getName() {
		return name;
	}
	
	public int size() {
		return colors.size();
	}
	
	public KColor get(int index) {
		if ( (index < 0) || (index >= size()) )
			return null;
		return (KColor)colors.get(index);
	}
	
	public void add(KColor color) {
		colors.add(color);
	}
	
	public KColor[] getAll() {
		KColor[] allColors = new KColor[size()];
		for (int i = 0; i < allColors.length; i++)
			allColors[i] = (KColor)colors.get(i);
		return allColors;
	}
	
	public String toString() {
		return name;
	}
	
	public void formatNamedColors() {
		ArrayList oldColors = colors; 
		colors = new ArrayList();
		String dict = org.freeasinspeech.kdelaf.ui.KLocale.getSelected();
		org.freeasinspeech.kdelaf.ui.KLocale.select("kdelibs_colors");
		for (int i = 0; i < oldColors.size(); i++) {
			KColor color = (KColor)oldColors.get(i);
			if (color.name != null)
				color.name = org.freeasinspeech.kdelaf.ui.KLocale._i18n("color", color.name);
			insertNamedColors(color);
		}
		org.freeasinspeech.kdelaf.ui.KLocale.select(dict);
	}
	
	protected void insertNamedColors(KColor color) {
		if ( (color.name == null) || (color.name.trim().length() == 0) || (color.name.indexOf("gray") != -1) || (color.name.indexOf("grey") != -1) )
			return;
		int findPos = -1;
		for (int i = 0; (findPos == -1) && (i < size()); ++i) {
			if (color.name.compareTo(get(i).name) < 0)
				findPos = i;
		}
		if (findPos != -1)
			colors.add(findPos, color);
		else
			add(color);
	}
}
