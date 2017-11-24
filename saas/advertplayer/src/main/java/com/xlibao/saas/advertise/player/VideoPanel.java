package com.xlibao.saas.advertise.player;

import java.awt.*;
import java.awt.peer.ComponentPeer;
import java.awt.image.MemoryImageSource;
import java.net.URL;

import javax.swing.JPanel;


public class VideoPanel extends JPanel{
	private static final long serialVersionUID = 4417214835406666167L;
	Player player;
	Canvas canvas;
	VideoPanel(Player pl){
		player=pl;
		setBackground(new Color(3,3,3));
		setLayout(new VideoLayout(player));
		canvas=new Canvas();
		add(canvas);
		canvas.setName("canvas");
		canvas.setBackground(new Color(3,3,3));
		//this.setVisible(true);
		//this.setDefaultCloseOperation(3);
		this.hideCursor();
	}

	long getWid(){
		long wid=-1;
		try {
			Class<?> cl = Class.forName("sun.awt.windows.WComponentPeer");
			java.lang.reflect.Field f = cl.getDeclaredField("hwnd");
			f.setAccessible(true);
			ComponentPeer peer = canvas.getPeer();
			wid = f.getLong(peer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wid;
	}

	public void hideCursor() {
//		Image image = Toolkit.getDefaultToolkit().createImage(
//				new MemoryImageSource(0, 0, new int[0], 0, 0));
//		this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(image,
//				new Point(0, 0), null));
//		URL classUrl = this.getClass().getResource("");
//		Image imageCursor = Toolkit.getDefaultToolkit().getImage(classUrl);
//		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
//				imageCursor,  new Point(0, 0), "cursor"));
		try {
			// 获取用户屏幕大小
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();

			Robot robot = new Robot();
			robot.mouseMove(0,(int)screenSize.getHeight());
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}


