package fudan.se.lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Lab1Application {

    @RestController
    public class HelloController {
        @CrossOrigin(origins = "*")
        @RequestMapping("/hello")
        public String index() {
            return "Hello, id: 17302010079, name: Song Yijing!";
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(Lab1Application.class, args);
    }

}
