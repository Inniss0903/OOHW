package UI;

import services.AppPreference;
import services.UIService.Login;
import services.UIService.Manager;
import services.UIService.ReviewHW;
import services.UIService.SetHW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerFrame extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JLabel jlWelcome;
    //导航栏按钮
    private JButton btnSubmitHomework, btnSetHomework, btnReviewHomework, btnOpenQuestionBank, btnEditInfo, btnLogout;
    //作业公告区
    private JLabel jlHWTitle;
    private JTextArea taHWBoard;
    //学生作业批改情况
    private JLabel jlReview;
    private JTextArea taReview;
    //按钮对应的指令文本
    private static final String SUBMIT_HOMEWORK = "提交作业";
    private static final String SET_HOMEWORK = "布置作业";
    private static final String REVIEW_HOMEWORK = "批改作业";
    private static final String OPEN_QUESTION_BANK = "打开题库";
    private static final String EDIT_INFO = "修改信息";
    private static final String LOGOUT = "退出登录";

    //用户类型
    private int userType, userId;
    //用户实例
    private Object user;
    //调用管理界面的服务
    private Manager managerService;

    /**
     * 构建时必须用户身份
     * @param userType
     * @param userId
     */
    public ManagerFrame(int userType, int userId) {
        //获取用户身份
        this.userType = userType;
        this.userId = userId;
        //调用服务
        managerService = new Manager();
        //设置窗口属性
        setWindowProperties();
        //设置主面板的属性
        setMainPanelProperties();
        //获取用户实例
        user = managerService.getUser(userType, userId);
    }

    /**
     * 设置主Panel的属性
     */
    private void setMainPanelProperties() {
        //设置主窗口的基本属性
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.setBackground(Color.LIGHT_GRAY);
        //默认主panel为焦点
        mainPanel.setFocusable(true);
        //mainPanel作为主Panel
        setContentPane(mainPanel);
        //设置Label欢迎界面
        addLabelArea();
        //添加导航栏
        addNavigation();
        //添加作业公告栏
        addHWBoard();
        //添加学生作业批改情况
        addHWReview();

    }


    /**
     * 设置该程序窗口的属性
     */
    private void setWindowProperties() {
        //设置窗口标题
        setTitle("作业管理系统" + new AppPreference().getAppVersion());
        //点击窗口"x"号即可关闭程序
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //程序窗口大小以及位置
        setSize(700, 700);
        FrameTools.setCenter(this);
    }

    /**
     * 添加开头标题
     */
    private void addLabelArea() {
        JPanel jpWelcome = new JPanel();
        //label的内容
        String titleContent = managerService.getTitleContent(userType, userId);
        jlWelcome = new JLabel(titleContent);
        //label的基本属性
        jlWelcome.setFont(new Font("Serief",Font.ITALIC + Font.BOLD, 16));
        //添加到lable子面板
        jpWelcome.add(jlWelcome);
        //添加到主面板
        mainPanel.add(jpWelcome);
    }

    /**
     * 添加导航栏
     */
    private void addNavigation() {
        JPanel jpNav = new JPanel();
        //按钮初始化

        btnSubmitHomework = new JButton(SUBMIT_HOMEWORK);
        btnSetHomework = new JButton(SET_HOMEWORK);
        btnReviewHomework = new JButton(REVIEW_HOMEWORK);
        btnOpenQuestionBank = new JButton(OPEN_QUESTION_BANK);
        btnEditInfo = new JButton(EDIT_INFO);
        btnLogout  = new JButton(LOGOUT);


        //添加按钮监听事件
        btnSubmitHomework.addActionListener(this);
        btnSetHomework.addActionListener(this);
        btnReviewHomework.addActionListener(this);
        btnOpenQuestionBank.addActionListener(this);
        btnEditInfo.addActionListener(this);
        btnLogout.addActionListener(this);

        //添加到导航栏面板
        jpNav.add(btnSubmitHomework);
        jpNav.add(btnSetHomework);
        jpNav.add(btnReviewHomework);
        jpNav.add(btnOpenQuestionBank);
        jpNav.add(btnEditInfo);
        jpNav.add(btnLogout);

        //添加到主面板
        mainPanel.add(jpNav);
    }

    /**
     * 添加作业公告栏
     */
    private void addHWBoard() {
        JPanel jpBoard = new JPanel();
        jlHWTitle = new JLabel();
        jlHWTitle.setText(managerService.getHWTitle());
        //设置JTextArea属性
        taHWBoard = new JTextArea();
        taHWBoard.setColumns(28);
        taHWBoard.setLineWrap(true);
        taHWBoard.setText(managerService.getHWContent());

        //添加到子面板
        jpBoard.setLayout(new GridLayout(2,1));
        jpBoard.add(jlHWTitle);
        jpBoard.add(taHWBoard);

        mainPanel.add(jpBoard);
    }


    /**
     * 添加学生作业批改情况
     */
    private void addHWReview() {
        JPanel jpReview = new JPanel();
        jlReview = new JLabel("作业批改情况");
        //设置JTextArea属性
        taReview = new JTextArea();
        taReview.setColumns(28);
        taReview.setLineWrap(true);
        taReview.setText(managerService.getHWReview(userId));

        //添加到子面板
        jpReview.setLayout(new GridLayout(2,1));
        jpReview.add(jlReview);
        jpReview.add(taReview);

        mainPanel.add(jpReview);
    }





    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SUBMIT_HOMEWORK:
                jumpToSubmitHW();
                break;
            case SET_HOMEWORK:
                jumpToSetHW();
                break;
            case REVIEW_HOMEWORK:
                jumpToReview();
                break;
            case OPEN_QUESTION_BANK:
                jumpToQuestionBank();
                break;
            case EDIT_INFO:
                jumpToEditInfo();
                break;
            case LOGOUT:
                jumpToLogin();
                break;
        }
    }


    /**
     * 学生提交作业界面
     */
    private void jumpToSubmitHW() {
        SubmitHWFrame submitHWFrame = new SubmitHWFrame(user);
        submitHWFrame.setVisible(true);
    }

    /**
     * 教师布置作业界面
     */
    private void jumpToSetHW() {
        SetHWFrame setHWFrame = new SetHWFrame();
        setHWFrame.setVisible(true);
    }

    /**
     * 跳转到教师批改作业界面
     */
    private void jumpToReview() {
        ReviewHWFrame reviewHWFrame = new ReviewHWFrame();
        reviewHWFrame.setVisible(true);
    }

    /**
     * 跳转到题库界面
     */
    private void jumpToQuestionBank() {
        QuestionBankFrame questionBankFrame = new QuestionBankFrame();
        questionBankFrame.setVisible(true);
    }


    /**
     * 退出登录，跳转到登录界面
     */
    private void jumpToLogin() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        dispose();
    }

    /**
     * 跳转到信息修改界面
     */
    private void jumpToEditInfo() {
        EditInfoFrame editInfoFrame = new EditInfoFrame(user);
        editInfoFrame.setVisible(true);
    }


}
