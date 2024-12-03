package com.specialist.kafka_demo.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.specialist.kafka_demo.model.Symbol;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Новый вариант сериализация, используется
 */

@Log4j2
@Component
public class SymbolToJson {

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Получение документа в виде Json строки
     *
     * @param symbol - объект
     * @return - Json строка
     */
    public String getJson(Symbol symbol) {
        if (symbol == null) {
            log.error("На маппер getJson поступил объект, строка [Symbol] == NULL!");
            return null;
        }
        try {
            return objectMapper.writeValueAsString(symbol);
        } catch (Exception e) {
            log.error("Error : Ошибка при парсинге документа KafkaMessage :{}", e.getMessage());
            log.debug("PrintStackTrace : Ошибка при парсинге KafkaMessage:",  e);
            return null;
        }
    }

}
