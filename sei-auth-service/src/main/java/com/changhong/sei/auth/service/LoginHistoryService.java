package com.changhong.sei.auth.service;

import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.dao.LoginHistoryDao;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:38
 */
@Service
public class LoginHistoryService {

    @Autowired
    private LoginHistoryDao dao;

    @Autowired
    private CacheBuilder cacheBuilder;

    /**
     * 记录登录错误次数
     */
    public void recordLoginFailureNum(String tenant, String account, String reqId) {
        if (StringUtils.isBlank(tenant)) {
            tenant = "none";
        }
        Integer num = cacheBuilder.get(Constants.LOGIN_NUM_KEY + ":" + tenant + ":" + account);
        if (Objects.isNull(num)) {
            num = 0;
        }
        cacheBuilder.set(Constants.LOGIN_NUM_KEY + ":" + tenant + ":" + account, ++num, (long) (30 * 60 * 1000));

        cacheBuilder.remove(Constants.VERIFY_CODE_KEY + reqId);
    }

    /**
     * 检查是否超过登录错误次数
     */
    public ResultData<String> checkLoginFailureNum(String tenant, String account) {
        if (StringUtils.isBlank(tenant)) {
            tenant = "none";
        }
        Integer num = cacheBuilder.get(Constants.LOGIN_NUM_KEY + ":" + tenant + ":" + account);
        if (Objects.isNull(num)) {
            num = 0;
        }
        if (num > 3) {
            return ResultData.fail("登录错误次数超过3次");
        } else {
            return ResultData.success("OK");
        }
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     */
    public PageResult<LoginHistory> findByPage(Search search) {
        return dao.findByPage(search);
    }

    /**
     * 添加登录历史
     *
     * @param history 登录历史
     */
    @Transactional(rollbackFor = Exception.class)
    public void addHistory(LoginHistory history) {
        if (Objects.nonNull(history)) {
            dao.save(history);
        }
    }

    /**
     * 更新会话登出时间
     *
     * @param sid 会话id
     */
    @Transactional(rollbackFor = Exception.class)
    public void setLogoutTime(String sid) {
        dao.setLogoutTime(sid, LocalDateTime.now());
    }

    /**
     * 更新会话登出时间
     *
     * @param sids 会话id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSetLogoutTime(Set<String> sids) {
        dao.batchSetLogoutTime(sids, LocalDateTime.now());
    }
}
