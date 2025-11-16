package bo.gob.yguasu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YguasuBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YguasuBackendApplication.class, args);
    }

}
