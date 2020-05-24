package org.freeasinspeech.kdelaf.ui;

import java.awt.*;
import java.awt.image.*;

import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.awt.geom.*;
import java.util.Map;

public class ImageUtilities
{	
	public static final int ANGLE_0 = 0;
	public static final int ANGLE_90 = 90;
	public static final int ANGLE_180 = 180;
	public static final int ANGLE_270 = 270;
	
	public static BufferedImage rotate(BufferedImage img, int angle) {
		/*AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), img.getWidth() / 2, img.getHeight() / 2);
		AffineTransformOp atOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return atOp.filter(img, atOp.createCompatibleDestImage(img, null) );*/
		BufferedImage result;
		if ( (angle == ANGLE_0) || (angle == ANGLE_180) ) {
			result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			if (angle == ANGLE_0) {
				for (int i = 0; i < img.getWidth(); i++)
					for (int j = 0; j < img.getHeight(); j++)
						result.setRGB(i, j, img.getRGB(i, j));
			}
			else { // angle == ANGLE_180
				for (int i = 0; i < img.getWidth(); i++)
					for (int j = 0; j < img.getHeight(); j++)
						result.setRGB(img.getWidth() - i - 1, img.getHeight() - j - 1, img.getRGB(i, j));
			}
		}
		else { //(angle == ANGLE_90) || (angle == ANGLE_270)
			result = new BufferedImage(img.getHeight(), img.getWidth(), BufferedImage.TYPE_INT_ARGB);
			if (angle == ANGLE_90) {
				for (int i = 0; i < img.getWidth(); i++)
					for (int j = 0; j < img.getHeight(); j++)
						result.setRGB(img.getHeight() - j - 1, img.getWidth() - i - 1, img.getRGB(i, j));
			}
			else { // angle == ANGLE_270
				for (int i = 0; i < img.getWidth(); i++)
					for (int j = 0; j < img.getHeight(); j++)
						result.setRGB(j, i, img.getRGB(i, j));
			}
		}
		return result;
	}
	
}
