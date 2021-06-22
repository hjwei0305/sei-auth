package com.changhong.sei.auth.service;

import com.changhong.sei.core.test.BaseUnit5Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-06-22 23:36
 */
class OnlineUserServiceTest extends BaseUnit5Test {
    @Autowired
    private OnlineUserService service;

    @Test
    void getExpireTime() {
    }

    @Test
    void addSession() {
    }

    @Test
    void removeSession() {
    }

    @Test
    void timedLogout() {
        service.timedLogout();
    }
}