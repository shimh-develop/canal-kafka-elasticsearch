package com.shimh.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.impl.SimpleCanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.shimh.canal.message.MessageHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author: shimh
 * @create: 2019年10月
 **/
public class SimpleCanalClient extends AbstractCanalClient{

    public SimpleCanalClient(String ip, Integer port, String destination, String username, String password, MessageHandler messageHandler) {
        this(ip, port, destination, username, password, 60000, 3600000, messageHandler);
    }

    public SimpleCanalClient(String ip, Integer port, String destination, String username, String password , int soTimeout, int idleTimeout, MessageHandler messageHandler) {
        this.connector = new SimpleCanalConnector(new InetSocketAddress(ip,
                port), username, password, destination , soTimeout, idleTimeout);
        this.messageHandler = messageHandler;
    }

}
