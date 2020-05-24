package org.freeasinspeech.kdelaf.ui;

import java.io.File;
/**
 * @author sekou.diakite@fais.org
 */
public class Utilities
{
	/** Stores the rollover listeners. */
	protected static java.util.Hashtable rolloverListeners = new java.util.Hashtable();
	/** Stores the pressed listeners. */
	protected static java.util.Hashtable pressedListeners = new java.util.Hashtable();
		
	public static String getPathName(String file) {
		String name = null;
		int separatorIndex = file.lastIndexOf(File.separator);
		if (separatorIndex > -1)
			name = file.substring(0, separatorIndex);
		return name;
	}
	
	public static String getPathNameEx(String file) {
		String name = getPathName(file);
		if (name == null)
			name = "." + File.separator;
		return name;
	}
	
	public static String getFileName(String file) {
		String name = null;
		int separatorIndex = file.lastIndexOf(File.separator);
		if (separatorIndex > -1)
			name = file.substring(separatorIndex + 1);
		else
			name = new String(file);
		return name;
	}
	
	public static String getExtension(String file) {
		String ext = getFileName(file).toLowerCase();
		int dotIndex = ext.lastIndexOf('.');
		if (dotIndex > 0) // (!=-1 hidden unix files)
			ext = ext.substring(dotIndex + 1);
		return ext;
	}
	
	protected static String[] copyStrArray(String []src) {
		String[] dest = null;
		if (src != null) {
			int size = src.length;
			dest = new String[size];
			for (int i = 0; i < size; i++)
				dest[i] = new String(src[i]);
		}
		return dest;
	}
	
	public static void registerRollover(javax.swing.AbstractButton b) {
		// [!]HACK
		if (b.getModel() != null) {
			RolloverListener rolloverListener = new RolloverListener(b.getModel());
			rolloverListeners.put(b, rolloverListener);
			b.addMouseListener(rolloverListener);
		}
		else
			org.freeasinspeech.kdelaf.Logger.log("GNU CLASSPATH BUG, getModel() == null");
	}
	
	public static void unregisterRollover(javax.swing.AbstractButton b) {
		if (rolloverListeners.containsKey(b)) {
			RolloverListener rolloverListener = (RolloverListener)rolloverListeners.remove(b);
			if (rolloverListener != null)
				b.removeMouseListener(rolloverListener);
		}
	}
	
	public static boolean isRolloverRegistered(javax.swing.AbstractButton b) {
		return rolloverListeners.containsKey(b);
	}
	
	public static void registerPressed(javax.swing.AbstractButton b) {
		// [!]HACK
		if (b.getModel() != null) {
			PressedListener pressedListener = new PressedListener(b.getModel());
			pressedListeners.put(b, pressedListener);
			b.addMouseListener(pressedListener);
		}
		else
			org.freeasinspeech.kdelaf.Logger.log("GNU CLASSPATH BUG, getModel() == null");
	}
	
	public static void unregisterPressed(javax.swing.AbstractButton b) {
		if (pressedListeners.containsKey(b)) {
			PressedListener pressedListener = (PressedListener)pressedListeners.remove(b);
			if (pressedListener != null)
				b.removeMouseListener(pressedListener);
		}
	}
	
	public static boolean isPressedRegistered(javax.swing.AbstractButton b) {
		return pressedListeners.containsKey(b);
	}
	
	public static void configure(java.awt.Graphics g) {
		if (g instanceof java.awt.Graphics2D)
			((java.awt.Graphics2D)g).setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public static String getClasspathVersion() {
		return isClasspath() ? System.getProperty("gnu.classpath.version") : null;
	}
	
	public static boolean isClasspath() {
		String classpathV = System.getProperty("gnu.classpath.version");
		return (classpathV != null);
	}
}

/**
 * Class RolloverListener
 * Listener for the roolover effect.
 * 
 * @author sekou.diakite@fais.org
 */
class RolloverListener extends java.awt.event.MouseAdapter
{
	/** The button model. */
	protected javax.swing.ButtonModel model;
	/**
	 * Constructor
	 * @param model The button model
	 */
	public RolloverListener(javax.swing.ButtonModel model) {
		this.model = model;
	}
	/**
	 * Activate the rollover
	 */
	public void mouseEntered(java.awt.event.MouseEvent e) {
		model.setRollover(true);
	}
	/**
	 * Deactivate the rollover
	 */
	public void mouseExited(java.awt.event.MouseEvent e) {
		model.setRollover(false);
	}
}

/**
 * Class PressedListener
 * Listener for the pressed effect.
 * 
 * @author sekou.diakite@fais.org
 */
class PressedListener extends java.awt.event.MouseAdapter
{
	/** The button model. */
	protected javax.swing.ButtonModel model;
	/**
	 * Constructor
	 * @param model The button model
	 */
	public PressedListener(javax.swing.ButtonModel model) {
		this.model = model;
	}
	/**
	 * Activate the pressed effect
	 */
	public void mousePressed(java.awt.event.MouseEvent e) {
		model.setPressed(true);
	}
	/**
	 * Deactivate the pressed effect
	 */
	public void mouseReleased(java.awt.event.MouseEvent e) {
		model.setPressed(false);
	}
}