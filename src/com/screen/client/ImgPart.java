package com.screen.client;

import java.util.Arrays;

public class ImgPart {
	private byte header;
	private byte curz;
	private byte totalz;
	private  String md5;
	private int datalength;//
	private byte [] data;
	private boolean isOK;
	
	 
	 
	public boolean isOK() {
		return isOK;
	}
	public void setOK(boolean isOK) {
		this.isOK = isOK;
	}
	public byte getHeader() {
		return header;
	}
	public void setHeader(byte header) {
		this.header = header;
	}
	public byte getCurz() {
		return curz;
	}
	public void setCurz(byte curz) {
		this.curz = curz;
	}
	public byte getTotalz() {
		return totalz;
	}
	public void setTotalz(byte totalz) {
		this.totalz = totalz;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public int getDatalength() {
		return datalength;
	}
	public void setDatalength(int datalength) {
		this.datalength = datalength;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ImgPart [数据校验: "+ (isOK?"通过":"错误") +" header=" + Integer.toHexString( header ) + ", curz=" + curz + ", totalz=" + totalz + ", md5=" + md5
				+ ", datalength=" + datalength + "]";
	}
	
	
}
