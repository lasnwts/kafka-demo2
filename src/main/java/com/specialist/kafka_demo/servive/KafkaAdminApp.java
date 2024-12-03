package com.specialist.kafka_demo.servive;

import com.specialist.kafka_demo.config.KafkaConfig;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaAdminApp {

    /** Создание топика */
    public static void createTopic(String topicName) {
        createTopics(List.of(topicName));
    }

    private static final Logger logger = LoggerFactory.getLogger(KafkaAdminApp.class);

    /**
     * createTopics() - статический метод для создания топиков.
     *
     * @param topicNames Коллекция названий топиков для создания.
     */
    private static boolean createTopics(List<String> topicNames) {
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Получение списка существующих топиков */
            Set<String> existingTopics = adminClient.listTopics().names().get();

            /** Фильтрация названий топиков, которые уже существуют */
            List<String> newTopicNames = topicNames.stream()
                    .filter(topicName -> !existingTopics.contains(topicName))
                    .toList();

            if (newTopicNames.isEmpty()) {
                logger.info("No new topics to create.");
                return false;
            }

            /** Количество партиций для топика */
            int numPartitions = 3;

            /** Фактор репликации для топика (replication factor) определяет, сколько копий (реплик) каждого раздела (partition) топика хранится на различных брокерах Kafka в кластере
             * 1 - каждый раздел топика будет иметь одну реплику, которая является лидером. В этом случае отказоустойчивость не обеспечивается, так как нет дублирования данных на других брокерах;
             */
            short replicationFactor = 1;

            /** Создание топиков из коллекции названий */
            List<NewTopic> newTopics = topicNames.stream()
                    .map(topicName -> new NewTopic(topicName, numPartitions, replicationFactor))
                    .toList();

            createTopics(adminClient, newTopics, newTopicNames);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при создании топика", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }



    private static void createTopics(AdminClient adminClient, List<NewTopic> newTopics, List<String> newTopicNames) throws InterruptedException, ExecutionException {
        try {
            adminClient.createTopics(newTopics).all().get();
            logger.info("Топики '{}' успешно созданы.", newTopicNames);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                logger.warn("Some topics already exist: {}", e.getMessage());
            } else {
                throw e;
            }
        }
    }



    /**
     * deleteAllTopics() - статический метод для удаления топиков.
     */
    public static void deleteAllTopics() {

        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Получение списка всех топиков */
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            KafkaFuture<Set<String>> namesFuture = listTopicsResult.names();

            Set<String> topicNames = namesFuture.get();
            logger.info("Found topics[2]: {}", topicNames);

            if (topicNames.isEmpty()) {
                logger.info("No topics to delete.");
            } else {
                /** Удаление всех топиков */
                DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(topicNames);

                KafkaFuture<Void> allDeletedFuture = deleteTopicsResult.all();

                /** Обработка результата удаления */
                allDeletedFuture.whenComplete((result, exception) -> {
                    if (exception == null) {
                        logger.info("All topics deleted successfully.");
                    } else {
                        logger.error("Failed to delete topics", exception);
                    }
                });

                /** Ожидание завершения асинхронной операции (блокирующий вызов) */
                allDeletedFuture.get();
            }

        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error deleting Kafka topics", e);
            Thread.currentThread().interrupt();
        }
    }



    /** Удаление одного топика */
    public boolean deleteOneTopics(String topicName){

        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {
            DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList(topicName));
            deleteTopicsResult.all().get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при удалении топика: {}", topicName, e);
            Thread.currentThread().interrupt();
            return false;
        }
    }


    /**
     * describeTopics() - статический метод для описания топиков.
     */
    private static void describeTopics() {
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            KafkaFuture<Set<String>> namesFuture = listTopicsResult.names();

            Set<String> topicNames = namesFuture.get();
            logger.info("Found topics: {}", topicNames);

            if (topicNames.isEmpty()) {
                logger.info("No topics found.");
                return;
            }

            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(topicNames);

            /** Получение всех результатов описания асинхронно */
            Map<String, KafkaFuture<TopicDescription>> futures = describeTopicsResult.values();

            /** Обработка каждого Future описания топика */
            futures.forEach((topicName, future) ->
                    future.whenComplete((topicDescription, exception) -> {
                        if (exception == null) {
                            logger.info("Topic: {}, Description: {}", topicName, topicDescription);
                        } else {
                            logger.error("Failed to describe topic {}", topicName, exception);
                        }
                    })
            );

            /** Ожидание завершения всех асинхронных операций */
            describeTopicsResult.all().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при описании топиков", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Метод для описания всех партиций указанного топика Kafka.
     *
     * @param topicName Название топика, для которого нужно получить информацию о партициях.
     */
    public static void describeTopicPartitions(String topicName) {

        /** Создание экземпляра AdminClient с использованием конфигурации Kafka, полученной из класса KafkaConfig. */
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Получение объекта DescribeTopicsResult, который представляет асинхронный результат описания топиков. */
            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Set.of(topicName));

            /** Вызов метода all() возвращает карту (Map), содержащую описание каждого топика, включая только один
             * топик topicName. Метод get() блокирует выполнение до тех пор, пока асинхронная операция не завершится,
             * и возвращает результат описания топиков.
             */
            Map<String, TopicDescription> topicDescriptionMap = describeTopicsResult.all().get();

            /** Извлечение описания конкретного топика */
            TopicDescription topicDescription = topicDescriptionMap.get(topicName);

            /** Получение информации о партициях. Метод partitions() объекта TopicDescription, который возвращает
             * коллекцию TopicPartitionInfo. Каждый элемент коллекции представляет информацию о каждой партиции топика.
             */
            Collection<TopicPartitionInfo> partitions = topicDescription.partitions();

            partitions.forEach(partitionInfo ->
                    logger.info("Partition: {}, Leader: {}, Replicas: {}, ISR: {}",
                            partitionInfo.partition(),
                            partitionInfo.leader(),
                            partitionInfo.replicas(),
                            partitionInfo.isr())
            );

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Failed to describe partitions for topic {}", topicName, e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Метод addPartitionsToTopic() добавляет партиций в Топик.
     *
     * @param topicName  Название топика.
     * @param totalCount Новое общее количество партиций.
     */
    public static void addPartitionsToTopic(String topicName, int totalCount) {
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Создание объекта NewPartitions с новым общим количеством партиций */
            NewPartitions newPartitions = NewPartitions.increaseTo(totalCount);

            /** Создание Map с топиком и количеством новых партиций */
            Map<String, NewPartitions> newPartitionsMap = Map.of(topicName, newPartitions);

            /** Создание партиции */
            CreatePartitionsResult createPartitionsResult = adminClient.createPartitions(newPartitionsMap);

            /** Обработка результат создания партиций */
            createPartitionsResult.all().get();
            logger.info("Партиции успешно добавлены к топику '{}'. Новое количество партиций: {}", topicName, totalCount);

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при добавлении партиций к топику {}", topicName, e);
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Получение списка топиков
     *
     * @return Список топиков
     */
    public List<String> getLisTopics() {
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            KafkaFuture<Set<String>> namesFuture = listTopicsResult.names();

            Set<String> topicNames = namesFuture.get();
            logger.info("Found topics: {}", topicNames);
            return topicNames.stream().toList();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при получении списка топиков", e);
            Thread.currentThread().interrupt();
            return List.of();
        }
    }

    /**
     * Простое создание Топиков
     */
    public boolean createTopic(String topicName, int partition, short replication) {
        return simpleCreate(topicName, partition, replication);
    }

    /**
     * Простое создание Топиков
     */
    public static boolean simpleCreate(String topicname, int partition, short replicationFactors) {

        /** Создание AdminClient с использованием конфигурации из KafkaConfig и блока try-with-resources */
        try (AdminClient adminClient = AdminClient.create(KafkaConfig.getAdminConfig())) {

            /** Название создаваемых топиков */
            String topicName = topicname;

            /** Количество партиций для топика */
            int numPartitions = partition;

            /** Фактор репликации для топика (replication factor) определяет, сколько копий (реплик) каждого раздела (partition)
             * топика хранится на различных брокерах Kafka в кластере.
             * Каждый раздел в Kafka имеет одного лидера (leader) и ноль или более реплик (replicas).
             * Пример значений replication factor:
             *   0 - не будет никаких реплик для разделов топика, данные каждого раздела будут храниться только на одном брокере, который и является лидером раздела;
             *   1 - каждый раздел топика будет иметь одну реплику, которая является лидером. В этом случае отказоустойчивость не обеспечивается, так как нет дублирования данных на других брокерах;
             *   2 - означает, что каждый раздел топика будет иметь две реплики: одна из них будет лидером, а вторая будет репликой. Это обеспечивает отказоустойчивость, так как данные будут доступны для чтения и записи, даже если один из брокеров станет недоступным.
             */
            short replicationFactor = replicationFactors;

            /** Создание еденичного топика */
            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            logger.info("Топик '{}' успешно создан.", topicName);
            return true;


        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка при создании топика", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
