package mops;

import mops.gruppen1.applicationService.GroupService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class that starts up the application.
 */
@SpringBootApplication
public class GruppenbildungApplication {

    /**
     * Method that starts up the application.
     * @param args Arguments that can be handled to the app per console.
     */
    public static void main(String[] args) {
        SpringApplication.run(GruppenbildungApplication.class, args);
    }
}
