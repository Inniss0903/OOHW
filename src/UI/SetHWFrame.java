package UI;

import services.UIService.SetHW;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetHWFrame extends JFrame implements ActionListener {
    private JPanel mainPanel = new JPanel();

    private JButton btnSubmit;

    private static final String SUBMIT = "提交";

    private JTextArea taContent;

    private SetHW setHWService;

    public SetHWFrame() {
        setHWService = new SetHW();
        //设置窗口属性
        setWindowProperties();
        //设置主面板的属性
        setMainPanelProperties();
    }

    /**
     * 设置主Panel的属性
     */
    private void setMainPanelProperties() {
        //设置主窗口的属性
        mainPanel = new JPanel();
        //默认主panel为焦点
        mainPanel.setFocusable(true);
        //mainPanel作为主Panel
        setContentPane(mainPanel);
        //添加提交按钮
        addButtonArea();
        //添加作业编辑区
        addTextArea();

    }



    /**
     * 设置该程序窗口的属性
     */
    private void setWindowProperties() {
        //设置窗口标题
        setTitle("布置作业");
        //点击窗口"x"号即可关闭程序
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //程序窗口大小以及位置
        setSize(300, 300);
        FrameTools.setCenter(this);
    }


    /**
     * 添加提交按钮
     */
    private void addButtonArea() {
        btnSubmit = new JButton(SUBMIT);

        btnSubmit.addActionListener(this);

        mainPanel.add(btnSubmit);
    }

    /**
     * 添加作业内容编辑区
     */
    private void addTextArea() {
        taContent = new JTextArea();
        //基本设置
        taContent.setLineWrap(true);
        taContent.setColumns(18);
        taContent.setRows(5);

        mainPanel.add(taContent);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SUBMIT:
                setHWService.setHW(taContent.getText());
                JOptionPane.showMessageDialog(this, "布置成功", "作业布置", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
}
