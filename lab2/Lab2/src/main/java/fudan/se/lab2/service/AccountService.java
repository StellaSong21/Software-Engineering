package fudan.se.lab2.service;

import fudan.se.lab2.entity.User;

public interface AccountService {

    boolean login(User user);

    boolean signup(User user);

    /**
     * Check the login status, you can maintain this status in environment variable.
     *
     * @return if user has already login, return true, else return false.
     */
    boolean checkStatus();
}
