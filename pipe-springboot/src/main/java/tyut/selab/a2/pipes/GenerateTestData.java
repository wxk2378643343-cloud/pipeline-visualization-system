package tyut.selab.a2.pipes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import tyut.selab.a2.pipes.service.IConverterService;
import tyut.selab.a2.pipes.service.IDataService;

@SpringBootApplication
@ServletComponentScan
public class GenerateTestData {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner run(IDataService service, IConverterService converter) throws Exception {
        return args -> {
            // do something
        };
    }
}
