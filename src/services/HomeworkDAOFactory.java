package services;

import models.Dao.TableCURD;
import models.Dao.TableCreator;
import models.HomeworkDesc;
import models.HomeworkSubmit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HomeworkDAOFactory {


    /**
     * 获取当前作业编号
     * @return
     */
    public static int getCurrentHWNumber() {
        AppPreference appPreference = new AppPreference();
        return appPreference.getCurrentHW();
    }

    /**
     * 从数据库中获取作业内容
     * @return
     */
    public static List<String> getHWContentByIdDao() {
        TableCURD tableCURD = new TableCURD();
        int num = HomeworkDAOFactory.getCurrentHWNumber();
        List<String> queryResult = new ArrayList<>();
        try {
            String tbName = TableCreator.getTbNameByClass(HomeworkDesc.class.getName());
            String field = "content";
            LinkedHashMap<String, String> condition = new LinkedHashMap<>();
            condition.put("number", String.valueOf(num));
            queryResult = tableCURD.query(tbName, field, condition);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return queryResult;

    }

    /**
     * 通过作业编号和学生id获取当前作业的分数
     * @param num
     * @param studentID
     * @return
     */
    public static List<String> getHWGradeByNumberAndStuId(int num, int studentID) {
        TableCURD tableCURD = new TableCURD();
        List<String> queryResult = new ArrayList<>();
        try {
            String tbName = TableCreator.getTbNameByClass(HomeworkSubmit.class.getName());
            String field = "grade";
            LinkedHashMap<String, String> condition = new LinkedHashMap<>();
            condition.put("number", String.valueOf(num));
            condition.put("studentID", String.valueOf(studentID));
            queryResult = tableCURD.query(tbName, field, condition);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return queryResult;
    }

    /**
     * 判断学生是否已提交当前作业
     * @param currentHWNumber
     * @param userId
     * @return
     */
    public static boolean hasSubmitHW(int currentHWNumber, int userId) {
        TableCURD tableCURD = new TableCURD();
        List<String> queryResult = new ArrayList<>();
        try {
            String tbName = TableCreator.getTbNameByClass(HomeworkSubmit.class.getName());
            String field = "studentID";
            LinkedHashMap condition = new LinkedHashMap();
            condition.put("number", String.valueOf(currentHWNumber));
            condition.put("studentID", String.valueOf(userId));
            queryResult = tableCURD.query(tbName, field, condition);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        //数据库搜寻不到结果表明学生未提交作业
        if (queryResult.size() == 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 讲作业提交情况保存到数据库中
     *
     * @param currentHWNumber
     * @param userId
     */
    public static void hwSubmit(int currentHWNumber, int userId) {
        TableCURD tableCURD = new TableCURD();
        try {
            String tbName = TableCreator.getTbNameByClass(HomeworkSubmit.class.getName());
            LinkedHashMap<String, String> record = new LinkedHashMap<>();
            record.put("number", String.valueOf(currentHWNumber));
            record.put("studentID", String.valueOf(userId));
            tableCURD.insert(tbName, record);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取当前作业名
     * @return
     */
    public static String getCurrentHWName() {
        //加上后缀
        String hwName = "hw" + getCurrentHWNumber() + ".docx";
        return hwName;
    }

    /**
     * 获取指定作业名
     * @param hwNumber
     * @return
     */
    public static String getHWName(int hwNumber) {
        //加上后缀
        String hwName = "hw" + hwNumber + ".docx";
        return hwName;
    }

    /**
     * 获取提交过某个作业的学生们的id
     * @param number
     * @return
     */
    public static List<String> getHWStudentIdByNumber(int number) {
        TableCURD tableCURD = new TableCURD();
        List<String> queryResult = new ArrayList<>();
        try {
            String tbName = TableCreator.getTbNameByClass(HomeworkSubmit.class.getName());
            String field = "studentId";
            LinkedHashMap<String, String> condition = new LinkedHashMap<>();
            condition.put("number",String.valueOf(number));
            queryResult = tableCURD.query(tbName, field, condition);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return queryResult;
    }

    /**
     * 获取某位学生提交过的作业
     * @param assStuId
     * @return
     */
    public static List<String> getHWNumberByStudentId(int assStuId) {
        TableCURD tableCURD = new TableCURD();
        List<String> queryResult = new ArrayList<>();
        try {
            String tbName = TableCreator.getTbNameByClass(HomeworkSubmit.class.getName());
            String field = "number";
            LinkedHashMap<String, String> condition = new LinkedHashMap<>();
            condition.put("studentID",String.valueOf(assStuId));
            queryResult = tableCURD.query(tbName, field, condition);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return queryResult;
    }

    /**
     * 根据作业编号和学生id更新学生成绩
     * @param hwNumber
     * @param gradeStudentId
     * @param grade
     */
    public static void updateGrade(int hwNumber, int gradeStudentId, int grade) {
        TableCURD tableCURD = new TableCURD();
        try {
            String tbName = TableCreator.getTbNameByClass(HomeworkSubmit.class.getName());
            LinkedHashMap<String, String> field = new LinkedHashMap<>();
            field.put("grade", String.valueOf(grade));
            LinkedHashMap<String, String> condition = new LinkedHashMap<>();
            condition.put("number", String.valueOf(hwNumber));
            condition.put("studentId", String.valueOf(gradeStudentId));
            tableCURD.update(tbName, field, condition);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 返回某个作业某为学生的得分
     * @param hwNumber
     * @param studentId
     * @return
     */
    public static int getHWGradeForStu(int hwNumber, int studentId) {
        int grade = -1;
        List<String> queryResult = getHWGradeByNumberAndStuId(hwNumber, studentId);
        if (queryResult.size() == 0) {
            //查询出错
            return grade;
        } else {
            //查询无结果
            if (queryResult.get(0) == null) {
                return grade;
            }
            grade = Integer.parseInt(queryResult.get(0));
        }

        return grade;
    }


    /**
     * 获取下载时的文件名
     *
     * @param gradeStudentId
     * @param hwNumber
     * @return
     */
    public static String getDownloadHWName(int gradeStudentId, int hwNumber) {
        String fileName = "";
        //学生学号
        fileName += UserDAOFactory.getAccountById(UserType.STUDENT, gradeStudentId);
        fileName += "_";
        //学生姓名
        fileName += UserDAOFactory.getNameById(UserType.STUDENT, gradeStudentId);
        fileName += "_";
        //作业编号
        fileName += hwNumber;

        return fileName;
    }

    /**
     * 获取文件的后缀名
     * @param file
     * @return
     */
    public static String getFileExtension(String file) {
        String extension = "";
        String[] fileS = file.split("\\.");
        extension = "." + fileS[fileS.length - 1];

        return extension;
    }

}
