package com.changhong.sei.auth.service;

import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.dao.LoginHistoryDao;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.service.BaseEntityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:38
 */
@Service
public class LoginHistoryService extends BaseEntityService<LoginHistory> {

    @Autowired
    private LoginHistoryDao dao;

    @Autowired
    private CacheBuilder cacheBuilder;

    @Override
    protected BaseEntityDao<LoginHistory> getDao() {
        return dao;
    }

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
}
