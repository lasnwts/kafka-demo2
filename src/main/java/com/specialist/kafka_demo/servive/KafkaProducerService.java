package com.specialist.kafka_demo.servive;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



/**
 * Класс отправки сообщений Кафка
 */
@Log4j2
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Простой вариант отправки сообщений, без проверки
     *
     * @param topic - топик
     * @param msg   - сообщение
     */
    public boolean sendMessage(String topic, String msg) {
        try {
            kafkaTemplate.send(topic, msg);
            log.info("UsbLog:Success send.Topic={}; Send message={}", topic, msg);
            return true;
        } catch (Exception exception) {
            log.error("!!!!<ERROR send message>[sendMessage(String topic, String msg)]!!!");
            log.error("send failure:topic:{}", topic);
            log.error("send failure:message:{}", msg);
            log.error("Exception:", exception);
            return false;
        }
    }

    /**
     * Простой вариант отправки сообщений, без проверки2
     *
     * @param topic - топик
     * @param msg   - сообщение
     */
    public boolean sendMessage(String topic, String key, String msg) {
        try {
            kafkaTemplate.send(topic, key, msg);
            log.info("Success send.Topic={}; key={}; Send message={};", topic, key, msg);
            return true;
        } catch (Exception exception) {
            log.error("send failure:topic:{}", topic);
            log.error("send failure:message:{}", msg);
            log.error("Exception::", exception);
            return false;
        }
    }

}
