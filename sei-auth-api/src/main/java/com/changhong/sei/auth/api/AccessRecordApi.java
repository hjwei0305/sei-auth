package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.AccessRecordCreateRequest;
import com.changhong.sei.auth.dto.AccessRecordFeatureResponse;
import com.changhong.sei.auth.dto.AccessRecordUserResponse;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 访问记录(AccessRecord)表API层
 *
 * @author sei
 * @since 2020-03-30 11:22:30
 */
@Valid
@FeignClient(name = "sei-auth", path = AccessRecordApi.PATH)
public interface AccessRecordApi {
    String PATH = "accessRecord";

    /**
     * 添加访问记录
     *
     * @param request 参数
     * @return 添加结果
     */
    @PostMapping(path = "add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "添加访问记录", notes = "添加访问记录")
    ResultData<Void> addRecord(@RequestBody @Validated AccessRecordCreateRequest record, HttpServletRequest request);

    /**
     * 获取时间段周期
     */
    @GetMapping(path = "getPeriods")
    @ApiOperation(value = "获取时间段周期", notes = "获取时间段周期")
    ResultData<List<Map<String, String>>> getPeriods();

    /**
     * 指定时间段访问top N的功能
     */
    @GetMapping(path = "getTopFeatures/{tenant}/{period}/{topNum}")
    @ApiOperation(value = "指定时间内访问top N的功能", notes = "指定时间内访问top N的功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenant", value = "租户代码", required = true, paramType = "path"),
            @ApiImplicitParam(name = "type", value = "数据类型(ALL-全部,API-接口,PAGE-页面功能)", paramType = "query"),
            @ApiImplicitParam(name = "period", value = "时间周期", required = true, paramType = "path"),
            @ApiImplicitParam(name = "topNum", value = "排名数", required = true, paramType = "path", defaultValue = "10")
    })
    ResultData<List<AccessRecordFeatureResponse>> getTopFeatures(@PathVariable("tenant") String tenant,
                                                                 @RequestParam(name = "type", required = false, defaultValue = "ALL") String type,
                                                                 @PathVariable("period") String period,
                                                                 @PathVariable("topNum") int topNum);

    /**
     * 指定时间段访问top N的人
     */
    @GetMapping(path = "getTopUsers/{tenant}/{period}/{topNum}")
    @ApiOperation(value = "指定时间内访问top N的人", notes = "指定时间内访问top N的人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenant", value = "租户代码", required = true, paramType = "path"),
            @ApiImplicitParam(name = "type", value = "数据类型(ALL-全部,API-接口,PAGE-页面功能)", paramType = "query"),
            @ApiImplicitParam(name = "period", value = "时间周期", required = true, paramType = "path"),
            @ApiImplicitParam(name = "topNum", value = "排名数", required = true, paramType = "path", defaultValue = "10")
    })
    ResultData<List<AccessRecordUserResponse>> getTopUsers(@PathVariable("tenant") String tenant,
                                                           @RequestParam(name = "type", required = false, defaultValue = "ALL") String type,
                                                           @PathVariable("period") String period,
                                                           @PathVariable("topNum") int topNum);

    /**
     * 指定时间段某一功能访问的人
     */
    @GetMapping(path = "getUsersByFeature/{tenant}/{period}/{topNum}")
    @ApiOperation(value = "指定时间内某一功能访问的人", notes = "指定时间内某一功能访问的人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenant", value = "租户代码", required = true, paramType = "path"),
            @ApiImplicitParam(name = "feature", value = "功能名称", required = true, paramType = "query"),
            @ApiImplicitParam(name = "period", value = "时间周期", required = true, paramType = "path"),
            @ApiImplicitParam(name = "topNum", value = "排名数", required = true, paramType = "path", defaultValue = "10")
    })
    ResultData<List<AccessRecordUserResponse>> getUsersByFeature(@PathVariable("tenant") String tenant,
                                                                 @RequestParam("feature") String feature,
                                                                 @PathVariable("period") String period,
                                                                 @PathVariable("topNum") int topNum);

    /**
     * 指定时间段某人访问的功能
     */
    @GetMapping(path = "getFeaturesByUser/{tenant}/{period}/{topNum}")
    @ApiOperation(value = "指定时间内某人访问的功能", notes = "指定时间内某人访问的功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenant", value = "租户代码", required = true, paramType = "path"),
            @ApiImplicitParam(name = "account", value = "账号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "period", value = "时间周期", required = true, paramType = "path"),
            @ApiImplicitParam(name = "topNum", value = "排名数", required = true, paramType = "path", defaultValue = "10")
    })
    ResultData<List<AccessRecordFeatureResponse>> getFeaturesByUser(@PathVariable("tenant") String tenant,
                                                                    @RequestParam("account") String account,
                                                                    @PathVariable("period") String period,
                                                                    @PathVariable("topNum") int topNum);
}