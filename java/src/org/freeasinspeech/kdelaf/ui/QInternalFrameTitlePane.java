package org.freeasinspeech.kdelaf.ui;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;

/**
 * Class QInternalFrameTitlePane
 * This class acts as a titlebar for JInternalFrames.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class QInternalFrameTitlePane extends BasicInternalFrameTitlePane {
	protected static KTitleStatics images = new KTitleStatics();
   
	/**
	 * Creates a new QInternalFrameTitlePane object that is used in the
	 * given JInternalFrame.
	 *
	 * @param f The JInternalFrame this QInternalFrameTitlePane will be used
	 *        in.
	 */
	public QInternalFrameTitlePane(JInternalFrame f) {
		super(f);
	}
	/**
	 * This method paints the TitlePane.
	 * @param g The Graphics object to paint with.
	 */
	public void paintComponent(Graphics g) {
		Utilities.configure(g);
		super.paintComponent(g);
	}
 /*
	private void setIcons(AbstractButton b, int JSC) {
		b.setIcon(new ImageIcon(images.getNormalImage(JSC)));
		b.setPressedIcon(new ImageIcon(images.getPressedImage(JSC)));
	}
 */
	private void setImages(KTitleButton b, int JSC) {
		b.setImage(images.getNormalImage(JSC));
		b.setPressedImage(images.getPressedImage(JSC));
		b.setRolloverImage(images.getRolloverImages(JSC));
	}
	/**
	 * This method creates the buttons used in the TitlePane.
	 */
	protected void createButtons() {
		/*super.createButtons();
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setText(null);
		iconButton.setContentAreaFilled(false);
		iconButton.setBorderPainted(false);
		iconButton.setText(null);
		maxButton.setContentAreaFilled(false);
		maxButton.setBorderPainted(false);
		maxButton.setText(null);*/
		maxButton = new KTitleButton();
		maxButton.addActionListener(maximizeAction);
		iconButton = new KTitleButton();
		iconButton.addActionListener(iconifyAction);
		closeButton = new KTitleButton();
		closeButton.addActionListener(closeAction);
		setButtonIcons();
	}
	
	protected void setButtonIcons() {
		super.setButtonIcons();
		if(frame.isIcon()) {
			setImages((KTitleButton)iconButton, KDEConstants.JSC_TitleBarShadeButton);
			setImages((KTitleButton)maxButton, KDEConstants.JSC_TitleBarMaxButton);
		} else if (frame.isMaximum()) {
			setImages((KTitleButton)iconButton, KDEConstants.JSC_TitleBarMinButton);
			setImages((KTitleButton)maxButton, KDEConstants.JSC_TitleBarNormalButton);
		} else {
			setImages((KTitleButton)iconButton, KDEConstants.JSC_TitleBarMinButton);
			setImages((KTitleButton)maxButton, KDEConstants.JSC_TitleBarMaxButton);
		}
		setImages((KTitleButton)closeButton, KDEConstants.JSC_TitleBarCloseButton);
	}
	
	protected LayoutManager createLayout() {
		return new KTitleLayout();
	}
	
	class KTitleLayout implements LayoutManager
	{
		int wSeparator = 2;
		int hSeparator = 2;
		public void addLayoutComponent(String name, Component c) {}
		public void removeLayoutComponent(Component c) {}
		public Dimension preferredLayoutSize(Container c)  {
			return minimumLayoutSize(c);
		}
		public Dimension minimumLayoutSize(Container c) {
			Dimension minimum = new Dimension(0, 0);
			String title = frame.getTitle();
			Icon icon = frame.getFrameIcon();
			Dimension iconButtonDim = (iconButton == null) ? new Dimension(0, 0) : iconButton.getPreferredSize();
			Dimension maxButtonDim = (maxButton == null) ? new Dimension(0, 0) : maxButton.getPreferredSize();
			Dimension closeButtonDim = (closeButton == null) ? new Dimension(0, 0) : closeButton.getPreferredSize();
			Dimension iconDim = (icon == null) ? new Dimension(0, 0) : new Dimension(icon.getIconWidth(), icon.getIconHeight());
			title = (title == null) ? "foo" : title;
			FontMetrics fontMetrics = frame.getFontMetrics(getFont());
			
			minimum.height = Math.max(
				iconDim.height, 
				Math.max(
					maxButtonDim.height,
					Math.max(
						closeButtonDim.height,
						Math.max(
							iconDim.height,
							fontMetrics.getHeight()
						)
					)
				)
			);
			if (frame.isClosable())
				minimum.width += closeButtonDim.width;
			if (frame.isMaximizable())
				minimum.width += maxButtonDim.width;
			if (frame.isIconifiable())
				minimum.width += iconDim.width;
			if (icon != null)
				minimum.width += iconDim.width + wSeparator;
			minimum.width += fontMetrics.stringWidth(title) + wSeparator * 2;
			
			return minimum;
		}
		public void layoutContainer(Container c) {
			int width = getWidth();
			int height = getHeight();
			Icon icon = frame.getFrameIcon();
			Dimension iconButtonDim = (iconButton == null) ? new Dimension(0, 0) : iconButton.getPreferredSize();
			Dimension maxButtonDim = (maxButton == null) ? new Dimension(0, 0) : maxButton.getPreferredSize();
			Dimension closeButtonDim = (closeButton == null) ? new Dimension(0, 0) : closeButton.getPreferredSize();
			Dimension iconDim = (icon == null) ? new Dimension(0, 0) : new Dimension(icon.getIconWidth(), icon.getIconHeight());		
			
			int x = wSeparator;
			menuBar.setBounds(x, (height - iconDim.height) / 2, iconDim.width, iconDim.height);
			x = width - closeButtonDim.width - wSeparator;
			
			if (frame.isClosable()) {
				closeButton.setBounds(x, (height - closeButtonDim.height) / 2, closeButtonDim.width, closeButtonDim.height);
				x -= closeButtonDim.width;
			}
			if (frame.isMaximizable()) {
				maxButton.setBounds(x, (height - maxButtonDim.height) / 2, maxButtonDim.width, maxButtonDim.height);
				x -= maxButtonDim.width;
			}
			if (frame.isIconifiable())
				iconButton.setBounds(x, (height - iconButtonDim.height) / 2, iconButtonDim.width, iconButtonDim.height);
		}
	}
	
	
	class KTitleButton extends JButton
	{
	      protected int w = -1;
	      protected int h = -1;
			
		protected BufferedImage image;
		protected BufferedImage pressedImage;
		protected BufferedImage rolloverImage;
			
		public KTitleButton() {
			super();
			setBorderPainted(false);
			setContentAreaFilled(false);
			setMargin(new Insets(0, 0, 0, 0));
			setFocusPainted(false);
			setOpaque(false);
			setText(null);
			setIcon(null);
		}
		
		public void requestFocus() {};
		/**
		 * This method returns true if the Component can be focused.
		 * @return false.
		 */
		public boolean isFocusable() {
			// These buttons cannot be given focus.
			return false;
		}
		
		
		public Dimension getPreferredSize() {
			if (w < 0)
				return super.getPreferredSize();
			return new Dimension(w, h);
		}
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
		
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}
		
		public void setImage(BufferedImage image) {
			this.image = image;
			w = image.getWidth();
			h = image.getHeight();
		}
		
		public void setPressedImage(BufferedImage pressedImage) {
			this.pressedImage = pressedImage;
		}
		
		public void setRolloverImage(BufferedImage rolloverImage) {
			this.rolloverImage = rolloverImage;
		}
		
		public void paint(Graphics g) {
			BufferedImage toDraw = null;
			if (getModel().isPressed())
				toDraw = pressedImage;
			else if (getModel().isRollover())
				toDraw = rolloverImage;
			if (toDraw == null)
				toDraw = image;
			if (toDraw != null)
				g.drawImage(toDraw, 0, 0, this);
		}
	}
}
class KTitleStatics
{
	protected BufferedImage[] normalImages = null;
	protected BufferedImage[] pressedImages = null;
	protected BufferedImage[] rolloverImages = null;
		
	public KTitleStatics() {
	}
	
	private int toArrayIndex(int JSC) {
		return JSC - KDEConstants.JSC_MIN;
	}
	
	public BufferedImage getNormalImage(int JSC) {
		int index = toArrayIndex(JSC);
		if ( (normalImages == null) || (normalImages[index] == null) ) {
			if (normalImages == null)
				normalImages = new BufferedImage[KDEConstants.JSC_LEN];
			try {
				normalImages[index] = KdeLAF.getClient().getTitleButtonImage(JSC, false, false);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				normalImages[index] = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
			}
		}
		return normalImages[index];
	}
	
	public BufferedImage getPressedImage(int JSC) {
		int index = toArrayIndex(JSC);
		if ( (pressedImages == null) || (pressedImages[index] == null) ) {
			if (pressedImages == null)
				pressedImages = new BufferedImage[KDEConstants.JSC_LEN];
			try {
				pressedImages[index] = KdeLAF.getClient().getTitleButtonImage(JSC, true, false);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				pressedImages[index] = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
			}
		}
		return pressedImages[index];
	}
	
	public BufferedImage getRolloverImages(int JSC) {
		int index = toArrayIndex(JSC);
		if ( (rolloverImages == null) || (rolloverImages[index] == null) ) {
			if (rolloverImages == null)
				rolloverImages = new BufferedImage[KDEConstants.JSC_LEN];
			try {
				rolloverImages[index] = KdeLAF.getClient().getTitleButtonImage(JSC, false, true);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				rolloverImages[index] = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
			}
		}
		return rolloverImages[index];
	}
}