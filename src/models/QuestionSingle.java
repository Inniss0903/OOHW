package models;

import models.DBAnnotations.DBConstraints;
import models.DBAnnotations.DBTable;
import models.DBAnnotations.SQLInteger;
import models.DBAnnotations.SQLString;

@DBTable(name = "QuestionSingle")
public class QuestionSingle {
    @SQLInteger(dbConstraints = @DBConstraints(primaryKey = true, autoIncrement = true))
    public int id;
    @SQLString(value = 200, dbConstraints = @DBConstraints(allowNull = false))
    public String topic;    //题干
    @SQLString(value = 200, dbConstraints = @DBConstraints(allowNull = false))
    public String knowledgePoint;   //知识点
    @SQLString(value = 40, dbConstraints = @DBConstraints(allowNull = false))
    public String choiceA;
    @SQLString(value = 40, dbConstraints = @DBConstraints(allowNull = false))
    public String choiceB;
    @SQLString(value = 40, dbConstraints = @DBConstraints(allowNull = false))
    public String choiceC;
    @SQLString(value = 40, dbConstraints = @DBConstraints(allowNull = false))
    public String choiceD;
    @SQLString(value = 5, dbConstraints = @DBConstraints(allowNull = false))
    public String rightAnswer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKnowledgePoint() {
        return knowledgePoint;
    }

    public void setKnowledgePoint(String knowledgePoint) {
        this.knowledgePoint = knowledgePoint;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
