package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*; 
import javax.swing.*; 
import javax.swing.filechooser.*;
/**
 * Provides presentation information about files and directories.
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
 public class KFileInfo extends FileView
 {
  /** Storage for cached icons. */
  protected Hashtable iconCache = new Hashtable();
  
  /**
  * Creates a new instance.
  */
  public KFileInfo()
  {
  }
  
  /**
  * Adds an icon to the cache, associating it with the given file/directory.
  *
  * @param f  the file/directory.
  * @param i  the icon.
  */
  public void cacheIcon(File f, Icon i)
  {
   iconCache.put(f, i);
  }
  
  /**
  * Clears the icon cache.
  */
  public void clearIconCache()
  {
   iconCache.clear();
  }
  
  /**
  * Retrieves the icon associated with the specified file/directory, if 
  * there is one.
  *
  * @param f  the file/directory.
  *
  * @return The cached icon (or <code>null</code>).
  */
  public Icon getCachedIcon(File f)
  {
   return (Icon)iconCache.get(f);
  }
  
  /**
  * Returns a description of the given file/directory.  In this 
  * implementation, the description is the same as the name returned by 
  * {@link #getName(File)}.
  *
  * @param f  the file/directory.
  *
  * @return A description of the given file/directory.
  */
  public String getDescription(File f)
  {
   return getName(f);
  }
  
  /**
  * Returns an icon appropriate for the given file or directory.
  *
  * @param f  the file/directory.
  *
  * @return An icon.
  */
  public Icon getIcon(File f)
  {
 /*  Icon val = getCachedIcon(f);
   if (val != null)
    return val;
   if (filechooser.isTraversable(f))
    val = directoryIcon;
   else
    val = fileIcon;
   cacheIcon(f, val);*/
   return null;
  }
  
  /**
  * Returns the name for the given file/directory.
  *
  * @param f  the file/directory.
  *
  * @return The name of the file/directory.
  */
  public String getName(File f)
  {
   return f.getName();
  }
  
  /**
  * Returns a localised description for the type of file/directory.
  *
  * @param f  the file/directory.
  *
  * @return A type description for the given file/directory.
  */
  public String getTypeDescription(File f)
  {/*
   if (filechooser.isTraversable(f))
    return "dir";
   else
    return "file";*/
	  return "type";
  }
  
  /**
  * Returns {@link Boolean#TRUE} if the given file/directory is hidden,
  * and {@link Boolean#FALSE} otherwise.
  *
  * @param f  the file/directory.
  *
  * @return {@link Boolean#TRUE} or {@link Boolean#FALSE}.
  */
  public Boolean isHidden(File f)
  {
   //return Boolean.valueOf(filechooser.getFileSystemView().isHiddenFile(f));
	  return Boolean.valueOf(false);
  }
 } 
