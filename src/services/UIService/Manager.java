package services.UIService;

import services.HomeworkDAOFactory;
import services.UserDAOFactory;
import services.UserType;

import java.util.List;

public class Manager {

    public String getTitleContent(int userType, int userId) {
        System.out.println("生成管理界面标题");
        String title = "";
        switch (userType) {
            case UserType.STUDENT:
                title = String.format("尊敬的%s同学你好,欢迎使用本系统。",
                        UserDAOFactory.getNameById(UserType.STUDENT, userId));
                break;
            case UserType.TEACHER:
                title = String.format("尊敬的%s老师你好,欢迎使用本系统。",
                        UserDAOFactory.getNameById(UserType.TEACHER, userId));
                break;
        }
        System.out.println(title);
        return title;
    }

    /**
     * 通过用户类型和id获取用户实例
     * @param userType
     * @param userId
     * @return
     */
    public Object getUser(int userType, int userId) {
        Object user = null;
        switch (userType) {
            case UserType.STUDENT:
                user = UserDAOFactory.getStudentById(userId);
            break;
            case UserType.TEACHER:
                user = UserDAOFactory.getTeacherById(userId);
            break;
        }

        return user;
    }


    /**
     * 获取当前作业标题
     * @return
     */
    public String getHWTitle() {
        String title = "";
        int number = HomeworkDAOFactory.getCurrentHWNumber();
        if (number == 0) {
            title = "当前无作业";
        } else {
            title = String.format("第%d次作业", number);
        }
        return  title;
    }




    /**
     * 获取作业内容
     * @return
     */
    public String getHWContent() {
        String content = "";
        int num = HomeworkDAOFactory.getCurrentHWNumber();
        if (num == 0) {
            content = "老师当前还未布置作业\n";
        } else {
            List<String> con = HomeworkDAOFactory.getHWContentByIdDao();
            if (con.size() == 0) {
                content = "获取作业出错！";
            } else {
                content = HomeworkDAOFactory.getHWContentByIdDao().get(0);
            }

        }

        return content;
    }


    /**
     * 根据学生id获取作业批改情况
     * @param studentId
     * @return
     */
    public String getHWReview(int studentId) {
        String review = "";
        int hwNumber = HomeworkDAOFactory.getCurrentHWNumber();
        List<String> queryResult = HomeworkDAOFactory.getHWGradeByNumberAndStuId(hwNumber, studentId);
        if (queryResult.size() == 0) {
            //数据库中对应的作业编号查询不到学生id的存在
            review = "您的作业未上交，请及时提交作业";
            return review;
        }

        if (queryResult.get(0) == null) {
            //成绩不存在为null
            review = "您的作业已提交，但作业还未批改，请耐心等待";
        } else {
            review = String.format("第%d次作业已批改，你的成绩为：%s", hwNumber, queryResult.get(0));
        }
        return review;
    }
}
