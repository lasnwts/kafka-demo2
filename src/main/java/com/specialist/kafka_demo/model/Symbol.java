package com.specialist.kafka_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Symbol {

    private long id;
    private char value;
    private String color;
    private String type;

}
