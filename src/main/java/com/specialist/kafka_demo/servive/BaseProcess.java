package com.specialist.kafka_demo.servive;

import com.specialist.kafka_demo.model.Symbol;
import com.specialist.kafka_demo.serializer.SymbolDeserializer;
import com.specialist.kafka_demo.serializer.SymbolToJson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class BaseProcess {

    @Value("${vowels.topic}")
    private String vowelsTopic;

    @Value("${consonants.topic}")
    private String consonantsTopic;


    private final KafkaProducerService kafkaProducerSymbol;
    private final Generation generation;
    private final SymbolToJson symbolToJson;
    private final SymbolDeserializer symbolDeserializer;

    public BaseProcess(KafkaProducerService kafkaProducerSymbol,
                       Generation generation, SymbolToJson symbolToJson, SymbolDeserializer symbolDeserializer) {
        this.kafkaProducerSymbol = kafkaProducerSymbol;
        this.generation = generation;

        this.symbolToJson = symbolToJson;
        this.symbolDeserializer = symbolDeserializer;
    }

    public void process() {
        log.info("Start process");
        List<Symbol> symbols = generation.generate();

        for (Symbol symbol : symbols) {
            switch (symbol.getValue()) {
                case 'a', 'e', 'i', 'o', 'u', 'j', 'y':
                    kafkaProducerSymbol.sendMessage(vowelsTopic, symbolToJson.getJson(symbol));
                    break;
                default:
                    kafkaProducerSymbol.sendMessage(consonantsTopic, symbolToJson.getJson(symbol));
                    break;
            }
        }
        log.info("End process");
    }

    /**
     * Обработка сообщения
     * @param message - сообщение
     */
    public void process(String message, String source) {
        Optional<Symbol> symbol = symbolDeserializer.getSymbol(message);
        if (symbol.isPresent()) {
            log.info("Received: Source:{}, Symbol:{}", source, symbol.get());
        } else {
            log.error("Symbol is null");
        }
    }
}
