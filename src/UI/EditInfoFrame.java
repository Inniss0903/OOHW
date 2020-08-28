package UI;

import services.UIService.EditInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditInfoFrame extends JFrame implements ActionListener {
    private JPanel mainPanel;

    private JLabel jlWelcome;
    //用户名、密码
    private JTextField tfName, tfPassword;
    private JButton btnChangeName, btnChangePwd;
    private static final String CHANGE_NAME = "修改用户名";
    private static final String CHANGE_PWD = "修改密码";
    private Object user;
    private EditInfo editInfoService;

    public EditInfoFrame(Object user) {
        this.user = user;
        editInfoService = new EditInfo();
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
        //添加信息修改面板
        addModifyInfo();
    }



    /**
     * 设置该程序窗口的属性
     */
    private void setWindowProperties() {
        //设置窗口标题
        setTitle("修改个人信息界面");
        //点击窗口"x"号即可关闭程序
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //程序窗口大小以及位置
        setSize(350, 400);
        FrameTools.setCenter(this);
    }

    /**
     * 添加开头注册标题
     */
    private void addLabelArea() {
        JPanel jpWelcome = new JPanel();
        jlWelcome = new JLabel("请按照下方提示修改信息");
        jpWelcome.add(jlWelcome);
        mainPanel.add(jpWelcome, BorderLayout.NORTH);
    }

    /**
     * 修改信息栏
     */
    private void addModifyInfo() {
        JPanel jpMod = new JPanel();

        //文本框设置
        tfName = new JTextField();
        tfPassword = new JTextField();

        tfName.setText(editInfoService.getUserName(user));
        tfPassword.setText(editInfoService.getUserPwd(user));

        tfName.setColumns(18);
        tfPassword.setColumns(18);

        //按钮设置
        btnChangeName = new JButton(CHANGE_NAME);
        btnChangePwd = new JButton(CHANGE_PWD);

        btnChangeName.addActionListener(this);
        btnChangePwd.addActionListener(this);

        //子面板布局
        JPanel jpName = new JPanel();
        JPanel jpPwd = new JPanel();
        jpName.add(tfName);jpName.add(btnChangeName);
        jpPwd.add(tfPassword);jpPwd.add(btnChangePwd);

        jpMod.setLayout(new GridLayout(2,1));
        jpMod.add(jpName); jpMod.add(jpPwd);
        //添加到主面板
        mainPanel.add(jpMod);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CHANGE_NAME:
                editInfoService.changeName(user, tfName.getText());
                JOptionPane.showMessageDialog(this, "用户名已修改");
                break;
            case CHANGE_PWD:
                editInfoService.changePwd(user, tfPassword.getText());
                JOptionPane.showMessageDialog(this, "用户密码已修改");
                break;
        }
    }
}
