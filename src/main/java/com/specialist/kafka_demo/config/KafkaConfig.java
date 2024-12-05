package com.specialist.kafka_demo.config;


import com.specialist.kafka_demo.serializer.SymbolSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/** Конфигурация не используется в этом проекте */

public class KafkaConfig {


    private static final String BOOTSTRAP_SERVERS = "localhost:9093";

    private static final String SSL_TRUSTSTORE_LOCATION = "/path/to/your/truststore";
    private static final String SSL_TRUSTSTORE_PASSWORD = "your_truststore_password";
    private static final String SSL_KEYSTORE_LOCATION = "/path/to/your/keystore";
    private static final String SSL_KEYSTORE_PASSWORD = "your_keystore_password";

    private KafkaConfig() {
    }

    public static Properties getAdminConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Настройки SSL: протокол безопасности для соединения с брокером Kafka
         properties.put("security.protocol", "SSL"); */

        /** Настройки SSL: путь к файлу доверенных хранилищ (truststore), который содержит сертификаты CA (Certificate Authority), необходимые для проверки доверительных сертификатов брокера Kafka
         properties.put("ssl.truststore.location", SSL_TRUSTSTORE_LOCATION); */

        /** Настройки SSL: пароль для доступа к доверенному хранилищу
         properties.put("ssl.truststore.password", SSL_TRUSTSTORE_PASSWORD); */

        /** Настройки SSL: путь к файлу хранилища ключей (keystore), который содержит клиентский сертификат и закрытый ключ, используемые для аутентификации клиента перед брокером Kafka (если это требуется)
         properties.put("ssl.keystore.location", SSL_KEYSTORE_LOCATION); */

        /** Настройки SSL: пароль для доступа к хранилищу ключей
         properties.put("ssl.keystore.password", SSL_KEYSTORE_PASSWORD); */

        return properties;
    }

}
