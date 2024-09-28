package org.example.repository;

import org.example.model.so.UserSO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xuyachang
 * @date 2024/9/23
 */
@Repository
public interface UserSORepository extends ElasticsearchRepository<UserSO, Long> {

}
