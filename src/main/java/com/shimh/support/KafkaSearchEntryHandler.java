package com.shimh.support;

import com.shimh.canal.entry.AbstractEntryHandler;
import com.shimh.canal.entry.MapRowDataHandler;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

/**
 * @author: shimh
 * @create: 2019年11月
 **/
public class KafkaSearchEntryHandler extends AbstractEntryHandler<String> {

    KafkaTemplate kafkaTemplate;

    public KafkaSearchEntryHandler(KafkaTemplate kafkaTemplate) {
        super(new JsonStringRowDataHandler());
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    protected void doInsert(String after) {
        kafkaTemplate.send("thing1", after);
    }

    @Override
    protected void doUpdate(String before, String after) {
        doInsert(after);
    }

    @Override
    protected void doDelete(String before) {
    }



}
