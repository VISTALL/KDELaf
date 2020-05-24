package org.freeasinspeech.kdelaf.ui.kfile;
 
import java.util.*;
import java.io.*;

import org.freeasinspeech.kdelaf.ui.*;
import org.freeasinspeech.kdelaf.Logger;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class Execute extends Thread
{
	protected boolean ended;
	protected File workFolder;
	protected String error;
	protected String[] cmd;
	protected Process subProcess;
	protected int returnCode;
			
	public Execute(String[] cmd, File workFolder) {
		super();
		init(cmd, workFolder);
	}
			
	public Execute(String[] cmd) {
		super();
		init(cmd, null);
	}
	
	private void init(String[] cmd, File workFolder) {
		ended = false;
		error = null;
		subProcess = null;
		this.cmd = cmd;
		this.workFolder = workFolder;
	}
		
	public void run() {
		setEnded(false);
		returnCode = 0;
		try {
			if (workFolder != null)
				setProcess(Runtime.getRuntime().exec(cmd, null, workFolder));
			else
				setProcess(Runtime.getRuntime().exec(cmd));
			getProcess().waitFor();
			returnCode = getProcess().exitValue();
			setProcess(null);
		} catch (Exception e) {
			Logger.error(e);
			error = e.toString();
		}
		setEnded(true);
	}
	
	private synchronized void setEnded(boolean ended) {
		this.ended = ended;
	}
	private synchronized void setProcess(Process subProcess) {
		this.subProcess = subProcess;
	}
	private synchronized Process getProcess() {
		return subProcess;
	}
	
	public int waitTerminaison() {
		while (!hasEnded()) {
			try {
				Thread.sleep(10);
			}
			catch (Exception e) {
				Logger.error(e);
			}
		}
		return getReturnCode();
	}
	
	public synchronized boolean hasEnded() {
		return ended;
	}
	public int getReturnCode() {
		return returnCode;
	}
	public synchronized void kill() {
		if (subProcess != null)
			subProcess.destroy();
	}
	public boolean hasError() {
		return (error != null);
	}
	public String getError() {
		return error;
	}
} 
