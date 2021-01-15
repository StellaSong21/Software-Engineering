package fudan.se.lab2.service.impl;

import fudan.se.lab2.constant.FileConstant;
import fudan.se.lab2.constant.InfoConstant;
import fudan.se.lab2.entity.User;
import fudan.se.lab2.repository.impl.UserRepositoryImpl;
import fudan.se.lab2.service.AccountService;
import fudan.se.lab2.util.FileUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.text.MessageFormat;


public class AccountServiceImpl implements AccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private Boolean hasLogin = false;

    @Override
    public boolean login(User user) {
        //initial
        hasLogin=false;
        //to check whether user is null
        if (user == null) {
            throw new RuntimeException(InfoConstant.NULL_POINTER);
        }

        //check name and password
        try {

            if (checkUsername(user.getName())) {
                boolean userMatch=checkUserInfoMatch(user);
                return userMatch;

            } else {
                hasLogin = false;
                throw new RuntimeException(MessageFormat.format(InfoConstant.USER_NOT_FOUND, user.getName()));
            }
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            throw e;
        }

    }

    @Override
    public boolean signup(User user) {

        try {
            if (checkUsername(user.getName())) {
                //account already exist
                throw new RuntimeException(MessageFormat.format(InfoConstant.Entity_EXIST,"User",user.getName()));
            } else if(checkPassword(user.getPassword())){
                //sign up part
                (new UserRepositoryImpl()).createUser(user);
                logger.info(MessageFormat.format(InfoConstant.SUCCESS_TO_SIGN_UP, user.getName()));
                return true;
            }
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            throw e;
        }
        return false;

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


    private boolean checkUsername(String userName) {
        if ("".equals(userName)) {
            throw new RuntimeException(InfoConstant.USERNAME_EMPTY);
        }
        boolean isExisted=false;
        try {
            isExisted=FileUtil.exist(userName, FileConstant.USER_CSV);
        } catch (RuntimeException e) {
            throw e;
        }
        return isExisted;
    }

    private boolean checkPassword(String pass){
        if("".equals(pass)){
            throw new RuntimeException(InfoConstant.PASS_EMPTY);
        }else {
            return true;
        }
    }

    private boolean checkUserInfoMatch(User user) {
        User userOfCsv = (new UserRepositoryImpl()).getUser(user.getName());
        hasLogin = user.getPassword().equals(userOfCsv.getPassword());
        if (hasLogin) {
            logger.info(MessageFormat.format(InfoConstant.SUCCESS_TO_LOGIN, user.getName()));
            return true;
        } else {
            throw new RuntimeException(InfoConstant.WRONG_MATCH);
        }
    }
}
