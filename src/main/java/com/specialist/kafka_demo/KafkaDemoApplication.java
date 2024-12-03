package com.specialist.kafka_demo;

import com.specialist.kafka_demo.servive.BaseProcess;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Класс для запуска приложения
 */

@Log4j2
@SpringBootApplication
public class KafkaDemoApplication implements CommandLineRunner {

    private final BaseProcess baseProcess;

    @Autowired
    public KafkaDemoApplication(BaseProcess baseProcess) {
        this.baseProcess = baseProcess;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${spring.application.name}:0.0.1") String appVersion) {
        return new OpenAPI().info(new Info()
                .title("API \n\r (Service [Микросервис: kafka-demo]")
                .contact(new Contact().email("alexl1967@mail.ru"))
                .version(appVersion)
                .description("API Kafka" +
                        " library for OpenAPI 3 with spring boot.")
                .termsOfService("../")
                .license(new License().name("specialist.ru")
                        .url("http://specialist.ru")));
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------");
        log.info("| Name service                 : Kafka-demo. Specialist 02.12.2024");
        log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------");
        baseProcess.process(); //Запускаем генерацию объектов и отправку в топики
    }
}
