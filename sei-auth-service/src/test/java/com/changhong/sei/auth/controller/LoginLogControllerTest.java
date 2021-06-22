package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.dto.LoginHistoryDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.dto.serach.SearchFilter;
import com.changhong.sei.core.test.BaseUnit5Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-06-17 16:35
 */
class LoginLogControllerTest extends BaseUnit5Test {
    @Autowired
    private LoginLogController controller;

    @Test
    void getTotalVisitCount() {
    }

    @Test
    void getTodayVisitCount() {
    }

    @Test
    void getTodayIp() {
    }

    @Test
    void getLastTenDaysVisitCount() {
    }

    @Test
    void getByBrowser() {
    }

    @Test
    void getByOperatingSystem() {
    }

    @Test
    void clearLog() {
    }

    @Test
    void getLoginLogByPage() {
        Search search = Search.createSearch();
        search.addFilter(new SearchFilter("loginDate", "2021-06-01 08:12:12", SearchFilter.Operator.GE, "date"));
        ResultData<PageResult<LoginHistoryDto>> resultData = controller.getLoginLogByPage(search);
        System.out.println(resultData);
    }
}