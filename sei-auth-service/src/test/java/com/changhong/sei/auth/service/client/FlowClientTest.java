package com.changhong.sei.auth.service.client;

import com.changhong.sei.auth.service.client.vo.FlowTaskPageResultVO;
import com.changhong.sei.core.dto.flow.FlowTask;
import com.changhong.sei.core.dto.serach.PageInfo;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.test.BaseUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-05-31 08:24
 */
public class FlowClientTest extends BaseUnitTest {
    @Autowired
    private FlowClient flowClient;

    @Test
    public void findTaskById() {
        FlowTask task = flowClient.findTaskById("123");
        System.out.println(task);
    }

    @Test
    public void findByBusinessModelIdWithAllCount() {
        Search search = Search.createSearch();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(1);
        pageInfo.setRows(10);
        search.setPageInfo(pageInfo);
        FlowTaskPageResultVO<FlowTask> flowTaskPageResult = flowClient.findByBusinessModelIdWithAllCount("", search);
        System.out.println(flowTaskPageResult);
    }
}