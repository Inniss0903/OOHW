package services;

import models.Dao.TableCURD;
import models.Dao.TableCreator;
import models.Student;
import models.Teacher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UserDAOFactory {
    /**
     * 表中无此用户
     */
    public static final int USER_NO_ACCOUNT = 0;
    /**
     * 用户密码错误
     */
    public static final int USER_PWD_WRONG = 1;
    /**
     * 用户密码正确
     */
    public static final int USER_PWD_RIGHT = 2;

    /**
     * @param userType
     * @param account
     * @return
     */
    public static List<String> getPasswordByAccount(int userType, String account) {
        TableCURD tableCURD = new TableCURD();
        List<String> queryResult = null;
        //根据账号执行查询操作
        String tbName = getTableNameByUserType(userType);
        String field = "password";
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        condition.put(getAccountFieldName(userType), String.format("'%s'", account));
        try {
            queryResult = tableCURD.query(getTableNameByUserType(userType), field, condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    /**
     * 检查用户登录的账号情况
     *
     * @param user
     * @param account
     * @param password
     * @return
     */
    public static int checkUserLoginInfo(int user, String account, String password) {
        List<String> queryResult = getPasswordByAccount(user, account);
        //如果返回的结果为空说明账号不存在
        if (queryResult.size() == 0) {
            return USER_NO_ACCOUNT;
        }
        if (queryResult.get(0).equals(password)) {
            return USER_PWD_RIGHT;
        } else {
            return USER_PWD_WRONG;
        }
    }

    /**
     * 根据用户身份获取对应的Table
     *
     * @param userType
     * @return
     */
    private static String getTableNameByUserType(int userType) {
        String tbName = "";
        switch (userType) {
            case UserType.STUDENT:
                try {
                    tbName = TableCreator.getTbNameByClass(Student.class.getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case UserType.TEACHER:
                try {
                    tbName = TableCreator.getTbNameByClass(Teacher.class.getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

        return tbName;
    }

    /**
     * 根据用户身份判断用户账号对应表中的列名（字段名）
     *
     * @param user
     * @return
     */
    private static String getAccountFieldName(int user) {
        String field = "";
        switch (user) {
            case UserType.STUDENT:
                field = "studentNumber";
                break;
            case UserType.TEACHER:
                field = "teacherNumber";
                break;
        }

        return field;
    }

    /**
     * 从数据库中通过账号查询id
     *
     * @param userType
     * @param account
     * @return
     */
    private static List<String> getIdByAccountDao(int userType, String account) {
        TableCURD tableCURD = new TableCURD();
        //生成查询所必须的条件
        String tbName = getTableNameByUserType(userType);
        String filed = "id";
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        condition.put(getAccountFieldName(userType), TableCURD.SQLStringType(account));
        List<String> queryResult = new ArrayList<>();
        //查询
        try {
            queryResult = tableCURD.query(tbName, filed, condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;

    }

    /**
     * 通过账号获取id
     *
     * @param userType
     * @param account
     * @return
     */
    public static int getIdByAccount(int userType, String account) {
        List<String> queryResult = getIdByAccountDao(userType, account);
        if (queryResult.size() == 0) {
            return -1;
        } else {
            int id = Integer.parseInt(queryResult.get(0));
            return id;
        }
    }

    /**
     * 在数据库中通过id获取name
     *
     * @param userType
     * @param id
     * @return
     */
    private static List<String> getNameByIdDao(int userType, int id) {
        TableCURD tableCURD = new TableCURD();
        //获取查询条件必要条件
        String tbName = getTableNameByUserType(userType);
        String field = "name";
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        condition.put("id", String.valueOf(id));
        //查询结果
        List<String> queryResult = new ArrayList<>();
        try {
            queryResult = tableCURD.query(tbName, field, condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;

    }

    /**
     * 在数据库中通过id获取account
     *
     * @param userType
     * @param id
     * @return
     */
    private static List<String> getAccountByIdDao(int userType, int id) {
        TableCURD tableCURD = new TableCURD();
        //获取查询条件必要条件
        String tbName = getTableNameByUserType(userType);
        String field = getAccountNameByUserType(userType);
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        condition.put("id", String.valueOf(id));
        //查询结果
        List<String> queryResult = new ArrayList<>();
        try {
            queryResult = tableCURD.query(tbName, field, condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;

    }

    /**
     * 通过用户id获取用户账号，不存在则为null
     *
     * @param userType
     * @param userId
     * @return
     */
    public static String getAccountById(int userType, int userId) {
        String account = null;
        List<String> queryResult = getAccountByIdDao(userType, userId);
        if (queryResult.size() == 0) {
            return account;
        } else {
            account = queryResult.get(0);
        }
        return account;

    }

    /**
     * 通过用户身份返回账号对应的的列名
     *
     * @param userType
     * @return
     */
    private static String getAccountNameByUserType(int userType) {
        String accountName = "";
        switch (userType) {
            case UserType.STUDENT:
                accountName = "studentNumber";
                break;
            case UserType.TEACHER:
                accountName = "teacherNumber";
        }
        return accountName;
    }

    /**
     * 通过id获取name
     *
     * @param userType
     * @param id
     * @return
     */
    public static String getNameById(int userType, int id) {
        String name = "";
        List<String> result = getNameByIdDao(userType, id);
        //用户名查询不到返回null
        if (result.size() == 0) {
            return null;
        } else {
            name = result.get(0);
        }
        return name;
    }

    /**
     * 根据学生账号（学号）获取学生的基本信息
     *
     * @param id
     * @return
     */
    private static List<List<String>> getStudentInfoDao(int id) {
        TableCURD tableCURD = new TableCURD();
        List<List<String>> queryResult = new ArrayList<>();
        String tbName = getTableNameByUserType(UserType.STUDENT);
        //查询字段
        List<String> fields = new ArrayList<>();
        fields.add("limitLevel");
        fields.add("name");
        fields.add("studentNumber");
        fields.add("password");
        //限制条件
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        condition.put("id", String.valueOf(id));

        try {
            queryResult = tableCURD.query(tbName, fields, condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    /**
     * 获取学生实例，只获取肯定存在而且常用的信息
     *
     * @param id
     * @return
     */
    public static Student getStudentById(int id) {
        Student student = new Student();
        List<List<String>> queryResult = getStudentInfoDao(id);
        if (queryResult.size() == 0) {  //查询不存在
            student.setId(id);
            student.setLimitLevel(-1);
            student.setStudentNumber(null);
            student.setName(null);
            student.setPassword(null);
        } else {
            student.setId(id);
            student.setLimitLevel(Integer.parseInt(queryResult.get(0).get(0)));
            student.setName(queryResult.get(0).get(1));
            student.setStudentNumber(queryResult.get(0).get(2));
            student.setPassword(queryResult.get(0).get(3));
        }

        return student;
    }

    /**
     * 根据教师账号获取教师的基本信息
     *
     * @param id
     * @return
     */
    private static List<List<String>> getTeacherInfoDao(int id) {
        TableCURD tableCURD = new TableCURD();
        List<List<String>> queryResult = new ArrayList<>();
        String tbName = getTableNameByUserType(UserType.TEACHER);
        //查询字段
        List<String> fields = new ArrayList<>();
        fields.add("limitLevel");
        fields.add("name");
        fields.add("teacherNumber");
        fields.add("password");
        //限制条件
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        condition.put("id", String.valueOf(id));
        try {
            queryResult = tableCURD.query(tbName, fields, condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryResult;

    }

    /**
     * 通过教师id获取教师实例
     *
     * @param id
     * @return
     */
    public static Teacher getTeacherById(int id) {
        Teacher teacher = new Teacher();
        List<List<String>> queryResult = getTeacherInfoDao(id);
        if (queryResult.size() == 0) {  //查询不存在
            teacher.setId(id);
            teacher.setLimitLevel(-1);
            teacher.setTeacherNumber(null);
            teacher.setName(null);
            teacher.setPassword(null);
        } else {
            teacher.setId(id);
            teacher.setLimitLevel(Integer.parseInt(queryResult.get(0).get(0)));
            teacher.setTeacherNumber(queryResult.get(0).get(1));
            teacher.setName(queryResult.get(0).get(2));
            teacher.setPassword(queryResult.get(0).get(3));
        }
        return teacher;
    }

    /**
     * 根据用户id更新用户名
     *
     * @param userType
     * @param id
     * @param userName
     */
    public static void updateUserNameById(int userType, int id, String userName) {
        TableCURD tableCURD = new TableCURD();
        LinkedHashMap<String, String> updateFields = new LinkedHashMap<>();
        updateFields.put("name", TableCURD.SQLStringType(userName));
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        condition.put("id", String.valueOf(id));
        switch (userType) {
            case UserType.STUDENT:
                try {
                    String tbName = TableCreator.getTbNameByClass(Student.class.getName());
                    tableCURD.update(tbName, updateFields, condition);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                break;
            case UserType.TEACHER:
                try {
                    String tbName = TableCreator.getTbNameByClass(Teacher.class.getName());
                    tableCURD.update(tbName, updateFields, condition);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 根据用户id修改用户密码
     *
     * @param userType
     * @param id
     * @param userPwd
     */
    public static void updateUserPwdById(int userType, int id, String userPwd) {
        TableCURD tableCURD = new TableCURD();
        LinkedHashMap<String, String> updateFields = new LinkedHashMap<>();
        updateFields.put("password", TableCURD.SQLStringType(userPwd));
        LinkedHashMap<String, String> condition = new LinkedHashMap<>();
        condition.put("id", String.valueOf(id));
        switch (userType) {
            case UserType.STUDENT:
                try {
                    String tbName = TableCreator.getTbNameByClass(Student.class.getName());
                    tableCURD.update(tbName, updateFields, condition);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                break;
            case UserType.TEACHER:
                try {
                    String tbName = TableCreator.getTbNameByClass(Teacher.class.getName());
                    tableCURD.update(tbName, updateFields, condition);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 查询所有的学生
     * @return
     */
    public static List<String> getAllStudentAccount() {
        TableCURD tableCURD = new TableCURD();
        List<String> queryResult = new ArrayList<>();
        try {
            String tbName = TableCreator.getTbNameByClass(Student.class.getName());
            String field = "studentNumber";
            queryResult = tableCURD.query(tbName, field, new LinkedHashMap<>());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return queryResult;
    }
}
