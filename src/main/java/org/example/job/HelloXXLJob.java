package org.example.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author xuyachang
 * @date 2024/4/27
 */
@Component
public class HelloXXLJob {

    @XxlJob("helloXXLJob")
    public void helloXXLJob(){
        System.out.println("Hello XXL JOB!");
    }
}
