package org.example.controller;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.to.UserTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;


/**
 * @author xuyachang
 * @date 2023/8/24
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("elasticsearch")
public class ElasticSearchController {

    private final ElasticsearchClient elasticsearchClient;

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    @GetMapping("get/{indexName}")
    public String getIndex(@PathVariable String indexName) {
        log.info("查询索引");
        try{
            ElasticsearchIndicesClient indices = elasticsearchClient.indices();
            GetIndexResponse getIndexResponse = indices.get(i -> i.index(indexName));
            return getIndexResponse.toString();
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询失败。报错信息={}",e.toString());
        }
        return "查询失败";
    }

    @GetMapping("create/{indexName}")
    public String createIndex(@PathVariable String indexName){
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index(indexName).build();
            CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(createIndexRequest);
//            CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> c.index(indexName));
            return createIndexResponse.toString();
        }catch (Exception e){
            e.printStackTrace();
            log.error("创建失败。报错信息={}",e.toString());
        }
        return "失败";

    }

    @GetMapping("delete/{indexName}")
    public String deleteIndex(@PathVariable String indexName){
        try {
            DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(c -> c.index(indexName));
            return deleteIndexResponse.toString();
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
            BooleanResponse existsResponse = elasticsearchClient.indices().exists(i -> i.index(indexName));
            return String.valueOf(existsResponse.value());
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询失败。报错信息={}",e.toString());
        }
        return "查询失败";
    }

    @GetMapping("document/create/{indexName}/{id}")
    public String createDocument(@PathVariable String indexName,@PathVariable String id){
        UserTO user = new UserTO();
        user.setId(1L);
        user.setName("张三");
        user.setMobile(id);
        user.setBirthday(new Date());

        try {
//            CreateRequest<Object> createRequest = new CreateRequest.Builder<>()
//                    .index(indexName)
//                    .id(id)
//                    .document(user)
//                    .build();
            CreateResponse createResponse = elasticsearchClient.create(i->i.index(indexName).id(id).document(user));
            return createResponse.toString();
        } catch (IOException e) {
            return "创建失败";
        }
    }
    @GetMapping("document/delete/{indexName}/{id}")
    public String deleteDocument(@PathVariable String indexName,@PathVariable String id){
        try {
//            DeleteRequest deleteRequest = new DeleteRequest.Builder()
//                    .index(indexName)
//                    .id(id)
//                    .build();
            DeleteResponse deleteResponse = elasticsearchClient.delete(i->i.index(indexName).id(id));
            return deleteResponse.toString();
        } catch (IOException e) {
            return "删除失败";
        }
    }

    @GetMapping("document/get/{id}")
    public String getDocument(@PathVariable String id){
        try {
            MatchQuery matchQuery = new MatchQuery.Builder()
                    .field("name").query(id)
                    .build();
            Query query = new Query.Builder().match(matchQuery).build();
            SearchRequest searchRequest = new SearchRequest.Builder().query(query).build();
            SearchResponse<Object> searchResponse = elasticsearchClient.search(searchRequest, Object.class);
            return searchResponse.toString();
        } catch (IOException e) {
            return "查询失败";
        }
    }

    @GetMapping("document/async/create/{indexName}/{id}")
    public String createDocumentAsync(@PathVariable String indexName,@PathVariable String id){
        UserTO user = new UserTO();
        user.setId(1L);
        user.setName("张三");
        user.setMobile(id);
        user.setBirthday(new Date());

        try {
//            CreateRequest<Object> createRequest = new CreateRequest.Builder<>()
//                    .index(indexName)
//                    .id(id)
//                    .document(user)
//                    .build();
            CompletableFuture<CreateResponse> createResponseCompletableFuture = elasticsearchAsyncClient.create(i -> i.index(indexName).id(id).document(user));
            createResponseCompletableFuture.whenComplete((response,e)->{
                log.info("创建文档响应结果:"+response.toString());
            });
            return "创建成功";
        } catch (Exception e) {
            return "创建失败";
        }
    }
}
