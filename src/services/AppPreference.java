package services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class AppPreference {
    private static final String AppVersionLabel = "appVersion";
    private static final String AppInitLabel = "appInit";
    private static final String AccountRemember = "hasAccountRemembered";
    private static final String PasswordRemember = "hasPasswordRemembered";
    private static final String Account = "account";
    private static final String Password = "password";
    private static final String DBStationLabel = "hasCreatedDB_";
    private static final String TableStationLabel = "hasCreatedTable_";
    private static final String CurrentHW = "currentHWNumber";

    //创建preference实例，位于项目主目录
    Preferences prefs = Preferences.userRoot().node("/src");


    /**
     * 更新app版本号
     * @param v
     */
    public void updateAppVersion(String v) {
        prefs.put(AppVersionLabel, v);
    }

    /**
     * 查询app版本号，-1代表版本号丢失
     * @return
     */
    public String getAppVersion() {
        return prefs.get(AppVersionLabel, "-1");
    }

    /**
     * app是否初始化
     * @param init
     */
    public void changeInitStation(boolean init) {
        prefs.putBoolean(AppInitLabel,init);
    }

    /**
     * 获取app初始化状态
     * @return
     */
    public boolean getInitStation() {
        return prefs.getBoolean(AppInitLabel, false);
    }

    /**
     * 账号是否要记住
     * @param s
     */
    public void changeAccountRememberStation(boolean s) {
        prefs.putBoolean(AccountRemember, s);
    }

    /**
     * 账号是否记住
     * @return
     */
    public boolean getAccountRememberStation() {
        return prefs.getBoolean(AccountRemember, false);
    }

    /**
     * 密码是否要记住
     * @param s
     */
    public void changePasswordRememberStation(boolean s) {
        prefs.putBoolean(PasswordRemember, s);
    }

    /**
     * 密码是否记住
     * @return
     */
    public boolean getPasswordRememberStation() {
        return prefs.getBoolean(PasswordRemember, false);
    }

    /**
     * 保存当前登录用户的账号
     * @param acc
     */
    public void setAccount(String acc) {
        prefs.put(Account, acc);
    }

    /**
     * 获取保存的账号
     * @return
     */
    public String getAccount() {
        return prefs.get(Account, "");
    }

    /**
     * 存储当前登录用户密码
     * @param pwd
     */
    public void setPassword(String pwd) {
        prefs.put(Password, pwd);
    }

    /**
     * 获取存储的密码
     * @return
     */
    public String getPassword() {
        return prefs.get(Password, "");
    }

    /**
     * 更改数据库是否创建的状态
     * @param dbName
     * @param hasCreated
     */
    public void updateDBStation(String dbName, boolean hasCreated) {
        if (hasCreated) {
            prefs.putBoolean(DBStationLabel + dbName, true);
        } else {
            prefs.putBoolean(DBStationLabel + dbName, false);
        }
    }

    /**
     * 查询该数据库是否已经创建
     * @param dbName
     * @return
     */
    public boolean queryDBStation(String dbName) {
        //默认数据库不存在
        boolean station = prefs.getBoolean(DBStationLabel + dbName, false);
        return station;
    }

    /**
     * 更改table是否创建的状态
     * @param tbName
     * @param hasCreated
     */
    public void updateTableStation(String tbName, boolean hasCreated) {
        if (hasCreated) {
            prefs.putBoolean(TableStationLabel + tbName, true);
        } else {
            prefs.putBoolean(TableStationLabel + tbName, false);
        }
    }

    /**
     * 查询该table是否创建
     * @param tbName
     * @return
     */
    public boolean queryTableStation(String tbName) {
        boolean station = prefs.getBoolean(TableStationLabel + tbName, false);
        return station;
    }

    /**
     * 设置当前作业编号
     * @param number
     */
    public void setCurrentHW(int number) {
        prefs.putInt(CurrentHW, number);
    }

    /**
     * 获取当前作业编号
     * @return
     */
    public int getCurrentHW() {
        return prefs.getInt(CurrentHW,0);
    }

    /**
     * 将preference文件导出为XML文件
     */
    public void export2XML() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("app.xml");
            prefs.exportNode(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
