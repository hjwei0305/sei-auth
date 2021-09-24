package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.LoginHistoryDto;
import com.changhong.sei.auth.dto.OnlineUserDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 登录日志API
 *
 * @author sei
 * @since 2020-04-10 09:22:26
 */
@Valid
@FeignClient(name = "sei-auth", path = LoginLogApi.PATH)
public interface LoginLogApi {
    /**
     * 服务访问目录
     */
    String PATH = "loginLog";

    /**
     * 获取系统登录总次数
     *
     * @return Long
     */
    @GetMapping(path = "getTotalVisitCount")
    @ApiOperation("获取系统登录总次数")
    ResultData<Long> getTotalVisitCount();

    /**
     * 获取系统今日登录总次数
     *
     * @return Long
     */
    @GetMapping(path = "getTodayVisitCount")
    @ApiOperation("获取系统登录总次数")
    ResultData<Long> getTodayVisitCount();

    /**
     * 获取系统今日访问IP数
     *
     * @return Long
     */
    @GetMapping(path = "getTodayIp")
    @ApiOperation("获取系统今日访问IP数")
    ResultData<Long> getTodayIp();

    /**
     * 获取系统近十天来的访问记录
     *
     * @param account 账号
     * @return 系统近十天来的访问记录
     */
    @GetMapping(path = "getLastTenDaysVisitCount")
    @ApiOperation("获取系统近十天来的访问记录")
    ResultData<List<Map<String, Object>>> getLastTenDaysVisitCount(@RequestParam("account") String account);

    /**
     * 按浏览器来统计数量
     */
    @GetMapping(path = "getByBrowser")
    @ApiOperation("按浏览器来统计数量")
    ResultData<List<Map<String, Object>>> getByBrowser();

    /**
     * 按操作系统内统计数量
     */
    @GetMapping(path = "getByOperatingSystem")
    @ApiOperation("按操作系统内统计数量")
    ResultData<List<Map<String, Object>>> getByOperatingSystem();

//    /**
//     * 清理日志
//     *
//     * @param clearBeforeDays 多少天之前的
//     * @param clearBeforeNum  多少条
//     */
//    @PostMapping(path = "clearLog")
//    @ApiOperation("清理日志")
//    ResultData<Void> clearLog(Integer clearBeforeDays, Integer clearBeforeNum);

    /**
     * 分页查询登录日志
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @PostMapping(path = "getLoginLogByPage", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "分页查询登录日志", notes = "分页查询登录日志")
    ResultData<PageResult<LoginHistoryDto>> getLoginLogByPage(@RequestBody Search search);

    /**
     * 分页查询在线用户
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @PostMapping(path = "getOnlineUserByPage", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "分页查询在线用户", notes = "分页查询在线用户")
    ResultData<PageResult<OnlineUserDto>> getOnlineUserByPage(@RequestBody Search search);

}