package fudan.se.lab2.repository;

import fudan.se.lab2.entity.User;

public interface UserRepository {
    /**
     * persist user in data/user.csv
     *
     * @param user
     */
    void createUser(User user);

    /**
     * get User by name in data/user.csv
     *
     * @param name
     * @return user
     */
    User getUser(String name);
}
