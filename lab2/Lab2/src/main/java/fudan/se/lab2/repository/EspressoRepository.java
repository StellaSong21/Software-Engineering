package fudan.se.lab2.repository;

import fudan.se.lab2.entity.Espresso;

public interface EspressoRepository {

    /**
     * Get Espresso by name in data/espresso.csv
     *
     * @param name
     * @return espresso
     */
    Espresso getEspresso(String name);

    void createEspresso(Espresso espresso);
}
