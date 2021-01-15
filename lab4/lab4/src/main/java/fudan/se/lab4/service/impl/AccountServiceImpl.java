package fudan.se.lab4.service.impl;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.entity.User;
import fudan.se.lab4.repository.UserRepository;
import fudan.se.lab4.repository.impl.UserRepositoryImpl;
import fudan.se.lab4.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;


public class AccountServiceImpl implements AccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private Boolean hasLogin = false;
    private UserRepository userRepository=new UserRepositoryImpl();
    @Override
    public boolean login(User user) {
        //initial
        hasLogin = false;
        //to check whether user is null
        if (user == null || user.getName() == null || user.getPassword() == null) {
            throw new RuntimeException(InfoConstant.NULL_POINTER);
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
            logger.info(e.getMessage());
            throw e;
        }

    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean signup(User user) {

        try {
            if(user == null || user.getName() == null || user.getPassword() == null){
                throw new RuntimeException(InfoConstant.NULL_POINTER);
            }
            if (checkUserExisted(user.getName())) {
                //account already exist
                throw new RuntimeException(MessageFormat.format(InfoConstant.ENTITY_EXIST, "User", user.getName()));
            }else if (!checkName(user.getName())){
                throw new RuntimeException(InfoConstant.INVALID_USERNAME);
            }else if (!checkPassword(user.getPassword())){
                throw new RuntimeException(InfoConstant.INVALID_PASSWORD);
            }else {
                userRepository.createUser(user);
                logger.info(MessageFormat.format(InfoConstant.SUCCESS_TO_SIGN_UP, user.getName()));
                return true;
            }
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean checkStatus() {
        if (hasLogin) {
            logger.info(InfoConstant.USER_LOGIN);
        } else {
            logger.info(InfoConstant.USER_NOT_LOGIN);
        }
        return hasLogin;
    }

    @Override
    public boolean checkName(String name) {
        if (name == null) {
            logger.info(InfoConstant.INVALID_USERNAME);
            return false;
        }
        if("".equals(name)) {
            logger.info(InfoConstant.INVALID_USERNAME);
            return false;
        }
        if (!name.startsWith("starbb_")) {
            logger.info(InfoConstant.INVALID_USERNAME);
            return false;
        }
        boolean isValid = name.matches("[0-9A-Za-z_]*");
        if (!isValid) {
            logger.info(InfoConstant.INVALID_USERNAME);
            return false;
        }
        if (name.length() < 8 || name.length() >= 50) {
            logger.info(InfoConstant.INVALID_USERNAME);
            return false;
        }
        logger.info(MessageFormat.format(InfoConstant.USERNAME_VALID, name));
        return true;
    }


    private boolean checkUserExisted(String userName) {
        if(userName==null) {
            throw new RuntimeException(InfoConstant.NULL_POINTER);
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
        if(password == null){
            logger.info(InfoConstant.INVALID_PASSWORD);
            return false;
        }
        if("".equals(password)) {
            logger.info(InfoConstant.INVALID_PASSWORD);
        }
        if(!(password.matches(".*[A-Za-z]+.*") && password.matches(".*[0-9]+.*") && password.matches(".*[_]+.*"))){
            logger.info(InfoConstant.INVALID_PASSWORD);
            return false;
        }
        if(password.matches(".*[^0-9A-Za-z_].*")){
            logger.info(InfoConstant.INVALID_PASSWORD);
            return false;
        }
        if(password.length() < 8 || password.length() >= 100){
            logger.info(InfoConstant.INVALID_PASSWORD);
            return false;
        }
        logger.info(MessageFormat.format(InfoConstant.PASSWORD_VALID, password));
        return true;
    }

    private boolean checkUserInfoMatch(User user) {
        if(user==null) {
            throw new RuntimeException(InfoConstant.NULL_POINTER);
        }
        User userOfCsv = userRepository.getUser(user.getName());
        hasLogin = user.getPassword().equals(userOfCsv.getPassword());
        if (hasLogin) {
            logger.info(MessageFormat.format(InfoConstant.SUCCESS_TO_LOGIN, user.getName()));
            return true;
        } else {
            throw new RuntimeException(InfoConstant.WRONG_MATCH);
        }
    }
}
