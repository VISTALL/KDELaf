package org.freeasinspeech.kdelaf.ui.kcolor;


import java.awt.Color;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class HSV
{
	public int h;
	public int s;
	public int v;
		
	public HSV() {
		h = 0;
		s = 0;
		v = 0;
	}
	public HSV(HSV hsv) {
		h = hsv.h;
		s = hsv.s;
		v = hsv.v;
	}
	
	public HSV(int h, int s, int v) {
		this.h = h;
		this.s = s;
		this.v = v;
	}
	
	public HSV(Color c) {
		setColor(c);
	}
	
	public static HSV fromColor(Color c) {
		return new HSV(c);
	}
	
	public static Color toColor(HSV hsv) {
		return hsv.toColor();
	}
	
	public static Color toColor(int h, int s, int v) {
		return (new HSV(h, s, v)).toColor();
	}
	
	public static HSV fromRGB(int rgb) {
		return new HSV(new Color(rgb));
	}
	
	public static int toRGB(HSV hsv) {
		return hsv.toRGB();
	}
	
	public static int toRGB(int h, int s, int v) {
		return (new HSV(h, s, v)).toRGB();
	}

	public int toRGB() {
		return toColor().getRGB();
	}
	
	public String toString() {
		return "HSV[h="+h+",s="+s+",v="+v+"]";
	}
	
	public boolean isAchromatic() {
		return ( s == 0 || h == -1 );
	}
	
	public Color toColor() {
		// from QT : void QColor::setHsv( int h, int s, int v )
		if ( (h < -1) || (s > 255) || (v > 255) )
			return new Color(0, 0, 0);
		int r=v, g=v, b=v;
		if ( s == 0 || h == -1 ) {			// achromatic case
			// Ignore
		} 
		else {					// chromatic case
			if (h >= 360)
				h %= 360;
			int f = h% 60;
			h /= 60;
			int p = (int)(2*v*(255-s)+255)/510;
			int q, t;
			if ( (h&1) != 0 ) {
				q = (int)(2*v*(15300-s*f)+15300)/30600;
				switch( h ) {
					case 1: r=(int)q; g=(int)v; b=(int)p; break;
					case 3: r=(int)p; g=(int)q; b=(int)v; break;
					case 5: r=(int)v; g=(int)p; b=(int)q; break;
				}
			} else {
				t = (int)(2*v*(15300-(s*(60-f)))+15300)/30600;
				switch( h ) {
					case 0: r=(int)v; g=(int)t; b=(int)p; break;
					case 2: r=(int)p; g=(int)v; b=(int)t; break;
					case 4: r=(int)t; g=(int)p; b=(int)v; break;
				}
			}
		}
		return new Color(r, g, b);
	}
	
	public void setColor(Color c) {
		// from QT : void QColor::hsv( int *h, int *s, int *v )
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int max = r;				// maximum RGB component
		int whatmax = 0;				// r=>0, g=>1, b=>2
		if ( (int)g > max ) {
			max = g;
			whatmax = 1;
		}
		if ( (int)b > max ) {
			max = b;
			whatmax = 2;
		}
		int min = r;				// find minimum value
		if ( (int)g < min ) min = g;
		if ( (int)b < min ) min = b;
		int delta = max-min;
		v = max;					// calc value
		s = (max != 0) ? (510*delta+max)/(2*max) : 0;
		if ( s == 0 ) {
			h = -1;				// undefined hue
		} else {
			switch ( whatmax ) {
			case 0:				// red is max component
				if ( g >= b )
					h = (120*(g-b)+delta)/(2*delta);
				else
					h = (120*(g-b+delta)+delta)/(2*delta) + 300;
				break;
			case 1:				// green is max component
				if ( b > r )
					h = 120 + (120*(b-r)+delta)/(2*delta);
				else
					h = 60 + (120*(b-r+delta)+delta)/(2*delta);
				break;
			case 2:				// blue is max component
				if ( r > g )
					h = 240 + (120*(r-g)+delta)/(2*delta);
				else
					h = 180 + (120*(r-g+delta)+delta)/(2*delta);
				break;
			}
		}
	}
}
