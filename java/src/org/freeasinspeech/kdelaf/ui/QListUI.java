package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.event.*;
 
/**
 * Class QListUI
 * ListUI for KDE look & feel.
 * 
 * @author sekou.diakite@fais.org
 */
public class QListUI extends BasicListUI {
 
 public QListUI() {
  super();
 }
 /**
 * Returns an instance of <code>QTextFieldUI</code>.
 * @return an instance of <code>QTextFieldUI</code>.
 */
 public static ComponentUI createUI(JComponent c) {
  return new QListUI();
 }
 /**
 * Installs various default settings (mostly colors) from the {@link
 * UIDefaults} into the {@link JList}
 *
 * @see #uninstallDefaults
 */
 protected void installDefaults() {
  super.installDefaults();
  list.setCursor(new Cursor(Cursor.HAND_CURSOR));
 }
 
 /**
 * Resets to <code>null</code> those defaults which were installed in 
 * {@link #installDefaults}
 */
 protected void uninstallDefaults() {
  super.uninstallDefaults();
  list.setCursor(null);
 }
}
