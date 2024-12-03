package com.specialist.kafka_demo.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.specialist.kafka_demo.model.Symbol;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Маппер для получения объекта из Json строки
 */

@Log4j2
@Component
public class SymbolDeserializer {

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Получение документа в виде объекта
     *
     * @param json - строка Json
     * @return - Symbol
     */
    public Optional<Symbol> getSymbol(String json) {
        if (json == null) {
            log.error("На маппер getSymbol поступил объект, строка [json] == NULL!");
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, Symbol.class));
        } catch (Exception e) {
            log.error("Error : Ошибка при парсинге документа KafkaMessage :{}", e.getMessage());
            log.debug("PrintStackTrace : Ошибка при парсинге KafkaMessage:",  e);
            return Optional.empty();
        }
    }

}
