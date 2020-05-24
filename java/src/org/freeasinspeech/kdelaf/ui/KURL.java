package org.freeasinspeech.kdelaf.ui;

import java.util.*;
import java.io.*;
import java.net.*;

import org.freeasinspeech.kdelaf.*; 

/**
 * 
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KURL
{
	protected URI uri;
	KURLTranslater translater;
		
	public KURL(String path) throws KURLSyntaxException {
		translater = new KURLTranslater();
		// patching the procol
		int indexOfSeparator = path.indexOf(":/");
		if (indexOfSeparator == -1)
			path = "file://" + path;
		try {
			uri = new URI(path);
		}
		catch (Exception e) {
			throw new KURLSyntaxException(e.toString());
		}
		if (!translater.isProtocolKnown(uri))
			throw new KURLSyntaxException("Unknown protocol : \"" + getProtocol() + "\"");			
	}
	
	public String getProtocol() {
		return uri.getScheme();
	}
	
	public String getPath() {
		return translater.getPath(uri);
	}
	
	public File getFile() {
		return translater.getFile(uri);
	}
	
	public String toString() {
		return uri.toString();
	}
	
	class KURLSyntaxException extends Exception
	{
		protected String msg;
		KURLSyntaxException() {
			msg = null;
		}
		KURLSyntaxException(String msg) {
			this.msg = msg;
		}
		public String toString() {
			if (msg != null)
				return msg;
			return "Unknwon error";
		}
	}
	
	protected class KURLTranslater
	{
		public boolean isProtocolKnown(URI uri) {
			String protocol = uri.getScheme();
			return (protocol.equals("file") || protocol.equals("media"));
		}
		
		public String getPath(URI uri) {
			String path = "";
			String protocol = uri.getScheme();
			
			if (protocol.equals("file"))
				path = uri.getPath();
			else if (protocol.equals("media"))
				path = "/media";
			
			return path;
		}
		
		public File getFile(URI uri) {
			return new File(getPath(uri));
		}
	}
}
