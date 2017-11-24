package com.xlibao.saas.advertise.player;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import net.miginfocom.swing.MigLayout;

public class Player implements WindowListener{

	VideoPanel videoPalel;
	JPanel controlBar;
	JButton button_Open;
	JButton button_Play;
	JButton button_Stop;
	PlayerProgressBar progressBar;
	JSlider volumeSlider;

	Process proc ;
	Thread t1;
	Thread t2;

	int length=0;//视频长度
	int videoWidth;//视频宽
	int videoHeight;//视频高
	float rate=1.2f;//宽高比
	int playOffset=0;//当前播放时间
	int volume=100;//音量

	//播放器状态
	boolean isPlay=false;
	boolean isPause=false;

	private static final String MPLAYER_CONFIG=  "mplayer"+ File.separator + "mplayer.exe";//mplayer程序路径
	private static final String PLAY_LIST_CONFIG =  "play.lst";//视频播放列表文件
	private static  String mplayerPath;
	private static  String playListPath;

	String loop="0";//是否循环播放。-1不循环，0无限循环，N循环多少次
	String ss="";//从多少秒时间开始播放。格式：a.<30>，b.<00:10:00>
	String fs="-fs";//设置全屏

	/**
	 * 循环播放视频
	 */
	public void playVideo(){
		Player player = new Player().loadPanel();
		player.play(0,0);
	}
	/**
	 * 播放视频规则
	 * @param loop 循环播放次数:-1不循环，0无限循环，n多少次
	 */
	public void playVideo(int loop){
		Player player = new Player().loadPanel();
		player.play(loop,0);
	}
	/**
	 * 播放视频
	 * @param loop循环播放次数:-1不循环，0无限循环，n多少次
	 * @param ss从多少秒时间开始播放
	 */
	public void playVideo(int loop, int ss){
		Player player = new Player().loadPanel();
		player.play(loop,ss);
	}

	//加载播放面板
	public Player loadPanel(){
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		//实例化一个播放器
		Player player=new Player();

		// 获取用户屏幕大小
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();


		JFrame	mainFrame=new JFrame("Java Player");
		//mainFrame.setBounds(config.playerBound);
		mainFrame.setBounds(new Rectangle(screenSize));//设置用户屏幕大小
		mainFrame.setUndecorated(true);//类似设置全屏
		mainFrame.addWindowListener(player);
		Container cp=mainFrame.getContentPane();
		cp.setLayout(new MigLayout("insets 0 0 0 0"));

		//添加播放器视频面板
		cp.add(player.getVideoPanel(),"width :2000:,height :2000:,wrap");
		//添加播放器控制面板
		//cp.add(player.getControlBar(),"");

		mainFrame.setVisible(true);
		return player;
	}

	public Player(){
		mplayerPath = this.getClass().getResource("/").getPath()+MPLAYER_CONFIG;
		playListPath = this.getClass().getResource("/").getPath()+PLAY_LIST_CONFIG;

		videoPalel=new VideoPanel(this);

		controlBar=new JPanel();
		controlBar.setLayout(new MigLayout());

		progressBar=new PlayerProgressBar(this);
		controlBar.add(progressBar,"width :2000:,span,wrap");

		JPanel p2=new JPanel();
		p2.setLayout(new MigLayout("insets 0 0 0 0,align center"));
		controlBar.add(p2,"width :2000:,span,wrap");

		button_Open=new JButton("Open...");
		p2.add(button_Open);
		button_Open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				play(0,0);
			}

		});

		button_Play=new JButton("Play");
		p2.add(button_Play);
		button_Play.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(isPlay){
					pause();
				}else {
					play(0,0);
				}
			}

		});

		button_Stop=new JButton("Stop");
		p2.add(button_Stop);
		button_Stop.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				stop();
			}

		});

		volumeSlider=new JSlider();
		p2.add(volumeSlider,"width ::70");
		volumeSlider.setValue(volume);
		volumeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if ((JSlider) e.getSource() == volumeSlider) {
					setVolume(volumeSlider.getValue());
					volume=volumeSlider.getValue();
				}

			}
		});

		//刷新播放进度显示
		Thread setProgressDelay = new Thread() {
			public void run() {
				while (true) {
					SwingUtilities.invokeLater(new Runnable(){
						public void run() {
							if (progressBar != null) {
								progressBar.setTime(playOffset, length);
							}
						}
					});
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		};
		setProgressDelay.start();
	}

	/**
	 *
	 * @param loop循环播放次数:-1不循环，0无限循环，n多少次
	 * @param ss从多少秒时间开始播放
	 */
	public Process play(int loop,int ss){
		stop();
		isPlay=true;
		button_Play.setText("Pause");
		//调用命令行,更多选项请参考mplayer文档
		String [] cmd = new String[] {
				this.mplayerPath,//mplayer路径
				"-vo","directx",//视频驱动
				"-identify", //输出详情
				"-slave", //slave模式播放
				"-wid",String.valueOf(videoPalel.getWid()),//视频窗口的window handle
				"-colorkey", "0x030303",//视频窗口的背景色
				"-osdlevel", String.valueOf(1),//osd样式
				"-loop",String.valueOf(loop),//循环播放次数:-1不循环，0无限循环，n多少次
				//"-ss",String.valueOf(ss),//从多少秒时间开始播放
				"-fs",//设置全屏
				//path //播放文件路径;多个视频在后面添加视频路径。例如（path,path2,.....）
				"-playlist", this.playListPath,//根据播放列表播放文件(每行一个视频地址).
				//"-fstype","fullscreen",//layer,stays_on_top,above,fullscreen

		};
		try {
			proc = Runtime.getRuntime().exec(cmd);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

		//读取并解析mplayer的输出信息
		final InputStream is1 = proc.getErrorStream();
		final InputStream is2 = proc.getInputStream();
		final Runnable errorReader = new Runnable() {
			public void run() {
				try {
					final BufferedReader lReader = new BufferedReader(new InputStreamReader(is1));
					for (String l = lReader.readLine(); l != null ; l = lReader.readLine()) {
						// System.out.println("ERROR "+l);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		};


		final Runnable standReader = new Runnable() {
			public void run() {
				try {
					final BufferedReader lReader = new BufferedReader(new InputStreamReader(is2));
					String l="";
					while ((l=lReader.readLine())!=null) {
						if (l.length() >= 10) {
							String s2 = l.substring(0, 9);
							if (s2.equals("ID_LENGTH")) {
								int index = l.indexOf(".");
								String s1 = l.substring(10,index);
								length = Integer.valueOf(s1);
							}
						}
						//解析播放时间
						if(l.length()>70&&l.startsWith("A:")){
							int index = l.indexOf(".");
							String s3 = l.substring(2, index);
							int index2 = 0;
							while (true) {
								int index3 = s3.indexOf(" ", index2);
								if (index3 != -1) {
									index2 = index3 + 1;
								} else {
									break;
								}
							}
							String s4 = s3.substring(index2);
							playOffset = Integer.valueOf(s4);
						}
						if (l.length() >= 15) {
							String s4 = l.substring(0, 15);
							//解析视频宽度
							if (s4.equals("ID_VIDEO_HEIGHT")) {
								String s1 = l.substring(16);
								videoHeight = Integer.valueOf(s1);
								rate = (float) videoWidth/ (float) videoHeight;
								videoPalel.doLayout();
							}
						}
						if (l.length() >= 14) {
							String s3 = l.substring(0, 14);
							//解析视频高度
							if (s3.equals("ID_VIDEO_WIDTH")) {
								String s1 = l.substring(15);
								videoWidth = Integer.valueOf(s1);
							}
						}

						if (l.startsWith("ID_LENGTH")) {
							int index = l.indexOf("=");
							//解析视频长度
							if (index > 0) {
								String value = l.substring(index + 1);
								float intvalue=Float.valueOf(value);
								length=(int)intvalue;
							}
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		};

		t1 = new Thread(errorReader);
		t2 = new Thread(standReader);
		t1.start();
		t2.start();

		return  proc;
	}

	//搜索时间
	void seekto(int value) {
		if(isPlay){
			isPause=false;
			button_Play.setText("Pause");
			if (proc != null&isPlay) {
				PrintStream s = new PrintStream(proc.getOutputStream());
				String command = "seek " + value + " 2\n";
				s.print(command);
				s.flush();
			}
		}
	}

	//设置音量
	void setVolume(int vol) {
		if (proc != null) {
			if (!isPause) {
				PrintStream s = new PrintStream(proc.getOutputStream());
				s.print("volume " + vol + " 1\n");
				s.flush();
			}
		}
	}

	//停止
	void stop(){
		if(proc!=null){
			proc.destroy();
		}
		playOffset=0;
		isPause=false;
		button_Play.setText("Play");
		progressBar.setValue(0);
	}

	//暂停
	void pause(){
		if (proc != null) {
			PrintStream s = new PrintStream(proc.getOutputStream());
			s.print("pause\n");
			s.flush();
			isPause=!isPause;
		}
		if(isPause){
			button_Play.setText("Play");
		}else{
			button_Play.setText("Pause");
		}

	}

	//播放结束
	void playComplete(){
		isPlay=false;
		isPause=false;
		button_Play.setText("Play");
		playOffset=0;
	}

	Container getVideoPanel(){
		return videoPalel;
	}

	Container getControlBar(){
		return controlBar;
	}

	//退出程序
	public void exit(){
		if(proc!=null){
			proc.destroy();
		}
		System.exit(0);
	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowClosed(WindowEvent e) {
		exit();
	}

	public void windowClosing(WindowEvent e) {
		exit();
	}

	public void windowDeactivated(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowOpened(WindowEvent e) {

	}

	public static void main(String []agr){
		Player player = new Player();
		player.playVideo();
	}
}
