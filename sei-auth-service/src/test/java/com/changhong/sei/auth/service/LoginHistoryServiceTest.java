package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dto.AccountResponse;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.dto.serach.SearchFilter;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-21 14:38
 */
public class LoginHistoryServiceTest extends BaseUnitTest {
    @Autowired
    private LoginHistoryService service;

    @Test
    public void findByPage() {
        Search search = new Search();
        search.addFilter(new SearchFilter("loginDate", "2020-06-30", SearchFilter.Operator.LE, "date"));
        LogUtil.error("测试会话");
        PageResult<LoginHistory> resultData = service.findByPage(search);
        System.out.println(JsonUtils.toJson(resultData));
    }
}