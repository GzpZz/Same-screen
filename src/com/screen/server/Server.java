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
		//׼�����ݡ�
		byte []srcImgData = ImgUtils.getZipScreenImg();
		System.out.println("��ǰͼƬ�ܴ�С��"+(srcImgData.length/1024)+"KB");
		//�����ݽ���ѹ��
		srcImgData = GZipUtils.compress(srcImgData);
		System.out.println("ѹ����ͼƬ�ܴ�С��"+(srcImgData.length/1024)+"KB");
		//���ݷֿ顣
		int times = srcImgData.length / bufsize; 
		if( srcImgData.length % bufsize != 0  ) {
			times += 1;
		}
		
		for(int i=0;i<times;i++) {
			bout.reset();//
			//����Э�飺�������ݡ�
			//1����֡ͷ��0x66
			byte [] header = {0x66};
			bout.write(header);
			//2���͵�ǰ֡��  
			byte []zhenCur = {(byte)(i+1)};
			bout.write(zhenCur);
			//3������֡��  
			byte []tzhenCur = {(byte)times};
			bout.write(tzhenCur);
			
			//4 �������ݲ��ֵ�md5ֵ��
			byte [ ] md5datas = null;
			int length = 0;//ÿһ�η��͵����ݳ��ȡ�
			if(i < times - 1 ) {//7
				length = bufsize;
			}else {//���һ�Ρ�
				length = srcImgData.length - i*bufsize;//7
			}
			//�����ݼ���MD5   32 bit
			md5datas = MD5Utils.md5_32(srcImgData,i*bufsize,length).getBytes();
			bout.write( md5datas );
			
			//5 ���͵�ǰ֡�����ݲ��ֳ��ȡ�   4 bit
			byte [ ] datalength = IntUtils.intToBytes( length );
			bout.write( datalength );
			 
			//6���ݲ��� 
			bout.write( srcImgData,i*bufsize,length );
			//ͳһ���͡�
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
				bout.write(0x00);//��0
			}
			alldatas = bout.toByteArray();
			//System.out.println("���ˣ�"+rest+"��0");
		}
		System.out.println("���ݣ�"+alldatas.length);
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
