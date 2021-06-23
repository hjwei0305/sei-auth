package com.changhong.sei.auth.service.scheduling;

import com.changhong.sei.auth.common.RandomUtils;
import com.changhong.sei.auth.service.AccessRecordService;
import com.changhong.sei.auth.service.OnlineUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 实现功能：定时间隔任务
 * 1.预算期间定时关闭
 * 2.预算池自动结转
 * 3.预算维度属性名称更新
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-06-16 14:13
 */
@Component
public class RunIntervalScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(RunIntervalScheduler.class);

    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private AccessRecordService accessRecordService;

//    /**
//     * 定时关闭过期的预算期间
//     * 每月最后一日的上午23:59触发
//     */
//    @Scheduled(cron = "0 59 23 28-31 * ?")
//    public void closingOverduePeriod() {
//        // localDate.lengthOfMonth() 本月总天数. localDate.getDayOfMonth() 本月当前天数
//        LocalDate localDate = LocalDate.now();
//        if (localDate.lengthOfMonth() == localDate.getDayOfMonth()) {
//            LOG.info("启动定时任务-关闭过期的预算期间");
//            ResultData<Void> resultData = periodService.closingOverduePeriod();
//            LOG.info("关闭过期的预算期间任务完成: {}", resultData);
//        }
//    }

    /**
     * 定时注销会话
     * 每小时触发一次
     * 第一次延迟1小时后执行，之后按fixedDelay的规则每3600秒执行一次
     */
    @Scheduled(cron = "13 11 */1 * * ?")
//    @Scheduled(initialDelay = 3600, fixedRate = 3600)
    public void timedLogout() {
        try {
            // 通过增加随机数,尽量避免多实例并发处理,导致数据库锁表
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(10));
            LOG.info("启动定时任务-定时注销会话");
            onlineUserService.timedLogout();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时清除访问日志数据
     * 上次执行完毕后36000秒再执行一次
     */
    @Scheduled(fixedDelay = 36000000)
    public void cleanAccessLog() {
        try {
            // 通过增加随机数,尽量避免多实例并发处理,导致数据库锁表
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(10));
            LOG.info("启动定时任务-定时清除访问日志数据");
            accessRecordService.cleanAccessLog();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
