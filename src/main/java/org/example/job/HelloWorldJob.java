package org.example.job;

import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.stereotype.Component;


@Component
public class HelloWorldJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("执行定时任务");
    }
}
