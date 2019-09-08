package com.screen.utils;
import java.io.*;  
import java.util.Date;
import java.util.zip.GZIPOutputStream;
import java.awt.*;  
import java.awt.image.*;  
import javax.imageio.ImageIO;  
import com.sun.image.codec.jpeg.*;

import net.coobird.thumbnailator.Thumbnails;  

public class ImgUtils {  
	private static ByteArrayOutputStream bout = new  ByteArrayOutputStream( 300 * 1024 );
    public static byte[]  getZipScreenImg() {
    	bout.reset();
    	try {
			ImageIO.write( ScreenUtils.cutScreen2() , "JPEG", bout);
			byte[]alldatas = bout.toByteArray();
	    	ByteArrayInputStream bin = new ByteArrayInputStream( alldatas );
	    	bout.reset();
	    	Thumbnails.of( bin  ).size(1024, 768).toOutputStream(bout);
			bin.close();
    	} catch (IOException e) {
			e.printStackTrace();
		}
		return bout.toByteArray();
	}
    public static void main(String[] args) throws Exception {
    	ByteArrayOutputStream bufout = new ByteArrayOutputStream();
    	byte [] alldatas = fileToArray("aa_jpg_bmp_1024_768.jpeg");
    	System.out.println("压缩前:"+alldatas.length);
    	bout.reset();
    	GZIPOutputStream zout = new GZIPOutputStream(  bufout );
    	zout.write( alldatas );//
    	alldatas = bufout.toByteArray();//压缩后的数据放到bufout。
    	zout.close();
    	System.out.println("压缩后:"+alldatas.length);
	}
    private static byte[] fileToArray(String file) throws Exception {
    	File f = new File(file);
    	FileInputStream fin = new FileInputStream(f);
    	byte [] bs = new byte[(int) f.length()];
    	fin.read(bs);
    	fin.close();
    	return bs;
    	
    	
    }
}  
