package com.screen.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import org.omg.Messaging.SyncScopeHelper;

import com.screen.client.ImgPart;
import com.screen.utils.GZipUtils;
import com.screen.utils.ImgUtils;
import com.screen.utils.ScreenUtils;
import com.utils.IntUtils;
import com.utils.MD5Utils;
public class Server {
	private static int bufsize = 1024 * 20 ; 
	public static void main(String[] args) throws Exception {
		MulticastSocket mcs = new MulticastSocket();
		mcs.setSendBufferSize( 500 *1024 );
		mcs.joinGroup( InetAddress.getByName("224.0.0.5"));
		InetSocketAddress addr = new InetSocketAddress("224.0.0.5", 8889);
		byte [] buf = new byte[1];
		DatagramPacket pack = new DatagramPacket(buf, buf.length,addr);
		while(true) {
			sendData(mcs,pack); 
		}
		
	}
	private static ByteArrayOutputStream bout = new ByteArrayOutputStream(bufsize);//
	private static void sendData(MulticastSocket mcs,DatagramPacket pack) throws IOException, NoSuchAlgorithmException {
		//准备数据。
		byte []srcImgData = ImgUtils.getZipScreenImg();
		System.out.println("当前图片总大小："+(srcImgData.length/1024)+"KB");
		//对数据进行压缩
		srcImgData = GZipUtils.compress(srcImgData);
		System.out.println("压缩后图片总大小："+(srcImgData.length/1024)+"KB");
		//数据分块。
		int times = srcImgData.length / bufsize; 
		if( srcImgData.length % bufsize != 0  ) {
			times += 1;
		}
		
		for(int i=0;i<times;i++) {
			bout.reset();//
			//按照协议：发送数据。
			//1发送帧头。0x66
			byte [] header = {0x66};
			bout.write(header);
			//2发送当前帧数  
			byte []zhenCur = {(byte)(i+1)};
			bout.write(zhenCur);
			//3发送总帧数  
			byte []tzhenCur = {(byte)times};
			bout.write(tzhenCur);
			
			//4 发送数据部分的md5值。
			byte [ ] md5datas = null;
			int length = 0;//每一次发送的数据长度。
			if(i < times - 1 ) {//7
				length = bufsize;
			}else {//最后一次。
				length = srcImgData.length - i*bufsize;//7
			}
			//对数据计算MD5   32 bit
			md5datas = MD5Utils.md5_32(srcImgData,i*bufsize,length).getBytes();
			bout.write( md5datas );
			
			//5 发送当前帧。数据部分长度。   4 bit
			byte [ ] datalength = IntUtils.intToBytes( length );
			bout.write( datalength );
			 
			//6数据部分 
			bout.write( srcImgData,i*bufsize,length );
			//统一发送。
			sendData(mcs,pack,bout);
			delay(100);
		}
	 
	}
	private static void sendData(MulticastSocket mcs,  DatagramPacket pack , ByteArrayOutputStream bout) throws IOException {
		byte [] alldatas = bout.toByteArray();
		int datalength = IntUtils.bytesToInt(alldatas,35, 4);
		if( datalength != bufsize) {
			int rest = bufsize - datalength;
			for( int i=0; i<rest; i ++ ) {
				bout.write(0x00);//补0
			}
			alldatas = bout.toByteArray();
			//System.out.println("补了："+rest+"个0");
		}
		System.out.println("数据："+alldatas.length);
		pack.setData( alldatas );
		mcs.send(pack);
	 
	}
	 
	private static void delay(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	 
	private  static byte []coutByteArrayMd5(byte[]src,int offset,int length){
		byte [] copydata = new byte[length];
		System.arraycopy(src, offset, copydata, 0, length);
		try {
			return MD5Utils.md5_32( copydata ).getBytes();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	 
}
