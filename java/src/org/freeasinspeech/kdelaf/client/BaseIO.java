package org.freeasinspeech.kdelaf.client;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.font.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.zip.*;


/**
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
class OutBuffer
{
	DataOutputStream out;
	
	public OutBuffer(Process p) {
		out = new DataOutputStream(p.getOutputStream());
	}
	
	public void writeInt(int val) {
		try {
			out.writeInt(val);
			out.flush();
		}
		catch (IOException e) {
			// not reachable
		}
	}
	
	public void writeChar(int val) {
		try {
			out.writeChar(val);
			out.flush();
		}
		catch (IOException e) {
			// not reachable
		}
	}
	
	public void writeByte(byte val) {
		try {
			out.writeByte(val);
			out.flush();
		}
		catch (IOException e) {
			// not reachable
		}
	}
	
	public void writeUnsignedByte(int val) {
		try {
			out.writeByte(val);
			out.flush();
		}
		catch (IOException e) {
			// not reachable
		}
	}
	
	public void writeBool(boolean val) {
		try {
			out.writeByte(val ? 1 : 0);
			out.flush();
		}
		catch (IOException e) {
			// not reachable
		}
	}
	
	public void writeChars(String val) {
		writeInt(val.length());
		try {
			out.writeChars(val);
			out.flush();
		}
		catch (IOException e) {
			// not reachable
		}
	}
	
	public void send()  throws IOException {
	}
}


/**
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
class InBuffer
{
	public static final int[] multArray = {16777216, 65536, 256, 1};
	protected DataInputStream data;
	protected DataOutputStream out;
	protected int lastImageSize;
	protected long lastImageTime;
	protected float screenRes;
		
	InBuffer(Process p) throws IOException {
		data = new DataInputStream(new BufferedInputStream(p.getInputStream()));
		out = new DataOutputStream(p.getOutputStream());
		screenRes = (float)Toolkit.getDefaultToolkit().getScreenResolution();
	}
	
	public void receive() throws IOException {
	}
	
	protected void synch() throws IOException {
		out.writeByte(1);
		out.flush();
	}
	
	public int readInt() throws IOException {
		return data.readInt();
	}
	
	public char readChar() throws IOException {
		return data.readChar();
	}
	
	public byte readByte() throws IOException {
		return data.readByte();
	}
	
	public int readUnsignedByte() throws IOException {
		return ((int)data.readByte() + 256) % 256;
	}
	
	public boolean readBool() throws IOException {
		return (data.readByte() == 1);
	}
	
	public String readString() throws IOException {
		String val = "";
		int len = readInt();
		for (int i = 0; i < len; i++)
			val += String.valueOf(readChar());
		return val;
	}
	
	public String[] readStrings() throws IOException {
		int len = readInt();
		String[] val = new String[len];
		for (int i = 0; i < len; i++)
			val[i] = readString();
		return val;
	}
	
	public Dimension readDimension() throws IOException {
		int width = readInt();
		int height = readInt();
		return new Dimension(width, height);
	}
	
	public Rectangle readRectangle() throws IOException {
		int x = readInt();
		int y = readInt();
		int width= readInt();
		int height = readInt();
		return new Rectangle(x, y, width, height);
	}
	
	public Color readColor() throws IOException {
		int r = readUnsignedByte();
		int g = readUnsignedByte();
		int b = readUnsignedByte();
		return new Color(r, g, b);
	}
	
	public Font readFont() throws IOException {
		String family = readString();
		float size = (float)readInt();
		boolean bold = readBool();
		boolean italic = readBool();
		size = size * screenRes / 72.0f;
		
		Map att = new HashMap();
		att.put(TextAttribute.FAMILY, family);
		att.put(TextAttribute.SIZE, new Float(size));
		if (bold)
			att.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		if (italic)
			att.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		return new Font(att);
	}
	
	protected byte[] readImageBytes(int numBytes) throws IOException {
		byte[] val = new byte[numBytes];
		int offset = 0;
		int tmpTotal = numBytes;
		long time = (new java.util.Date()).getTime();
		while (tmpTotal > KDEConstants.MAX_PACKET_SIZE) {
			data.readFully(val, offset, KDEConstants.MAX_PACKET_SIZE);
			synch();
			tmpTotal -= KDEConstants.MAX_PACKET_SIZE;
			offset += KDEConstants.MAX_PACKET_SIZE;
		}
		if (tmpTotal > 0)
			data.readFully(val, offset, tmpTotal);
		lastImageTime = (new java.util.Date()).getTime() - time;
		lastImageSize = numBytes;
		return val;
	}
	
	public int getLastImageSize() {
		return lastImageSize;
	}
	
	public long getLastImageTime() {
		return lastImageTime;
	}
	
	int unsigned(byte b) {
		return ((int)b + 256) % 256;
	}
	
	public BufferedImage readImage() throws IOException {
		int width = readInt();
		int height = readInt();
		int numBytes = width * height * 4;
		byte[] bytes = readImageBytes(numBytes);
		BufferedImage img = null;
		if (numBytes == 0) {
			img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
		else {
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			int[] rgbArray = new int[numBytes / 4];
			int iDot4;
			for (int i = 0; i < rgbArray.length; i++) {
				iDot4 = i * 4;
				rgbArray[i] = (new Color(unsigned(bytes[iDot4 + 1]), unsigned(bytes[iDot4 + 2]), unsigned(bytes[iDot4 + 3]), unsigned(bytes[iDot4]))).getRGB();
			}
			img.setRGB(0, 0, width, height, rgbArray, 0, width);
		}
		return img;
	}
	/*public BufferedImage readImage() throws IOException {
		return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	}*/
	
	public static boolean equalsIgnoreAlpha(int rgb1, int rgb2) {
		Color q1 = new Color(rgb1);
		Color q2 = new Color(rgb2);
		return ( (q1.getRed() == q2.getRed()) && (q1.getGreen() == q2.getGreen()) && (q1.getBlue() == q2.getBlue()));
	}
	
	public static BufferedImage applyTransparency(BufferedImage img, Color transparentColor) {
		if (transparentColor != null) {
			int transparentRGB = (new Color(0, 0, 0, 0)).getRGB();
			int iTransparentColor = transparentColor.getRGB();
			for (int i = 0; i < img.getWidth(); i++)
				for (int j = 0; j < img.getHeight(); j++) {
					if (equalsIgnoreAlpha(img.getRGB(i, j), iTransparentColor))
						img.setRGB(i, j, transparentRGB);
				}
		}
		return img;
	}
}

/**
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
class ServerListener extends Thread
{
	
	protected DataInputStream data;
	protected boolean doKill = false;
		
	
	public ServerListener(Process server) throws IOException {
		data = new DataInputStream(server.getErrorStream());
	}
	
	public void run() {
		String line;
		doKill = false;
		while (!doKill && true) {
			try {
				while ( !doKill && ( (line = data.readLine()) != null ) ) {
					int indexOfPipe = line.indexOf('|');
					if (indexOfPipe == 1) {
						char code = line.charAt(0);
						String msg = line.substring(2);
						switch (code) {
							case 'e':
								org.freeasinspeech.kdelaf.Logger.errorWorker(msg);
								break;
							case 'w':
								org.freeasinspeech.kdelaf.Logger.warningWorker(msg);
								break;
							default:
								org.freeasinspeech.kdelaf.Logger.logWorker(msg);
						}
					}
					else
						org.freeasinspeech.kdelaf.Logger.logWorker(line);
				}
				Thread.sleep(100);
			}
			catch (Exception e) {
				org.freeasinspeech.kdelaf.Logger.error(e);
			}
		}
	}
	
	public void kill() {
		doKill = true;
	}
}