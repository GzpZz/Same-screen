package com.screen.utils;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

public class ScreenUtils {
	private static int w;
	private static int h;
	private static int total =  100*1024;//缓冲区的默认大小。
	private static Dimension d;
	static {
		 d = Toolkit.getDefaultToolkit().getScreenSize();
	}
	public static byte [] cutScreen() {
		Robot rb;
		try {
			rb = new Robot();
			BufferedImage img = rb.createScreenCapture( new Rectangle( d ));
			ByteArrayOutputStream bout = new ByteArrayOutputStream( total );
			BufferedImage small_img = rb.createScreenCapture( new Rectangle( 1366, 768 ));
			Graphics g = small_img.getGraphics();
			g.drawImage(img, 0, 0, small_img.getWidth(), small_img.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);
			g.dispose();
			ImageIO.write(small_img, "jpeg", bout);
			return bout.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage cutScreen2() {
		Robot rb;
		try {
			rb = new Robot();
			BufferedImage img = rb.createScreenCapture( new Rectangle( d ));
			return  img;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
