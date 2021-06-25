package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AccessRecordApi;
import com.changhong.sei.auth.dto.AccessRecordCreateRequest;
import com.changhong.sei.auth.dto.AccessRecordFeatureResponse;
import com.changhong.sei.auth.dto.AccessRecordUserResponse;
import com.changhong.sei.auth.dto.TimePeriod;
import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.auth.service.AccessRecordService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.util.EnumUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访问记录(AccessRecord)表控制层
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@RestController
@Api(value = "AccessRecordApi", tags = "访问记录服务")
@RequestMapping(path = AccessRecordApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccessRecordController implements AccessRecordApi {
    /**
     * 访问记录服务对象
     */
    @Autowired
    private AccessRecordService accessRecordService;

    /**
     * 添加访问记录
     *
     * @param request 参数
     * @return 添加结果
     */
    @Override
    public ResultData<Void> addRecord(AccessRecordCreateRequest record, HttpServletRequest request) {
        SessionUser sessionUser = ContextUtil.getSessionUser();
        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setType("PAGE");

        accessRecord.setTenantCode(sessionUser.getTenantCode());
        accessRecord.setUserId(sessionUser.getUserId());
        accessRecord.setUserAccount(sessionUser.getLoginAccount());
        accessRecord.setUserName(sessionUser.getUserName());
        accessRecord.setAppModule(record.getAppCode());
        accessRecord.setFeatureCode(record.getFeatureCode());
        accessRecord.setFeature(record.getFeature());
        accessRecord.setTraceId("NaN");
        accessRecord.setPath(record.getUrl());
        accessRecord.setUrl(record.getUrl());
        accessRecord.setMethod("GET");
        accessRecord.setStatusCode(200);
        accessRecord.setDuration(-1L);
        accessRecord.setIp(HttpUtils.getClientIP(request));
        //解析agent字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(HttpUtils.getUserAgent(request));

        accessRecord.setBrowser(userAgent.getBrowser().getName());
        accessRecord.setOsName(userAgent.getOperatingSystem().getName());
        accessRecord.setAccessTime(LocalDateTime.now());

        return accessRecordService.addRecord(accessRecord);
    }

    /**
     * 获取时间段周期
     */
    @Override
    public ResultData<List<Map<String, String>>> getPeriods() {
        Map<String, String> map = EnumUtils.getEnumMap(TimePeriod.class);
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> mapData;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mapData = new HashMap<>();
            mapData.put("value", entry.getKey());
            mapData.put("name", entry.getValue());
            data.add(mapData);
        }
        return ResultData.success(data);
    }

    /**
     * 指定时间段访问top N的功能
     */
    @Override
    public ResultData<List<AccessRecordFeatureResponse>> getTopFeatures(String tenant, String type, String period, int topNum) {
        TimePeriod timePeriod = EnumUtils.getEnum(TimePeriod.class, period);
        return accessRecordService.getTopFeatures(tenant, type, timePeriod, topNum);
    }

    /**
     * 指定时间段访问top N的人
     */
    @Override
    public ResultData<List<AccessRecordUserResponse>> getTopUsers(String tenant, String type, String period, int topNum) {
        TimePeriod timePeriod = EnumUtils.getEnum(TimePeriod.class, period);
        return accessRecordService.getTopUsers(tenant, type, timePeriod, topNum);
    }

    /**
     * 指定时间段某一功能访问的人
     */
    @Override
    public ResultData<List<AccessRecordUserResponse>> getUsersByFeature(String tenant, String feature, String period, int topNum) {
        TimePeriod timePeriod = EnumUtils.getEnum(TimePeriod.class, period);
        return accessRecordService.getUsersByFeature(tenant, feature, timePeriod, topNum);
    }

    /**
     * 指定时间段某人访问的功能
     */
    @Override
    public ResultData<List<AccessRecordFeatureResponse>> getFeaturesByUser(String tenant, String account, String period, int topNum) {
        TimePeriod timePeriod = EnumUtils.getEnum(TimePeriod.class, period);
        return accessRecordService.getFeaturesByUser(tenant, account, timePeriod, topNum);
    }
}