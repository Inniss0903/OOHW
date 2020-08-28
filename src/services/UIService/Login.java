package services.UIService;

import models.*;
import models.Dao.TableCreator;
import services.AppPreference;
import services.UserDAOFactory;

public class Login {
    private AppPreference appPreference;

    /**
     * 用户登录信息合法
     */
    public static final int LOGIN_PASS = 0;
    /**
     * 用户账号不存在
     */
    public static final int LOGIN_NO_ACCOUNT = 1;
    /**
     * 用户密码错误
     */
    public static final int LOGIN_WRONG_PWD = 2;

    public Login() {
        appPreference = new AppPreference();
    }

    /**
     * app的初始化设置
     */
    public void appInit() {
        //添加版本号
        appPreference.updateAppVersion("1.0");
        //如果app未初始化
        if (!appPreference.getInitStation()) {
            System.out.println("程序未初始化，准备初始化");
            createTable();
            System.out.println("程序初始化完毕");
            appPreference.changeInitStation(true);
        }

    }

    /**
     * 创建app所需Table
     */
    public void createTable() {
        TableCreator tableCreator = new TableCreator();
        try {
            //创建学生表
            tableCreator.createTable(Student.class.getName());
            //创建教师表
            tableCreator.createTable(Teacher.class.getName());
            //创建作业描述表
            tableCreator.createTable(HomeworkDesc.class.getName());
            //创建作业提交情况表
            tableCreator.createTable(HomeworkSubmit.class.getName());
            //创建题库表（暂时只支持四项单选题）
            tableCreator.createTable(QuestionSingle.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查用户账号密码是否正确
     *
     * @param account
     * @param password
     * @return
     */
    public int userPass(int user, String account, String password) {
        int loginResult = -1;
        int checkResult = UserDAOFactory.checkUserLoginInfo(user, account, password);
        switch (checkResult) {
            case UserDAOFactory.USER_NO_ACCOUNT:
                loginResult = LOGIN_NO_ACCOUNT; //无此账号
                break;
            case UserDAOFactory.USER_PWD_RIGHT:
                loginResult = LOGIN_PASS;   //账号密码正确
                break;
            case UserDAOFactory.USER_PWD_WRONG:
                loginResult = LOGIN_WRONG_PWD;  //密码错误
                break;
        }

        return loginResult;
    }

    /**
     * 账号是否要记住
     * @param s
     */
    public void changeAccRemStation(boolean s) {
        appPreference.changeAccountRememberStation(s);
    }

    /**
     * 账号是否记住
     * @return
     */
    public boolean getAccRemStation() {
        return appPreference.getAccountRememberStation();
    }

    /**
     * 密码是否要记住
     * @param s
     */
    public void changePwdRemStation(boolean s) {
        appPreference.changePasswordRememberStation(s);
    }

    /**
     * 密码是否记住
     */
    public boolean getPwdRemStation() {
        return appPreference.getPasswordRememberStation();
    }

    /**
     * 保存当前登录用户的账号
     * @param acc
     */
    public void setAccount(String acc) {
        appPreference.setAccount(acc);
    }

    /**
     * 获取保存的账号
     * @return
     */
    public String getAccount() {
        return appPreference.getAccount();
    }

    /**
     * 存储当前登录用户密码
     * @param pwd
     */
    public void setPassword(String pwd) {
        appPreference.setPassword(pwd);
    }

    /**
     * 获取存储的密码
     * @return
     */
    public String getPassword() {
        return appPreference.getPassword();
    }

    /**
     * 根据用户需要来保存账号密码
     * @param account
     * @param accR
     * @param pwd
     * @param pwdR
     */
    public void storeAccountAndPassword(String account, boolean accR, String pwd, boolean pwdR) {
        //是否需要保存账号
        if (accR) {
            setAccount(account);
        }
        //是否需要保存密码
        if (pwdR) {
            setPassword(pwd);
        }

    }

}
