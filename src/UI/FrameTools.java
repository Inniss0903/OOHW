package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FrameTools {

    /**
     * 将Frame设置到屏幕中央
     * @param f
     */
    public static void setCenter(Frame f) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        int x = (kit.getScreenSize().width - f.getWidth()) / 2;
        int y = (kit.getScreenSize().height - f.getHeight()) / 2;
        f.setLocation(x, y);
    }

    /**
     * 设置文本域不可编辑，自动换行，列宽18
     * @param ta
     */
    public static void setQuestionTextProp(JTextArea ta) {
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setColumns(18);
    }

    /**
     * 使文本域处于编辑状态
     */
    public static void editTextAreaStation(JTextArea ta) {
        ta.setVisible(true);
        ta.setEditable(true);
    }

    /**
     * 实现文本框不选中的文字提示功能
     */
    static class JTextFieldHintListener implements FocusListener {
        private String hintText;
        private JTextField textField;

        public JTextFieldHintListener(JTextField jTextField,String hintText) {
            this.textField = jTextField;
            this.hintText = hintText;
            jTextField.setText(hintText);  //默认直接显示
            jTextField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(FocusEvent e) {
            //获取焦点时，清空提示内容
            String temp = textField.getText();
            if(temp.equals(hintText)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            //失去焦点时，没有输入内容，显示提示内容
            String temp = textField.getText();
            if(temp.equals("")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }
        }
    }
}
