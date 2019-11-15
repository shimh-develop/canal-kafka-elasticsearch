package com.shimh;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: shimh
 * @create: 2019年11月
 **/
public class ElasticSearchTest extends Test{

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private TransportClient client;

    /**
     * 创建一个文档 如果ID已经存在，会覆盖掉以前的，比如这次的字段只有一个，那文档也会变为只有一个字段
     */
    @org.junit.Test
    public void insert() {
        IndexRequest request = new IndexRequest("test", "test", "1");
        Map<String, String> map = new HashMap<>();
        map.put("name", "shimh");
        map.put("location", "shandong2");
        map.put("sex", "1");
        request.source(map);

        client.index(request);

    }
    /**
     * 创建一个文档 如果ID已经存在，会覆盖掉以前的，比如这次的字段只有一个，那文档也会变为只有一个字段
     */
    @org.junit.Test
    public void insert2() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "shimh");
        map.put("location", "shandong3");
        map.put("sex", "1");
        client.prepareIndex("test", "test", "2").setSource(map).get();

    }

    /**
     * 根据ID更新一个文档，可以更新哪个字段就只加哪个字段，ID不存在啥也不做
     */
    @org.junit.Test
    public void update() {
        UpdateRequest request = new UpdateRequest("test", "test", "1");
        Map<String, String> map = new HashMap<>();
        map.put("sex", "2");
        request.doc(map);
        client.update(request);
    }

    /**
     * 根据ID更新一个文档，可以更新哪个字段就只加哪个字段，ID不存在会报错
     */
    @org.junit.Test
    public void update2() {
        Map<String, String> map = new HashMap<>();
        map.put("sex", "2");
        client.prepareUpdate("test", "test", "2").setDoc(map).get();
    }

    /**
     * 根据ID删除一个文档， 如果ID不存在不会报错
     */
    @org.junit.Test
    public void delete() {
        DeleteRequest request = new DeleteRequest("test", "test", "1");
        client.delete(request);
    }

    /**
     * 根据ID删除一个文档， 如果ID不存在不会报错
     */
    @org.junit.Test
    public void delete2() {
        client.prepareDelete("test", "test", "2").get();
    }

    @org.junit.Test
    public void delete3() {
//        SearchRequest searchRequest = new SearchRequest("test");
//        searchRequest.types("type");
//        SearchSourceBuilder builder = SearchSourceBuilder.searchSource();
//        builder.query(QueryBuilders.matchQuery("gender", "male"));
//        searchRequest.source(builder);
//        DeleteByQueryRequest request =
//                new DeleteByQueryRequest(searchRequest);

        BulkByScrollResponse response =
                DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                        .filter(QueryBuilders.matchQuery("sex", "1")) //查询条件
                        .source("test") //index(索引名)
                        .get();  //执行

        long deleted = response.getDeleted(); //删除文档的数量
        System.out.println(deleted);
    }

    /**
     * 根据ID获取文档 ID不存在 不会报错
     */
    @org.junit.Test
    public void get() {
        GetRequest getRequest = new GetRequest("test", "test", "1");
        System.out.println(client.get(getRequest).actionGet());
    }

    /**
     * 批量操作
     */
    @org.junit.Test
    public void batch() {

        IndexRequest request = new IndexRequest("test", "test", "3");
        Map<String, String> map = new HashMap<>();
        map.put("name", "shimh");
        map.put("location", "shandong2");
        map.put("sex", "1");
        request.source(map);
        IndexRequest request2 = new IndexRequest("test", "test", "4");
        Map<String, String> map2 = new HashMap<>();
        map2.put("name", "shimh");
        map2.put("location", "shandong2");
        map2.put("sex", "1");
        request2.source(map2);
        client.prepareBulk().add(request).add(request2).get();
    }


    @org.junit.Test
    public void batch2() {

        BulkProcessor bulkProcessor =  BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {

            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
            }

        }).setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                //.setConcurrentRequests(1)
                .setConcurrentRequests(0) // 测试使用
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();

        IndexRequest request = new IndexRequest("test", "test", "9");
        Map<String, String> map = new HashMap<>();
        map.put("name", "shimh");
        map.put("location", "shandong2");
        map.put("sex", "1");
        request.source(map);
        IndexRequest request2 = new IndexRequest("test", "test", "10");
        Map<String, String> map2 = new HashMap<>();
        map2.put("name", "shimh");
        map2.put("location", "shandong2");
        map2.put("sex", "1");
        request2.source(map2);

        bulkProcessor.add(request);
        bulkProcessor.add(request2);

        bulkProcessor.flush();
        bulkProcessor.close();
    }








    /**
     * 查询 可以分页，但深度分页会有问题
     *
     * 可以自定义 QueryBuilder
     */
    @org.junit.Test
    public void search() {

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch();
        searchRequestBuilder.setIndices("test");
        searchRequestBuilder.setTypes("test");
        searchRequestBuilder.setSize(4000);

        // searchRequestBuilder.setQuery();
        // searchRequestBuilder.addAggregation();
        // 执行
        SearchResponse searchResponse = searchRequestBuilder.get();
        // 搜索结果
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        System.out.println(searchHits.length);

        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.getSourceAsString());
        }

    }

    /**
     * 游标方式分页获取数据 不能用于用户点击分页，使用后台数据量大的情况
     */
    @org.junit.Test
    public void scroll() {

        SearchResponse searchResponse = client.prepareSearch("test", "test")
                .setSize(1)
                .setScroll(new TimeValue(10000))
                .get();

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        while (searchHits != null && searchHits.length > 0) {

            for (SearchHit searchHit : searchHits) {
                System.out.println(searchHit.getSourceAsString());
            }

            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(new TimeValue(10000)).execute().actionGet();
            searchHits = searchResponse.getHits().getHits();
        }
    }
}
