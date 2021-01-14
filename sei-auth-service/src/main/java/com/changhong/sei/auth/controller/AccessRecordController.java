package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AccessRecordApi;
import com.changhong.sei.auth.dto.AccessRecordCreateRequest;
import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.auth.service.AccessRecordService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 访问记录(AccessRecord)表控制层
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@RestController
@Api(value = "AccessRecordApi", tags = "访问记录服务")
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
        accessRecord.setType("API");

        accessRecord.setTenantCode(sessionUser.getTenantCode());
        accessRecord.setUserId(sessionUser.getUserId());
        accessRecord.setUserAccount(sessionUser.getLoginAccount());
        accessRecord.setUserName(sessionUser.getUserName());
        accessRecord.setAppModule(record.getAppCode());
        accessRecord.setTraceId("NaN");
        accessRecord.setPath(record.getUrl());
        accessRecord.setUrl(record.getUrl());
        accessRecord.setMethod("GET");
        accessRecord.setDuration(-1L);
        accessRecord.setIp(HttpUtils.getClientIP(request));
        //解析agent字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));

        accessRecord.setBrowser(userAgent.getBrowser().getName());
        accessRecord.setOsName(userAgent.getOperatingSystem().getName());
        accessRecord.setAccessTime(LocalDateTime.now());

        return accessRecordService.addRecord(accessRecord);
    }
}