package com.specialist.kafka_demo;

import com.specialist.kafka_demo.servive.Generation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class KafkaDemoApplication implements CommandLineRunner {

	private final Generation generation;

	@Autowired
	public KafkaDemoApplication(Generation generation) {
		this.generation = generation;
	}


	public static void main(String[] args) {
		SpringApplication.run(KafkaDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("----------------------------------------------------------------------------------------------------------------------------------------------------------");
		log.info("| Name service                 : Kafka-demo. Specialist 02.12.2024");
	generation.generate();
	}
}