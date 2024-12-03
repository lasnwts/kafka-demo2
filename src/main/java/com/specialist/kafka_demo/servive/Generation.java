package com.specialist.kafka_demo.servive;

import com.specialist.kafka_demo.model.Symbol;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для генерации символов
 */
@Log4j2
@Component
public class Generation {
    public List<Symbol> generate(){
        log.info("Generation symbols");
        List<Symbol> symbols=new ArrayList<>();
        for(char i='a'; i<='z'; i++){
            int c=i;
            symbols.add(new Symbol(c,i,null,null));
        }
        return symbols;
    }
}


