package fudan.se.lab4.service;

import fudan.se.lab4.entity.User;

public interface AccountService {

    boolean login(User user);

    boolean signup(User user);

    /**
     * Check the login status, you can maintain this status in environment variable.
     *
     * @return if user has already login, return true, else return false.
     */
    boolean checkStatus();


    /**
     * check whether the username satisfies:
     * starts with starbb_;
     * only concludes letters, figure and underline;
     * the length is between 8 and 50 (8 =< len < 50).
     *
     * @param name the given username to check
     * @return if username is legal, return true, else return false.
     */
    boolean checkName(String name);


    /**
     * Check whether the given password is valid
     *
     * @param password the given password to check
     * @return whether the password is valid
     */
    boolean checkPassword(String password);
}
