package com.xlibao.saas.market.core.scan;

import com.xlibao.common.CommonUtils;
import com.xlibao.saas.market.core.service.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author chinahuangxc
 */
@org.springframework.stereotype.Component
public class VGFrame extends javax.swing.JFrame {

    private static final int MAX_LINE_COUNT = 100000;
    private static final int MAX_SPACE_COUNT = String.valueOf(MAX_LINE_COUNT).length() + 2;

    @Autowired
    private ApplicationService applicationService;

    static {
        try {
            String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            UIManager.setLookAndFeel(windows);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        VGFrame frame = new VGFrame();
        frame.setVisible(true);
    }

    public VGFrame() {
        initComponents();

//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setAlwaysOnTop(true);
//        setUndecorated(true);

        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("").getPath() + "/logo64.png");
        setIconImage(img);

        connectorDevice();

        lightControl(true);
    }

    private void initComponents() {
        JPanel panelContent = new javax.swing.JPanel();
        JLabel labTip = new javax.swing.JLabel();
        JLabel labScanContent = new javax.swing.JLabel();
        txtScanContent = new javax.swing.JTextField();
        btnConnectDevice = new javax.swing.JButton();
        btnDisconnectDevice = new javax.swing.JButton();
        JLabel labDeviceVersion = new javax.swing.JLabel();
        btnLightOn = new javax.swing.JButton();
        btnLightOff = new javax.swing.JButton();
        JButton btnClose = new JButton();
        JButton btnSubmit = new javax.swing.JButton();
        labLastScanContent = new javax.swing.JLabel();
        labDeviceVersionValue = new javax.swing.JLabel();
        JScrollPane scrollPane1 = new javax.swing.JScrollPane();
        txtHistory = new javax.swing.JTextArea();
        JButton btnClearHistory = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("扫码器，必须一直处于置顶状态");
        setBackground(new java.awt.Color(60, 63, 65));

        panelContent.setBackground(new java.awt.Color(60, 63, 65));

        labTip.setForeground(new java.awt.Color(255, 255, 255));
        labTip.setText("小惠科技扫码功能面板");

        labScanContent.setForeground(new java.awt.Color(255, 255, 255));
        labScanContent.setText("扫码内容：");

        txtScanContent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtScanContentFocusLost(evt);
            }
        });
        txtScanContent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtScanContentKeyReleased(evt);
            }
        });

        btnConnectDevice.setText("连接设备");
        btnConnectDevice.setOpaque(false);
        btnConnectDevice.addActionListener(this::btnConnectDeviceActionPerformed);

        btnDisconnectDevice.setText("关闭设备");
        btnDisconnectDevice.setOpaque(false);
        btnDisconnectDevice.addActionListener(this::btnDisconnectDeviceActionPerformed);

        labDeviceVersion.setForeground(new java.awt.Color(255, 255, 255));
        labDeviceVersion.setText("扫码设备版本号：");

        btnLightOn.setText("开灯");
        btnLightOn.setOpaque(false);
        btnLightOn.addActionListener(this::btnLightOnActionPerformed);

        btnLightOff.setText("关灯");
        btnLightOff.setOpaque(false);
        btnLightOff.addActionListener(this::btnLightOffActionPerformed);

        btnClose.setText("关闭程序");
        btnClose.setOpaque(false);
        btnClose.addActionListener(this::btnCloseActionPerformed);

        btnSubmit.setText("提交");
        btnSubmit.setOpaque(false);
        btnSubmit.addActionListener(this::btnSubmitActionPerformed);

        labLastScanContent.setForeground(new java.awt.Color(255, 255, 255));
        labLastScanContent.setText("<html>最后一次扫码内容：</html>");

        labDeviceVersionValue.setForeground(new java.awt.Color(153, 153, 153));
        labDeviceVersionValue.setText("未连接设备  ");

        scrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        txtHistory.setEditable(false);
        txtHistory.setBackground(new java.awt.Color(0, 0, 0));
        // txtHistory.setColumns(20);
        txtHistory.setFont(new java.awt.Font("宋体", 0, 15));
        txtHistory.setForeground(new java.awt.Color(32, 32, 255));
        // txtHistory.setRows(1);
        txtHistory.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "操作历史", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(204, 204, 255)));
        txtHistory.setRequestFocusEnabled(false);
        scrollPane1.setViewportView(txtHistory);

        txtHistory.setToolTipText("最多可显示：" + MAX_LINE_COUNT + "条操作记录");

        btnClearHistory.setText("清空操作历史");
        btnClearHistory.setOpaque(false);
        btnClearHistory.addActionListener(this::btnClearHistoryActionPerformed);

        javax.swing.GroupLayout panelContentLayout = new javax.swing.GroupLayout(panelContent);
        panelContent.setLayout(panelContentLayout);
        panelContentLayout.setHorizontalGroup(
                panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelContentLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelContentLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(labDeviceVersion)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labDeviceVersionValue))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContentLayout.createSequentialGroup()
                                                .addGroup(panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(scrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labTip, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelContentLayout.createSequentialGroup()
                                                                .addGroup(panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(panelContentLayout.createSequentialGroup()
                                                                                .addComponent(labScanContent)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(txtScanContent, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(btnSubmit))
                                                                        .addGroup(panelContentLayout.createSequentialGroup()
                                                                                .addComponent(btnConnectDevice)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(btnDisconnectDevice)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(btnLightOn)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(btnLightOff)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(btnClose))
                                                                        .addComponent(labLastScanContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 473, Short.MAX_VALUE)))
                                                .addContainerGap())))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContentLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnClearHistory)
                                .addContainerGap())
        );
        panelContentLayout.setVerticalGroup(
                panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelContentLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labTip)
                                .addGap(24, 24, 24)
                                .addGroup(panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(txtScanContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labScanContent)
                                        .addComponent(btnSubmit))
                                .addGap(18, 18, 18)
                                .addGroup(panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnConnectDevice)
                                        .addComponent(btnDisconnectDevice)
                                        .addComponent(btnLightOn)
                                        .addComponent(btnLightOff)
                                        .addComponent(btnClose))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labLastScanContent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClearHistory)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labDeviceVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labDeviceVersionValue, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(938, 703));
        setLocationRelativeTo(null);
    }

    private void txtScanContentKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scanContent();
        }
    }

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {
        scanContent();
    }

    private void txtScanContentFocusLost(java.awt.event.FocusEvent evt) {
        txtScanContent.requestFocus();
    }

    private void btnConnectDeviceActionPerformed(java.awt.event.ActionEvent evt) {
        connectorDevice();
    }

    private void btnDisconnectDeviceActionPerformed(java.awt.event.ActionEvent evt) {
        disconnectDevice();
    }

    private void btnLightOnActionPerformed(java.awt.event.ActionEvent evt) {
        lightControl(true);
    }

    private void btnLightOffActionPerformed(java.awt.event.ActionEvent evt) {
        lightControl(false);
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "该功能暂不开放");
    }

    private void btnClearHistoryActionPerformed(java.awt.event.ActionEvent evt) {
        txtHistory.setText("");
        lineIndex = 0;
        appendHistory("清空了操作历史记录");
    }

    private javax.swing.JLabel labDeviceVersionValue;
    private javax.swing.JLabel labLastScanContent;
    private javax.swing.JTextArea txtHistory;
    private javax.swing.JTextField txtScanContent;
    private JButton btnConnectDevice;
    private JButton btnDisconnectDevice;
    private JButton btnLightOn;
    private JButton btnLightOff;

    private void connectorDevice() {
        String version = "无法连接扫码设备";
        try {
            VGApi.connectDevice();
            version = VGApi.getDeviceVersion();

            version = version.trim();

            appendHistory("连接设备成功，当前设备版本号：" + version);

            btnConnectDevice.setEnabled(false);
            btnDisconnectDevice.setEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            appendHistory("连接设备发生错误，错误消息：" + ex.getMessage());
        }
        labDeviceVersionValue.setText(version + "  ");
    }

    private void disconnectDevice() {
        try {
            VGApi.disconnectDevice();

            appendHistory("关闭扫码设备成功");

            labDeviceVersionValue.setText("未连接扫码设备  ");

            btnConnectDevice.setEnabled(true);
            btnDisconnectDevice.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            appendHistory("关闭扫码设备发生错误，错误消息：" + ex.getMessage());
        }
    }

    private void scanContent() {
        String scanContent = txtScanContent.getText();
        if (CommonUtils.isNullString(scanContent)) {
            appendHistory("扫码内容为空");
            return;
        }
        scanContent = scanContent.trim();
        // 重新将焦点设置到文本框
        txtScanContent.requestFocus();
        labLastScanContent.setText("<html>最后一次扫码内容：<font color=\"red\">" + scanContent + "<font></html>");

        txtScanContent.setText("");

        appendHistory("扫码取货，二维码内容：" + scanContent);

        applicationService.scanPickUp(scanContent);
    }

    private void lightControl(boolean on) {
        try {
            VGApi.lightControl(on);
            appendHistory("控制灯光效果成功，当前灯光效果为：" + on);

            if (on) {
                btnLightOn.setEnabled(false);
                btnLightOff.setEnabled(true);
            } else {
                btnLightOn.setEnabled(true);
                btnLightOff.setEnabled(false);
            }
        } catch (Exception ex) {
            appendHistory("控制灯光效果为：" + on + "时发生了异常，异常信息：" + ex.getMessage());
        }
    }

    private int lineIndex = 0;

    private void appendHistory(String text) {
        int lineCount = txtHistory.getLineCount();
        if (lineCount > MAX_LINE_COUNT) {
            txtHistory.setText("");
            lineIndex = 0;
            appendHistory("由于操作记录已经超过了行数限制(最大记录行数：" + MAX_LINE_COUNT + "行)，记录已被系统自动清空......");
        }
        String li = String.valueOf(++lineIndex);
        txtHistory.append(li);
        txtHistory.append(CommonUtils.calculationSeparator(MAX_SPACE_COUNT, li, CommonUtils.SPACE) + "|" + CommonUtils.SPACE);
        txtHistory.append(CommonUtils.nowFormat());
        txtHistory.append(CommonUtils.SPACE);
        txtHistory.append(text);
        txtHistory.append(CommonUtils.ENTER);
        // 始终将光标移到最后
        txtHistory.setCaretPosition(txtHistory.getDocument().getLength());
    }
}