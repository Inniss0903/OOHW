package models.Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TableCURD {
    //jdbc的连接
    private Connection connection;

    /**
     * 构造函数初始化与数据库的连接
     */
    public TableCURD() {
        DBConnection dbc = new DBConnection();
        connection = dbc.getConnection();
    }

    /**
     * 根据table名，字段名（列名）和字段（列）对应的值生成完整的SQL命令以插入数据
     * 例如：tbName为user，record内有两个键值对【"id" - "1"】【"name" - "gem"】
     * 使用此函数就会生成如下的SQL语句：
     * INSERT INTO user
     * (id,name)
     * VALUES(1,'gemwang');
     * @param tbName 表名
     * @param record 要插入的字段及其对应的值
     * @return 完整的插入指令
     */
    public String createInsertSQLCommand(String tbName, LinkedHashMap<String, String> record ) {
        String insertSQL = "INSERT INTO " + tbName + "\n";
        StringBuilder filedSQL = new StringBuilder("(");
        StringBuilder valuesSQL = new StringBuilder(")\nVALUES(");

        //遍历字段-值的hashmap
        for (String field : record.keySet()) {
            filedSQL.append(field + ",");
            valuesSQL.append(record.get(field) + ",");
        }
        //删除多于的","，拼接成完整的insert命令
        insertSQL += filedSQL.substring(0, filedSQL.length() - 1) +
                valuesSQL.substring(0, valuesSQL.length() - 1) + ");";

        return insertSQL;
    }

    /**
     * 将数据插入到表中
     * @param tbName 表名
     * @param record 要插入的字段及其对应的值
     * @throws SQLException SQL异常
     */
    public void insert(String tbName, LinkedHashMap<String, String> record) throws SQLException {
        String insertSQL = createInsertSQLCommand(tbName, record);
        execute(insertSQL);
    }

    /**
     * 生成删除指令
     * 例如：tbName为user，deleteCondition为【"id" - "1"，"name" - "'gem'"】
     * 生成如下的SQL指令：
     * DELETE FROM user where id=1 AND name='gem';
     * @param tbName 表名
     * @param deleteCondition 将要删除记录的约束条件
     * @return 完整的SQL删除指令
     */
    public String createDeleteSQLCommand(String tbName, LinkedHashMap<String, String> deleteCondition) {
        String deleteSQL = "DELETE FROM " + tbName;
        String condition = createWhereConditionSQL(deleteCondition);
        deleteSQL += condition + ";";
        return deleteSQL;
    }

    /**
     * 执行数据库的删除指令
     * @param tbName 表名
     * @param deleteCondition 将要删除记录的约束条件
     * @throws SQLException SQL异常
     */
    public void delete(String tbName, LinkedHashMap<String, String> deleteCondition) throws SQLException {
        String deleteSQL = createDeleteSQLCommand(tbName, deleteCondition);
        execute(deleteSQL);
    }

    /**
     * 生成指令——在表中查询某一个字段的所有信息
     * 例如：tbName为user，filed为id，queryCondition为【"id" - "2"】
     * 可生成如下的SQL语句：
     * SELECT id FROM user WHERE id=2;
     * @param tbName 数据库名
     * @param field 要查询的字段
     * @param queryCondition 查询条件
     * @return 完整的SQL查询指令
     */
    public String createQuerySQLCommand(String tbName, String field,
                                        LinkedHashMap<String, String> queryCondition) {
        String querySQL = String.format("SELECT %s FROM %s", field, tbName);
        String conditon = createWhereConditionSQL(queryCondition);
        querySQL += conditon;

        return querySQL + ";";
    }

    /**
     * 生成指令——在表中查询多个字段的所有信息
     * 例如：tbName为user，filedList为[id, name]，queryCondition为【"id" - "2"】
     * 可生成如下的SQL语句：
     * SELECT id,name FROM user WHERE id=2;
     * @param tbName 数据库名
     * @param fieldList 要查询的字段列表
     * @param queryCondition 要查询的限制条件
     * @return 完整的SQL查询指令
     */
    public String createQuerySQLCommand(String tbName, List<String> fieldList,
                                        LinkedHashMap<String, String> queryCondition) {
        String fields;
        //若filedList为空则默认搜寻所有字段
        if (!fieldList.isEmpty()) {
            StringBuilder filedsBuilder = new StringBuilder();
            for (String filed : fieldList) {
                filedsBuilder.append(filed + ",");
            }
            fields = filedsBuilder.substring(0, filedsBuilder.length() - 1);
        } else {
            fields = "*";
        }

        return createQuerySQLCommand(tbName, fields, queryCondition);
    }

    /**
     * 在表中查询某个字段
     * @param tbName 表名
     * @param filed 字段（列）
     * @param queryCondition 查询条件
     * @return 完整的查询指令
     * @throws SQLException SQL异常
     */
    public List<String> query(String tbName, String filed,
                           LinkedHashMap<String, String> queryCondition) throws SQLException {
        // TODO 两个query
        String querySQL = createQuerySQLCommand(tbName, filed, queryCondition);
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(querySQL);
        //获取结果集的数据
        List<String> resultList = processResultSet(resultSet);
        stmt.close();
        return resultList;
    }

    /**
     * 在表中查询某些字段
     * @param tbName 表名
     * @param filedList 字段（列）名列表
     * @param queryCondition 查询条件
     * @return 完整的查询指令
     * @throws SQLException
     */
    public List<List<String>> query(String tbName, List<String> filedList,
                           LinkedHashMap<String, String> queryCondition) throws SQLException {
        String querySQL = createQuerySQLCommand(tbName, filedList, queryCondition);
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(querySQL);
        //获取结果集的数据
        List<List<String>> resultList = processResultSet(resultSet, filedList.size());
        stmt.close();
        return resultList;
    }
    //todo
//    public List<List<String>> queryAllColumns(String tbName, LinkedHashMap<String, String> queryCondition) {
//        String querySQL = createQuerySQLCommand(tbName, new ArrayList<>(),queryCondition);
//        Statement stmt = connection.createStatement();
//        ResultSet resultSet = stmt.executeQuery(querySQL);
//
//    }


    /**
     * 生成完整的更新SQL指令
     * @param tbName 表名
     * @param updateFileds 更新字段名
     * @param updateCondition 更新的约束条件
     * @return 完整的更新指令
     */
    public String createUpdateSQLCommand(String tbName, LinkedHashMap<String, String> updateFileds,
                                         LinkedHashMap<String, String> updateCondition) {
        String updateSQL = String.format("UPDATE %s SET ", tbName);
        StringBuilder newRecord = new StringBuilder();
        // TODO 处理updateFields为空的情况
        for (String urd : updateFileds.keySet()) {
            newRecord.append(String.format("%s=%s,", urd, updateFileds.get(urd)));
        }
        String condition = createWhereConditionSQL(updateCondition);
        updateSQL += newRecord.substring(0, newRecord.length() - 1) + condition + ";";

        return updateSQL;
    }

    /**
     * 执行数据库的更新指令
     * @param tbName 表名
     * @param updateFields 要更新的字段名们
     * @param updateCondition 更新的约束条件
     * @throws SQLException SQL异常
     */
    public void update(String tbName, LinkedHashMap<String, String> updateFields,
                       LinkedHashMap<String, String> updateCondition) throws SQLException {
        String updateSQL = createUpdateSQLCommand(tbName, updateFields, updateCondition);
        execute(updateSQL);
    }


    /**
     * 根据限制条件创建完整的where条件语句
     * 例如：condition为【"id" - "2", "name" - "'gem'"】
     * 生成如下的语句：
     *  WHERE id=2 AND name='gem'
     * @param condition where约束条件
     * @return 带where的约束条件
     */
    private String createWhereConditionSQL(LinkedHashMap<String, String> condition) {
        String cond = "";
        //限制条件为空则返回空字符串，否则创建完整的条件语句
        if (!condition.isEmpty()) {
            for (String field : condition.keySet()) {
                cond += String.format(" %s=%s AND",field, condition.get(field));
            }
            //删除多余的" AND"字符串
            cond = " WHERE" + cond.substring(0, cond.length() - 4);
        }

        return cond;
    }

    /**
     * 执行Statement的execute方法
     * @param SQLCommand
     * @throws SQLException
     */
    private void execute(String SQLCommand) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(SQLCommand);
        stmt.close();
    }

    /**
     * 将结果集处理列表，忽略查询的列值类型，一律返回String
     * 此方法返回的每行只有一个列
     * @param rs 数据库执行完的结果集
     * @return 记录组成的列表
     * @throws SQLException SQL异常
     */
    private List<String> processResultSet(ResultSet rs) throws SQLException {
        List<String> rsList = new ArrayList<>();
        while (rs.next()) {
            rsList.add(rs.getString(1));
        }
        return rsList;
    }

    /**
     * 将结果集处理列表，忽略查询的列值类型，一律返回String
     * 此方法返回的每行有多个列
     * @param rs 数据库执行完的结果集
     * @param size 要返回的列的个数
     * @return 记录组成的列表
     * @throws SQLException
     */
    private List<List<String>> processResultSet(ResultSet rs, int size) throws SQLException {
        List<List<String>> rsList = new ArrayList<>();
        //TODO 怎么获取一个表中列的个数
        while (rs.next()) {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= size; i++) {
                row.add(rs.getString(i));
            }
            rsList.add(row);
        }

        return rsList;
    }
    /**
     * 将java的String值添加单引号，以成为满足数据库String类型的值
     * @param s
     * @return
     */
    public static String SQLStringType(String s) {
        return String.format("'%s'", s);
    }


}
