package fudan.se.lab4.service.impl;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.entity.User;
import fudan.se.lab4.repository.UserRepository;
import fudan.se.lab4.repository.impl.UserRepositoryImpl;
import fudan.se.lab4.service.AccountService;
import fudan.se.lab4.service.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;


public class AccountServiceImpl implements AccountService {
    //    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private Boolean hasLogin = false;
    private UserRepository userRepository = new UserRepositoryImpl();

    //    private LoggerService loggerService = new LoggerServiceImpl();
    @Override
    public boolean login(User user) {
        //initial
        hasLogin = false;
        //to check whether user is null
        if (user == null || user.getName() == null || user.getPassword() == null) {
            LoggerServiceImpl.getLoggerService().log(InfoConstant.FUNCTION_PARAMETER_POINTER_NULL, InfoConstant.LOGGER_FAIL_STATUS);
            return false;
        }

        //check name and password
        try {

            if (checkUserExisted(user.getName())) {
                boolean userMatch = checkUserInfoMatch(user);
                return userMatch;

            } else {
                hasLogin = false;
                throw new RuntimeException(InfoConstant.USER_NOT_FOUND);
            }
        } catch (RuntimeException e) {
            LoggerServiceImpl.getLoggerService().log(e.getMessage(), InfoConstant.LOGGER_FAIL_STATUS);
            return false;
        }

    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean signup(User user) {

        try {
            if (user == null || user.getName() == null || user.getPassword() == null) {
                throw new RuntimeException(InfoConstant.FUNCTION_PARAMETER_POINTER_NULL);
            }
            if (checkUserExisted(user.getName())) {
                //account already exist
                throw new RuntimeException(MessageFormat.format(InfoConstant.ENTITY_EXIST, "User", user.getName()));
            } else if (!checkName(user.getName())) {
                throw new RuntimeException(InfoConstant.USERNAME_INVALID);
            } else if (!checkPassword(user.getPassword())) {
                throw new RuntimeException(InfoConstant.PASSWORD_INVALID);
            } else {
                userRepository.createUser(user);
                LoggerServiceImpl.getLoggerService().log(MessageFormat.format(InfoConstant.SIGN_UP_SUCCEED, user.getName()), InfoConstant.LOGGER_SUCCESS_STATUS);
                return true;
            }
        } catch (RuntimeException e) {
            LoggerServiceImpl.getLoggerService().log(e.getMessage(), InfoConstant.LOGGER_FAIL_STATUS);
            return false;
        }
    }

    @Override
    public boolean checkStatus() {
        if (hasLogin) {
            LoggerServiceImpl.getLoggerService().log(InfoConstant.USER_LOGIN_ALREADY, InfoConstant.LOGGER_SUCCESS_STATUS);
        } else {
            LoggerServiceImpl.getLoggerService().log(InfoConstant.USER_NOT_LOGIN, InfoConstant.LOGGER_FAIL_STATUS);
        }
        return hasLogin;
    }

    @Override
    public boolean checkName(String name) throws RuntimeException {
        if (name == null) {
            return false;
        }
        if ("".equals(name)) {
            return false;
        }
        if (!name.startsWith("starbb_")) {
            return false;
        }
        boolean isValid = name.matches("[0-9A-Za-z_]*");
        if (!isValid) {
            return false;
        }
        if (name.length() < 8 || name.length() >= 50) {
            return false;
        }
        LoggerServiceImpl.getLoggerService().log(MessageFormat.format(InfoConstant.USERNAME_VALID, name), InfoConstant.LOGGER_SUCCESS_STATUS);
        return true;
    }


    private boolean checkUserExisted(String userName) {
        if (userName == null) {
            throw new RuntimeException(InfoConstant.FUNCTION_PARAMETER_POINTER_NULL);
        }
        boolean isExisted = false;
        try {
            isExisted = userRepository.isExisted(userName);
        } catch (RuntimeException e) {
            throw e;
        }
        return isExisted;
    }

    @Override
    public boolean checkPassword(String password) {
        if (password == null) {
//            throw new RuntimeException(InfoConstant.PASSWORD_INVALID);
            return false;
        }
        if ("".equals(password)) {
//            throw new RuntimeException(InfoConstant.PASSWORD_INVALID);
            return false;
        }
        if (!(password.matches(".*[A-Za-z]+.*") && password.matches(".*[0-9]+.*") && password.matches(".*[_]+.*"))) {
//            throw new RuntimeException(InfoConstant.PASSWORD_INVALID);
            return false;
        }
        if (password.matches(".*[^0-9A-Za-z_].*")) {
//            throw new RuntimeException(InfoConstant.PASSWORD_INVALID);
            return false;
        }
        if (password.length() < 8 || password.length() >= 100) {
//            throw new RuntimeException(InfoConstant.PASSWORD_INVALID);
            return false;
        }
        LoggerServiceImpl.getLoggerService().log(MessageFormat.format(InfoConstant.PASSWORD_VALID, password), InfoConstant.LOGGER_SUCCESS_STATUS);
        return true;
    }

    private boolean checkUserInfoMatch(User user) throws RuntimeException {
        if (user == null) {
            throw new RuntimeException(InfoConstant.FUNCTION_PARAMETER_POINTER_NULL);
        }
        User userOfCsv = userRepository.getUser(user.getName());
        hasLogin = user.getPassword().equals(userOfCsv.getPassword());
        if (hasLogin) {
            LoggerServiceImpl.getLoggerService().log(MessageFormat.format(InfoConstant.SIGN_UP_SUCCEED, user.getName()), InfoConstant.LOGGER_SUCCESS_STATUS);
            return true;
        } else {
            throw new RuntimeException(InfoConstant.USERNAME_OR_PASSWORD_WRONG);
        }
    }
}
