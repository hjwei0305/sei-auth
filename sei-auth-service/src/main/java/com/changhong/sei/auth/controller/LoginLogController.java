package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.LoginLogApi;
import com.changhong.sei.auth.dto.LoginHistoryDto;
import com.changhong.sei.auth.dto.OnlineUserDto;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.auth.entity.OnlineUser;
import com.changhong.sei.auth.service.LoginHistoryService;
import com.changhong.sei.auth.service.OnlineUserService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-06-03 11:31
 */
@RestController
@Api(value = "LoginLogApi", tags = "登录日志服务")
@RequestMapping(path = LoginLogApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginLogController implements LoginLogApi {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private OnlineUserService onlineUserService;

    /**
     * 获取系统登录总次数
     *
     * @return Long
     */
    @Override
    public ResultData<Long> getTotalVisitCount() {
        return null;
    }

    /**
     * 获取系统今日登录总次数
     *
     * @return Long
     */
    @Override
    public ResultData<Long> getTodayVisitCount() {
        return null;
    }

    /**
     * 获取系统今日访问IP数
     *
     * @return Long
     */
    @Override
    public ResultData<Long> getTodayIp() {
        return null;
    }

    /**
     * 获取系统近十天来的访问记录
     *
     * @param account 账号
     * @return 系统近十天来的访问记录
     */
    @Override
    public ResultData<List<Map<String, Object>>> getLastTenDaysVisitCount(String account) {
        return null;
    }

    /**
     * 按浏览器来统计数量
     */
    @Override
    public ResultData<List<Map<String, Object>>> getByBrowser() {
        return null;
    }

    /**
     * 按操作系统内统计数量
     */
    @Override
    public ResultData<List<Map<String, Object>>> getByOperatingSystem() {
        return null;
    }

    /**
     * 清理日志
     *
     * @param clearBeforeDays 多少天之前的
     * @param clearBeforeNum  多少条
     */
    public ResultData<Void> clearLog(Integer clearBeforeDays, Integer clearBeforeNum) {
        return null;
    }

    /**
     * 分页查询登录日志
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    public ResultData<PageResult<LoginHistoryDto>> getLoginLogByPage(Search search) {
        PageResult<LoginHistory> pageResult = loginHistoryService.findByPage(search);
        PageResult<LoginHistoryDto> result = new PageResult<>(pageResult);
        List<LoginHistory> histories = pageResult.getRows();
        if (CollectionUtils.isNotEmpty(histories)) {
            List<LoginHistoryDto> dtoList = histories.stream().map(h -> modelMapper.map(h, LoginHistoryDto.class)).collect(Collectors.toList());
            result.setRows(dtoList);
        }
        return ResultData.success(result);
    }

    /**
     * 分页查询在线用户
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    public ResultData<PageResult<OnlineUserDto>> getOnlineUserByPage(Search search) {
        PageResult<OnlineUser> pageResult = onlineUserService.findByPage(search);
        PageResult<OnlineUserDto> result = new PageResult<>(pageResult);
        List<OnlineUser> onlineUsers = pageResult.getRows();
        if (CollectionUtils.isNotEmpty(onlineUsers)) {
            List<OnlineUserDto> dtoList = onlineUsers.stream().map(h -> modelMapper.map(h, OnlineUserDto.class)).collect(Collectors.toList());
            result.setRows(dtoList);
        }
        return ResultData.success(result);
    }
}
