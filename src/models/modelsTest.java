package models;

import services.AppPreference;
import models.Dao.DBConnection;
import models.Dao.TableCURD;
import models.Dao.TableCreator;
import services.UIService.Manager;
import services.UIService.QuestionBank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class modelsTest {
    public static final String tbName = "user";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        //已通过的测试
//        tableCreatorSQLTest();
//        dbConnectionTest();
//        tableCreateTest();
//        export2XMLTest();
//        insertSQLCreateTest();
//        querySQLCreateTest();
        //准备测试
//        deleteSQLCreateTest();
//        updateSQLCreateTest();
        //未完成的测试
//        export2XMLTest();
//        AppPreference appPreference = new AppPreference();
//        System.out.println(appPreference.getAccount() + "\n" + appPreference.getPassword());
        QuestionSingle qs = new QuestionSingle();
        QuestionBank questionBank = new QuestionBank();
        System.out.println(questionBank.checkAnswer(null, null));
    }

    private static void updateSQLCreateTest() {
        System.out.println("\n----------updateSQLCreateTest-----------v");
        LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
        LinkedHashMap<String, String> map2 = new LinkedHashMap<>();
        map1.put("id","3");map1.put("name","'gem'");
        map2.put("id", "2");
        TableCURD tableCURD = new TableCURD();
        System.out.println(tableCURD.createUpdateSQLCommand("user", map1,
                new LinkedHashMap<>()));
        System.out.println(tableCURD.createUpdateSQLCommand("user", map1, map2));
    }

    private static void querySQLCreateTest() throws SQLException {
        System.out.println("\n----------querySQLCreateTest-----------v");
        String field = "id";
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        TableCURD t = new TableCURD();
        System.out.println(t.createQuerySQLCommand("user", field, new LinkedHashMap<String, String>()));
        condition.put("id", "2");
        condition.put("name", "'gemwang1'");
        System.out.println(t.createQuerySQLCommand("user", field, condition));

        List<String> list = new ArrayList<>();
        System.out.println(t.createQuerySQLCommand("user", new ArrayList<>(), condition));
        list.add("id");
        list.add("name");
        System.out.println(t.createQuerySQLCommand("user", list, condition));

        System.out.println("结果集：");
        //单列查询
        System.out.println("单列查询结果");
        List<String> fields = new ArrayList<>();
        fields.add("id");fields.add("name");
        List<String> rs = t.query("user","id", condition);
        for (String col : rs) {
            System.out.println("id: " + col);
        }

        //多列查询
        System.out.println("多列查询结果");
        List<List<String>> rsl = t.query(tbName, fields, new LinkedHashMap<>());
        for (List<String> l : rsl) {
            for (String col : l) {
                System.out.print("\t" + col);
            }
            System.out.println("");
        }

    }

    private static void deleteSQLCreateTest() {
        System.out.println("\n----------deleteSQLCreateTest-----------v");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","2");
        map.put("name", "'gemw'");
        TableCURD tableCURD = new TableCURD();
        System.out.println(tableCURD.createDeleteSQLCommand("user",map));

    }

    private static void insertSQLCreateTest() {
        System.out.println("\n----------insertSQLCreateTest-----------v");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("id","3");
        map.put("name", "'33llll'");

        for (String k : map.keySet()) {
            System.out.println("key: " + k + "\tvalue: " + map.get(k));
        }

        TableCURD tableCURD = new TableCURD();
        System.out.println(tableCURD.createInsertSQLCommand("user",  map));

        try {
            tableCURD.insert("user", map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void dbConnectionTest() {
        System.out.println("\n----------DBConnectionTest-----------v");
        DBConnection dbConnection = new DBConnection();
        System.out.println("数据库是否存在" + new AppPreference().queryDBStation("magsys"));
    }

    private static void tableCreatorSQLTest() throws ClassNotFoundException {
        System.out.println("\n------------TableCreatorSQLTest-----------v");
        Student s = new Student();
        System.out.println(s.getClass());
        String sql = new TableCreator().getTableCreateSQLByClass(s.getClass().getName());
    }

    private static void tableCreateTest() throws ClassNotFoundException {
        System.out.println("\n-------TableCreateTest-----------v");
        Student s = new Student();
        new TableCreator().createTable(s.getClass().getName());
        System.out.println("表是否存在" + new AppPreference().queryTableStation(
                TableCreator.getTbNameByClass(s.getClass().getName())));
    }

    private static void export2XMLTest() {
        new AppPreference().export2XML();
    }
}
