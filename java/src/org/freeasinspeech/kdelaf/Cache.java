package org.freeasinspeech.kdelaf;
	
import java.util.*;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class Cache
{
	/** The minimum size of the cache in byte. */
	protected static final int MIN_SIZE = 10240;
		
	protected long maxSize;
	protected long totalSize;
	protected HashMap data;
	protected TreeMap keys;
		
	public Cache() {
		init();
		maxSize = MIN_SIZE;
	}
	
	public Cache(long maxSize) {
		init();
		this.maxSize = Math.max(maxSize, MIN_SIZE);
	}
	
	protected void init() {
		data = new HashMap(100);
		keys = new TreeMap();
		totalSize = 0;
	}
	
	// in bytes
	public long getTotalSize() {
		return totalSize;
	}
	
	public int count() {
		return data.size();
	}
	
	public int maxDataSize() {
		return (int)((double)maxSize / 4.0);
	}
	
	protected void add(String key, CacheEntry ce) {
		totalSize += ce.size;
		data.put(key, ce);
		keys.put(new Long(ce.time), key);
		forceLimitations();
	}
	
	public void add(String key, Object obj, long size) {
		add(key, new CacheEntry(obj, size));
	}
	
	public Object get(String key) {
		CacheEntry ce = (CacheEntry)data.get(key);
		if (ce != null) {
			// updates the date
			keys.remove(new Long(ce.time));
			ce.updateTime();
			keys.put(new Long(ce.time), key);
			return ce.obj;
		}
		else
			return null;
	}
	
	public void remove(String key) {
		CacheEntry ce = (CacheEntry)data.remove(key);
		keys.remove(new Long(ce.time));
		totalSize -= ce.size;
	}
	
	public void clear() {
		keys.clear();
		data.clear();
	}
	
	protected void forceLimitations() {
		while (totalSize > maxSize) {
			remove((String)keys.get(keys.firstKey()));
		}
	}
	public long getTime() {
		return (new Date()).getTime();
	}
	
	class CacheEntry
	{
		public Object obj;
		public long size;
		public long time;
		CacheEntry(Object obj, long size, long time) {
			this.obj = obj;
			this.size = size;
			this.time = time;
		}
		CacheEntry(Object obj, long size) {
			this.obj = obj;
			this.size = size;
			updateTime();
		}
		public long updateTime() {
			time = (new Date()).getTime();
			return time;
		}
	}
} 
