package com.specialist.kafka_demo.controller;

import com.specialist.kafka_demo.servive.ApiLayer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Класс для тестирования канала отправки почтовых сооющений
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Контроллер для проверки почтовой подсистемы", description = "Проверка отправки почты")
public class ApiController {

    private final ApiLayer apiLayer;

    private final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    public ApiController(ApiLayer apiLayer) {
        this.apiLayer = apiLayer;
    }

    /**
     * Тест отправки почты
     */
    @GetMapping(value = "/")
    @Operation(summary = "Получить список всех топиков")
    public ResponseEntity<List<String>> getTopic() {
        List<String> topics = new ArrayList<>();
        try {
            topics = apiLayer.getTopics();
        } catch (Exception e) {
            logger.error("Произошла ошибка при запросе списка топиков. StackTrace:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    /**
     * Тест отправки почты
     */
    @DeleteMapping(value = "/{topicname}")
    @Operation(summary = "Введите имя топика для удаления")
    public ResponseEntity<String> delTopic(@Parameter(description = "Введите имя топика")
             @PathVariable("topicname") String topicname) {
        logger.info("WebAPI - команда удалить топик:{}", topicname);
        if (apiLayer.deleteTopic(topicname)) {
            logger.info("Топик успешно удален:{}", topicname);
            return new ResponseEntity<>("Топик успешно удален", HttpStatus.OK);
        } else {
            logger.error("Ошибка при удалении топика:{}", topicname);
            return new ResponseEntity<>("Ошибка при попытке удалить топик", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{topicname}")
    @Operation(summary = "Введите имя топика для создания")
    public ResponseEntity<String> createTopic(@Parameter(description = "Введите имя топика")
                                              @PathVariable("topicname") String topicname) {
        logger.info("WebAPI - команда создать топик:{}", topicname);
        //Тут можно добавить параметры топика
        if (apiLayer.createTopic(topicname, 1, (short) 1)) {
            logger.info("Топик успешно создан:{}", topicname);
            return new ResponseEntity<>("Топик успешно создан", HttpStatus.OK);
        } else {
            logger.error("Ошибка при создании топика:{}", topicname);
            return new ResponseEntity<>("Ошибка при попытке создать топик", HttpStatus.BAD_REQUEST);
        }
    }

}
