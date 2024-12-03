package com.specialist.kafka_demo.servive;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class ApiLayer {

    private final KafkaAdminApp kafkaAdminApp;

    @Autowired
    public ApiLayer(KafkaAdminApp kafkaAdminApp) {
        this.kafkaAdminApp = kafkaAdminApp;
    }

    public void createTopic(String topicName){
        log.info("Create topic: {}", topicName);
        kafkaAdminApp.createTopic(topicName);
    }

    public void deleteAllTopics(){
        log.info("Delete all topics");
        kafkaAdminApp.deleteAllTopics();
    }

    /**
     * Получение списка топиков
     * @return - список топиков
     */
    public List<String> getTopics(){
        log.info("Api command - Get all topics");
        return kafkaAdminApp.getLisTopics();
    }

    /**
     * Удаление топика
     * @param topicName - имя топика
     */
    public boolean deleteTopic(String topicName){
        log.info("Api command - Delete topic:{}", topicName);
        try {
            return kafkaAdminApp.deleteOneTopics(topicName);
        } catch (Exception e) {
            log.error("Error delete topic:{}", topicName, e);
            return false;
        }
    }

    /**
     * Создание топика
     * @param topicName - имя топика
     * @param partitionNumber - количество партиций
     * @param replication - фактор репликации
     * @return - true - если топик создан
     */
    public boolean createTopic(String topicName, int partitionNumber, short replication){
        log.info("Api command - Create topic:{}, partition:{}, replication Factor:{}", topicName, partitionNumber, replication);
        return kafkaAdminApp.createTopic(topicName, partitionNumber, replication);
    }
}
