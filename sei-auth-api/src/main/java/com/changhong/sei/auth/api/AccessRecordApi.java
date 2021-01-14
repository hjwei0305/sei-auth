package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.AccessRecordCreateRequest;
import com.changhong.sei.auth.dto.AccountResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 访问记录(AccessRecord)表API层
 *
 * @author sei
 * @since 2020-03-30 11:22:30
 */
@Valid
@RequestMapping(path = "accessRecord", produces = MediaType.APPLICATION_JSON_VALUE)
public interface AccessRecordApi {

    /**
     * 添加访问记录
     *
     * @param request 参数
     * @return 添加结果
     */
    @PostMapping(path = "add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "添加访问记录", notes = "添加访问记录")
    ResultData<Void> addRecord(@RequestBody AccessRecordCreateRequest record, HttpServletRequest request);


}