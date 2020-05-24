package org.freeasinspeech.kdelaf.ui;
 
import java.util.*;
import java.io.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.freeasinspeech.kdelaf.ui.kfile.*;
import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
 
/**
 * Class KDEFileChooserUI
 * FileChooserUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class KDEFileChooserUI extends FileChooserUI  implements PropertyChangeListener {
 protected JFileChooser filechooser = null;
  /** An optional accessory panel. */
  JPanel accessoryPanel = new JPanel();
 
 public KDEFileChooserUI(JFileChooser filechooser) {
 }

/**
* Returns the optional accessory panel.
*
* @return The optional accessory panel.
*/
public JPanel getAccessoryPanel()
{
return accessoryPanel;
}

 /**
  * Installs the UI for the specified component.
  * 
  * @param c  the component (should be a {@link JFileChooser}).
  */
 public void installUI(JComponent c)
 {
  if (c instanceof JFileChooser)
  {
   JFileChooser fc = (JFileChooser) c;
   this.filechooser = fc;
   fc.resetChoosableFileFilters();
   //createModel();
   //clearIconCache();
   installComponents(fc);
    /*
   installListeners(fc);*/
   
   /*Object path = filechooser.getCurrentDirectory();
   if (path != null)
    parentPath = path.toString().substring(path.toString().lastIndexOf("/"));
    */
  }
 }
 
 /**
  * Uninstalls this UI from the given component.
  * 
  * @param c  the component (should be a {@link JFileChooser}).
  */
 public void uninstallUI(JComponent c)
 {
  //model = null;
  /*uninstallListeners(filechooser);*/
  uninstallComponents(filechooser);
  filechooser = null;
 }
 
 /**
  * Creates and install the subcomponents for the file chooser.
  *
  * @param fc  the file chooser.
  */
 public void installComponents(JFileChooser fc) {
		if (filechooser.getCurrentDirectory() == null) {
			filechooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		}
      filechooser.setFileHidingEnabled(!KFileConfig.getShowHiddenFiles());
  KFileView fileView = new KFileIconView(fc);
  KFileFavorites favorites = new KFileFavorites(fc);
  KFileCombos combos = new KFileCombos(fc);
  KFileToolBar tools = new KFileToolBar(fc);
  KFilePreview preview = new KFilePreview(fc);
  MyManager manager = new MyManager(tools, favorites, preview);
  
  fc.setLayout(new BorderLayout());
  getAccessoryPanel().add(preview);
  JPanel panKFileViewAndKFileCombos = new JPanel(new BorderLayout());
  panKFileViewAndKFileCombos.add(fileView, BorderLayout.CENTER);
  panKFileViewAndKFileCombos.add(combos, BorderLayout.SOUTH);
  fc.add(panKFileViewAndKFileCombos, BorderLayout.CENTER);
  fc.add(favorites, BorderLayout.WEST);
  fc.add(tools, BorderLayout.NORTH);
  fc.add(getAccessoryPanel(), BorderLayout.EAST);
  if (fc.getAccessory() != null)
    getAccessoryPanel().add(fc.getAccessory());
  fileView.reload();
  
  installListeners(fc);
  
  MyManager.init(fc);
 }

 /**
  * Creates and install the subcomponents for the file chooser.
  *
  * @param fc  the file chooser.
  */
 public void uninstallComponents(JFileChooser fc) {
  uninstallListeners(fc);
  KFileBase.freeKPopupMenu();
   //fc.remove();
 }
 /**
 * Installs the listeners required by this UI delegate.
 *
 * @param fc  the file chooser.
 */
 protected void installListeners(JFileChooser fc)
 {
  fc.addPropertyChangeListener(this);
 }
 
 /**
 * Uninstalls the listeners previously installed by this UI delegate.
 *
 * @param fc  the file chooser.
 */
 protected void uninstallListeners(JFileChooser fc)
 {
  fc.removePropertyChangeListener(this);
 }

 
 public void propertyChange(PropertyChangeEvent evt) {
  String n = evt.getPropertyName();
  if (n.equals(JFileChooser.ACCESSORY_CHANGED_PROPERTY)) {
   JComponent old = (JComponent)evt.getOldValue();
   if (old != null)
    getAccessoryPanel().remove(old);
   JComponent newval = (JComponent)evt.getNewValue();
   if (newval != null)
    getAccessoryPanel().add(newval);
  }
  MyManager.sendPropertyChangeEvent(n);
 }
 
 /**
  * Returns an instance of <code>QMenuBarUI</code>.
  * @return an instance of <code>QMenuBarUI</code>.
  */
 public static ComponentUI createUI(JComponent c) {
  return new KDEFileChooserUI((JFileChooser) c);
 }
 
 public javax.swing.filechooser.FileFilter getAcceptAllFileFilter(JFileChooser fc) {
  return new AcceptAllFileFilter();
 }
 
 public FileView getFileView(JFileChooser fc) {
  return new KFileInfo();
 }
 
 public String getApproveButtonText(JFileChooser fc) {
  String result = fc.getApproveButtonText();
  if (result == null)
  {
   if (fc.getDialogType() == JFileChooser.SAVE_DIALOG)
    result = KLocale.i18n("Save As");
   else
    result = KLocale.i18n("Open");
  }
  return result;
 }
 
 public String getDialogTitle(JFileChooser fc) {
  String result = fc.getDialogTitle();
  if (result == null)
   result = getApproveButtonText(fc);
  return result;
 }
 
 public void rescanCurrentDirectory(JFileChooser fc) {
  Logger.log("Todo : rescanCurrentDirectory");
 }
 
 public void ensureFileIsVisible(JFileChooser fc, File f) {
  Logger.log("Todo : ensureFileIsVisible");
 }
 
 /**
  * A file filter that accepts all files.
  */
 protected class AcceptAllFileFilter extends javax.swing.filechooser.FileFilter
 {
  /**
   * Creates a new instance.
   */
  public AcceptAllFileFilter()
  {
  // Nothing to do here.
  }
  
  /**
   * Returns <code>true</code> always, as all files are accepted by this
   * filter.
   *
   * @param f  the file.
   *
   * @return Always <code>true</code>.
   */
  public boolean accept(File f)
  {
   return true;
  }
  
  /**
   * Returns a description for this filter.
   *
   * @return A description for the file filter.
   */
  public String getDescription()
  {
   return "desc";
  }
 }

}

/*
public void propertyChange(PropertyChangeEvent e)
    {
      JFileChooser filechooser = getFileChooser();
      
      String n = e.getPropertyName();
      
      
      else if (n.equals(JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY))
        {
          filterModel.propertyChange(e);
        }
      else if (n.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY))
        {
          filterModel.propertyChange(e);
        }
        
                                        if (e.getPropertyName().equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY))
                                          {
                                            JFileChooser fc = getFileChooser();
                                            FileFilter[] choosableFilters = fc.getChoosableFileFilters();
                                            filters = choosableFilters;
                                            fireContentsChanged(this, 0, filters.length);
                                            selected = e.getNewValue();
                                            fireContentsChanged(this, -1, -1);
                                          }
                                        else if (e.getPropertyName().equals(
                                                JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY))
                                          {
                                            // repopulate list
                                            JFileChooser fc = getFileChooser();
                                            FileFilter[] choosableFilters = fc.getChoosableFileFilters();
                                            filters = choosableFilters;
                                            fireContentsChanged(this, 0, filters.length);
                                          }
      else if (n.equals(JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY)
                 || n.equals(JFileChooser.DIALOG_TITLE_CHANGED_PROPERTY))
        {
          Window owner = SwingUtilities.windowForComponent(filechooser);
          if (owner instanceof JDialog)
            ((JDialog) owner).setTitle(getDialogTitle(filechooser));
          approveButton.setText(getApproveButtonText(filechooser));
          approveButton.setToolTipText(
                  getApproveButtonToolTipText(filechooser));
          approveButton.setMnemonic(getApproveButtonMnemonic(filechooser));
        }
      
      else if (n.equals(JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY))
        approveButton.setText(getApproveButtonText(filechooser));
      
      else if (n.equals(
              JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY))
        approveButton.setToolTipText(getApproveButtonToolTipText(filechooser));
      
      else if (n.equals(JFileChooser.APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY))
        approveButton.setMnemonic(getApproveButtonMnemonic(filechooser));

      else if (n.equals(
              JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY))
        {
          if (filechooser.getControlButtonsAreShown())
            {
              topPanel.add(controls, BorderLayout.EAST);
            }
          else
            topPanel.remove(controls);
          topPanel.revalidate();
          topPanel.repaint();
          topPanel.doLayout();
        }
      
      else if (n.equals(
              JFileChooser.ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY))
        {
          if (filechooser.isAcceptAllFileFilterUsed())
            filechooser.addChoosableFileFilter(
                    getAcceptAllFileFilter(filechooser));
          else
            filechooser.removeChoosableFileFilter(
                    getAcceptAllFileFilter(filechooser));
        }
      
 
      if (
          || n.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY))
        {
          // Remove editing component
          if (fileTable != null)
            fileTable.removeAll();
          if (fileList != null)
            fileList.removeAll();
          startEditing = false;
          
          // Set text on button back to original.
          if (filechooser.getDialogType() == JFileChooser.SAVE_DIALOG)
            {
              directoryLabel = save;
              dirLabel.setText(directoryLabel);
              filechooser.setApproveButtonText(saveButtonText);
              filechooser.setApproveButtonToolTipText(saveButtonToolTipText);
            }
          
          rescanCurrentDirectory(filechooser);
        }
      
      filechooser.revalidate();
      filechooser.repaint();
    }
  };
  */