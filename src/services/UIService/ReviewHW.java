package services.UIService;

import models.HomeworkSubmit;
import services.HomeworkDAOFactory;
import services.UserDAOFactory;
import services.UserType;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReviewHW {

    /**
     * 查询已提交某次作业同学的学号
     * @param hwNumber
     * @return
     */
    public List<String> queryHWSubStuNumber(String hwNumber) {
        List<String> queryResult = new ArrayList<>();
        List<String> studentIdList = queryHWSubIdByNumber(hwNumber);
        for (String stuId : studentIdList) {
            queryResult.add(UserDAOFactory.getAccountById(UserType.STUDENT, Integer.parseInt(stuId)));
        }
        return queryResult;
    }

    /**
     * 查询已提交改作业的学生id
     * @param hwNumber
     * @return
     */
    public List<String> queryHWSubIdByNumber(String hwNumber) {
        List<String> queryResult = HomeworkDAOFactory.getHWStudentIdByNumber(Integer.parseInt(hwNumber));
        return queryResult;
    }

    /**
     * 给某个作业中的某一位同学打分
     * @param hwNumber
     * @param gradeStudentId
     * @param grade
     */
    public void giveGrade(int hwNumber, int gradeStudentId, int grade) {
        // TODO 判断是否为合法数字
        HomeworkDAOFactory.updateGrade(hwNumber, gradeStudentId, grade);
    }

    /**
     * 获取当前学生的得分情况，若无评分返回空
     * @param hwNumber
     * @param gradeStudentId
     * @return
     */
    public String getGrade(String hwNumber, int gradeStudentId) {
        String gradeText = "";
        int hwNum = Integer.parseInt(hwNumber);
        int grade = HomeworkDAOFactory.getHWGradeForStu(hwNum, gradeStudentId);
        if (grade == -1) {
            //返回-1表示未评分
            return gradeText;
        } else {
            gradeText = String.valueOf(grade);
        }

        return gradeText;
    }

    /**
     * 下载学生作业到指定位置
     * @param downloadPath
     * @param gradeStudentId
     * @param hwNumber
     */
    public void downloadHW(String downloadPath, int gradeStudentId, int hwNumber) {
        SubmitHW submitHWService = new SubmitHW();
        String sourcePath = submitHWService.getUserFilePath(UserType.STUDENT, gradeStudentId);
        String sourceFile = sourcePath + File.separatorChar + HomeworkDAOFactory.getHWName(hwNumber);

        String downloadName = HomeworkDAOFactory.getDownloadHWName(gradeStudentId, hwNumber);
        //文件名后加拓展名
        String downloadFile = downloadPath + File.separatorChar + downloadName +
                HomeworkDAOFactory.getFileExtension(".docx");

        submitHWService.copyFile(downloadFile, sourceFile);

    }

    /**
     * 返回选课学生的列表
     * @return
     */
    public List<String> getStudentList() {
        List<String> studentList = new ArrayList<>();
        studentList = UserDAOFactory.getAllStudentAccount();
        return studentList;
    }

    /**
     * 返回指定学生的作业列表
     * @param assStuId
     * @return
     */
    public List<String> getAssHWList(int assStuId) {
        List<String> hwList = new ArrayList<>();
        hwList = HomeworkDAOFactory.getHWNumberByStudentId(assStuId);
        //查询不到指定id或是未提交作业都返回空列表
        if (hwList.size() == 0){
            return new ArrayList<>();
        }
        if (hwList.get(0) == null) {
            return new ArrayList<>();
        }
        return hwList;
    }

    /**
     * 获取某个学生某项作业的分数
     * @param studentId
     * @param selectIndex
     * @param assStuHWList
     * @return
     */
    public String getAssHWGrade(int studentId, int selectIndex, List<String> assStuHWList) {
        String gradeInfo = "";
        //未提交作业无分数
        if (assStuHWList.size() == 0 || selectIndex < 0) {
            return gradeInfo;
        }
        int hwNumber = Integer.parseInt(assStuHWList.get(selectIndex));
        List<String> queryResult = HomeworkDAOFactory.getHWGradeByNumberAndStuId(hwNumber, studentId);
        //无对应记录
        if (queryResult.size() == 0) {
            return gradeInfo;
        }
        //有记录无分数
        if (queryResult.get(0) == null) {
            return gradeInfo;
        } else {
            gradeInfo = queryResult.get(0);
        }

        return gradeInfo;
    }

    /**
     * 获取将要评分学生的id
     * @param hwStudentId
     * @param selectedIndex
     * @return
     */
    public int recordId(List<String> hwStudentId, int selectedIndex) {
        int id = -1;
        if (selectedIndex <0 || hwStudentId.size() == 0) {
            return id;
        }
        id = Integer.parseInt(hwStudentId.get(selectedIndex));
        return id;
    }
}
