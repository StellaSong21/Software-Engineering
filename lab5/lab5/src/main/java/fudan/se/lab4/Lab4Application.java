package fudan.se.lab4;

import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.entity.Drinks;
import fudan.se.lab4.repository.DrinkRepository;
import fudan.se.lab4.repository.impl.DrinkRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab4Application {

    public static void main(String[] args) {

        SpringApplication.run(Lab4Application.class, args);
        EnvironmentContext e = EnvironmentContext.getEnvironmentContext();
        DrinkRepository drinkRepository=new DrinkRepositoryImpl();
        Drinks w=drinkRepository.getDrink("redTea");
        int a=0;

    }
}
