package com.screen.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.screen.utils.GZipUtils;
import com.utils.IntUtils;
import com.utils.MD5Utils;
/**
 * 只接收。
 * @author Administrator
 */

public class ReciveClient {
	private static int bufsize = 1024 *  20 ; 
	private static ByteArrayOutputStream bufout_small = new ByteArrayOutputStream(30*1024);
	private static ByteArrayOutputStream bufout_big = new ByteArrayOutputStream(1024*1024);
	private static ShowImgFrame view;
	public static void main(String[] args) throws Exception {
		view = new ShowImgFrame();
		MulticastSocket mcs = new MulticastSocket(8889);
		mcs.setReceiveBufferSize( 1024*1024 );
		mcs.joinGroup( InetAddress.getByName("224.0.0.5"));
		mcs.setLoopbackMode(false);
		try {
			receiveData(mcs);
		}catch (Exception es) {
			es.printStackTrace();
		}
	}

	private static void receiveData(MulticastSocket mcs) throws IOException, NoSuchAlgorithmException {
		  byte [] buf = new byte[bufsize+39];
		  LinkedList<ImgPart> onePicture = new LinkedList<ImgPart>();
		  boolean findfirstflag = false;
		  while(true) {
			  DatagramPacket  datapack = new DatagramPacket(buf,buf.length );
			  int receive_length = 0;
			  bufout_small.reset();
			  while(true) {
				  mcs.receive(datapack);
				  receive_length += datapack.getLength();
				  bufout_small.write(buf, 0, datapack.getLength() );//将数据写到缓冲区。
				  if( receive_length >= bufsize + 39 ) break;
			    }
			 
				 byte []data = bufout_small.toByteArray();
				 if( data[0] == 0x66) {
					 //帧头正确。
					 ImgPart p = new ImgPart();
					//帧头
					p.setHeader( data[0] );
					//当前帧
					p.setCurz( data[1]  );
					//总帧
					p.setTotalz( data[2] );
					//md5
					p.setMd5( new String(data, 3, 32));
					//数据长度    35  36 37 38
					p.setDatalength( IntUtils.bytesToInt(data,35, 4) );
					//
					//byte [] dest = new byte[ p.getDatalength() ];
				    //System.arraycopy( data ,39,dest, 0, dest.length);
					//数据校验
				   String md5str  = MD5Utils.md5_32( data,39, p.getDatalength());
				   p.setOK( md5str.equalsIgnoreCase( p.getMd5() ) );
				   System.out.println(p);
				   if(p.getCurz() ==1) {
					   findfirstflag = true;
				   }
				   //如果找到第一帧。缓存起来。
				   if( findfirstflag ) {
					   onePicture.add(p);
					   bufout_big.write(data,39, p.getDatalength());
				   }
				   if(p.getCurz() == p.getTotalz() ) {
					   //展示完整一张图片。
					   showOneCompletePicture(onePicture);
					   onePicture.clear();
					   findfirstflag = false;
					   bufout_big.reset();
				   }
				 }else {
					 onePicture.clear();
					 findfirstflag = false;
				 }
		  }
		  
	}
	/**
	 * 
	 * @param onePicture
	 */
	private static void showOneCompletePicture(LinkedList<ImgPart> onePicture) {
		//BufferedImage img = new BufferedImage(1366, 768, BufferedImage.TYPE_INT_RGB);
		byte [] bs = GZipUtils.extract( bufout_big.toByteArray() );
		ImageIcon icon = new ImageIcon(   bs );
		view.updateView(icon.getImage());
	}  
	
}
