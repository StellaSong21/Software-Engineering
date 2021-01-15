package fudan.se.lab4.service.impl;

import fudan.se.lab4.context.EnvironmentContext;
import fudan.se.lab4.service.LoggerService;

public class LoggerServiceImpl implements LoggerService {

    @Override
    public String log(String name) {
        EnvironmentContext context=EnvironmentContext.getEnvironmentContext();
        return context.getBundle().getString(name);
    }
}
