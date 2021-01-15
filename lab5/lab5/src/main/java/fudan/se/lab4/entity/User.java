package fudan.se.lab4.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class User {
    private String name;
    private String password;
    private static Logger logger = LoggerFactory.getLogger(User.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {

            this.name = name;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
            this.password = password;

    }

    @Override
    public String toString() {
        return MessageFormat.format("Name: {0}, Password: {1}", this.getName(), this.getPassword());
    }

}
