package UI;

import services.HomeworkDAOFactory;
import services.UIService.SubmitHW;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SubmitHWFrame extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JTextField tfFileName, tfDir;
    private JButton btnSelect, btnUpload;
    private static final String SELECT = "选择";
    private static final String UPLOAD = "上传";
    //存储文件保存的路径
    private String savePath;
    //保存用户实例
    private Object user;
    //调用服务
    private SubmitHW submitHWService;

    public SubmitHWFrame(Object user) {
        this.user = user;
        //调用服务
        submitHWService = new SubmitHW();
        //设置窗口属性
        setWindowProperties();
        //设置主面板的属性
        setMainPanelProperties();
    }

    /**
     * 设置该程序窗口的属性
     */
    private void setWindowProperties() {
        //设置窗口标题
        setTitle("提交作业文件");
        //点击窗口"x"号即可关闭程序
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //程序窗口大小以及位置
        setSize(300, 150);
        FrameTools.setCenter(this);
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
        //添加按钮
        addButtonArea();
        //添加文本框区
        addTextFieldArea();
    }


    /**
     * 添加按钮区，选择和保存文件
     */
    private void addButtonArea() {
        JPanel jpBTN = new JPanel();
        btnSelect = new JButton(SELECT);
        btnUpload = new JButton(UPLOAD);

        btnSelect.addActionListener(this);
        btnUpload.addActionListener(this);

        jpBTN.add(btnSelect);
        jpBTN.add(btnUpload);

        //添加到主面板
        mainPanel.add(jpBTN);
    }

    /**
     * 添加文本框区域，显示选择的文件名以及路径
     */
    private void addTextFieldArea() {
        JPanel jpText = new JPanel();
        jpText.setLayout(new GridLayout(2,1));
        tfFileName = new JTextField();
        tfDir = new JTextField();

        tfFileName.setColumns(18);
        tfDir.setColumns(18);

        tfFileName.setEditable(false);
        tfDir.setEditable(false);

        jpText.add(tfFileName);
        jpText.add(tfDir);

        //添加到主面板
        mainPanel.add(jpText);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SELECT:
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("仅支持docx", "docx"));
                int val = chooser.showOpenDialog(SubmitHWFrame.this);
                if (val == JFileChooser.APPROVE_OPTION) {   //确定选择文件
                    tfFileName.setText(chooser.getSelectedFile().getName());
                    tfDir.setText(chooser.getCurrentDirectory().toString());
                }

                if (val == JFileChooser.CANCEL_OPTION) {    //取消选择文件
                    tfFileName.setText("");
                    tfDir.setText("");
                }
                break;
            case UPLOAD:
                submitHWService.upload(user, tfDir.getText() + File.separatorChar + tfFileName.getText(),
                        HomeworkDAOFactory.getCurrentHWName());
                JOptionPane.showMessageDialog(this, "上传成功", "作业提交", JOptionPane.INFORMATION_MESSAGE);
                break;
        }

    }
}
