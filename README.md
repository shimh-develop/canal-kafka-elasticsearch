# canal-kafka-elasticsearch

数据由 canal -> kafka -> elasticsearch，最终将MySQL的数据插入到elasticsearch中

## canal
* canal客户端拉取数据，将数据放入kafka
* com.shimh.canal 包下对 canal-client 做了一些简单封装

## kafka
* 生产者发送数据
* 消费者可以批量或单条处理数据
* 多个消费者并行处理

## elasticsearch
* 批量插入 elasticsearch
* ElasticSearchTest.java 整理了一些常用的 elasticsearch Java API 示例
