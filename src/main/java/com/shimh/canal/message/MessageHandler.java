package com.shimh.canal.message;

/**
 * @author: shimh
 * @create: 2019年10月
 **/
public interface MessageHandler<T> {

    void handler(T t);

}
