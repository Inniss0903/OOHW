package models;

import models.DBAnnotations.DBConstraints;
import models.DBAnnotations.DBTable;
import models.DBAnnotations.SQLInteger;
import models.DBAnnotations.SQLString;

@DBTable(name = "Student")
public class Student {
    @SQLInteger(dbConstraints = @DBConstraints(primaryKey = true, autoIncrement = true))
    private int id;
    @SQLInteger
    private int limitLevel;
    @SQLString(30)
    private String name;
    @SQLString(30)
    private String studentNumber;
    @SQLString(30)
    private String password;
    @SQLInteger
    private int grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLimitLevel() {
        return limitLevel;
    }

    public void setLimitLevel(int limitLevel) {
        this.limitLevel = limitLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }


}
