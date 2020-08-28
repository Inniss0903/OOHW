package models;

import models.DBAnnotations.DBConstraints;
import models.DBAnnotations.DBTable;
import models.DBAnnotations.SQLInteger;
import models.DBAnnotations.SQLString;

@DBTable(name = "HomeworkDesc")
public class HomeworkDesc {
    @SQLInteger(dbConstraints = @DBConstraints(primaryKey = true))
    public int number;
    @SQLString(200)
    public String content;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
