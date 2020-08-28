package models.Dao;

import services.AppPreference;

import java.sql.*;

public class DBConnection {
    //数据库名
    private static String dbName;
    //与数据库的连接
    private static Connection con;
    //连接数据库所需的参数
    private static final String URL = "jdbc:mysql://localhost/";
    private static final String URLParams = "?userSSL=true&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT";
    private static final String USER = "root";
    private static final String PWD = "gemwang";

    public DBConnection() {
        dbName = "magsys";
        //数据库不存在则创建
        if (!hasCreatedDB(dbName)) {
            new DBCreator().createDB(dbName);
        }
        //建立连接
        setConnection(URL + dbName + URLParams);
    }



    /**
     * 与所需数据库建立连接
     * @param url
     */
    private void setConnection(String url) {
        try {
            //加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            //获取数据库连接
            con = DriverManager.getConnection(url, USER, PWD);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("连接到数据库" + dbName);
    }

    /**
     * 获取MySQL数据库的连接
     * @return
     */
    public Connection getConnection() {
        return con;
    }

    /**
     * 判断该数据库是否存在
     * @param dbName
     * @return
     */
    private boolean hasCreatedDB(String dbName) {
        AppPreference appPrefs = new AppPreference();
        return appPrefs.queryDBStation(dbName);
    }

}
