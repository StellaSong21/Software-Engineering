package fudan.se.lab4.repository;

import fudan.se.lab4.entity.GreenTea;

public interface GreenTeaRepository {



        /**
         * Get Tea by name in data/greenTea.csv
         *
         * @param name
         * @return greenTea
         */
        GreenTea getGreenTea(String name);

        void createGreenTea(GreenTea greenTea);

}
