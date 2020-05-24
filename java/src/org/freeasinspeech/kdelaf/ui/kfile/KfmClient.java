package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*;

import org.freeasinspeech.kdelaf.ui.*;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KfmClient
{	
	protected static String error = null;
		
	public static String lastError() {
		return error;
	}
	
	public static boolean moveToTrash(File src) {
		return moveToTrash(src.getAbsolutePath());
	}
	public static boolean moveToTrash(String src) {
		String[] tmp = new String[1];
		tmp[0] = src;
		return move(tmp, "trash:/");
	}
	public static boolean moveToTrash(File []src) {
		return move(src, "trash:/");
	}
	public static boolean moveToTrash(String []src) {
		return move(src, "trash:/");
	}
	public static boolean move(File []src, String dest) {
		String[] tmp = new String[src.length];
		for (int i = 0; i < src.length; i++)
			tmp[i] = src[i].getAbsolutePath();
		return move(tmp, "trash:/");
	}
	public static boolean move(String []src, String dest) {
		String[] cmd = new String[3 + src.length];
		cmd[0] = "kfmclient";
		cmd[1] = "move";
		for (int i = 0; i < src.length; i++)
			cmd[2 + i] = src[i];
		cmd[cmd.length - 1] = dest;
		return run(cmd);
	}
	private static boolean run(String[] cmd) {
		error = null;
		Execute exec = new Execute(cmd);
		exec.start();
		int returnCode = exec.waitTerminaison();
		error = exec.getError();
		return ( (returnCode == 0) && (error == null) );
	}
}
