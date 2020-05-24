package org.freeasinspeech.kdelaf.ui;
 
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.freeasinspeech.kdelaf.*;
import org.freeasinspeech.kdelaf.client.*;
 
 /**
 * Class KScrollBarUI
 * ScrollBarUI for KDE look & feel.
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KScrollBarUI extends ScrollBarUI implements MouseListener, MouseMotionListener, MouseWheelListener, PropertyChangeListener, ChangeListener, ComponentListener
{
	/** Stores the static data of an horizontal scrollbar. */
	protected static KScrollBarStatics horiz = new KScrollBarStatics(true);
	/** Stores the static data of an vertical scrollbar. */
	protected static KScrollBarStatics verti = new KScrollBarStatics(false);
	/** The scrollbar metrics. */
	protected Rectangle[] metrics;
	/** The scrollbar. */
	protected JScrollBar scrollbar;	
	/** The timer used to move the thumb when the mouse is held. */
	protected Timer scrollTimer;
	/** The scroll listener. */
	protected ScrollListener scrollListener;
	/** The first scrollbar third button. */
	protected Rectangle thirdButton = null;
	/** true when the sub button(s) is(are) pressed. */
	protected boolean subPressed = false;
	/** true when the add button is pressed. */
	protected boolean addPressed = false;
	/** true when the page sub aread is pressed. */
	protected boolean pageSubPressed = false;
	/** true when the page add aread is pressed. */
	protected boolean pageAddPressed = false;
	/** true when the slider is rolled over by the mouse. */
	protected boolean sliderRollover = false;
	/** true when the slider is pressed. */
	protected boolean sliderPressed = false;
	/** The offset between the mouse pointer and the slider (when dragging). */
	protected int mouseSliderOffset = 0;
	/** The last mouse position. */
	protected Point lastMousePosition = new Point(0, 0);
	
	/** A metric constant. */
	protected static final int SubLine	= 0;
	/** A metric constant. */
	protected static final int AddLine	= 1;
	/** A metric constant. */
	protected static final int SubPage	= 2;
	/** A metric constant. */
	protected static final int AddPage	= 3;
	/** A metric constant. */
	protected static final int First	= 4;
	/** A metric constant. */
	protected static final int Last	= 5;
	/** A metric constant. */
	protected static final int Slider	= 6;
	/** A metric constant. */
	protected static final int Groove	= 7;
	/** Indicates that the scrolling direction is positive. */
	private static final int POSITIVE_SCROLL = 1;
	/** Indicates that the scrolling direction is negative. */
	private static final int NEGATIVE_SCROLL = -1;
		
	/**
	 * Returns an instance of <code>KScrollBarUI</code>.
	 * @return an instance of <code>KScrollBarUI</code>.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new KScrollBarUI();
	}
	
	/**
	 * This method installs the UI for the component. This can include setting
	 * up listeners, defaults,  and components. This also includes initializing
	 * any data objects.
	 * @param c The JComponent to install.
	 */
	public void installUI(JComponent c) {
		scrollbar = (JScrollBar)c;
		
		scrollTimer = new Timer(300, null);
		scrollListener = new ScrollListener();
		
		// listeners
		scrollbar.addMouseMotionListener(this);
		scrollbar.addMouseListener(this);
		scrollbar.addMouseWheelListener(this);
		scrollbar.addPropertyChangeListener(this);
		scrollbar.getModel().addChangeListener(this);
		scrollbar.addComponentListener(this);
		scrollTimer.addActionListener(scrollListener);
	}

	/**
	 * This method uninstalls the UI. This includes removing any defaults,
	 * listeners, and components that this UI may have initialized. It also
	 * nulls any instance data.
	 * @param c The Component to uninstall for.
	 */	
	public void uninstallUI(JComponent c) {
		// listeners
		if (scrollTimer != null)
			scrollTimer.removeActionListener(scrollListener);
		scrollbar.removeComponentListener(this);
		scrollbar.getModel().removeChangeListener(this);
		scrollbar.removePropertyChangeListener(this);
		scrollbar.removeMouseWheelListener(this);
		scrollbar.removeMouseListener(this);
		scrollbar.removeMouseMotionListener(this);
		
		scrollbar = null;
		scrollListener = null;
		scrollTimer = null;
	}
	
	/**
	 * This method returns the maximum size for this JComponent.
	 * @param c The JComponent to measure the maximum size for.
	 * @return The maximum size for the component.
	 */
	public Dimension getMaximumSize(JComponent c) {
		Dimension max = getPreferredSize(c);
		if (scrollbar.getOrientation() == SwingConstants.HORIZONTAL)
			max.width = Integer.MAX_VALUE;
		else
			max.height = Integer.MAX_VALUE;
		return max;
	}
	
	/**
	 * This method returns the minimum size for this JComponent.
	 * @param c The JComponent to measure the maximum size for.
	 * @return The minimum size for the component.
	 */
	public Dimension getMinimumSize(JComponent c) {
		return (scrollbar.getOrientation() == SwingConstants.HORIZONTAL) ? horiz.getMinimumSize() : verti.getMinimumSize();
	}
	
	/**
	 * This method returns the preferred size for this JComponent.
	 * @param c The JComponent to measure the maximum size for.
	 * @return The preferred size for the component.
	 */
	public Dimension getPreferredSize(JComponent c) {
		return (scrollbar.getOrientation() == SwingConstants.HORIZONTAL) ? horiz.getPreferredSize() : verti.getPreferredSize();
	}

	/**
	 * Returns true if val is near to first than last.
	 * @param val The value to test.
	 * @param first The first value.
	 * @param last The last value.
	 */
	private boolean nearFirst(int val, int first, int last) {
		int diffFirst = Math.abs(val - first);
		int diffLast = Math.abs(val - last);
		return (diffFirst < diffLast);
	}
	
	/**
	 * Updates the slider metrics (+AddPage + SubPage).
	 */
	protected void updateSliderAndBlockMetrics() {
		try {
			int width = scrollbar.getWidth();
			int height = scrollbar.getHeight();
			boolean horizontal = (scrollbar.getOrientation() == SwingConstants.HORIZONTAL);
			int min = scrollbar.getMinimum();
			int max = scrollbar.getMaximum() - scrollbar.getModel().getExtent();
			int val = scrollbar.getValue();
			int pageStep = scrollbar.getBlockIncrement();
			Rectangle[] tmp = KdeLAF.getClient().getScrollBarMetrics(
				width, 
				height,
				horizontal,
				min,
				max,
				val,
				pageStep
			);
			metrics[SubPage] = tmp[SubPage];
			metrics[AddPage] = tmp[AddPage];
			metrics[Slider] = tmp[Slider];
			// deal with slider mouves and page up / down
			if ( ((pageSubPressed) || (pageAddPressed)) && metrics[Slider].contains(lastMousePosition) ) {
				if (pageSubPressed) {
					pageSubPressed = false;
					endScroll(NEGATIVE_SCROLL, false);
				}
				else if (pageAddPressed) {
					pageAddPressed = false;
					endScroll(POSITIVE_SCROLL, false);
				}
			}
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
	}
	
	/**
	 * Updates the scrollbar metrics.
	 */
	protected void updateScrollBarMetrics() {
		try {
			int width = scrollbar.getWidth();
			int height = scrollbar.getHeight();
			boolean horizontal = (scrollbar.getOrientation() == SwingConstants.HORIZONTAL);
			int min = scrollbar.getMinimum();
			int max = scrollbar.getMaximum() - scrollbar.getModel().getExtent();
			int val = scrollbar.getValue();
			int pageStep = scrollbar.getBlockIncrement();
			metrics = KdeLAF.getClient().getScrollBarMetrics(
				width, 
				height,
				horizontal,
				min,
				max,
				val,
				pageStep
			);
			thirdButton = null;
			if (horizontal) {
				// platinium
				if (nearFirst(metrics[SubLine].x, scrollbar.getWidth(), 0)) ;
				// next
				else if (nearFirst(metrics[AddLine].x, 0, scrollbar.getWidth())) ;
				// three button or windows
				else {
					int diff = metrics[AddLine].x - metrics[AddPage].x - metrics[AddPage].width;
					if (diff > 0) {
						metrics[AddLine].x -= diff;
						metrics[AddLine].width += diff;
					}
					
					// three button style
					if (nearFirst(metrics[AddLine].width, metrics[SubLine].width * 2, metrics[SubLine].width))
						thirdButton = new Rectangle(metrics[AddLine].x, metrics[AddLine].y, metrics[AddLine].width / 2, metrics[AddLine].height);
					// else windows style
				}
			}
			else {
				// platinium
				if (nearFirst(metrics[SubLine].y, scrollbar.getHeight(), 0)) ;
				// next
				else if (nearFirst(metrics[AddLine].y, 0, scrollbar.getHeight())) ;
				// three button or windows
				else {
					int diff = metrics[AddLine].y - metrics[AddPage].y - metrics[AddPage].height;
					if (diff > 0) {
						metrics[AddLine].y -= diff;
						metrics[AddLine].height += diff;
					}
					
					// three button style
					if (nearFirst(metrics[AddLine].height, metrics[SubLine].height * 2, metrics[SubLine].height))
						thirdButton = new Rectangle(metrics[AddLine].x, metrics[AddLine].y, metrics[AddLine].width, metrics[AddLine].height / 2);
					// else windows style
				}
			}
			/*
			info : the AddLine coordinates may be false (three button style),
			but the third button is tested first (so no need to update the AddLine coordinates).
			*/
		}
		catch (Exception e) {
			KdeLAF.onError(e);
			metrics = new Rectangle[8];
			for (int i = 0; i < metrics.length; i++)
				metrics[i] = new Rectangle(10, 10);
			thirdButton = null;
		}
	}

	/**
	 * This method is called when the component is painted.
	 * @param g The Graphics object to paint with.
	 * @param c The JComponent to paint.
	 */
	public void paint(Graphics g, JComponent c) {
		try {
			int width = scrollbar.getWidth();
			int height = scrollbar.getHeight();
			boolean horizontal = (scrollbar.getOrientation() == SwingConstants.HORIZONTAL);
			boolean enabled = scrollbar.isEnabled();
			int min = scrollbar.getMinimum();
			int max = scrollbar.getMaximum() - scrollbar.getModel().getExtent();
			int val = scrollbar.getValue();
			int pageStep = scrollbar.getBlockIncrement();
			BufferedImage img = KdeLAF.getClient().getScrollBarImage(
				width, 
				height,
				horizontal,
				enabled,
				min,
				max,
				val,
				pageStep,
				subPressed,
				addPressed,
				sliderPressed
			);
			g.drawImage(img, 0, 0, c);
		}
		catch (Exception e) {
			KdeLAF.onError(e);
		}
	}
	
	/**
	 * Sets the scrollbar value.
	 * @param value The new value.
	 */
	protected void setValue(int value) {
		scrollbar.setValue(value);
		updateSliderAndBlockMetrics();
	}

	/**
	 * The method scrolls the thumb by a block in the  direction specified.
	 * @param direction The direction to scroll.
	 */
	protected void scrollByBlock(int direction) {
		int offset = direction * Math.abs(scrollbar.getBlockIncrement(direction));
		setValue(scrollbar.getValue() + offset);
	}

	/**
	 * The method scrolls the thumb by a unit in the direction specified.
	 * @param direction The direction to scroll.
	 */
	protected void scrollByUnit(int direction) {
		int offset = direction * Math.abs(scrollbar.getUnitIncrement(direction));
		setValue(scrollbar.getValue() + offset);
	}
	
	/**
	 * Called when the user press one of the buttons.
	 * @param int direction The scroll direction.
	 * @param byBlock true if we must scroll by block
	 */
	protected void startScroll(int direction, boolean byBlock) {
		scrollTimer.stop();
		scrollListener.setScrollByBlock(byBlock);
		scrollListener.setDirection(direction);
		scrollTimer.setDelay(100);
		scrollTimer.start();
		scrollbar.repaint();
	}
	
	/**
	 * Called when the user release one of the buttons.
	 * One last step is done.
	 * @param int direction The scroll direction.
	 */
	protected void endScroll(int direction) {
		endScroll(direction, true);
	}
	
	/**
	 * Called when the user release one of the buttons.
	 * @param int direction The scroll direction.
	 * @param oneLastStep When true one last step is done.
	 */
	protected void endScroll(int direction, boolean oneLastStep) {
		scrollTimer.stop();
		scrollTimer.setDelay(300);
		if (oneLastStep)
			scrollByUnit(direction);
		scrollbar.repaint();
	}

	/**
	 * This method returns the value in the scrollbar's range given the coordinates.
	 * If the value is out of range, it will return the closest legal value.
	 * This is package-private to avoid an accessor method.
	 * @param pos The coordinates to calculate a value for.
	 * @return The value for the given coordinates.
	 */
	int valueForPosition(Point pos) {
		int val = 0;
		int min = scrollbar.getMinimum();
		int max = scrollbar.getMaximum() - scrollbar.getModel().getExtent();
		int range = max - min;
		int screenRange = 0;
		int screenVal = 0;
		if (scrollbar.getOrientation() == SwingConstants.HORIZONTAL) {
			screenRange = metrics[Groove].width - metrics[Slider].width;
			screenVal = pos.x - metrics[Groove].x;
		}
		else {
			screenRange = metrics[Groove].height - metrics[Slider].height;
			screenVal = pos.y - metrics[Groove].y;
		}
		if (screenVal <= 0)
			val = min;
		else if (screenVal >= screenRange)
			val = max;
		else
			val = (int)Math.round((double)range * (double)screenVal / (double)screenRange);
		return val;
	}
	
	// *********** EVENTS ***********
	public void mouseClicked(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
		if (scrollbar.isEnabled()) {
			lastMousePosition = e.getPoint();
			if ( (thirdButton != null) && thirdButton.contains(e.getPoint()) ) {
				subPressed = true;
				startScroll(NEGATIVE_SCROLL, false);
			}
			else if (metrics[SubLine].contains(e.getPoint())) {
				subPressed = true;
				startScroll(NEGATIVE_SCROLL, false);
			}
			else if (metrics[AddLine].contains(e.getPoint())) {
				addPressed = true;
				startScroll(POSITIVE_SCROLL, false);
			}
			else if (metrics[Slider].contains(e.getPoint())) {
				sliderPressed = true;
				if (scrollbar.getOrientation() == SwingConstants.HORIZONTAL)
					mouseSliderOffset = e.getPoint().x - metrics[Slider].x;
				else
					mouseSliderOffset = e.getPoint().y - metrics[Slider].y;
				scrollbar.repaint();
			}
			else if (metrics[SubPage].contains(e.getPoint())) {
				pageSubPressed = true;
				startScroll(NEGATIVE_SCROLL, true);
			}
			else if (metrics[AddPage].contains(e.getPoint())) {
				pageAddPressed = true;
				startScroll(POSITIVE_SCROLL, true);
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (scrollbar.isEnabled()) {
			if (subPressed) {
				subPressed = false;
				endScroll(NEGATIVE_SCROLL);
			}
			else if (addPressed) {
				addPressed = false;
				endScroll(POSITIVE_SCROLL);
			}
			else if (pageSubPressed) {
				pageSubPressed = false;
				endScroll(NEGATIVE_SCROLL);
			}
			else if (pageAddPressed) {
				pageAddPressed = false;
				endScroll(POSITIVE_SCROLL);
			}
			else if (sliderPressed) {
				sliderPressed = false;
				scrollbar.repaint();
			}
		}
	}
	public void mouseEntered(MouseEvent e) {
		if (scrollbar.isEnabled() && metrics[Slider].contains(e.getPoint()))
			sliderRollover = true;
	}
	public void mouseExited(MouseEvent e) {
		if (scrollbar.isEnabled() && sliderRollover)
			sliderRollover = false;
	}
	public void mouseDragged(MouseEvent e) {
		if (scrollbar.isEnabled() && sliderPressed) {
			Point p = e.getPoint();
			if (scrollbar.getOrientation() == SwingConstants.HORIZONTAL)
				p.x -= mouseSliderOffset;
			else
				p.y -= mouseSliderOffset;
			setValue(valueForPosition(p));
		}
	}
	public void mouseMoved(MouseEvent e) {
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (scrollbar.isEnabled())
			scrollByBlock(e.getWheelRotation() < 0 ? NEGATIVE_SCROLL : POSITIVE_SCROLL);
	}
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals("model")) {
			((BoundedRangeModel)e.getOldValue()).removeChangeListener(this);
			scrollbar.getModel().addChangeListener(this);
			updateScrollBarMetrics();
		}
	}
	public void stateChanged(ChangeEvent e) {
		updateScrollBarMetrics();
		scrollbar.repaint();
	}
	public void componentResized(ComponentEvent e) {
		updateScrollBarMetrics();
	}
	public void componentMoved(ComponentEvent e) {
	}
	public void componentShown(ComponentEvent e) {
	}
	public void componentHidden(ComponentEvent e) {
	}

	/**
	 * A helper class that listens for events from the timer that is used to
	 * move the thumb.
	 * @author gnu classpath
	 */
	protected class ScrollListener implements ActionListener
	{
		/** The direction the thumb moves in. */
		private transient int direction;

		/** Whether movement will be in blocks. */
		private transient boolean block;

		/**
		 * Creates a new ScrollListener object. The default is scrolling
		 * positively with block movement.
		 */
		public ScrollListener() {
			direction = POSITIVE_SCROLL;
			block = true;
		}

		/**
		 * Creates a new ScrollListener object using the given direction and block.
		 * @param dir The direction to move in.
		 * @param block Whether movement will be in blocks.
		 */
		public ScrollListener(int dir, boolean block) {
			direction = dir;
			this.block = block;
		}

		/**
		 * Sets the direction to scroll in.
		 * @param direction The direction to scroll in.
		 */
		public void setDirection(int direction) {
			this.direction = direction;
		}

		/**
		 * Sets whether scrolling will be done in blocks.
		 * @param block Whether scrolling will be in blocks.
		 */
		public void setScrollByBlock(boolean block) {
			this.block = block;
		}

		/**
		 * Called every time the timer reaches its interval.
		 * @param e The ActionEvent fired by the timer.
		 */
		public void actionPerformed(ActionEvent e) {
			if (block) {
				// Only need to check it if it's block scrolling
				// We only block scroll if the click occurs
				// in the track.
				/*if (!trackListener.shouldScroll(direction))
				{
					trackHighlight = NO_HIGHLIGHT;
					scrollbar.repaint();
					return;
				}*/
				scrollByBlock(direction);
			}
			else
				scrollByUnit(direction);
		}
	}
}

class KScrollBarStatics
{
	protected static final int HACK_MIN = 75;
	protected static final int HACK_PREFF = 100;
	public boolean horizontal;
	private Dimension minimumSize = null;
	private Dimension preferredSize = null;
		
	public KScrollBarStatics(boolean horizontal) {
		this.horizontal = horizontal;
	}
	
	public Dimension getMinimumSize() {
		if (minimumSize == null) {
			try {
				int key = horizontal ? org.freeasinspeech.kdelaf.client.KDEConstants.SZ_HSCROLLBARMIN : org.freeasinspeech.kdelaf.client.KDEConstants.SZ_VSCROLLBARMIN;
				minimumSize = KdeLAF.getClient().getSimplePreferredSize(key);
				if (horizontal)
					minimumSize.width = Math.max(minimumSize.width, HACK_MIN);
				else
					minimumSize.height = Math.max(minimumSize.height, HACK_MIN);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				minimumSize = new Dimension(0, 0);
			}
		}
		return minimumSize;
	}
	
	public Dimension getPreferredSize() {
		if (preferredSize == null) {
			try {
				int key = horizontal ? org.freeasinspeech.kdelaf.client.KDEConstants.SZ_HSCROLLBAR : org.freeasinspeech.kdelaf.client.KDEConstants.SZ_VSCROLLBAR;
				preferredSize = KdeLAF.getClient().getSimplePreferredSize(key);
				if (horizontal)
					preferredSize.width = Math.max(preferredSize.width, HACK_PREFF);
				else
					preferredSize.height = Math.max(preferredSize.height, HACK_PREFF);
			}
			catch (Exception e) {
				KdeLAF.onError(e);
				preferredSize = new Dimension(0, 0);
			}
		}
		return preferredSize;
	}
}
