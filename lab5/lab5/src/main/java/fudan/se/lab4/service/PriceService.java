package fudan.se.lab4.service;

import fudan.se.lab4.currency.Currency;

public interface PriceService {
    /**
     *
     * @param rmb 基础价格（以人民币记录）
     * @param currency 需要的货币种类的对象
     * @return 算过汇率的最后价格
     */
    double charge(double rmb, Currency currency) ;

    /**
     *
     * @param name 系统要转换的货币单位 如RMB、HDK等
     */
    void changeCurrentCurrency(String name);
}
