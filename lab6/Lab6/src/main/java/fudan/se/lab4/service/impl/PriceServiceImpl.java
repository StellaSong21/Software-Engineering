//package fudan.se.lab4.service.impl;
//
//import fudan.se.lab4.context.EnvironmentContext;
//import fudan.se.lab4.currency.Currency;
//import fudan.se.lab4.service.LoggerService;
//import fudan.se.lab4.service.PriceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class PriceServiceImpl implements PriceService {
//    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
//    private LoggerService loggerService = new LoggerServiceImpl();
//    public double charge(double price) {
//        return price;
//    }
////    public void changeCurrentCurrency(String name){
////        for(Currency allowedCurrency : EnvironmentContext.getEnvironmentContext().getCurrencies()){
////            if(name.equals(allowedCurrency.getType())){
////                EnvironmentContext.getEnvironmentContext().setCurrencyNow(allowedCurrency);
////                return;
////            }
////        }
////        logger.info(loggerService.log("CURRENCY_NOT_SUPPORT"));
////        throw new RuntimeException(loggerService.log("CURRENCY_NOT_SUPPORT"));
////    }
//}
