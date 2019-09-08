package com.utils;

public class IntUtils {
	public static byte [] intToBytes(int num) {
		byte []bs = new byte[4];
		bs[0] = (byte) ( ( num>>(8*0) ) & 0xFF);
		bs[1] = (byte) ( ( num>>(8*1) ) & 0xFF);
		bs[2] = (byte) ( ( num>>(8*2) ) & 0xFF);
		bs[3] = (byte) ( ( num>>(8*3) ) & 0xFF);
		return bs;
	}
	
	public static int bytesToInt(byte []src,int offset,int length) {
		byte [] dest = new byte[4];
		System.arraycopy(src, offset, dest, 0, 4);
		return bytesToInt(dest);
	}
	public static int bytesToInt(byte []bs) {
		int total = 0;
		total +=( (bs[3]<0? bs[3]  + 256 : bs[3] )<< (8*3) );
		total +=( (bs[2]<0? bs[2]  + 256 : bs[2] )<< (8*2) );
		total +=( (bs[1]<0? bs[1]  + 256 : bs[1] )<< (8*1) );
		total +=( (bs[0]<0? bs[0]  + 256 : bs[0] )<< (8*0) );
		return total;
	}
}
