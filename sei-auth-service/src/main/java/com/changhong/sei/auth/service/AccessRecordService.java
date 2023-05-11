package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.AccessRecordDao;
import com.changhong.sei.auth.dto.AccessRecordFeatureResponse;
import com.changhong.sei.auth.dto.AccessRecordUserResponse;
import com.changhong.sei.auth.common.TimePeriod;
import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.limiter.support.lock.SeiLock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 访问记录(AccessRecord)表服务实现类
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@Service("accessRecordService")
public class AccessRecordService {
    @Autowired
    private AccessRecordDao dao;

    /**
     * 添加访问记录
     *
     * @param record 参数
     * @return 添加结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultData<Void> addRecord(AccessRecord record) {
        if (Objects.nonNull(record)) {
            dao.save(record);
            return ResultData.success();
        } else {
            // 访问记录不能为空
            return ResultData.fail(ContextUtil.getMessage("access_record_0001"));
        }
    }

    /**
     * 批量添加访问记录
     *
     * @param records 访问记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchAddRecord(List<AccessRecord> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            dao.save(records);
        }
    }

    /**
     * 清除小于指定时间的数据
     */
    @SeiLock(key = "'auth:cleanAccessLog'")
    @Transactional(rollbackFor = Exception.class)
    public void cleanAccessLog() {
        dao.cleanAccessLog(LocalDateTime.now().minusMonths(1));
    }

    /**
     * 指定时间段访问top N的功能
     */
    public ResultData<List<AccessRecordFeatureResponse>> getTopFeatures(String tenant, String type, TimePeriod period, int topNum) {
        if (StringUtils.isBlank(tenant)) {
            // 租户代码不能为空.
            return ResultData.fail(ContextUtil.getMessage("access_record_0002"));
        }
        if (topNum <= 0) {
            topNum = 10;
        }
        // 排名数
        Pageable pageable = PageRequest.of(0, topNum);

        List<AccessRecordFeatureResponse> result;
        if (StringUtils.isBlank(type) || StringUtils.equalsIgnoreCase("ALL", type)) {
            result = dao.getTopFeatures(tenant, period.getTargetTime(), pageable);
        } else {
            result = dao.getTopFeatures(tenant, type, period.getTargetTime(), pageable);
        }

        return ResultData.success(result);
    }

    /**
     * 指定时间段访问top N的人
     */
    public ResultData<List<AccessRecordUserResponse>> getTopUsers(String tenant, String type, TimePeriod period, int topNum) {
        if (StringUtils.isBlank(tenant)) {
            // 租户代码不能为空.
            return ResultData.fail(ContextUtil.getMessage("access_record_0002"));
        }
        if (topNum <= 0) {
            topNum = 10;
        }
        // 排名数
        Pageable pageable = PageRequest.of(0, topNum);

        List<AccessRecordUserResponse> result;
        if (StringUtils.isBlank(type) || StringUtils.equalsIgnoreCase("ALL", type)) {
            result = dao.getTopUsers(tenant, period.getTargetTime(), pageable);
        } else {
            result = dao.getTopUsers(tenant, type, period.getTargetTime(), pageable);
        }

        return ResultData.success(result);
    }

    /**
     * 指定时间段某一功能访问的人
     */
    public ResultData<List<AccessRecordUserResponse>> getUsersByFeature(String tenant, String feature, TimePeriod period, int topNum) {
        if (StringUtils.isBlank(tenant)) {
            // 租户代码不能为空.
            return ResultData.fail(ContextUtil.getMessage("access_record_0002"));
        }
        if (StringUtils.isBlank(feature)) {
            // 功能不能为空.
            return ResultData.fail(ContextUtil.getMessage("access_record_0003"));
        }
        if (topNum <= 0) {
            topNum = 10;
        }
        // 排名数
        Pageable pageable = PageRequest.of(0, topNum);

        List<AccessRecordUserResponse> result = dao.getUsersByFeature(tenant, feature, period.getTargetTime(), pageable);

        return ResultData.success(result);
    }

    /**
     * 指定时间段某人访问的功能
     */
    public ResultData<List<AccessRecordFeatureResponse>> getFeaturesByUser(String tenant, String account, TimePeriod period, int topNum) {
        if (StringUtils.isBlank(tenant)) {
            // 租户代码不能为空.
            return ResultData.fail(ContextUtil.getMessage("access_record_0002"));
        }
        if (StringUtils.isBlank(account)) {
            // 账号参数不能为空.
            return ResultData.fail(ContextUtil.getMessage("access_record_0004"));
        }
        if (topNum <= 0) {
            topNum = 10;
        }
        // 排名数
        Pageable pageable = PageRequest.of(0, topNum);

        List<AccessRecordFeatureResponse> result = dao.getFeaturesByUser(tenant, account, period.getTargetTime(), pageable);
        return ResultData.success(result);
    }
}
