package com.specialist.kafka_demo.servive;

import com.specialist.kafka_demo.config.KafkaConfig;
import com.specialist.kafka_demo.model.Symbol;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

/**
 * Класс для отправки сообщений в Kafka
 */

@Log4j2
@Service
public class KafkaProducerSymbol {
    /**
     * Отправка сообщений
     * @param message - тело сообщения
     * @param topic - топик
     */
    public void sendMessage(Symbol message, String topic) {
        log.info("Send message: {}", message);
        try (KafkaProducer<String, Symbol> kafkaProducer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {
            ProducerRecord<String, Symbol> producerRecord = new ProducerRecord<>(topic, message);
            kafkaProducer.send(producerRecord);
            log.info("Отправлено сообщение:value-{} в топик:{}", message, topic);
            log.info("Отправка завершена.");
        } catch (Exception e) {
            log.error("Ошибка:{} при отправке сообщения:{} в топик:{} Kafka", e.getMessage(), message, topic);
        }
    }
}
