package services;

import models.Dao.TableCURD;
import models.Dao.TableCreator;
import models.QuestionSingle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class QuestionDAOFactory {
    /**
     * 获取题库中单选题的所有id
     * @return
     */
    public static List<String> getAllQueSinIdDao() {
        TableCURD tableCURD = new TableCURD();
        List<String> queryResult = new ArrayList<>();
        try {
            String tbName = TableCreator.getTbNameByClass(QuestionSingle.class.getName());
            String filed = "id";
            queryResult = tableCURD.query(tbName, filed, new LinkedHashMap<>());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return queryResult;
    }

    /**
     * 通过id获取数据库中的题目信息
     * @param id
     * @return
     */
    public static List<List<String>> getQuestSinByIdDao(int id) {
        TableCURD tableCURD = new TableCURD();
        List<List<String>> queryResult = new ArrayList<>();
        try {
            String tbName = TableCreator.getTbNameByClass(QuestionSingle.class.getName());
            List<String> fieldList = new ArrayList<>();
            fieldList.add("topic");
            fieldList.add("knowledgePoint");
            fieldList.add("choiceA");
            fieldList.add("choiceB");
            fieldList.add("choiceC");
            fieldList.add("choiceD");
            fieldList.add("rightAnswer");
            LinkedHashMap<String, String> condition = new LinkedHashMap<>();
            condition.put("id", String.valueOf(id));
            queryResult = tableCURD.query(tbName, fieldList, condition);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return queryResult;
    }

    /**
     * 通过id获取单选题实例
     * @param id
     * @return
     */
    public static QuestionSingle getQueSinById(int id) {
        QuestionSingle qs = new QuestionSingle();
        List<List<String>> queryResult = getQuestSinByIdDao(id);
        qs.setId(id);
        if (queryResult.size() == 0) {
            qs.setTopic("暂无题目");
            qs.setChoiceA("");
            qs.setChoiceB("");
            qs.setChoiceC("");
            qs.setChoiceD("");
            qs.setKnowledgePoint("暂无知识点");
            qs.setRightAnswer("");
            return qs;
        }
        //题目只要查到所有值都不为空
        List<String> qsInfo = queryResult.get(0);
        qs.setTopic(qsInfo.get(0));
        qs.setKnowledgePoint(qsInfo.get(1));
        qs.setChoiceA(qsInfo.get(2));
        qs.setChoiceB(qsInfo.get(3));
        qs.setChoiceC(qsInfo.get(4));
        qs.setChoiceD(qsInfo.get(5));
        qs.setRightAnswer(qsInfo.get(6));

        return qs;
    }

    /**
     * 将一个单选题实例插入其中
     * @param qs
     */
    public static void insertQuesSin(QuestionSingle qs) {
        TableCURD tableCURD = new TableCURD();
        try {
            String tbName = TableCreator.getTbNameByClass(QuestionSingle.class.getName());
            LinkedHashMap<String, String> record = new LinkedHashMap<>();
            record.put("topic",TableCURD.SQLStringType(qs.getTopic()));
            record.put("knowledgePoint",TableCURD.SQLStringType(qs.getKnowledgePoint()));
            record.put("choiceA",TableCURD.SQLStringType(qs.getChoiceA()));
            record.put("choiceB",TableCURD.SQLStringType(qs.getChoiceB()));
            record.put("choiceC",TableCURD.SQLStringType(qs.getChoiceC()));
            record.put("choiceD",TableCURD.SQLStringType(qs.getChoiceD()));
            record.put("rightAnswer",TableCURD.SQLStringType(qs.getRightAnswer()));
            tableCURD.insert(tbName, record);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


}
