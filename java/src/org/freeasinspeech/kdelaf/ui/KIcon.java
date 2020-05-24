package org.freeasinspeech.kdelaf.ui;

import java.awt.Toolkit;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.util.Vector;
import java.util.Enumeration;

import org.freeasinspeech.kdelaf.Cache;

/**
 * Class KIcon :
 * Easy access to a KDE icon.
 * 
 * @author sekou.diakite@fais.org
 */
public class KIcon
{
	/** The size in of one size of the square. */
	protected int size;
		
	/** The context (KIcon.C_ANY, KIcon.C_ACTION, KIcon.C_APPLICATION, KIcon.C_DEVICE, KIcon.C_FILESYSTEM or KIcon.C_MIMETYPE). */
	protected int context;
		
	/** The path. */
	protected String path;
		
	/** The overlay paths. */
	protected Vector overlayPaths;
		
	/** Context : Some icon with unknown purpose. */
	public static final int C_ANY = 0;
	/** Context : An action icon (e.g. 'save', 'print'). */
	public static final int C_ACTION = 1;
	/** Context : An icon that represents an application. */
	public static final int C_APPLICATION = 2;
	/** Context : An icon that represents a device. */
	public static final int C_DEVICE = 3;
	/** Context : An icon that represents a file system. */
	public static final int C_FILESYSTEM = 4;
	/** Context : An icon that represents a mime type (or file type). */
	public static final int C_MIMETYPE = 5;
		
	/** Classic size : 16x16. */
	public static final int S_16x16 = 16;
	/** Classic size : 22x22. */
	public static final int S_22x22 = 22;
	/** Classic size : 32x32. */
	public static final int S_32x32 = 32;
	/** Classic size : 48x48. */
	public static final int S_48x48 = 48;
	/** Classic size : 64x64. */
	public static final int S_64x64 = 64;
	/** Classic size : 128x128. */
	public static final int S_128x128 = 128;
		
	/**
	 * Constructor.
	 * @param size The size in pixel.
	 * @param context The context (KIcon.C_ANY, KIcon.C_ACTION, KIcon.C_APPLICATION, KIcon.C_DEVICE, KIcon.C_FILESYSTEM or KIcon.C_MIMETYPE).
	 * @param path The path.
	 */
	public KIcon(int size, int context, String path) {
		this.size = size;
		this.context = context;
		this.path = new String(path);
		overlayPaths = null;
	}
	
	/**
	 * Returns the size in pixel.
	 * @return The size in pixel.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Returns the context (KIcon.C_ANY, KIcon.C_ACTION, KIcon.C_APPLICATION, KIcon.C_DEVICE, KIcon.C_FILESYSTEM or KIcon.C_MIMETYPE).
	 * @return The context (KIcon.C_ANY, KIcon.C_ACTION, KIcon.C_APPLICATION, KIcon.C_DEVICE, KIcon.C_FILESYSTEM or KIcon.C_MIMETYPE).
	 */
	public int getContext() {
		return context;
	}
	
	/**
	 * Returns the path.
	 * @return The path.
	 */
	public String getPath() {
		return new String(path);
	}
	
	public void addOverlay(String overlayPath) {
		if (overlayPaths == null)
			overlayPaths = new Vector();
		overlayPaths.add(overlayPath);
	}
	
	/**
	 * Returns the name.
	 * @return The name.
	 */
	public String getName() {
		String name = Utilities.getFileName(path);
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex > -1)
			name = name.substring(0, dotIndex);
		return name;
	}
	
	/**
	 * Returns true when the file type is known.
	 * @return true when the file type is known.
	 */
	public static boolean fileTypeIsKnown(String pathToFile) {
		String ext = Utilities.getExtension(pathToFile);
		return (ext.equals("png") || ext.equals("gif"));
	}
	
	/**
	 * Returns true if the icon exist and is loadable.
	 * @return true if the icon exist and is loadable.
	 */
	public boolean isValid() {
		boolean valid = false;
		if (
				(size >= S_16x16) &&
				(
					(context == C_ANY) ||
					(context == C_ACTION) ||
					(context == C_APPLICATION) ||
					(context == C_DEVICE) ||
					(context == C_FILESYSTEM) ||
					(context == C_MIMETYPE)
				) 
			) {
			java.io.File iconFile = new java.io.File(path);
			valid = (iconFile.exists() && iconFile.isFile() && iconFile.canRead() && fileTypeIsKnown(path));
		}
		return valid;
	}
	
	/**
	 * Returns the icon or null if not valid.
	 * @return the icon or null if not valid.
	 */
	public Icon load() {
		KIconWrapper icon = null;
		if (isValid()) {
			icon = new KIconWrapper(path, overlayPaths);
			if (icon.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE)
				icon = null;
		}
		return icon;
	}
	
	/**
	 * Returns the icon or null if not valid.
	 * @return cache An icon cache.
	 * @return the icon or null if not valid.
	 */
	public Icon load(Cache cache) {
		String key = toString();
		// search on the cache
		KIconWrapper icon = (KIconWrapper)cache.get(key);
		if (icon == null) {
			// not on the cache
			// gets the icon traditionaly
			icon = (KIconWrapper)load();
			long iconSize = (icon != null) ? icon.size() : 0;
			if (iconSize < cache.maxDataSize())
				cache.add(key, icon, iconSize);
		}
		return icon;
	}
	
	/**
	 * Returns the icon or null if not valid.
	 * @return width the wanted width.
	 * @return height the wanted height.
	 * @return the icon or null if not valid.
	 */
	public Icon load(int width, int height) {
		Icon icon = null;
		if (isValid()) {
			java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().createImage(path);
			if (img != null) {
				if ( (img.getWidth(null) != width) || (img.getHeight(null) != height) )
					img = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(img);
			}
		}
		return icon;
	}
	
	public static String contextToString(int aContext) {
		String contextString = null;
		switch (aContext) {
			case C_ACTION:
				contextString = "actions";
				break;
			case C_APPLICATION:
				contextString = "app";
				break;
			case C_DEVICE:
				contextString = "device";
				break;
			case C_FILESYSTEM:
				contextString = "filesystem";
				break;
			case C_MIMETYPE:
				contextString = "mimetype";
				break;
			default:
				contextString = "any";
		}
		return contextString;
	}
	
	public static String sizeToString(int aSize) {
		return aSize+"x"+aSize;
	}
	
	public static int sizeToSide(int aSize) {
		return aSize;
	}
	
	public String toString() {
		String str = "KIcon:" + contextToString(context) + ":" + sizeToString(size) + ":" + path;
		if (overlayPaths != null) {
			str += ":Overlay(";
			for (Enumeration e = overlayPaths.elements() ; e.hasMoreElements() ;)
				str += (String)e.nextElement() + ",";
			str = str.substring(0, str.length() - 1) + ")";
		}
		return str;
	}
/*
	class KIconDisplay extends Image implements ImageObserver
	{
		public KIconDisplay(int width, int height, String path, Vector overlayPaths) {
			init(width, height, path, overlayPaths);
		}

		public KIconDisplay(int width, int height, String path) {
			init(width, height, path, null);
		}

		public KIconDisplay(int size, String path, Vector overlayPaths) {
			int side = sizeToSide(size);
			init(side, side, path, overlayPaths);
		}

		public KIconDisplay(int size, String path) {
			int side = sizeToSide(size);
			init(side, side, path, null);
		}

		protected void init(int width, int height, String path, Vector overlayPaths) {
			this.width = width;
			this.height = height;
		}

		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
			boolean loaded = (infoflags & ALLBITS) = ALLBITS;
		}
public abstract int getWidth(ImageObserver observer)
public abstract int getHeight(ImageObserver observer)
public abstract ImageProducer getSource()
public abstract Graphics getGraphics()
public abstract Object getProperty(String name, ImageObserver observer)
public abstract void flush()
	
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color backup = g.getColor();
			boolean black = true;
			for (int i = 0; i < width; i+=2) {
				for (int j = 0; j < height; j+=2) {
					if (black)
						g.setColor(Color.BLACK);
					else
						g.setColor(Color.WHITE);
					g.drawLine(i+x, j+y, i+x+2, j+y);
					g.drawLine(i+x, j+y+1, i+x+2, j+y+1);
					black = !black;
				}
				black = !black;
			}
			g.setColor(backup);
		}
		public int getIconWidth() {
			return width;
		}

		public int getIconHeight() {
			return height;
		}
	}*/
	
	class KIconWrapper extends ImageIcon
	{
		protected String path;
		protected Vector overlayIcons;
		public KIconWrapper(String path, Vector overlayPaths) {
			super(path);
			init(overlayPaths);
		}
		public KIconWrapper(String path) {
			super(path);
			init(null);
		}
		private void init(Vector overlayPaths) {
			overlayIcons = null;
			if (overlayPaths != null) {
				overlayIcons = new Vector();
				for (Enumeration e = overlayPaths.elements() ; e.hasMoreElements() ;) {
					ImageIcon overlay = new ImageIcon((String)e.nextElement());
					if (overlay.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE)
						overlayIcons.add(overlay);
				}
				if (overlayIcons.size() == 0)
					overlayIcons = null;
			}
		}
		public void paintIcon(Component c, Graphics g, int x, int y) {
			super.paintIcon(c, g, x, y);
			if (overlayIcons != null) {
				for (Enumeration e = overlayIcons.elements() ; e.hasMoreElements() ;)
					((Icon)e.nextElement()).paintIcon(c, g, x, y);
			}
		}
		public long size() {
			return getIconHeight() * getIconWidth() * 4;
		}
	}
}


