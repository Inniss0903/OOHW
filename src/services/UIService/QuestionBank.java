package services.UIService;

import models.QuestionSingle;
import services.QuestionDAOFactory;

import java.util.List;
import java.util.Random;

public class QuestionBank {

    /**
     * 从题库中获取例题并实例化
     * @return
     */
    public QuestionSingle getOneQuest() {
        List<String> idList = QuestionDAOFactory.getAllQueSinIdDao();
        int randomId = 0;
        //如果有数据
        if (idList.size() != 0) {
            //随机取题
            randomId = new Random().nextInt(idList.size()) + 1;
        }
        QuestionSingle qs = QuestionDAOFactory.getQueSinById(randomId);

        return qs;
    }

    /**
     * 检查用户选择的正确行
     * @param rightAnswer
     * @param userChoice
     * @return
     */
    public String checkAnswer(String rightAnswer, String userChoice) {
        String info = "";
        //正确选项为空时
        if (rightAnswer == null || rightAnswer == "") {
            info = "当前题目出错";
            return info;
        }
        if (userChoice == null) {
            info = "请你做出选择";
            return info;
        }

        if (rightAnswer.equals(userChoice)) {
            info = "恭喜你答对了";
        } else {
            info = "很遗憾你答错了，正确选项为" + rightAnswer;
        }

        return info;
    }

    /**
     * 存储实例，返回存储结果
     * @param qs
     */
    public String storeQueSin(QuestionSingle qs) {
        String info = "";
        if (qs.getTopic().equals("") || qs.getRightAnswer() == null || qs.getKnowledgePoint().equals("") ||
        qs.getChoiceA().equals("") || qs.getChoiceB().equals("") || qs.getChoiceC().equals("") ||
        qs.getChoiceC().equals("") || qs.getChoiceD().equals("")) {
            info = "请补全例题";
            return info;
        }

        QuestionDAOFactory.insertQuesSin(qs);
        info = "格式正确，插入数据库";

        return info;
    }
}
