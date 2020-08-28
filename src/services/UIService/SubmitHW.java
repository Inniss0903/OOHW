package services.UIService;

import models.Student;
import models.Teacher;
import services.HomeworkDAOFactory;
import services.UserType;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;

public class SubmitHW {

    /**
     * 将目标源文件复制到指定位置
     * @param savedFileName
     * @param sourceFileName
     */
    public void copyFile(String savedFileName, String sourceFileName) {
        System.out.println("文件保存到：" + savedFileName);
        File savedFile = new File(savedFileName);
        File sourceFile = new File(sourceFileName);
        //利用FileChannel进行文件复制以实现提交功能
        FileChannel source = null;
        FileChannel save = null;
        //如果要保存的路径文件夹不存在则创建
        if (!savedFile.getParentFile().exists() && !savedFile.isDirectory()) {
            savedFile.getParentFile().mkdirs();
        }
        try {
            source = new FileInputStream(sourceFile).getChannel();
            save = new FileOutputStream(savedFile).getChannel();
            save.transferFrom(source,0, source.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally { //关闭channel
            try {
                source.close();
                save.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 通过用户类型和用户id来返回应该保存的文件路径
     * @param userType
     * @param id
     * @return
     */
    public String getUserFilePath(int userType, int id) {
        String filePath = "src" + File.separatorChar + "homework" + File.separatorChar;
        switch (userType) {
            case UserType.STUDENT:
                filePath += "Student" + File.separatorChar +
                        id + File.separatorChar;
                break;
            case UserType.TEACHER:
                filePath += "Teacher" + File.separatorChar +
                        id + File.separatorChar;
                break;
        }
        return filePath;
    }


    /**
     * 上传作业文件
     * @param user
     * @param sourceFileName
     * @param hwName
     */
    public void upload(Object user, String sourceFileName, String hwName) {
        String savedFileName = "";
        int userId = 0;
        int userType = 0;
        //根据用户身份选择要保存的文件夹目录
        if (user instanceof Student) {
            Student s = (Student) user;
            //学生选择上传文件
            userType = UserType.STUDENT;
            userId = s.getId();
        } else if (user instanceof Teacher) {
            //教师选择上传文件
            Teacher t = (Teacher) user;
            userType = UserType.TEACHER;
            userId = t.getId();
        }

        savedFileName = getUserFilePath(userType, userId) + hwName;
        //将作业文件保存到指定位置标示作业上交
        copyFile(savedFileName, sourceFileName);

        //将作业提交情况保存到数据库中
        if (userType == UserType.STUDENT) {
            submitHW(HomeworkDAOFactory.getCurrentHWNumber(), userId);
        }


    }

    /**
     * 讲作业保存到数据库中
     * @param currentHWNumber
     * @param userId
     */
    private void submitHW(int currentHWNumber, int userId) {
        boolean exist = HomeworkDAOFactory.hasSubmitHW(currentHWNumber, userId);
        //如果学生之前未提交则将作业提交状态保存到数据库中
        if (!exist) {
            HomeworkDAOFactory.hwSubmit(currentHWNumber, userId);
        }
    }
}
