package fudan.se.lab2.repository;

import fudan.se.lab2.entity.Cappuccino;

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
