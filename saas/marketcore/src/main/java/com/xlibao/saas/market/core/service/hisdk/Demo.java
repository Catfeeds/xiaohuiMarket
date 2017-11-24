package com.xlibao.saas.market.core.service.hisdk;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;

public class Demo {

    private Vapi V = new Vapi();

    private static Demo vGuangSample = null;
    private JFrame frame;
    private JTextArea decodeTextArea;
    private JLabel lblDeviceStatus;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                vGuangSample = new Demo();
                vGuangSample.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Demo() throws UnsupportedEncodingException {
        // 初始化控件
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 660, 494);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton buttonBegin = new JButton("打开设备");
        buttonBegin.addActionListener(arg0 -> {
            if (V.vbarOpen()) { // 打开设备
                JOptionPane.showMessageDialog(null, "连接设备成功");
                try {
                    lblDeviceStatus.setText(V.getDevInfo());
                    decodeThread();// 解码线程调用
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                decodeTextArea.setText(null);
            } else {
                JOptionPane.showMessageDialog(null, "连接设备失败");
            }
        });
        buttonBegin.setBounds(264, 380, 90, 23);
        frame.getContentPane().add(buttonBegin);

        JButton buttonEnd = new JButton("关闭设备");
        buttonEnd.addActionListener(e -> {
            V.vbarClose();
            //buttonQuit.setEnabled(false);
        });
        buttonEnd.setBounds(360, 380, 90, 23);
        frame.getContentPane().add(buttonEnd);

        JButton buttonQuit = new JButton("退出程序");
        buttonQuit.addActionListener(e -> {
            V.vbarClose();
            System.exit(0);
        });
        buttonQuit.setBounds(460, 380, 90, 23);
        frame.getContentPane().add(buttonQuit);

        decodeTextArea = new JTextArea();
        decodeTextArea.setRows(5);
        decodeTextArea.setLineWrap(true);
        decodeTextArea.setColumns(10);
        decodeTextArea.setBounds(113, 78, 380, 211);
        frame.getContentPane().add(decodeTextArea);

        JLabel label = new JLabel("解码结果：");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(new Font("宋体", Font.BOLD, 14));
        label.setBounds(10, 78, 93, 23);
        frame.getContentPane().add(label);


        JLabel lblNewLabel_5 = new JLabel("版本信息：");
        lblNewLabel_5.setFont(new Font("宋体", Font.BOLD, 14));
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNewLabel_5.setBounds(10, 324, 93, 23);
        frame.getContentPane().add(lblNewLabel_5);

        lblDeviceStatus = new JLabel("设备未连接");
        lblDeviceStatus.setBounds(122, 324, 93, 23);
        lblDeviceStatus.setEnabled(false);
        frame.getContentPane().add(lblDeviceStatus);


        JButton buttonLightOn = new JButton("开灯");
        buttonLightOn.addActionListener(e -> V.vbarBackLight(true));
        buttonLightOn.setBounds(113, 380, 66, 23);
        frame.getContentPane().add(buttonLightOn);

        JButton buttonLightOff = new JButton("关灯");
        buttonLightOff.addActionListener(e -> V.vbarBackLight(false));
        buttonLightOff.setBounds(189, 380, 66, 23);
        frame.getContentPane().add(buttonLightOff);
    }

    private class Decode implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    String decode = V.getDecodeResult();
                    if (decode != null) {
                        decodeTextArea.setText(decode);
                    }
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void decodeThread() {
        Decode device = new Decode();
        Thread dev = new Thread(device);
        dev.start();
    }
}