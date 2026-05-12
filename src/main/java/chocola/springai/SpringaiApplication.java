package chocola.springai;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class SpringaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringaiApplication.class, args);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
