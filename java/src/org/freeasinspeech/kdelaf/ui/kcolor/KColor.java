package org.freeasinspeech.kdelaf.ui.kcolor;

import java.awt.Color;
import java.util.ArrayList;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class  KColor
{
	public Color color;
	public HSV hsv;
	public String name;
	
	public static final KColor BLACK = new KColor();
		
	public KColor(KColor color) {
		name = copy(color.name);
		hsv = copy(color.hsv);
		this.color = copy(color.color);
	}
	public KColor() {
		name = null;
		color = Color.BLACK;
		hsv = new HSV(0, 0, 0);
	}
	public KColor(Color color, HSV hsv, String name) {
		this.color = copy(color);
		this.hsv = copy(hsv);
		this.name = copy(name);
	}
	public KColor(Color color, HSV hsv) {
		this.color = copy(color);
		this.hsv = copy(hsv);
		name = null;
	}
	public KColor(Color color, String name) {
		this.color = copy(color);
		hsv = HSV.fromColor(color);
		this.name = copy(name);
	}
	public KColor(Color color) {
		this.color = copy(color);
		hsv = HSV.fromColor(color);
		name = null;
	}
	public KColor(HSV hsv, String name) {
		this.hsv = copy(hsv);
		color = HSV.toColor(hsv);
		this.name = copy(name);
	}
	public KColor(HSV hsv) {
		this.hsv = copy(hsv);
		color = HSV.toColor(hsv);
		name = null;
	}
	private String copy(String s) {
		if (s == null)
			return null;
		return new String(s);
	}
	private HSV copy(HSV hsv) {
		if (hsv == null)
			return null;
		return new HSV(hsv);
	}
	private Color copy(Color c) {
		if (c == null)
			return null;
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
} 
