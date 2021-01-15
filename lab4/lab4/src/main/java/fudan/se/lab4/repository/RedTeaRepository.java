package fudan.se.lab4.repository;

import fudan.se.lab4.entity.RedTea;

public interface RedTeaRepository {


        /**
         * Get Tea by name in data/redTea.csv
         *
         * @param name
         * @return redTea
         */
        RedTea getRedTea(String name);

        void createRedTea(RedTea redTea);

}
