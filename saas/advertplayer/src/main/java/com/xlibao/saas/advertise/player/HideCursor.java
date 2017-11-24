package com.xlibao.saas.advertise.player;

import java.awt.*;
import java.awt.image.MemoryImageSource;

import javax.swing.JFrame;

public class HideCursor extends JFrame {

    HideCursor() {
        this.setBounds(300, 300, 300, 300);
        this.setTitle("鼠标进来就是死");
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
        this.hideCursor();
    }

    public void hideCursor() {
        Image image = Toolkit.getDefaultToolkit().createImage(
                new MemoryImageSource(0, 0, new int[0], 0, 0));
        this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(image,
                new Point(0, 0), null));
    }

    public static void main(String args[])
    {
        new HideCursor();
    }
}
