package UI;

import services.UIService.Register;
import services.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RegisterFrame extends JFrame implements ActionListener, ItemListener {
    private JPanel mainPanel;
    private JLabel jlWelcome;
    //用户身份为学生或教师
    private ButtonGroup btgIdent;
    private JRadioButton rbStudent, rbTeacher;
    //用户名、密码、确认密码
    private JTextField tfName, tfAccount, tfPassword, tfPwdCheck;
    //用户身份
    private int userIdentity;
    //按钮区
    private JButton btnConfirm, btnCheck;
    //按钮对应的指令文本
    private static final String CONFIRM = "确认";
    private static final String CHECK = "检查";
    //注册服务
    private Register register;
    
    public RegisterFrame() {
        //调用注册服务
        register = new Register();
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
        //设置Label欢迎界面
        addLabelArea();
        //添加用户身份选择
        addUserIdentity();
        //添加用户信息填写区
        addTextFieldArea();
        //添加注册按钮区
        addButtonArea();
    }


    /**
     * 设置该程序窗口的属性
     */
    private void setWindowProperties() {
        //设置窗口标题
        setTitle("注册界面");
        //点击窗口"x"号即可关闭程序
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //程序窗口大小以及位置
        setSize(300, 700);
        FrameTools.setCenter(this);
    }

    /**
     * 添加开头注册标题
     */
    private void addLabelArea() {
        JPanel jpWelcome = new JPanel();
        jlWelcome = new JLabel("请按照下方提示按要求进行注册");
        jpWelcome.add(jlWelcome);
        mainPanel.add(jpWelcome, BorderLayout.NORTH);
    }

    /**
     * 添加用户身份识别区
     */
    private void addUserIdentity() {
        JPanel jpIdent = new JPanel();
        JLabel jLabel = new JLabel("用户身份选择:");

        btgIdent = new ButtonGroup();
        rbStudent = new JRadioButton("学生", true);//默认学生身份
        rbTeacher = new JRadioButton("教师");


        //添加单选框到子布局
        jpIdent.setLayout(new GridLayout(1,2));
        jpIdent.add(jLabel);
        jpIdent.add(rbStudent);jpIdent.add(rbTeacher);

        //添加用户身份选项到ButtonGroup
        btgIdent.add(rbStudent);
        btgIdent.add(rbTeacher);

        //监听用户选项
        rbStudent.addItemListener(this);
        rbTeacher.addItemListener(this);

        mainPanel.add(jpIdent, BorderLayout.CENTER);
    }

    /**
     * 添加用户信息填写文本框区域
     */
    private void addTextFieldArea() {
        tfName = new JTextField();
        tfAccount = new JTextField();
        tfPassword = new JTextField();
        tfPwdCheck = new JTextField();

        //文本框提示
        tfName.addFocusListener(new FrameTools.JTextFieldHintListener(tfName, "用户姓名"));
        tfAccount.addFocusListener(new FrameTools.JTextFieldHintListener(tfAccount, "账号：学号/教工号"));
        tfPassword.addFocusListener(new FrameTools.JTextFieldHintListener(tfPassword, "密码"));
        tfPwdCheck.addFocusListener(new FrameTools.JTextFieldHintListener(tfPwdCheck, "确认密码"));

        //设置列宽
        tfName.setColumns(18);
        tfAccount.setColumns(18);
        tfPassword.setColumns(18);
        tfPwdCheck.setColumns(18);

        //文本框区域布局
        JPanel jpText = new JPanel();
        jpText.setLayout(new GridLayout(4,1));
        jpText.add(tfName);
        jpText.add(tfAccount);
        jpText.add(tfPassword);
        jpText.add(tfPwdCheck);

        //添加到主面板
        mainPanel.add(jpText);

    }

    /**
     * 添加注册界面的按钮
     */
    private void addButtonArea() {
        JPanel jpButton = new JPanel();
        btnConfirm = new JButton(CONFIRM);
        btnCheck = new JButton(CHECK);
        //按钮区布局
        jpButton.setLayout(new GridLayout(1,2));
        jpButton.add(btnConfirm);
        jpButton.add(btnCheck);
        //添加监听事件
        btnConfirm.addActionListener(this);
        btnCheck.addActionListener(this);

        mainPanel.add(jpButton);
    }

    /**
     * 按钮监听
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CONFIRM:
                confirmClick();
                break;
            case CHECK:
                checkClick();
                break;
        }
    }

    /**
     * 点击确认键
     */
    private void confirmClick() {
        int infoType = register.checkRegisterInfo(tfName.getText(), tfAccount.getText(),
                tfPassword.getText(), tfPwdCheck.getText());
        //检查用户信息是否通过，通过就将信息存储到数据库中，否则加以提示
        if (infoType == Register.CHECK_INFO_PASS) {
            register.storeUser(userIdentity, tfName.getText(), tfAccount.getText(), tfPassword.getText());
            JOptionPane.showMessageDialog(this,"注册成功", "注册结果", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, register.getInfoByType(infoType),"检查结果",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 点击检查键
     */
    private void checkClick() {
        int infoType = register.checkRegisterInfo(tfName.getText(), tfAccount.getText(),
                tfPassword.getText(), tfPwdCheck.getText());
        JOptionPane.showMessageDialog(this, register.getInfoByType(infoType),"检查结果",JOptionPane.INFORMATION_MESSAGE);
    }



    /**
     * 用户身份选择框状态改变监听
     * @param e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (rbStudent.isSelected()) {
            userIdentity = UserType.STUDENT;
        }
        if (rbTeacher.isSelected()) {
            userIdentity = UserType.TEACHER;
        }
    }

}
