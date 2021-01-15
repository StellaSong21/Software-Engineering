package fudan.se.lab4.service;

public interface LoggerService {
    /**
     *
     * @param des
     * @param status  成功log/失败log
     */
    void log(String des,String status);
}
