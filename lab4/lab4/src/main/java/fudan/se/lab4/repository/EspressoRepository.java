package fudan.se.lab4.repository;

import fudan.se.lab4.entity.Espresso;

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
