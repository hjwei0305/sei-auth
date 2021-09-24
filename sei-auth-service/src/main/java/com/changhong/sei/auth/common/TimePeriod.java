package com.changhong.sei.auth.common;

import com.changhong.sei.annotation.Remark;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 实现功能：时间周期
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-18 10:46
 */
public enum TimePeriod {
    /**
     * 最近1天
     */
    @Remark(value = "TimePeriod_DAY_1", comments = "最近1天")
    DAY_1(1, ChronoUnit.DAYS),
    /**
     * 最近5天
     */
    @Remark(value = "TimePeriod_DAY_5", comments = "最近5天")
    DAY_5(5, ChronoUnit.DAYS),
    /**
     * 最近10天
     */
    @Remark(value = "TimePeriod_DAY_10", comments = "最近10天")
    DAY_10(10, ChronoUnit.DAYS),
    /**
     * 最近1月
     */
    @Remark(value = "TimePeriod_MONTH_1", comments = "最近1月")
    MONTH_1(1, ChronoUnit.MONTHS),
    /**
     * 最近3月
     */
    @Remark(value = "TimePeriod_MONTH_3", comments = "最近3月")
    MONTH_3(3, ChronoUnit.MONTHS),
    /**
     * 最近6月
     */
    @Remark(value = "TimePeriod_MONTH_6", comments = "最近6月")
    MONTH_6(6, ChronoUnit.MONTHS),
    /**
     * 最近1年
     */
    @Remark(value = "TimePeriod_YEAR_1", comments = "最近1年")
    YEAR_1(1, ChronoUnit.YEARS);

    TimePeriod(int num, ChronoUnit unit) {
        this.num = num;
        this.unit = unit;
    }

    // 数量
    private final int num;
    // 单位
    private final ChronoUnit unit;

    /**
     * 当前时间
     */
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    /**
     * 目标时间
     */
    public LocalDateTime getTargetTime() {
        return getCurrentTime().minus(num, unit);
    }
}
