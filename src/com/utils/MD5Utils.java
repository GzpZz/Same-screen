package com.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//a   0000 1010
//0a 
public class MD5Utils {
	 public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException {
	}
	 public static String md52( File file) throws NoSuchAlgorithmException {
		 //将大的数据拆分称多次update。
		  MessageDigest dg = MessageDigest.getInstance("md5");
		  try( FileInputStream fin = new FileInputStream(file);) {
			  byte []bs = new byte[1024];
				 while(true) {
					 int num = fin.read(bs);
					 if( num == -1)break;
					 dg.update(bs,0,num );
				 }
		  }catch (Exception e) {  
		  }
			
		  byte []rs = dg.digest();
		  return bytetostr(rs);
	 }
	//10>>1
	//file==>byte[]。
	public static String md5_322(File file) throws NoSuchAlgorithmException {
			ByteArrayOutputStream  bout = new ByteArrayOutputStream( );
		    byte []bs = new byte[1024];
			try(FileInputStream fin = new FileInputStream(file);) {
				while(true) {
					int num = fin.read(bs);
					if(num ==-1 )break;
					bout.write(bs, 0, num);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return md5( bout.toByteArray() );
	}
	//10>>1
	public static String md5_32(File file) throws NoSuchAlgorithmException {
		//file==>byte[]。
		byte []bigbyte = new byte[ 1024*10 ];//有可能需要动态扩容。记录当前有效数据的偏移量(游标)
		int posi = 0;//标记读了多少。
		byte []bs = new byte[1024];//20*1024*1024  30M
		try(FileInputStream fin = new FileInputStream(file);) {
			while(true) {
				int num = fin.read(bs);
				if(num ==-1 )break;
				//检验容量 
				if(posi + num > bigbyte.length ) {
					//扩容：创建一个新数组(数组长度*1.5)。复制。变量覆盖。
					byte []newatrr = new byte[bigbyte.length + bigbyte.length >> 1 ];
					System.arraycopy(bigbyte, 0, newatrr, 0, bigbyte.length );
					bigbyte = newatrr;
				}
				//将 bs[0]~bs[num] 传到 bigbyte。
				for(int i=0;i<num;i++) {
					bigbyte[posi+i] = bs[i];
				}
				posi +=num;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte []bss = new byte[posi];
		System.arraycopy(bigbyte, 0, bss, 0, posi);
		return md5(bss);
	}
	
	public static String md5_32(byte [] srcbyte) throws NoSuchAlgorithmException {
		return md5(srcbyte);
	}
	public static String md5_16(byte [] srcbyte) throws NoSuchAlgorithmException {
		return md5(srcbyte).substring(8, 24);
	}
	private static String md5(byte [] srcbyte) throws NoSuchAlgorithmException {
		MessageDigest dg = MessageDigest.getInstance("md5");
		byte [] rs = dg.digest(srcbyte);
		//-128~+127
		return bytetostr(rs);
	}
	
	public static String md5_32(byte [] srcbyte,int offset, int length) throws NoSuchAlgorithmException {
		MessageDigest dg = MessageDigest.getInstance("md5");
		dg.update(srcbyte, offset, length);
		byte [] rs = dg.digest();
		//-128~+127
		return bytetostr(rs);
	}
	
	private static String bytetostr(byte []bs) {
		StringBuilder sb = new StringBuilder();
		for(byte b:bs) {
			int a = b<0 ? 256 + b : b;//
			String str = Integer.toHexString(a);
			if(a<16) {
				sb.append("0");
			}
			sb.append(str);
		}
		 return sb.toString();
	}
	public static void toBinary() {
		int a = 1245654;
		byte b = 112;
		String str_2 = Integer.toBinaryString(a);//转成二进制字符串。
		System.out.println(str_2);
		//
		String str_16 = Integer.toHexString( b);
		System.out.println(str_16);
	}

}
