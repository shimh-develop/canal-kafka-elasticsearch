package com.shimh.canal.message;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.shimh.canal.entry.AbstractEntryHandler;
import com.shimh.canal.entry.EntryHandlerFactory;

import java.util.List;

/**
 * @author: shimh
 * @create: 2019年10月
 **/
public class SimpleMessageHandler implements MessageHandler<Message>{

    private EntryHandlerFactory entryHandlerFactory;

    public SimpleMessageHandler (EntryHandlerFactory entryHandlerFactory) {
        this.entryHandlerFactory = entryHandlerFactory;
    }

    @Override
    public void handler(Message message) {
        List<CanalEntry.Entry> entries = message.getEntries();
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType().equals(CanalEntry.EntryType.ROWDATA)) {

                AbstractEntryHandler entryHandler = entryHandlerFactory.getOrDefaultHandler(entry.getHeader().getTableName());
                if (null != entryHandler) {
                    entryHandler.handler(entry);
                }
            }
        }
    }
}
