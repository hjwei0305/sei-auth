package com.changhong.sei.auth.service.client;

import com.changhong.sei.auth.service.client.vo.FlowTaskPageResultVO;
import com.changhong.sei.core.config.properties.mock.MockUserProperties;
import com.changhong.sei.core.context.mock.LocalMockUser;
import com.changhong.sei.core.context.mock.MockUser;
import com.changhong.sei.core.dto.flow.FlowTask;
import com.changhong.sei.core.dto.serach.PageInfo;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.util.thread.ThreadLocalHolder;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-05-31 08:24
 */@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowClientTest {
    @Autowired
    private FlowClient flowClient;
    @Autowired
    public MockUserProperties properties;
    public static StopWatch stopWatch;

    @BeforeClass
    public static void setup() {
        // 初始化
        ThreadLocalHolder.begin();

        System.out.println("开始进入单元测试.......");
    }

    @Before
    public void mock() {
//        LOG.debug("当前模拟用户: {}", mockUser.mockUser(properties.getTenantCode(), properties.getAccount()));
        MockUser mockUser = new LocalMockUser();

        System.out.println("当前模拟用户: " + mockUser.mockUser(properties));
        stopWatch = StopWatch.createStarted();
    }

    @After
    public void after() {
        stopWatch.stop();
        System.out.println("耗时(ms): " + stopWatch.getTime());
    }


    @AfterClass
    public static void cleanup() {
        // 释放
        ThreadLocalHolder.end();
        System.out.println("单元测试资源释放.......");
    }

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