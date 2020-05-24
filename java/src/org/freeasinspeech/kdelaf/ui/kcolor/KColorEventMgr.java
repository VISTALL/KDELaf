package org.freeasinspeech.kdelaf.ui.kcolor;


import java.awt.Color;
import java.util.LinkedList;
import java.util.Iterator;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
class KColorEventMgr
{
	protected static LinkedList listeners = new LinkedList();
	
	public static void addColorEventListener(KColorEventListener l) {
		listeners.add(l);
	}
	
	public static void sendColorEvent(KColor c) {
		sendColorEvent(c, null);
	}
	
	public static void sendColorEvent(KColor c, KColorEventListener exception) {
		Iterator it = listeners.iterator();
		while (it.hasNext()) {
			KColorEventListener next = (KColorEventListener)it.next();
			if (next != exception)
				next.onColorSelection(new KColor(c));
		}
	}
	
	interface KColorEventListener
	{
		public void onColorSelection(KColor c);
	}
}
