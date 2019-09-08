package com.screen.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtils {
	private static ByteArrayOutputStream bout = new  ByteArrayOutputStream( 10 * 1024 );
	public static byte [] compress(byte [] bs,int offset,int length ) {
		bout.reset();
		//System.out.println("—πÀı«∞:  "+offset+","+length);
		try {
			GZIPOutputStream gzout = new GZIPOutputStream(bout);
			gzout.write(bs, offset, length);
			gzout.close();
			byte[] rs = bout.toByteArray();
			//System.out.println("—πÀı∫Û:"+rs.length);
			return rs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bs;
	}
	
	private static byte [] tempbs = new byte[1024*10];
	private static ByteArrayOutputStream bout2 = new  ByteArrayOutputStream( 10 * 1024 );
	public static byte[]extract(byte [] bs,int offset, int length){
		bout2.reset();
		ByteArrayInputStream bin = new ByteArrayInputStream(bs, offset, length);		
		GZIPInputStream gzin;
		try {
			gzin = new GZIPInputStream(bin);
			while(true) {
				int num = gzin.read(tempbs);
				if(num == -1) {
					break;
				}
				bout2.write(tempbs, 0, num );
			}
			gzin.close();
			return bout2.toByteArray();
			
		} catch (IOException e) {
			 return bout2.toByteArray();
		}
	}
	public static byte[] compress(byte[] srcImgData) {
		return compress(srcImgData,0,srcImgData.length);
	}
	public static byte[] extract(byte[] byteArray) {
		 
		return extract(byteArray,0,byteArray.length);
	}
 
	
}
