package fudan.se.lab2;

import fudan.se.lab2.constant.InfoConstant;
import fudan.se.lab2.entity.Coffee;
import fudan.se.lab2.entity.User;
import fudan.se.lab2.repository.impl.CappuccinoRepositoryImpl;
import fudan.se.lab2.repository.impl.EspressoRepositoryImpl;
import fudan.se.lab2.service.impl.AccountServiceImpl;
import fudan.se.lab2.service.impl.PriceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class Lab2Application {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static String[] coffee = {"Cappuccino", "Espresso"};

    public static void main(String[] args) {
        SpringApplication.run(Lab2Application.class, args);
        AccountServiceImpl accountService = new AccountServiceImpl();
        PriceServiceImpl priceService = new PriceServiceImpl();
        Scanner scanner = new Scanner(System.in);
        boolean endFlag = false;
        User user = new User();
        Map<Coffee, Integer> map = new HashMap<>();
        boolean status = false;

        while (!endFlag) {
            if (!status) {
                System.out.println("Welcome to Star Dad Cafe!");
                System.out.println("What kind of service do you need?");
                System.out.println("\t1：Login"
                        + "\n\t2：Signup"
                        + "\n\t0：Exit");
                String order = scanner.nextLine();
                switch (order) {
                    case "1":
                        user = getUser();
                        accountService.login(user);
                        status = accountService.checkStatus();
                        break;
                    case "2":
                        user = getUser();
                        accountService.signup(user);
                        break;
                    case "0":
                        endFlag = true;
                        break;
                    default:
                        System.out.println("Sorry, I can't understand it. Please re-enter.");
                        break;
                }
            } else {
                System.out.println("Hello, " +  user.getName());
                Coffee coffee = getCoffee();
                if (coffee == null) {
                    System.out.println("Invalid number, automatic exit");
                    endFlag = true;
                    continue;
                }
                int size = getSize();
                if (size == 0) {
                    System.out.println("Invalid number, automatic exit");
                    endFlag = true;
                    continue;
                }
                coffee.setSize(size);
                int num = getNum();
                if (num == -1) {
                    System.out.println("Invalid number, automatic exit");
                    endFlag = true;
                    continue;
                }
                map.put(coffee, num);
                priceService.cost(map);
                endFlag = true;
            }
        }
        exit();
    }


    private static User getUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("May I have your name?");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        return user;
    }

    private static Coffee getCoffee() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What kind of coffee would you like to drink?");
        for (int i = 0; i < coffee.length; i++) {
            System.out.println("\t" + (i + 1) + "：" + coffee[i]);
        }
        System.out.println("\telse：Exit");
        String coffeeKind = scanner.nextLine();
        Coffee coffee = null;
        if ("1".equals(coffeeKind)) {
            coffee = new CappuccinoRepositoryImpl().getCappuccino("cappuccino");
        } else if ("2".equals(coffeeKind)) {
            coffee = new EspressoRepositoryImpl().getEspresso("espresso");
        }
        return coffee;
    }

    private static int getSize() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like a big, medium or small cup?");
        System.out.println("\t1: small");
        System.out.println("\t2: medium");
        System.out.println("\t3: big");
        System.out.println("\telse：Exit");
        String sizei = scanner.nextLine();
        if ("1".equals(sizei)) {
            return 1;
        } else if ("2".equals(sizei)) {
            return 2;
        } else if ("3".equals(sizei)) {
            return 3;
        } else {
            return 0;
        }
    }

    private static int getNum() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many glasses would you like?");
        String numStr = scanner.nextLine();
        try {
            return Integer.parseInt(numStr);
        } catch (IllegalArgumentException e) {
            logger.info(MessageFormat.format(InfoConstant.INVALID_NUMBER, numStr));
            return -1;
        }
    }

    private static void exit() {
        System.out.println("Look forward to serving you next time. Welcome to come again!");
        System.exit(0);
    }
}
