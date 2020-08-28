package models;

import models.DBAnnotations.DBConstraints;
import models.DBAnnotations.DBTable;
import models.DBAnnotations.SQLInteger;
import models.DBAnnotations.SQLString;

@DBTable(name = "Teacher")
public class Teacher {
    @SQLInteger(dbConstraints = @DBConstraints(primaryKey = true, autoIncrement = true))
    private int id;
    @SQLInteger
    private int limitLevel;
    @SQLString(30)
    private String name;
    @SQLString(30)
    private String teacherNumber;
    @SQLString(30)
    private String password;

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

    public String getTeacherNumber() {
        return teacherNumber;
    }

    public void setTeacherNumber(String teacherNumber) {
        this.teacherNumber = teacherNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
