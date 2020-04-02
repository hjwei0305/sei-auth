package com.changhong.sei.auth.service;

import com.changhong.sei.core.constant.Constants;
import com.changhong.sei.core.context.ContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-10 01:03
 */
@Component
public class TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    public void testError() {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        int m = 1;
        int n = 0;
        System.out.println(m/n);
    }

    @Async
    public void doTaskOne() throws Exception {
        log.info("开始做任务一");
        long start = System.currentTimeMillis();
        log.info("当前会话用户: {}", ContextUtil.getSessionUser());
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        log.info("完成任务一，耗时：" + (end - start) + "毫秒");
    }

    @Async
    public void doTaskTwo() throws Exception {
        log.info("开始做任务二");
        long start = System.currentTimeMillis();
        log.info("当前会话用户: {}", ContextUtil.getSessionUser());
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        log.info("完成任务二，耗时：" + (end - start) + "毫秒");
    }

    @Async
    public void doTaskThree() throws Exception {
        log.info("开始做任务三");
        long start = System.currentTimeMillis();
        log.info("当前会话用户: {}", ContextUtil.getSessionUser());
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        log.info("完成任务三，耗时：" + (end - start) + "毫秒");
    }
}
