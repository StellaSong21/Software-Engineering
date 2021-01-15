package fudan.se.lab4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Lab4ApplicationTests {
	RuleRepositoryImpl ruleRepository = new RuleRepositoryImpl();

	@Test
	public void test1() {

			Drinks drinks = new DrinkRepositoryImpl().getDrink("redTea");
			drinks.setSize(1);
			Map<Drinks,Integer> map = new HashMap<>();
			map.put(drinks,1);
			Drinks drinks1 = new DrinkRepositoryImpl().getDrink("redTea");
			if (map.get(drinks1)!=null){
				System.out.println(map.get(drinks1));
			}else {
				System.out.println(111);
			}



		}

}
