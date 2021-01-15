package fudan.se.lab4.entity;

import fudan.se.lab4.service.LoggerService;
import fudan.se.lab4.service.impl.LoggerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ingredient{
        private String name;
        private String description;
        private double price;
//        private int size;
        private static Logger logger = LoggerFactory.getLogger(Coffee.class);
    private LoggerService loggerService = new LoggerServiceImpl();
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            if (!"".equals(description)) {
                this.description = description;
            } else {
                failToCreate();
            }

        }

//        public int getSize() {
//            return size;
//        }

//        public void setSize(int size) {
//            if (size >= 1 && size <= 3) {
//                this.size = size;
//            } else {
//                logger.info(InfoConstant.INVALID_SIZE);
//                throw new RuntimeException(InfoConstant.INVALID_SIZE);
//            }
//        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if (!"".equals(name)) {
                this.name = name;
            } else {
                failToCreate();
            }

        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

//        double priceOfSize() {
//            //big-size3-$6 middle-size2-$4 small-size1-$2
//            checkSize();
//            return getSize() * 2.0;
//        }

//        private void checkSize() {
//            if (getSize() <= 0 || getSize() >= 4) {
//                logger.info(InfoConstant.INVALID_SIZE);
//                throw new RuntimeException(InfoConstant.INVALID_SIZE);
//            }
//        }

        private void failToCreate() {
            logger.info(loggerService.log("INGREDIENT_FAILED_CREATE"));
            throw new RuntimeException(loggerService.log("INGREDIENT_FAILED_CREATE"));
        }
}
