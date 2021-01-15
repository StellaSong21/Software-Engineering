package fudan.se.lab4.service.impl;

import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.service.LoggerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerServiceImpl implements LoggerService {
    private Logger loggerSucc;
    private Logger loggerFail;
    private static final LoggerServiceImpl loggerService = new LoggerServiceImpl();
    private LoggerServiceImpl(){
        try {
            loggerSucc = Logger.getLogger("starbb.logger.success");
            FileHandler fileHandlerSucc = new FileHandler(FileConstant.LOGGER_SUCCESS,true);
            fileHandlerSucc.setLevel(Level.INFO);
            loggerSucc.addHandler(fileHandlerSucc);

            loggerFail = Logger.getLogger("starbb.logger.fail");
            FileHandler fileHandlerFail = new FileHandler(FileConstant.LOGGER_FAIL,true);
            fileHandlerFail.setLevel(Level.INFO);
            loggerFail.addHandler(fileHandlerFail);
        }catch (IOException e){
            loggerFail.info(InfoConstant.LOGGERFILE_FAIL_TO_OPEN);
        }
    }

    public static LoggerServiceImpl getLoggerService() {
        return loggerService;
    }

    @Override
    public void log(String des,String status) {
        if(status.equals(InfoConstant.LOGGER_SUCCESS_STATUS)){
            loggerSucc.info(des);
        }else if(status.equals(InfoConstant.LOGGER_FAIL_STATUS)){
            loggerFail.info(des);
        }
    }

    public Logger getLogger(String status){
        if(status.equals(InfoConstant.LOGGER_SUCCESS_STATUS)){
            return loggerSucc;
        }else{
            return loggerFail;
        }
    }
}
