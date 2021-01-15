package fudan.se.lab4.repository;

import fudan.se.lab4.entity.Rule;

import java.util.List;
import java.util.ResourceBundle;

public interface RuleRepository {
    /**
     *
     * @return 数据库中读取的1行rule对象
     */
    Rule getRule(String[] item,ResourceBundle bundle);
    List<Rule> getRulesFromCSV(String path,ResourceBundle bundle,List<Rule> rules);
}
