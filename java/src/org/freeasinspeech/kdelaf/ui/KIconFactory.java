package org.freeasinspeech.kdelaf.ui;

import java.util.*;
import java.io.File;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;

import org.freeasinspeech.kdelaf.*; 


// [!] todo rechercher quand necessaire = ne pas tous charger
// on peut quand meme charger les plus demande

/**
 * Class KIconFactory :
 * Easy access to KDE icon theme.
 * 
 * @author diakite_at_freeasinspeech_dot_org
 */
public class KIconFactory
{
	/** True when the KIconFactory is loaded. */
	protected static boolean loaded = false;
	
	/** The icon theme (and it's parents). */
	protected static IconTheme iconTheme = null;
		
	/**
	 * Loads the icons infos if not already loaded.
	 */
	protected static void load() {
		load(false);
	}
	
	/**
	 * Explore recursively the given path and call addIcon on the files.
	 * @param path The path to explore.
	 * @param isRoot true is the path is the root icon path.
	 */
	protected static void explore(File path, boolean isRoot) {
		if (!path.isHidden()) {
			if (path.isDirectory()) {
				File index = new File(path, "index.theme");
				if (index.exists() && index.isFile() && index.canRead())
					iconTheme = new IconTheme(index);
			}
		}
	}
	
	/**
	 * Loads the icons infos.
	 * @param force If true loads Load the icons infos even if there are already loaded.
	 */
	protected synchronized static void load(boolean force) {
		if ( (!loaded) || force) {
			IconTheme.clearCached();
			iconTheme = null;
			long time = System.currentTimeMillis();
			String[] paths = null;
			try {
				paths = KdeLAF.getClient().getIconPaths();
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				paths = new String[1];
				paths[0] = "/usr/share/icons/default.kde";
			}
			for (int i = 0; (i < paths.length) && (iconTheme == null); i++)
				explore(new File(paths[i]), true);
			if (iconTheme == null)
				iconTheme = new IconTheme();
			loaded = true;
			Logger.log("KIconFactory::load() in " + (System.currentTimeMillis() - time) + "Ms");
		}
	}
	
	/**
	 * Reloads the icons infos.
	 */
	public static void reload() {
		load(true);
	}
	
	public static KIcon getIcon(int size, int context, String name) {
		load();
		return iconTheme.findIcon(name, context, size);
	}
	
	public static KIcon getIcon(File file, KMimeItem mime, int size) {
		String iconName = mime.getIconName();
		KIcon icon = null;
		if ((new File(iconName)).isAbsolute()) {
			icon = new KIcon(size, KIcon.C_ANY, iconName);
		}
		else
			icon = getIconBySize(size, iconName);
		// special cases overlays
		if (file.exists() && file.isFile()) {
			if (!file.canRead()) {
				// lock overlay
				KIcon overlay = getIconBySize(size, "lock_overlay");
				if (!(overlay instanceof KIconFallback))
					icon.addOverlay(overlay.getPath());
				// [!] todo zip overlay
				// [!] todo link overlay
			}
		}
		return icon;
	}
	
	public static KIcon getIcon(File file, int size) {
		return getIcon(file, KMime.getMime(file), size);
	}
		
	public static KIcon getIconBySize(int size, String name) {
		load();
		return iconTheme.findIcon(name, KIcon.C_ANY, size);
	}
	
	public static KIcon getIconByContext(int context, String name) {
		load();
		return iconTheme.findIcon(name, context, -1);
	}
	
	public static KIcon getIconByName(String name) {
		load();
		return iconTheme.findIcon(name, KIcon.C_ANY, -1);
	}
}

/**
 * Class IconTheme :
 * Index the icons of a theme.
 * Implemented following FreeDesktop.org "Icon Theme Specification" :
 * http://standards.freedesktop.org/icon-theme-spec/icon-theme-spec-latest.html
 *
 * @author diakite_at_freeasinspeech_dot_org
 */
class IconTheme
{
	protected static Hashtable cachedValues = new Hashtable();
	protected String name;
	protected String path;
	protected IconTheme parent = null;
	protected ArrayList childs = new ArrayList();

	public IconTheme(File index) {
		KConfig config = new KConfig(index);
		KConfigGroup group = config.getGroup("Icon Theme");
		this.path = index.getParentFile().getAbsolutePath();
		name = group.get("Name");
		if (name == null)
			name = "";
		importDirs(group.get("Directories"));

		String inherits = group.get("Inherits");
		if ( (inherits == null) || (inherits.length() == 0) )
			inherits = name.toLowerCase().equals("hicolor") ? null : "hicolor";
		if (inherits != null) {
			File parentIndex = new File(new File(index.getParentFile().getParentFile(), inherits), "index.theme");
			if (parentIndex.exists() && parentIndex.isFile() && parentIndex.canRead())
				parent = new IconTheme(parentIndex);
		}
	}

	/**
	 * Builds an empty theme (fallback)
	 */
	public IconTheme() {
		name = "fallback";
		path = "/dev/null";
	}

	public static void clearCached() {
		cachedValues.clear();
	}

	public KIcon findIcon(String icon, int context, int size) {
		return findIcon(icon, context, size, false);
	}

	public KIcon findIcon(String icon, int context, int size, boolean firstOK) {
		String key = icon + String.valueOf(context) + String.valueOf(size);
		KIcon filename = (KIcon)cachedValues.get(key);
		if (filename == null) {
			filename = findIconHelper(icon, context, size, firstOK);
			if (filename == null)
				filename = new KIconFallback(icon, (size > 0) ? size : 16, context);
			cachedValues.put(key, filename);
		}
		return filename;
	}

	public KIcon findIconHelper(String icon, int context, int size, boolean firstOK) {
		KIcon filename = lookupIcon(icon, context, size, firstOK);
		if (filename != null)
			return filename;

		if (parent != null)
			return parent.findIconHelper(icon, context, size, true);
		return null;
	}

	protected KIcon lookupIcon(String icon, int context, int size, boolean firstOK) {
		if ( firstOK || ((context == KIcon.C_ANY) && (size <= 0)) )
			return lookupIconFirst(icon);

		Iterator it = childs.iterator();
		KIcon filename = null;
		if (context == KIcon.C_ANY)
			while ( it.hasNext() && (filename == null) ) {
				IconThemeDir aDir = (IconThemeDir)it.next();
				if (aDir.getSize() == size)
					filename = aDir.lookupIcon(icon);
			}
		else if (size <= 0)
			while ( it.hasNext() && (filename == null) ) {
				IconThemeDir aDir = (IconThemeDir)it.next();
				if (aDir.getContext() == context)
					filename = aDir.lookupIcon(icon);
			}
		else
			while ( it.hasNext() && (filename == null) ) {
				IconThemeDir aDir = (IconThemeDir)it.next();
				if ( (aDir.getSize() == size) && (aDir.getContext() == context) )
					filename = aDir.lookupIcon(icon);
			}
		if (filename != null)
			return filename;
		
		return lookupIconFirst(icon);
	}

	protected KIcon lookupIconFirst(String icon) {
		Iterator it = childs.iterator();
		KIcon filename = null;
		while ( it.hasNext() && (filename == null) )
			filename = ((IconThemeDir)it.next()).lookupIcon(icon);
		return filename;
	}

	protected void importDirs(String dirs) {
		if ( (dirs != null) && (dirs.length() > 0) ) {
			String[] dirArray = dirs.split(",");
			for (int i = 0; i < dirArray.length; i++) {
				IconThemeDir dir = new IconThemeDir(dirArray[i]);
				if (dir.isValid())
					addDir(dir);
			}
		}
	}

	protected void addDir(IconThemeDir dir) {
		childs.add(dir);
	}

	class IconThemeDir
	{
		protected String dirName;
		protected int size = -1;
		protected int context = -1;

		public IconThemeDir(String dirName) {
			this.dirName = dirName;
			if (dirName != null) {
				guessSize();
				guessContext();
			}
		}

		public boolean isValid() {
			return ( (size > 0) && (context > -1) );
		}

		public int getContext() {
			return size;
		}

		public int getSize() {
			return context;
		}

		public File getDir() {
			return new File(path, dirName);
		}

		public String getDirName() {
			return dirName;
		}

		protected KIcon lookupIcon(String icon) {
			File iconFile = new File(getDir(), icon + ".png");
			if (iconFile.exists() && iconFile.canRead() && iconFile.isFile())
				return new KIcon(size, context, iconFile.getAbsolutePath());
			return null;
		}

		protected void guessSize() {
			int cutIndex = dirName.indexOf("x");
			if (cutIndex > -1) {
				try {
					size = Integer.parseInt(dirName.substring(0, cutIndex));
				}
				catch (NumberFormatException e) {
				}
			}
		}

		protected void guessContext() {
			int cutIndex = dirName.lastIndexOf("/");
			if (cutIndex > -1) {
				String strContext = dirName.substring(cutIndex + 1);
				if (strContext.equals("actions"))
					context = KIcon.C_ACTION;
				else if (strContext.equals("apps"))
					context = KIcon.C_APPLICATION;
				else if (strContext.equals("devices"))
					context = KIcon.C_DEVICE;
				else if (strContext.equals("filesystems"))
					context = KIcon.C_FILESYSTEM;
				else if (strContext.equals("mimetypes"))
					context = KIcon.C_MIMETYPE;
				else
					context = KIcon.C_ANY;
			}
		}
		
		public String toString() {
			return "IconThemeDir["+dirName+","+size+","+KIcon.contextToString(context)+"]";
		}
	}
}


class KIconFallback extends KIcon implements Icon
{
	protected int width;
	protected int height;
	
	/**
	 * Constructor.
	 * @param name The name of the icon.
	 * @param size The size in pixel.
	 * @param context The context (KIcon.C_ANY, KIcon.C_ACTION, KIcon.C_APPLICATION, KIcon.C_DEVICE, KIcon.C_FILESYSTEM or KIcon.C_MIMETYPE).
	 */
	public KIconFallback(String name, int size, int context) {
		super(size, context, "/fallbackKIcon/" + name + ".png");
		guessDimension();
	}
	
	/**
	 * Constructor.
	 * @param name The name of the icon.
	 * @param context The context (KIcon.C_ANY, KIcon.C_ACTION, KIcon.C_APPLICATION, KIcon.C_DEVICE, KIcon.C_FILESYSTEM or KIcon.C_MIMETYPE).
	 */
	public KIconFallback(String name, int context) {
		super(S_16x16, context, "/fallbackKIcon/" + name + ".png");
		guessDimension();
	}
	
	/**
	 * Constructor.
	 * @param name The name of the icon.
	 */
	public KIconFallback(String name) {
		super(S_16x16, KIcon.C_ANY, "/fallbackKIcon/" + name + ".png");
		guessDimension();
	}	
	
	protected void guessDimension() {
		switch (size) {
			case S_16x16:
				width = 16;
				height = 16;
				break;
			case S_22x22:
				width = 22;
				height = 22;
				break;
			case S_32x32:
				width = 32;
				height = 32;
				break;
			case S_48x48:
				width = 48;
				height = 48;
				break;
			case S_64x64:
				width = 64;
				height = 64;
				break;
			case S_128x128:
				width = 128;
				height = 128;
				break;
			default:
				double sqrt = Math.sqrt((double)size);
				int rSqrt = (int)Math.ceil(sqrt);
				width = rSqrt;
				height = rSqrt;
		}
	}
	
	/**
	 * Returns true if the icon exist and is loadable.
	 * @return true if the icon exist and is loadable.
	 */
	public boolean isValid() {
		return true;
	}
	
	/**
	 * Returns the icon.
	 * @return the icon.
	 */
	public Icon load() {
		return this;
	}
	
	/**
	 * Returns the icon or null if not valid.
	 * @return cache An icon cache.
	 * @return the icon or null if not valid.
	 */
	public Icon load(Cache cache) {
		return this;
	}
	
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
}
