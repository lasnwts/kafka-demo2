package com.specialist.kafka_demo.servive;

import org.springframework.stereotype.Component;

@Component
public class Generation {

    public void generate(){
        for(char i='a'; i<='z'; i++){
            int c=i;
            System.out.println(i+" :"+c+".");
        }
    }
}


