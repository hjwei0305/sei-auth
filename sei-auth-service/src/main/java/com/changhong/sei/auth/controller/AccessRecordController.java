package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.auth.service.AccessRecordService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;

/**
 * 访问记录(AccessRecord)表控制层
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@RestController
@Api(value = "AccessRecordApi", tags = "访问记录服务")
public class AccessRecordController {
    /**
     * 访问记录服务对象
     */
    @Autowired
    private AccessRecordService accessRecordService;

}