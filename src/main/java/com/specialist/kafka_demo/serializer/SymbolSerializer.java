package com.specialist.kafka_demo.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.specialist.kafka_demo.model.Symbol;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Log4j2
public class SymbolSerializer implements Serializer<Symbol> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, Symbol data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            log.error("Error serializing Person to JSON", e);
            return null;
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Symbol data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
