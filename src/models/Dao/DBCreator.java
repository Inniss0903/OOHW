package models.Dao;

import services.AppPreference;

import java.sql.*;

public class DBCreator {
    //与数据库的连接
    private static Connection con;
    //连接数据库所需的参数
    private static final String URL = "jdbc:mysql://localhost/";
    private static final String URLParams = "?userSSL=true&useUnicode=true&characterEncoding=UTF8&serverTimezone=GMT";
    private static final String USER = "root";
    private static final String PWD = "gemwang";

    /**
     * 利用MySQL的JDBC创建数据库
     * @param dbName
     */
    public void createDB(String dbName) {

        try {
            //加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            //获取数据库连接
            con = DriverManager.getConnection(URL + URLParams, USER, PWD);
            //创建Statement
            Statement smt = con.createStatement();
            //查询数据库是否存在的sql语句
            String checkDBSql = String.format("SHOW DATABASES LIKE '%s';", dbName);
            //执行结果
            ResultSet resultSet = smt.executeQuery(checkDBSql);

            //对判断数据库是否存在做出响应
            responseResult(smt, resultSet, dbName);

            //关闭连接
            smt.close();
            resultSet.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private void responseResult(Statement smt, ResultSet resultSet, String dbName) throws SQLException {
        if (resultSet.next()) {
            System.out.println(dbName + "数据库存在");
        } else {
            //不存在则创建
            System.out.println(dbName + "数据库不存在, 准备创建");
            String createDBSql = String.format("CREATE DATABASE IF NOT EXISTS " +
                    "%s character set utf8;", dbName);
            smt.execute(createDBSql);
        }

        //再次检查是否完成创建
        ResultSet checkAgain = smt.executeQuery(String.format("SHOW DATABASES LIKE '%s';", dbName));
        if (checkAgain.next()) {
            //创建完成后写入配置中
            System.out.println("prepare for writing in AppPreference");
            AppPreference appPreference = new AppPreference();
            appPreference.updateDBStation(dbName, true);
        }
    }


}
