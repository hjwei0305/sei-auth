package com.changhong.sei.auth.service.client.vo;

import com.changhong.sei.core.dto.serach.PageResult;

import java.io.Serializable;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/10/26 9:32      谭军(tanjun)                    新建
 * <p/>
 * *************************************************************************************************
 */
public class FlowTaskPageResultVO<T extends Serializable> extends PageResult<T> {
    private static final long serialVersionUID = -249382803337645968L;
    private Long allTotal;//所有待办的总数

    public Long getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(Long allTotal) {
        this.allTotal = allTotal;
    }
}
