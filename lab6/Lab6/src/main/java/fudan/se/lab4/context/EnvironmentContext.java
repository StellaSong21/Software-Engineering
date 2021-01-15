package fudan.se.lab4.context;

import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.currency.Currency;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.RuleRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import fudan.se.lab4.repository.impl.IngredientRepositoryImpl;
import fudan.se.lab4.repository.impl.RuleRepositoryImpl;

import java.util.*;

public class EnvironmentContext {
    private ResourceBundle bundle;
//    private ArrayList<Currency> currencies=new ArrayList<>();
    private ArrayList<String[]> specialDrinks=new ArrayList<>();
    private static final EnvironmentContext environmentContext = new EnvironmentContext();
    private List<Rule> rules = new ArrayList<>();
//    private Currency currencyNow;
    private Map<String,String> paths;
    private int number;//下一个订单号
    private String currentCurrencySymbol = "RMB";
    private EnvironmentContext(){
        paths=new HashMap<>();
        paths.put("language","en");
        paths.put("drinks","data/drinks_RMB.csv");
        paths.put("ingredient","data/ingredient_RMB.csv");
        bundle=ResourceBundle.getBundle("starbb",new Locale(paths.get("language")));
        number=1;
//        String money;
//        try {
//            money = bundle.getString("CURRENCY");
//        }catch (Exception e){
//            throw new RuntimeException(bundle.getString("INIT_FAILED"));
//        }
//        String[] curArray=money.split(";");
//        for (String key:curArray) {
//            String[] array=key.split("_");
//            currencies.add(new Currency(array[2],array[0],Double.parseDouble(array[1])));
//        }
//        currencyNow = currencies.get(0);
//        String special=bundle.getString("SPECIAL");
//        if(!special.equals("")){
//            String[] speArray=special.split(";");
//            for (String key:speArray) {
//                String[] array=key.split(",");
//                specialDrinks.add(array);
//            }
//        }

//        String rulePaths = bundle.getString("RULE_PATH");
//        String[] rulePathArr = rulePaths.split(";");
//        for(int i = 0; i < rulePathArr.length; i++){
//            try {
//                Class clazz;
//                if(i == 0){
//                   clazz = Class.forName("fudan.se.lab4.repository.impl.RuleRepositoryImpl");
//                }else {
//                    clazz = Class.forName("fudan.se.lab4.repository.impl.RuleRepositoryImpl"+i);
//                }
//                RuleRepository ruleRepository = (RuleRepository) clazz.newInstance();
//                this.rules = ruleRepository.getRulesFromCSV(rulePathArr[i],bundle,this.rules);
//
//            }catch (Exception e){
//                throw new RuntimeException(bundle.getString("INIT_FAILED"));
//            }
//        }


        RuleRepository ruleRepository = new RuleRepositoryImpl();
        this.rules = ruleRepository.getRulesFromCSV(FileConstant.SALESRULES_CSV,bundle,this.rules);
    }
    public static EnvironmentContext getEnvironmentContext(){
        return environmentContext;
    }

//    public ArrayList<Currency> getCurrencies() {
//        return currencies;
//    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public ArrayList<String[]> getSpecialDrinks() {
        return specialDrinks;
    }

    public List<Rule> getRules() {
        return rules;
    }

//    public Currency getCurrencyNow() {
//        return currencyNow;
//    }
//
//    public void setCurrencyNow(Currency currencyNow) {
//        this.currencyNow = currencyNow;
//    }
    public int createAnOrder() {
        return number++;
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public String getCurrentCurrencySymbol() {
        return currentCurrencySymbol;
    }

    public void changeLanguage(String path) { //en,ch
        paths.put("language",path);
        bundle=ResourceBundle.getBundle("starbb",new Locale(paths.get("language")));
    }
    public void changeCurrency(String path) { //RMB,USD,HDK
        String drinkPath="data/drinks_"+path+".csv";
        String ingredientPath ="data/ingredient_"+path+".csv";
        paths.put("drinks",drinkPath);
        paths.put("ingredient",ingredientPath);

        switch (path){
            case "RMB":
                currentCurrencySymbol = "¥";
                break;
            case "USD":
                currentCurrencySymbol = "$";
                break;
            case "HDK":
                currentCurrencySymbol = "HK$";
                break;
        }
    }
}

