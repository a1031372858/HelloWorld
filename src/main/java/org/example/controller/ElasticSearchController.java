package org.example.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @GetMapping("index")
    public String helloWorld() {
        log.info("查询索引");
        try{
            GetIndexResponse getIndexResponse = elasticsearchClient.indices().get(i -> i.index("xuyachang"));
            return getIndexResponse.toString();
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询失败。报错信息={}",e.toString());
        }
        return "查询失败";
    }

    @GetMapping("create")
    public String createIndex(String indexName){
        try {
            CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> c.index(indexName));
            return createIndexResponse.toString();
        }catch (Exception e){
            e.printStackTrace();
            log.error("创建失败。报错信息={}",e.toString());
        }
        return "失败";

    }
}
