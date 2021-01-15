package fudan.se.lab4.repository.impl;

import com.csvreader.CsvReader;
import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.entity.Rule;
import fudan.se.lab4.repository.RuleRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RuleRepositoryImpl implements RuleRepository {
    private DrinkRepositoryImpl drinkRepository = new DrinkRepositoryImpl();
    public class Item{
        protected int requiretType;//0价格 1杯数
        protected double number;
        protected List<Drinks> drinksList;
        public Item(int requiretType,double number,List<Drinks> drinksList) {
            this.requiretType=requiretType;
            this.number=number;
            this.drinksList=drinksList;
        }
        public int getRequiretType() {
            return requiretType;
        }
        public double getNumber() {
            return number;
        }
        public List<Drinks> getDrinksList() {
            return drinksList;
        }
    }

    public List<Rule> getRulesFromCSV(String dataFilePath,ResourceBundle bundle,List<Rule> rules){
        CsvReader reader;
//        List<Rule> ret = new ArrayList<>();
        try {
            reader = new CsvReader(dataFilePath, FileConstant.CSV_SEPARATOR, Charset.forName(FileConstant.CHARSET));
            while (reader.readRecord()) {
                String[] item = reader.getValues();
                rules.add(getRule(item,bundle));
            }
            return rules;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Rule getRule(String[] item,ResourceBundle bundle){
        try {
            int groupId = Integer.parseInt(item[0]);
            int scope = Integer.parseInt(item[1]);
            int profitType = Integer.parseInt(item[2]);
            double profit = Double.parseDouble(item[3]);
            boolean canAdd = (item[4].equals("1"))?true:false;
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z", Locale.ENGLISH);
            Date from = (item[5].equals("null"))?null:dynamicChangeYear(item[5]);
            Date to = (item[6].equals("null"))?null:dynamicChangeYear(item[6]);
            int isOnlyBasicsDrinks = Integer.parseInt(item[7]);
            return new Rule(groupId,scope,profitType,profit,canAdd,from,to,isOnlyBasicsDrinks,getRuleList(item[8]),getRuleList(item[9]),getRuleList(item[10]),Integer.parseInt(item[11]));
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private Date dynamicChangeYear(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss z", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        try {
            Date current = new Date();
            calendar.setTime(current);
            int year = calendar.get(Calendar.YEAR);
            Date when = df.parse(time);
            calendar.setTime(when);
            if(calendar.get(Calendar.YEAR)==1970) {
                calendar.set(Calendar.YEAR,year);
                when=calendar.getTime();
                return when;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    private List<Item> getRuleList(String str){
        if(str.equals("null")){
            return null;
        }
        List<Item> retList = new ArrayList<>();
        String[] smallStrArr = str.split("@");
        for(String s : smallStrArr){
            String[] args = s.split(":");
            int requireType = Integer.parseInt(args[0]);
            double number = Double.parseDouble(args[1]);
            if(args[2].equals("null")){
                retList.add(new Item(requireType,number,null));
            }else {
                List<Drinks> dkl = new ArrayList<>();
                String[] dklObj = args[2].split("&");
                for(String s2 : dklObj){
                    String[] drinkDes = s2.split("#");
                    for(int i = 1; i < drinkDes.length; i++){
                        Drinks drink = drinkRepository.getDrinkInit(drinkDes[0]);
                        drink.setSize(Integer.parseInt(drinkDes[i]));
                        dkl.add(drink);
                    }
                }
                dkl.sort(new Comparator<Drinks>() {
                    @Override
                    public int compare(Drinks o1, Drinks o2) {
                        return (int)o2.getPrice()*100 - (int)o1.getPrice()*100;
                    }
                });
                Item item = new Item(requireType,number,dkl);
                retList.add(item);
            }

        }
        return retList;
    }
}
