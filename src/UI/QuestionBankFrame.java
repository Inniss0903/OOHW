package UI;

import models.QuestionSingle;
import services.UIService.QuestionBank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class QuestionBankFrame extends JFrame implements ActionListener, ItemListener {
    private JPanel mainPanel;

    private JTextArea taTopic, taKonPoint, taChoiceA, taChoiceB, taChoiceC, taChoiceD;
    private JButton btnAddQue, btnPractice, btnCheck, btnConfirmAdd, btnCancelAdd;
    private JRadioButton rbChoiceA, rbChoiceB, rbChoiceC, rbChoiceD;
    //当前单选题实例
    private QuestionSingle que;
    //用户的选择
    private String userChoice;
    private static final String ADD_QUE = "添加例题";
    private static final String PRACTICE = "练习一题";
    private static final String CHECK = "检查答案";
    private static final String CONFIRM_ADD = "确定添加";
    private static final String CANCEL_ADD = "取消添加";

    private QuestionBank qstBankService;

    public QuestionBankFrame() {
        //初始化数据
        initData();
        qstBankService = new QuestionBank();
        //设置窗口属性
        setWindowProperties();
        //设置主面板的属性
        setMainPanelProperties();
    }

    private void initData() {
        que = new QuestionSingle();
    }

    /**
     * 设置窗口属性
     */
    private void setWindowProperties() {
        //设置窗口标题
        setTitle("题库界面");
        //点击窗口"x"号即可关闭程序
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //程序窗口大小以及位置
        setSize(300, 700);
        FrameTools.setCenter(this);
    }

    /**
     * 设置主面板的属性
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
        //添加导航栏
        addNavigationPanel();
        //添加题目描述
        addQuestionDesc();
        //添加用户选项
        addUserSelect();
        //添加确定提交例题按钮
        addConfirmAddQue();
    }


    /**
     * 设置欢迎标签
     */
    private void addLabelArea() {
        JLabel jlWelcome = new JLabel("欢迎使用题库");
        mainPanel.add(jlWelcome);
    }

    /**
     * 导航栏
     */
    private void addNavigationPanel() {
        JPanel jpNav = new JPanel();
        btnAddQue = new JButton(ADD_QUE);
        btnPractice = new JButton(PRACTICE);
        btnCheck = new JButton(CHECK);

        btnAddQue.addActionListener(this);
        btnPractice.addActionListener(this);
        btnCheck.addActionListener(this);

        jpNav.add(btnAddQue);jpNav.add(btnPractice);jpNav.add(btnCheck);

        mainPanel.add(jpNav);

    }

    /**
     * 题目描述
     */
    private void addQuestionDesc() {
        JPanel jpQueDesc = new JPanel();
        jpQueDesc.setLayout(new GridLayout(9,1, 5,5));
        jpQueDesc.setBackground(Color.lightGray);
        taTopic = new JTextArea();
        taChoiceA = new JTextArea();
        taChoiceB = new JTextArea();
        taChoiceC = new JTextArea();
        taChoiceD = new JTextArea();
        taKonPoint = new JTextArea();

        //文本框属性
        FrameTools.setQuestionTextProp(taTopic);
        FrameTools.setQuestionTextProp(taChoiceA);
        FrameTools.setQuestionTextProp(taChoiceB);
        FrameTools.setQuestionTextProp(taChoiceC);
        FrameTools.setQuestionTextProp(taChoiceD);
        FrameTools.setQuestionTextProp(taKonPoint);


        jpQueDesc.add(new JLabel("题目描述"));
        jpQueDesc.add(taTopic);
        jpQueDesc.add(new JLabel("题目选项"));
        jpQueDesc.add(taChoiceA);jpQueDesc.add(taChoiceB);
        jpQueDesc.add(taChoiceC);jpQueDesc.add(taChoiceD);
        jpQueDesc.add(new JLabel("题目知识点"));
        jpQueDesc.add(taKonPoint);

        mainPanel.add(jpQueDesc);

    }

    /**
     * 用户选项
     */
    private void addUserSelect() {
        JPanel jpUserSe = new JPanel();
        ButtonGroup btng = new ButtonGroup();
        rbChoiceA = new JRadioButton("A");
        rbChoiceB = new JRadioButton("B");
        rbChoiceC = new JRadioButton("C");
        rbChoiceD = new JRadioButton("D");

        //监听选项
        rbChoiceA.addItemListener(this);
        rbChoiceB.addItemListener(this);
        rbChoiceC.addItemListener(this);
        rbChoiceD.addItemListener(this);

        btng.add(rbChoiceA);
        btng.add(rbChoiceB);
        btng.add(rbChoiceC);
        btng.add(rbChoiceD);

        jpUserSe.add(rbChoiceA);
        jpUserSe.add(rbChoiceB);
        jpUserSe.add(rbChoiceC);
        jpUserSe.add(rbChoiceD);

        mainPanel.add(jpUserSe);
    }

    /**
     * 用户选择添加例题
     */
    private void addConfirmAddQue() {
        JPanel jpAdd = new JPanel();
        btnConfirmAdd = new JButton(CONFIRM_ADD);
        btnCancelAdd = new JButton(CANCEL_ADD);

        btnConfirmAdd.setVisible(false);
        btnCancelAdd.setVisible(false);

        btnCancelAdd.addActionListener(this);
        btnConfirmAdd.addActionListener(this);

        jpAdd.add(btnConfirmAdd);
        jpAdd.add(btnCancelAdd);

        mainPanel.add(jpAdd);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ADD_QUE:
                addQue();
                break;
            case PRACTICE:
                //从题库中获取例题
                que = qstBankService.getOneQuest();
                refreshQUE(que);
                break;
            case CHECK:
                checkAnswer();
                break;
            case CONFIRM_ADD:
                confirmAdd();
                break;
            case CANCEL_ADD:
                cancelAdd();
                break;
        }

    }


    /**
     * 添加例题
     */
    private void addQue() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //按钮可见
                btnConfirmAdd.setVisible(true);
                btnCancelAdd.setVisible(true);
                JOptionPane.showMessageDialog(QuestionBankFrame.this, "请依次向下输入题目信息，" +
                        "ABCD四个选项，然后在输入本题知识点，最后选择正确的选项，点击确认提交即可");
                FrameTools.editTextAreaStation(taTopic);
                FrameTools.editTextAreaStation(taChoiceA);
                FrameTools.editTextAreaStation(taChoiceB);
                FrameTools.editTextAreaStation(taChoiceC);
                FrameTools.editTextAreaStation(taChoiceD);
                FrameTools.editTextAreaStation(taKonPoint);
            }
        });
    }

    /**
     * 添加例题
     */
    private void confirmAdd() {
        QuestionSingle qs = new QuestionSingle();
        qs.setTopic(taTopic.getText());
        qs.setKnowledgePoint(taKonPoint.getText());
        qs.setChoiceA(taChoiceA.getText());
        qs.setChoiceB(taChoiceB.getText());
        qs.setChoiceC(taChoiceC.getText());
        qs.setChoiceD(taChoiceD.getText());
        qs.setRightAnswer(userChoice);
        //存储例题
        String storeInfo = qstBankService.storeQueSin(qs);
        JOptionPane.showMessageDialog(this, storeInfo,
                "例题添加结果", JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * 使确认和提交两个按钮不可见
     */
    private void invisibleAdd() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                btnConfirmAdd.setVisible(false);
                btnCancelAdd.setVisible(false);
            }
        });
    }

    /**
     * 取消添加例题
     */
    private void cancelAdd() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrameTools.setQuestionTextProp(taTopic);
                FrameTools.setQuestionTextProp(taChoiceA);
                FrameTools.setQuestionTextProp(taChoiceB);
                FrameTools.setQuestionTextProp(taChoiceC);
                FrameTools.setQuestionTextProp(taChoiceD);
                FrameTools.setQuestionTextProp(taKonPoint);
                taKonPoint.setVisible(false);

                invisibleAdd();
            }
        });
    }


    /**
     * 刷新题目
     * @param qs
     */
    private void refreshQUE(QuestionSingle qs) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                taTopic.setText("题目：" + qs.getTopic());
                taChoiceA.setText("A." + qs.getChoiceA());
                taChoiceB.setText("B." + qs.getChoiceB());
                taChoiceC.setText("C." + qs.getChoiceC());
                taChoiceD.setText("D." + qs.getChoiceD());
                taKonPoint.setText("知识点：" + qs.getKnowledgePoint());
                taKonPoint.setVisible(false);
            }
        });
    }

    /**
     * 检查答案，刷新界面
     */
    private void checkAnswer() {
        String answerInfo = qstBankService.checkAnswer(que.getRightAnswer(), userChoice);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                taKonPoint.setVisible(true);
                JOptionPane.showMessageDialog(QuestionBankFrame.this, answerInfo, "答题情况", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (rbChoiceA.isSelected()) {
            userChoice = "A";
        } else if (rbChoiceB.isSelected()) {
            userChoice = "B";
        } else if (rbChoiceC.isSelected()) {
            userChoice = "C";
        } else if (rbChoiceD.isSelected()) {
            userChoice = "D";
        }
    }
}
