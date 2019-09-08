package com.screen.client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ShowImgFrame extends JFrame{
	private Image img;
	private  JPanel jp;
	public ShowImgFrame() {
		super.setSize(800, 800);
		jp = new JPanel() {
			 public void paint(Graphics g) {
				 if(img!=null)
				 g.drawImage(img, 0,0, this.getWidth(), this.getHeight(), null);
			 }
		 };
		 super.add(jp);
		 super.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		 super.setLocationRelativeTo(null);
		 super.setVisible(true);
	}
	
	public void updateView(Image img) {
		this.img = img;
		jp.repaint();
	}
}
