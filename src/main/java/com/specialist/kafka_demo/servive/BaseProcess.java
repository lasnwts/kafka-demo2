package com.specialist.kafka_demo.servive;

import com.specialist.kafka_demo.model.Symbol;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Log4j2
@Service
public class BaseProcess {

    @Value("${vowels.topic}")
    private String vowelsTopic;

    @Value("${consonants.topic}")
    private String consonantsTopic;


    private final KafkaProducerSymbol kafkaProducerSymbol;
    private final Generation generation;

    public BaseProcess(KafkaProducerSymbol kafkaProducerSymbol,
                       Generation generation) {
        this.kafkaProducerSymbol = kafkaProducerSymbol;
        this.generation = generation;

    }

    public void process() {
        log.info("Start process");
        List<Symbol> symbols = generation.generate();

        symbols.forEach(new Consumer<Symbol>() {
            @Override
            public void accept(Symbol symbol) {
                if ("a".equalsIgnoreCase(String.valueOf(symbol.getValue())) ||
                        "e".equalsIgnoreCase(String.valueOf(symbol.getValue())) ||
                        "i".equalsIgnoreCase(String.valueOf(symbol.getValue())) ||
                        "o".equalsIgnoreCase(String.valueOf(symbol.getValue())) ||
                        "u".equalsIgnoreCase(String.valueOf(symbol.getValue())) ||
                        "j".equalsIgnoreCase(String.valueOf(symbol.getValue())) ||
                        "y".equalsIgnoreCase(String.valueOf(symbol.getValue()))) {
                    kafkaProducerSymbol.sendMessage(symbol, vowelsTopic);
                } else {
                    kafkaProducerSymbol.sendMessage(symbol, consonantsTopic);
                }

            }
        });

//        for (Symbol symbol : symbols) {
//            switch (symbol.getValue()) {
//                case 'a', 'e', 'i', 'o', 'u', 'j', 'y':
//                    kafkaProducerSymbol.sendMessage(symbol, vowelsTopic);
//                    break;
//                default:
//                    kafkaProducerSymbol.sendMessage(symbol, consonantsTopic);
//                    break;
//            }
//        }
        log.info("End process");
    }
}
