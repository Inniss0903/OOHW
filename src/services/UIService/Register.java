package services.UIService;

import models.Dao.TableCURD;
import models.Dao.TableCreator;
import models.Student;
import models.Teacher;
import services.UserType;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class Register {

    //检查注册信息的合法性
    /**
     * 注册信息合法
     */
    public static final int CHECK_INFO_PASS = 0;
    /**
     * 两次密码输入不一致
     */
    public static final int CHECK_INFO_PWD_INCONSISTENT = 1;
    /**
     * 信息填写不完整，有空缺
     */
    public static final int CHECK_INFO_EMPTY = 2;

    /**
     * 检测用户填写的注册信息是否合法
     * @param name
     * @param account
     * @param pwd
     * @param pwdCheck
     * @return
     */
    public int checkRegisterInfo(String name, String account, String pwd, String pwdCheck) {
        int info = CHECK_INFO_PASS;
        if (name.equals("") || account.equals("") ||
        pwd.equals("")) {
            info = CHECK_INFO_EMPTY;
        }
        if (!pwd.equals(pwdCheck)) {
            info = CHECK_INFO_PWD_INCONSISTENT;
        }
        return info;
    }

    /**
     * 根据检查用户注册信息的类型返回其详细内容
     * @param type
     * @return
     */
    public String getInfoByType(int type) {
        String info = "";
        switch (type) {
            case CHECK_INFO_PASS:
                info = "信息填写正确";
                break;
            case CHECK_INFO_PWD_INCONSISTENT:
                info = "两次密码输入不一致";
                break;
            case CHECK_INFO_EMPTY:
                info = "信息不完整，有空缺";
                break;
        }

        return info;
    }

    /**
     * 将用户信息存储到数据库中
     * @param user
     * @param name
     * @param account
     * @param pwd
     */
    public void storeUser(int user, String name, String account, String pwd) {
        TableCURD tableCURD = new TableCURD();
        LinkedHashMap<String, String> record = new LinkedHashMap<>();
        record.put("name", TableCURD.SQLStringType(name));
        record.put("password", TableCURD.SQLStringType(pwd));
        switch (user) {
            case UserType.STUDENT:
                System.out.println("存储学生信息");
                record.put("limitLevel", "1");
                record.put("studentNumber", TableCURD.SQLStringType(account));
                try {
                    tableCURD.insert(TableCreator.getTbNameByClass(Student.class.getName()),record);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case UserType.TEACHER:
                System.out.println("存储教师信息");
                record.put("limitLevel", "2");
                record.put("teacherNumber", TableCURD.SQLStringType(account));
                try {
                    tableCURD.insert(TableCreator.getTbNameByClass(Teacher.class.getName()), record);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
