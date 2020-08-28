package services.UIService;

import models.Dao.TableCURD;
import models.Dao.TableCreator;
import models.HomeworkDesc;
import services.AppPreference;
import services.HomeworkDAOFactory;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class SetHW {

    /**
     * 布置作业
     * @param content
     */
    public void setHW(String content) {
        int num = HomeworkDAOFactory.getCurrentHWNumber() + 1;
        store(num, content);
    }

    /**
     * 存储到数据库
     * @param num
     * @param content
     */
    private void store(int num, String content) {
        TableCURD tableCURD = new TableCURD();
        LinkedHashMap<String, String> record = new LinkedHashMap<>();
        try {
            String tbName = TableCreator.getTbNameByClass(HomeworkDesc.class.getName());
            record.put("number", String.valueOf(num));
            record.put("content", TableCURD.SQLStringType(content));
            tableCURD.insert(tbName, record);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        //保存当前作业
        AppPreference appPreference = new AppPreference();
        appPreference.setCurrentHW(num);

    }
}
