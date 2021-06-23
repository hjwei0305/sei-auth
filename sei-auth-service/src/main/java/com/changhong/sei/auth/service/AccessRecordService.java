package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.AccessRecordDao;
import com.changhong.sei.auth.dto.AccessRecordFeatureResponse;
import com.changhong.sei.auth.dto.AccessRecordUserResponse;
import com.changhong.sei.auth.dto.TimePeriod;
import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.service.BaseEntityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 访问记录(AccessRecord)表服务实现类
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@Service("accessRecordService")
public class AccessRecordService extends BaseEntityService<AccessRecord> {
    @Autowired
    private AccessRecordDao dao;

    @Override
    protected BaseEntityDao<AccessRecord> getDao() {
        return dao;
    }

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
            return ResultData.fail("访问记录不能为空");
        }
    }

    /**
     * 指定时间段访问top N的功能
     */
    public ResultData<List<AccessRecordFeatureResponse>> getTopFeatures(String tenant, String type, TimePeriod period, int topNum) {
        if (StringUtils.isBlank(tenant)) {
            return ResultData.fail("租户代码不能为空.");
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
            return ResultData.fail("租户代码不能为空.");
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
            return ResultData.fail("租户代码不能为空.");
        }
        if (StringUtils.isBlank(feature)) {
            return ResultData.fail("功能不能为空.");
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
            return ResultData.fail("租户代码不能为空.");
        }
        if (StringUtils.isBlank(account)) {
            return ResultData.fail("账号参数不能为空.");
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