package fudan.se.lab4.repository;

import fudan.se.lab4.entity.Drinks;

public interface DrinkRepository {
     Drinks getDrink(String name);
}
