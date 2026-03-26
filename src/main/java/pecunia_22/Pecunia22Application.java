package pecunia_22;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Pecunia22Application {

    public static void main(String[] args) {
        SpringApplication.run(Pecunia22Application.class, args);
    }

}
