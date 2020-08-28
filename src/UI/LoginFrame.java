package UI;

import services.UIService.Login;
import services.UserDAOFactory;
import services.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LoginFrame extends JFrame implements ActionListener, ItemListener {
    private JPanel mainPanel;
    private JLabel jlWelcome;
    //账号密码区
    private JTextField tfAccount, tfPassword;
    //是否记住账号密码勾选框
    private JCheckBox cbAccount, cbPassword;
    //登录注册按钮
    private JButton btnLoginStu, btnLoginTec, btnRegister;
    //按钮对应的指令文本
    private static final String LOGIN_STU = "学生登录";
    private static final String LOGIN_TEC = "教师登录";
    private static final String REGISTER = "注册";
    //调用Login服务
    private Login loginService;

    public LoginFrame() {
        //调用登录服务
        loginService = new Login();
        //app初始化设置
        new Login().appInit();
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
        //设置背景色
        mainPanel.setBackground(Color.orange);
        //默认主panel为焦点
        mainPanel.setFocusable(true);
        //mainPanel作为主Panel
        setContentPane(mainPanel);
        //设置Label欢迎界面
        addLabelArea();
        //添加文本区域
        addTextFieldArea();
        //添加按钮区域
        addButtonArea();
    }


    /**
     * 设置该程序窗口的属性
     */
    private void setWindowProperties() {
        //设置窗口标题
        setTitle("登录界面");
        //点击窗口"x"号即可关闭程序
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //程序窗口大小以及位置
        setSize(350, 200);
        FrameTools.setCenter(this);
    }

    /**
     * 添加程序开头的label
     */
    private void addLabelArea() {
        jlWelcome = new JLabel("欢迎使用教务系统");
        mainPanel.add(jlWelcome);
    }

    /**
     * 添加按钮区域
     */
    private void addButtonArea() {
        btnLoginStu = new JButton(LOGIN_STU);
        btnLoginTec = new JButton(LOGIN_TEC);
        btnRegister = new JButton(REGISTER);

        //按钮区域子面板
        JPanel jpBtn = new JPanel();
        jpBtn.add(btnLoginStu);
        jpBtn.add(btnLoginTec);
        jpBtn.add(btnRegister);

        //按钮监听事件
        btnLoginStu.addActionListener(this);
        btnLoginTec.addActionListener(this);
        btnRegister.addActionListener(this);

        //添加到主面板
        mainPanel.add(jpBtn);
    }

    /**
     * 设置文本框区域，用来填写账号和密码
     */
    private void addTextFieldArea() {
        tfAccount = new JTextField("账号");
        tfPassword = new JTextField("密码");

        //复选框也放到此区域
        checkBoxInit();

        //设置列宽
        tfAccount.setColumns(15);
        tfPassword.setColumns(15);

        //若用户设置设置文本内容
        setAccAndPwdText();

        //文本框区域子面板
        JPanel jpTextFiled = new JPanel();
        jpTextFiled.setLayout(new GridLayout(2, 1));//设置子面板的布局
        JPanel jpAccount = new JPanel(); //账号面板
        jpAccount.add(tfAccount);jpAccount.add(cbAccount);
        JPanel jpPassword = new JPanel(); //密码面板
        jpPassword.add(tfPassword);jpPassword.add(cbPassword);

        jpTextFiled.add(jpAccount);
        jpTextFiled.add(jpPassword);

        //添加到主面板
        mainPanel.add(jpTextFiled);
    }

    /**
     * 根据用户的选择决定是否填充账号密码
     */
    private void setAccAndPwdText() {
        //是否需要填充账号
        if (cbAccount.isSelected()) {
            tfAccount.setText(loginService.getAccount());
        } else {
            //设置文本提示
            tfAccount.addFocusListener(new FrameTools.JTextFieldHintListener(tfAccount, "账号：学号/教工号"));
        }

        //是否需要填充密码
        if (cbPassword.isSelected()) {
            tfPassword.setText(loginService.getPassword());
        } else {
            //设置文本提示
            tfPassword.addFocusListener(new FrameTools.JTextFieldHintListener(tfPassword, "密码"));
        }

    }

    /**
     * 初始化勾选框的状态
     */
    private void checkBoxInit() {
        cbAccount = new JCheckBox("记住账号", loginService.getAccRemStation());
        cbPassword = new JCheckBox("记住密码", loginService.getPwdRemStation());

        cbAccount.addItemListener(this);
        cbPassword.addItemListener(this);
    }


    /**
     * 事件监听并执行
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case LOGIN_STU:
                System.out.println("学生登录");
                studentLogin();
                break;
            case LOGIN_TEC:
                System.out.println("教师登录");
                teacherLogin();
                break;
            case REGISTER:
                System.out.println("注册");
                //点击注册按钮跳转到新界面
                jumpToRegister();
                break;

        }

    }

    /**
     * 监听复选框
     * @param e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox cb = (JCheckBox) e.getItem();
        /**
         * 将复选框的状态保存起来
         */
        if (cb.equals(cbAccount)) {
            //账号复选框状态改变
            loginService.changeAccRemStation(cb.isSelected());
        } else if (cb.equals(cbPassword)) {
            //密码复选框状态改变
            loginService.changePwdRemStation(cb.isSelected());
        }

    }

    /**
     * 学生登录
     */
    private void studentLogin() {
        int loginResult = loginService.userPass(UserType.STUDENT, tfAccount.getText(), tfPassword.getText());
        switch (loginResult) {
            case Login.LOGIN_PASS:  //账号密码正确跳转到管理界面
                jumpToMain(UserType.STUDENT, tfAccount.getText());
                break;
            case Login.LOGIN_NO_ACCOUNT:
                JOptionPane.showMessageDialog(this, "没有此用户", "学生登录错误", JOptionPane.ERROR_MESSAGE);
                break;
            case Login.LOGIN_WRONG_PWD:
                JOptionPane.showMessageDialog(this, "密码错误", "学生登录错误", JOptionPane.ERROR_MESSAGE);
                break;
        }

    }

    /**
     * 教师登录
     */
    private void teacherLogin() {
        int loginResult = loginService.userPass(UserType.TEACHER, tfAccount.getText(), tfPassword.getText());
        switch (loginResult) {
            case Login.LOGIN_PASS:  //账号密码正确
                jumpToMain(UserType.TEACHER, tfAccount.getText());
                break;
            case Login.LOGIN_NO_ACCOUNT:
                JOptionPane.showMessageDialog(this, "没有此用户", "教师登录错误", JOptionPane.ERROR_MESSAGE);
                break;
            case Login.LOGIN_WRONG_PWD:
                JOptionPane.showMessageDialog(this, "密码错误", "教师登录错误", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    /**
     * 跳转到注册界面
     */
    private void jumpToRegister() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
    }

    /**
     * 跳转到管理界面
     *
     * @param userType
     */
    private void jumpToMain(int userType, String account) {
        int userId = UserDAOFactory.getIdByAccount(userType, account);
        //是否需要保存账号密码
        loginService.storeAccountAndPassword(tfAccount.getText(), cbAccount.isSelected(),
                tfPassword.getText(), cbPassword.isSelected());
        //界面跳转
        ManagerFrame managerFrame = new ManagerFrame(userType, userId);
        managerFrame.setVisible(true);
        dispose();
    }

}
