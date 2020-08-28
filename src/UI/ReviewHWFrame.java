package UI;

import services.HomeworkDAOFactory;
import services.UIService.ReviewHW;
import services.UserDAOFactory;
import services.UserType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class ReviewHWFrame extends JFrame implements ActionListener, ItemListener {
    private JPanel mainPanel;
    // TODO 是否为数字
    private JTextField tfHWNumber, tfGrade, tfStuGrade;
    private JComboBox cbHWSubmit, cbStudentName, cbStudentHW;
    private JButton btnHWQuery, btnDownload, btnGrade, btnStuQuery;
    private static final String HW_QUERY  = "作业查询";
    private static final String DOWNLOAD = "下载";
    private static final String GRADE = "打分";
    private static final String STU_QUERY = "学生查询";



    //存放当前作业提交学生的信息， 选课学生列表，指定学生的作业情况
    private List<String> hwStudentId, hwList, stuList, assStuHWList;
    //当前被评分的学生的id，以及批改的作业编号, 制定学生id
    private int gradeStudentId, gradeHWNumber, assStuId;

    private ReviewHW reviewHWService;

    public ReviewHWFrame() {
        //初始化一下存储信息
        initData();
        reviewHWService = new ReviewHW();
        //设置窗口属性
        setWindowProperties();
        //设置主面板的属性
        setMainPanelProperties();
    }

    /**
     * 初始化一些存储信息
     */
    private void initData() {
        hwStudentId = new ArrayList<>();
        hwList = new ArrayList<>();
        stuList = new ArrayList<>();
        assStuHWList = new ArrayList<>();

        gradeStudentId = -1;
        gradeHWNumber = 0;
        assStuId = -1;

    }

    /**
     * 设置该程序窗口的属性
     */
    private void setWindowProperties() {
        //设置窗口标题
        setTitle("作业批改界面");
        //点击窗口"x"号即可关闭程序
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //程序窗口大小以及位置
        setSize(400, 500);
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
        //某次上交作业情况
        addHWSubmission();
        //指定同学的作业情况
        addAssignedStudentHW();

    }

    /**
     * 统计某次作业的上交情况
     */
    private void addHWSubmission() {
        JPanel jpSub = new JPanel();
        jpSub.setLayout(new GridLayout(2,1));

        addQueryHW(jpSub);

        //显示某次作业的提交情况并可以评分
        addShowHW(jpSub);

        mainPanel.add(jpSub);

    }

    /**
     * 添加某次作业的统计情况面板
     * @param jpSub
     */
    private void addShowHW(JPanel jpSub) {
        //显示某次作业的情况
        JPanel jp2 = new JPanel();
        cbHWSubmit = new JComboBox();  //显示当前作业提交人数
        cbHWSubmit.addItemListener(this);

        btnDownload = new JButton(DOWNLOAD);    //下载当前作业
        btnDownload.addActionListener(this);

        tfGrade = new JTextField(); //得分文本框
        tfGrade.setColumns(4);

        btnGrade = new JButton(GRADE);  //打分
        btnGrade.addActionListener(this);
        jp2.add(cbHWSubmit);jp2.add(tfGrade);jp2.add(btnDownload);jp2.add(btnGrade);

        jpSub.add(jp2);

    }

    /**
     * 添加查询某次作业的导航面板
     * @param jpSub
     */
    private void addQueryHW(JPanel jpSub) {
        //查询某次作业
        JPanel jp1 = new JPanel();
        JLabel jlHWNumber = new JLabel("要查询的作业编号：");
        tfHWNumber = new JTextField();   //作业编号文本框
        tfHWNumber.setColumns(3);
        tfHWNumber.setText(String.valueOf(HomeworkDAOFactory.getCurrentHWNumber()));

        btnHWQuery = new JButton(HW_QUERY); //作业查询按钮
        btnHWQuery.addActionListener(this);
        jp1.add(jlHWNumber);jp1.add(tfHWNumber); jp1.add(btnHWQuery);

        jpSub.add(jp1);
    }

    /**
     * 统计指定学生的上交情况
     */
    private void addAssignedStudentHW() {
        JPanel jpAss = new JPanel();
        jpAss.setLayout(new GridLayout(2,1));

        addStuQueryPanel(jpAss);
        addStuHwPanel(jpAss);

        mainPanel.add(jpAss);
    }


    /**
     * 添加展示需交作业学生面板
     * @param jpAss
     */
    private void addStuQueryPanel(JPanel jpAss) {
        JPanel jpSQ = new JPanel();
        JLabel jlStu = new JLabel("要查询的学生");
        cbStudentName = new JComboBox();
        cbStudentName.addItemListener(this);

        btnStuQuery = new JButton(STU_QUERY);
        btnStuQuery.addActionListener(this);

        jpSQ.add(jlStu);jpSQ.add(cbStudentName);jpSQ.add(btnStuQuery);

        jpAss.add(jpSQ);

    }

    /**
     * 添加指定学生的作业情况
     * @param jpAss
     */
    private void addStuHwPanel(JPanel jpAss) {
        JPanel jpHW = new JPanel();
        JLabel jlHW = new JLabel("学生提交作业情况");
        cbStudentHW = new JComboBox();
        cbStudentHW.addItemListener(this);

        tfStuGrade = new JTextField();
        tfStuGrade.setColumns(3);

        jpHW.add(jlHW);jpHW.add(cbStudentHW);jpHW.add(tfStuGrade);

        jpAss.add(jpHW);
    }

    /**
     * 添加作业列表
     * @param hwList
     */
    private void addHWItem(List<String> hwList) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //添加之前清除所有item
                cbHWSubmit.removeAllItems();
                //列表为空表示无人提交作业
                if (hwList.size() == 0) {
                    cbHWSubmit.addItem("当前作业无人提交");
                    return;
                }
                //循环加载item
                for (String item : hwList) {
                    cbHWSubmit.addItem(item);
                }
            }
        });
    }

    /**
     * 添加选课学生列表
     * @param stuList
     */
    private void addStuItem(List<String> stuList) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cbStudentName.removeAllItems();
                if (stuList.size() == 0) {
                    cbStudentName.addItem("当前无选课学生");
                    return;
                }

                for (String item : stuList) {
                    cbStudentName.addItem(item);
                }
            }
        });

    }

    /**
     * 添加指定学生的作业情况
     * @param assStuList
     */
    private void addAssStuHW(List<String> assStuList) {
        SwingUtilities.invokeLater(() -> {
            cbStudentHW.removeAllItems();
            if (assStuList.size() == 0) {
                cbStudentHW.addItem("该学生目前未提交过作业");
                return;
            }

            for (String item : assStuList) {
                cbStudentHW.addItem(item);
            }
        });

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case HW_QUERY:
                hwQuery();
                break;
            case DOWNLOAD:
                // TODO 检查是否可以下载
                downloadHW();
                break;
            case GRADE:
                giveAGrade();
                break;
            case STU_QUERY:
                stuQuery();
                break;
        }
    }


    /**
     * 按下查询键，查询本次作业提交信息，然后展示到面板上
     */
    private void hwQuery() {
        gradeHWNumber = Integer.parseInt(tfHWNumber.getText());
        hwStudentId = reviewHWService.queryHWSubIdByNumber(tfHWNumber.getText());
        hwList = reviewHWService.queryHWSubStuNumber(tfHWNumber.getText());
        addHWItem(hwList);
    }

    /**
     * 按下打分键，给当前学生打分
     */
    private void giveAGrade() {
        int hwNumber = Integer.parseInt(tfHWNumber.getText());
        int grade = Integer.parseInt(tfGrade.getText());
        reviewHWService.giveGrade(hwNumber, gradeStudentId, grade);
        //提示打分完成
        JOptionPane.showMessageDialog(this, "已评分", "评分结果", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 下载文件
     */
    private void downloadHW() {
        JFileChooser fcDownload = new JFileChooser();
        fcDownload.setDialogTitle("下载作业");
        fcDownload.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int val = fcDownload.showOpenDialog(ReviewHWFrame.this);
        if (val == JFileChooser.APPROVE_OPTION) {   //确定选择文件
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ReviewHWFrame.this,
                    "正在下载文件至" + fcDownload.getSelectedFile().getPath(),
                    "作业文件下载", JOptionPane.INFORMATION_MESSAGE));
            reviewHWService.downloadHW(fcDownload.getSelectedFile().getPath(), gradeStudentId, gradeHWNumber);

        }

    }

    /**
     * 查询选课学生
     */
    private void stuQuery() {
        stuList = reviewHWService.getStudentList();
        addStuItem(stuList);
    }


    @Override
    public void itemStateChanged(ItemEvent e) {
        //老师查看作业提交情况
        if (e.getSource() == cbHWSubmit) {
            System.out.println("开始选择学生作业:");
            //记录当前要被评分的学生id
            gradeStudentId = reviewHWService.recordId(hwStudentId, cbHWSubmit.getSelectedIndex());
            //刷新得分文本框
            refreshGradeText();
        } else if (e.getSource() == cbStudentName) {
            //学生名单，点击学生名单，刷新作业信息
            refreshAssStudentHWInfo();
        } else if (e.getSource() == cbStudentHW) {
            //指定学生作业信息，点击查看分数
            String gradeInfo = reviewHWService.getAssHWGrade(assStuId, cbStudentHW.getSelectedIndex(), assStuHWList);
            refreshAssStudentGrade(gradeInfo);
        }

    }


    /**
     * 刷新某位同学的作业信息
     */
    private void refreshAssStudentHWInfo() {
        assStuId = UserDAOFactory.getIdByAccount(UserType.STUDENT,
                stuList.get(cbStudentName.getSelectedIndex()));
        assStuHWList = reviewHWService.getAssHWList(assStuId);
        addAssStuHW(assStuHWList);  //刷新作业列表
    }

    /**
     * 刷新得分文本框
     */
    private void refreshGradeText() {
        SwingUtilities.invokeLater(() -> tfGrade.setText(reviewHWService.getGrade(tfHWNumber.getText(), gradeStudentId)));
    }

    /**
     * 刷新指定学生的得分文本框
     * @param gradeText
     */
    private void refreshAssStudentGrade(String gradeText) {
        SwingUtilities.invokeLater(() -> tfStuGrade.setText(gradeText));
    }
}
