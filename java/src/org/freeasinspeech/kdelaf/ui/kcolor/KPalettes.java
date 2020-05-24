package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.Color;
import java.util.Hashtable;
import java.io.File;
import java.util.regex.*;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KPalettes
{
	/** Resolve rgb->name Hashtable<Integer, String>. */
	protected static Hashtable rgbToName = new Hashtable();
	/** Resolve name->rgb Hashtable<String, Integer>. */
	protected static Hashtable nameToRgb = new Hashtable();
	/** The palettes name->palette Hashtable<String, KPalette>. */
	protected static Hashtable palettes = new Hashtable();
	/** True when the color palettes are loaded. */
	protected static boolean initialized = false;
	
	public static String[] getPaletteNames() {
		init();
		String[] paletteNames = new String[palettes.size()];
		int i = 0;
		for (java.util.Enumeration e = palettes.keys(); e.hasMoreElements();)
			paletteNames[i++] = (String)e.nextElement();
		return paletteNames;
	}
	
	public static KPalette[] getPalettes() {
		init();
		KPalette[] res = new KPalette[palettes.size()];
		int i = 0;
		for (java.util.Enumeration e = palettes.keys(); e.hasMoreElements();)
			res[i++] = getPalette((String)e.nextElement());
		return res;
	}
	
	public static int getRGB(String name) {
		init();
		Integer rgb = (Integer)nameToRgb.get(name);
		if (rgb == null)
			rgb = new Integer(Color.BLACK.getRGB());
		return rgb.intValue();
	}
	
	public static Color getColor(String name) {
		return new Color(getRGB(name));
	}
	
	public static String getName(int RGB) {
		init();
		return (String)rgbToName.get(new Integer(RGB));
	}
	
	public static String getName(Color color) {
		return getName(color.getRGB());
	}
	
	public static KPalette getPalette(String paletteName) {
		init();
		KPalette palette = (KPalette)palettes.get(paletteName);
		if (palette == null) {
			palette = new KPalette(paletteName);
			palettes.put(paletteName, palette);
		}
		return palette;
	}
	
	protected static void addColor(String paletteName, Color color, String colorName) {
		if (colorName != null) {
			Integer RGB = new Integer(color.getRGB());
			rgbToName.put(RGB, colorName);
			nameToRgb.put(colorName, RGB);
		}
		KPalette palette = (KPalette)palettes.get(paletteName);
		if (palette == null) {
			palette = new KPalette(paletteName);
			palettes.put(paletteName, palette);
		}
		palette.add(new KColor(color, colorName));
	}
	
	protected static synchronized void init() {
		if (!initialized) {
			String[] confPaths = org.freeasinspeech.kdelaf.ui.KConfig.getPaths();
			for (int i = 0; i < confPaths.length; i++) {
				File confDir = new File(confPaths[i], "colors");
				if (confDir.exists() && confDir.canRead() && confDir.isDirectory()) {
					File[] confFiles = confDir.listFiles();
					for (int j = 0; j < confFiles.length; j++) {
						if (confFiles[j].canRead() && confFiles[j].isFile())
							importColorConfigFile(confFiles[j], null);
					}
				}
			}
			initialized = true;
		}
	}
	
	public static boolean load(File confFile, KPalette pal) {
		boolean ok = true;
		if (confFile.exists() && confFile.canRead() && confFile.isFile())
			importColorConfigFile(confFile, pal);
		else
			ok = false;
		return ok;
	}
	
	protected static void importColorConfigFile(File confFile, KPalette pal) {
		try {
			java.io.BufferedReader r = new java.io.BufferedReader(new java.io.FileReader(confFile));
			String line;
			String paletteName = confFile.getName();
			int colorIndex = 0;
			// read the firstline (expect something like ".+ Palette.*")
			line = r.readLine();
			if ( (line == null) || ( (line.indexOf(" Palette") == -1) && (!line.startsWith("!"))) )
				return;
			// the valid line pattern
			Pattern p = Pattern.compile("^(\\d{1,3})\\s+(\\d{1,3})\\s+(\\d{1,3})\\s*(.*)$");
			// read the colors
			while ( (line = r.readLine()) != null ) {
				line = line.trim();
				if ( (line.length() > 0) && (line.charAt(0) != '#') ) {
					Matcher m = p.matcher(line);
					if (m.matches()) {
						try {
							int red = Integer.parseInt(m.group(1));
							int green = Integer.parseInt(m.group(2));
							int blue = Integer.parseInt(m.group(3));
							String colorName = m.group(4);
							if (colorName != null) {
								colorName = colorName.trim();
								if (colorName.length() == 0)
									colorName = null;
							}
							if (pal != null)
								pal.add(new KColor(new Color(red, green, blue), colorName));
							else
								addColor(paletteName, new Color(red, green, blue), colorName);
						}
						catch (NumberFormatException nfe) {
						}
					}
				}
			}
		}
		catch (java.io.IOException ioe) {
		}
		catch (PatternSyntaxException pse) {
		}
	}
}
