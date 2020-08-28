package models;

import models.DBAnnotations.DBTable;
import models.DBAnnotations.SQLInteger;
import models.DBAnnotations.SQLString;

@DBTable(name = "HomeworkSubmit")
public class HomeworkSubmit {
    @SQLInteger
    public int number;
    @SQLInteger
    public int studentID;
    @SQLInteger
    public int grade;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
