package org.example.model.request;

import com.alibaba.fastjson2.JSON;
import lombok.Data;
import org.example.model.to.UserTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuyachang
 * @date 2024/9/28
 */
@Data
public class BulkCreateRequest {
    private List<UserTO> userTOList;
}
