package ju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class UniversityApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityApiApplication.class, args);
    }

}
