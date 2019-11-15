package com.shimh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author: shimh
 * @create: 2019年11月
 **/
public class KafkaTest extends Test {



    @Autowired
    KafkaTemplate kafkaTemplate;

    @org.junit.Test
    public void send() {
        kafkaTemplate.send("test", "shimh");
    }
}
