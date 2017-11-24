package com.xlibao.saas.advertise.player;

import java.awt.event.MouseEvent;

import javax.swing.JProgressBar;

public class PlayerProgressBar extends JProgressBar{
	private static final long serialVersionUID = -972301782528385389L;
	int maxNum=9000;
	PlayerProgressBar progressBar;
	int x1=0;
	int x2=0;
	Player player;
	PlayerProgressBar(final Player player){
		super();
		this.player=player;
		progressBar=this;
		setMaximum(maxNum);
		addMouseListener(new java.awt.event.MouseListener() {

			public void mouseClicked(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {
				double sel = ((double) e.getX()/ ((double) progressBar.getWidth()) * progressBar.getMaximum());
				progressBar.setValue((int) sel);
				int length=player.length;
				int ds = (int) ((sel / progressBar.getMaximum()) * length);
				player.seekto(ds);
			}

		});

	}

	void setTime(int time,int length){
		double sel=(time / (double) length)* getMaximum();
		progressBar.setValue((int) sel);
	}


}
