package services.UIService;

import models.Student;
import models.Teacher;
import services.UserDAOFactory;
import services.UserType;

public class EditInfo {

    /**
     * 根据用户类型返回用户名
     * @param user
     * @return
     */
    public String getUserName(Object user) {
        String name = "";
        if (user instanceof Student) {
            Student student = (Student)user;
            name = student.getName();
        } else if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            name = teacher.getName();
        }

        return name;
    }

    /**
     * 获取用户密码
     * @param user
     * @return
     */
    public String getUserPwd(Object user) {
        String pwd = "";
        if (user instanceof Student) {
            Student student = (Student)user;
            pwd = student.getPassword();
        } else if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            pwd = teacher.getPassword();
        }
        return pwd;
    }

    /**
     * 更改用户名
     * @param user
     * @param name
     */
    public void changeName(Object user, String name) {
        if (user instanceof Student) {
            Student student = (Student)user;
            UserDAOFactory.updateUserNameById(UserType.STUDENT, student.getId(), name);
        } else if (user instanceof Teacher) {
            Teacher teacher = (Teacher)user;
            UserDAOFactory.updateUserNameById(UserType.TEACHER, teacher.getId(), name);
        }
    }


    /**
     * 修改用户密码
     * @param user
     * @param pwd
     */
    public void changePwd(Object user, String pwd) {
        if (user instanceof Student) {
            Student student = (Student)user;
            UserDAOFactory.updateUserPwdById(UserType.STUDENT, student.getId(), pwd);
        } else if (user instanceof Teacher) {
            Teacher teacher = (Teacher)user;
            UserDAOFactory.updateUserPwdById(UserType.TEACHER, teacher.getId(), pwd);
        }
    }
}
