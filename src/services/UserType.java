package services;

public class UserType {
    //用户身份编号，便于拓展多角色
    /**身份为学生*/
    public static final int STUDENT = 0;
    /**身份为教师*/
    public static final int TEACHER = 1;

    /**
     * 通过用户身份编号获取用户类型
     * @param id
     * @return
     */
    public static String getUserTypeById(int id) {
        String user = "";
        switch (id) {
            case STUDENT:
                user = "Student";
                break;
            case TEACHER:
                user = "Teacher";
                break;
        }
        return user;
    }
}
