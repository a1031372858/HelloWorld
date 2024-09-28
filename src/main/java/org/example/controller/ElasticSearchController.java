package org.example.controller;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.example.model.request.BulkCreateRequest;
import org.example.model.to.UserTO;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author xuyachang
 * @date 2023/8/24
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("elasticsearch")
public class ElasticSearchController {

    private final RestHighLevelClient restHighLevelClient;

    private final AtomicInteger nameNum = new AtomicInteger(1);


    @GetMapping("get/{indexName}")
    public String getIndex(@PathVariable String indexName) {
        log.info("查询索引");
        try{
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            Map<String, List<AliasMetadata>> aliases = getIndexResponse.getAliases();
            Map<String, MappingMetadata> mappings = getIndexResponse.getMappings();
            Map<String, Settings> settings = getIndexResponse.getSettings();

            return "查询成功";
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询失败。报错信息={}",e.toString());
        }
        return "查询失败";
    }

    @GetMapping("create/{indexName}")
    public String createIndex(@PathVariable String indexName){
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            return String.valueOf(createIndexResponse.isAcknowledged());
        }catch (Exception e){
            e.printStackTrace();
            log.error("创建失败。报错信息={}",e.toString());
        }
        return "失败";

    }

    @GetMapping("delete/{indexName}")
    public String deleteIndex(@PathVariable String indexName){
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            AcknowledgedResponse response = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return String.valueOf(response.isAcknowledged());
        }catch (Exception e){
            e.printStackTrace();
            log.error("创建失败。报错信息={}",e.toString());
        }
        return "失败";

    }

    @GetMapping("exist/{indexName}")
    public String existsIndex(@PathVariable String indexName) {
        log.info("检查是否存在");
        try{
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            return exists ? "索引存在" : "索引不存在";
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询失败。报错信息={}",e.toString());
        }
        return "查询失败";
    }

    @PostMapping("document/create/{indexName}")
    public String createDocument(@PathVariable String indexName,@RequestBody UserTO userTO){
        if(Objects.isNull(userTO.getId())){
            return "id不能为空";
        }

        try {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(indexName).id(userTO.getId().toString());
            indexRequest.source(JSON.toJSONString(userTO),XContentType.JSON);
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            return index.toString();
        } catch (IOException e) {
            return "创建失败";
        }
    }

    @PostMapping("document/update/{indexName}")
    public String updateDocument(@PathVariable String indexName,@RequestBody UserTO userTO){
        try {
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(indexName).id(userTO.getId().toString());
            updateRequest.doc(XContentType.JSON,"mobile",userTO.getMobile());
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            return String.valueOf(update.getIndex());
        } catch (IOException e) {
            return "更新失败";
        }
    }

    @GetMapping("document/delete/{indexName}/{id}")
    public String deleteDocument(@PathVariable String indexName,@PathVariable String id){
        try {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.index(indexName).id(id);
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
            return String.valueOf(deleteResponse.getResult().toString());
        } catch (IOException e) {
            return "删除失败";
        }
    }

    @GetMapping("document/get/{indexName}/{id}")
    public String getDocument(@PathVariable String indexName,@PathVariable String id){
        try {
            GetRequest request =new GetRequest();
            request.index(indexName).id(id);
            GetResponse getResponse = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            return getResponse.getSourceAsString();
        } catch (IOException e) {
            return "查询失败";
        }
    }

    @PostMapping("document/bulkCreate/{indexName}")
    public String bulkCreateDocument(@PathVariable String indexName,@RequestBody BulkCreateRequest request){
        BulkRequest bulkRequest = new BulkRequest();
        request.getUserTOList().forEach(o->{
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(indexName)
                    .id(o.getId().toString())
                    .source(JSON.toJSONString(o),XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return String.valueOf(bulkResponse.getIngestTookInMillis());
        } catch (IOException e) {
            return "批量操作失败";
        }
    }
    @GetMapping("document/bulkDelete/{indexName}")
    public String bulkDeleteDocument(@PathVariable String indexName){
        BulkRequest bulkRequest = new BulkRequest();
        DeleteRequest request1 = new DeleteRequest();
        request1.index(indexName).id("1003");
        request1.index(indexName).id("1004");
        bulkRequest.add(request1);
        try {
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return bulkResponse.toString();
        } catch (IOException e) {
            return "批量操作失败";
        }
    }

    @GetMapping("document/allQuery/{indexName}")
    public String allQuery(@PathVariable String indexName){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        searchRequest.source(query);
        List<String> result = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit:hits) {
                result.add(hit.getSourceAsString());
            }
            return JSON.toJSONString(result);
        } catch (IOException e) {
            return "查询失败";
        }
    }

    @GetMapping("document/conditionQuery/{indexName}")
    public String conditionQuery(@PathVariable String indexName,String mobile){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("mobile", mobile);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(queryBuilder);
        searchRequest.source(sourceBuilder);
        List<String> result = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit:hits) {
                result.add(hit.getSourceAsString());
            }
            return JSON.toJSONString(result);
        } catch (IOException e) {
            return "查询失败";
        }

    }

    @GetMapping("document/page/{indexName}")
    public String page(@PathVariable String indexName,String mobile,int pageNo){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        MatchAllQueryBuilder builder = QueryBuilders.matchAllQuery();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(builder);
        //页码，从0开始
        sourceBuilder.from(pageNo);
        //页大小
        sourceBuilder.size(5);
        //排序
        sourceBuilder.sort("id", SortOrder.DESC);
        //包含的字段
        String[] s1 = {"id","name","mobile"};
        //去除的字段
        String[] s2 = {};
        sourceBuilder.fetchSource(s1,s2);
        searchRequest.source(sourceBuilder);
        List<String> result = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit:hits) {
                result.add(hit.getSourceAsString());
            }
            return JSON.toJSONString(result);
        } catch (IOException e) {
            return "查询失败";
        }
    }

    @GetMapping("document/combQuery/{indexName}")
    public String combQuery(@PathVariable String indexName,String mobile,int id){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchQuery("id",id));
        queryBuilder.must(QueryBuilders.matchQuery("mobile",mobile));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(queryBuilder);
        searchRequest.source(sourceBuilder);
        List<String> result = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit:hits) {
                result.add(hit.getSourceAsString());
            }
            return JSON.toJSONString(result);
        } catch (IOException e) {
            return "查询失败";
        }
    }

    @GetMapping("document/rangeQuery/{indexName}")
    public String rangeQuery(@PathVariable String indexName,String start,int end){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("id");
        queryBuilder.gte(start);
        queryBuilder.lte(end);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(queryBuilder);
        searchRequest.source(sourceBuilder);
        List<String> result = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit:hits) {
                result.add(hit.getSourceAsString());
            }
            return JSON.toJSONString(result);
        } catch (IOException e) {
            return "查询失败";
        }
    }


    @GetMapping("document/fuzzyQuery/{indexName}")
    public String fuzzyQuery(@PathVariable String indexName,String mobile){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        FuzzyQueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("mobile", mobile).fuzziness(Fuzziness.ONE);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(queryBuilder);
        searchRequest.source(sourceBuilder);
        List<String> result = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit:hits) {
                result.add(hit.getSourceAsString());
            }
            return JSON.toJSONString(result);
        } catch (IOException e) {
            return "查询失败";
        }
    }
}
