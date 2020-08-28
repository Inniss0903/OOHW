package models.Dao;

import models.DBAnnotations.DBConstraints;
import models.DBAnnotations.DBTable;
import models.DBAnnotations.SQLInteger;
import models.DBAnnotations.SQLString;
import services.AppPreference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableCreator {
    //table创建
    private String tbName;
    private String tableCreateSQL;
    //app配置信息
    private AppPreference pref;


    /**
     * 构造函数初始化参数
     */
    public TableCreator() {
        tbName = "";
        tableCreateSQL = "";
        pref = new AppPreference();
    }

    /**
     * 通过类名来创建table，生成一个完整的MySQL语句
     * @param className
     * @throws ClassNotFoundException
     */
    private void createTableSQL(String className) throws ClassNotFoundException {
        //加载该类
        Class<?> cl = Class.forName(className);
        tbName = getTbNameByClass(className);
        System.out.println("table name is " + tbName);

        //生成table中的column命令集
        List<String> columnDefs = new ArrayList<String>();
        for (Field field : cl.getDeclaredFields()) {
            //获取一个注解
            Annotation[] anns = field.getDeclaredAnnotations();

            //该注解不是table的column则跳过
            if (anns.length < 1)
                continue;

            //将生成column语句添加到columDefs列表中
            addColumnDefs(anns[0], field ,columnDefs);

            //创建table的完整命令
            StringBuilder createCommand = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tbName + "(");
            for (String columnDef : columnDefs) {
                createCommand.append("\n    " + columnDef + ",");
            }
            tableCreateSQL = createCommand.substring(0, createCommand.length() - 1) + ");";
        }

    }

    /**
     * 判断不同的SQL语句类型，进而判断创建的column类型，然后生成标准的MySQL语句
     * @param ann
     * @param field
     * @param columnDefs
     */
    private void addColumnDefs(Annotation ann, Field field, List<String> columnDefs) {
        String columnName = null;
        //int类型
        if (ann instanceof SQLInteger) {
            SQLInteger sInt = (SQLInteger) ann;
            //如果未在注解中定义column的名字，则使用该实体类中的名字
            if (sInt.name().length() < 1) {
                columnName = field.getName();
            } else {
                columnName = sInt.name();
            }

            columnDefs.add(columnName + " INT" + getDBConstraints(sInt.dbConstraints()));
        }

        //字符类型
        if (ann instanceof SQLString) {
            SQLString sString = (SQLString) ann;
            //如果未在注解中定义column的名字，则使用该实体类中的名字
            if (sString.name().length() < 1) {
                columnName = field.getName();
            } else {
                columnName = sString.name();
            }

            columnDefs.add(columnName + " VARCHAR(" + sString.value()
                    + ")" + getDBConstraints(sString.dbConstraints()));
        }

        //待扩展
    }

    /**
     * 根据类名获取table名
     * 一般传入的类都是完整的路径：比如models.Student，需要去掉包名只剩下Student
     * 等同于getSimpleName()，但是为了防止输入错误
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    public static String getTbNameByClass(String className) throws ClassNotFoundException {
        //加载该类
        Class<?> cl = Class.forName(className);
        //根据注解获取table名
        DBTable dbTable = cl.getAnnotation(DBTable.class);
        String dbName = dbTable.name();
        //去掉包名
        String[] dbNameSplit = dbName.split("\\.");
        dbName = dbNameSplit[dbNameSplit.length - 1];
        return dbName;
    }

    /**
     * 通过DBConstraints获取MySQL的约束命令字符
     * @param dbConstraints
     * @return
     */
    private static String getDBConstraints(DBConstraints dbConstraints) {
        String constraints = "";
        //是否可以为空
        if (!dbConstraints.allowNull()) {
            constraints += " NOT NULL";
        }
        //是否为主键
        if (dbConstraints.primaryKey()) {
            constraints += " PRIMARY KEY";
        }
        //值是否唯一
        if (dbConstraints.unique()) {
            constraints += " UNIQUE";
        }
        //是否自增
        if (dbConstraints.autoIncrement()) {
            constraints += " AUTO_INCREMENT";
        }

        return constraints;
    }

    /**
     * 通过类名返回完整的table创建SQL语句
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    public String getTableCreateSQLByClass(String className) throws ClassNotFoundException {
        createTableSQL(className);
        System.out.println(tableCreateSQL);
        return tableCreateSQL;
    }

    /**
     * 根据类名来创建table
     * @param className
     */
    public void createTable(String className) throws ClassNotFoundException {
        //先在app配置中查看是否已经创建该表，已创建则提示并退出
        if (pref.queryTableStation(getTbNameByClass(className))) {
            System.out.println(getTbNameByClass(className) + "表已创建");
            return;
        }
        Statement stmt = null;
        Connection con = new DBConnection().getConnection();
        try {
            //通过已生成的SQL语句进行创建
            stmt = con.createStatement();
            stmt.execute(getTableCreateSQLByClass(className));
            //建立Table后在AppPreference中记录
            pref.updateTableStation(tbName, true);
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
