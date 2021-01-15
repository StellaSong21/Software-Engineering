package fudan.se.lab4.repository;

import fudan.se.lab4.entity.Cappuccino;

public interface CappuccinoRepository {
    /**
     * Get Cappuccino by name in data/cappuccino.csv
     *
     * @param name
     * @return cappuccino
     */
    Cappuccino getCappuccino(String name);

    void createCappuccino(Cappuccino cappuccino);
}
