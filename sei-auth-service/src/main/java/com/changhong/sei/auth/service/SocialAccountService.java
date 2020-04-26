package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.SocialAccountDao;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.entity.SocialAccount;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.service.BaseEntityService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能：社交平台账户业务逻辑实现
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:53
 */
@Service
public class SocialAccountService extends BaseEntityService<SocialAccount> {

    @Autowired
    private SocialAccountDao dao;
    @Autowired
    private AccountService accountService;

    @Override
    protected BaseEntityDao<SocialAccount> getDao() {
        return dao;
    }

    /**
     * 验证openId合法性:
     * 判断SocialAccount中是否存在该openid的数据。
     * 若存在，直接进行登录。
     * 若不存在，将数据，存储到SocialAccount中，引导用户绑定SEI平台账号。
     * 若本站已存在账号，直接关联账号即可。
     * 若本站不存在账号，引导用户注册，成功后与当前openid关联即可
     */
    public ResultData<Account> checkAccount(final String channel, final String openId) {
        if (StringUtils.isBlank(openId)) {
            return ResultData.fail("OpenId is null.");
        }

        List<SocialAccount> socialAccounts = dao.findListByProperty(SocialAccount.FIELD_OPEN_ID, openId);
        if (CollectionUtils.isNotEmpty(socialAccounts)) {
            SocialAccount socialAccount = socialAccounts.stream().filter(a -> StringUtils.equals(channel, a.getChannelCode())).findAny().orElse(null);
            if (Objects.nonNull(socialAccount)) {
                Account account = accountService.getByAccountAndTenantCode(socialAccount.getAccount(), socialAccount.getTenantCode());
                if (Objects.nonNull(account)) {
                    return ResultData.success(account);
                }
            }
        }
        return ResultData.fail("openId未绑定SEI平台账号: " + openId);
    }


    /**
     * 绑定账号
     */
    @Transactional
    public ResultData<String> bindingAccount(final String tenant, final String account, final String openId, final String channel) {
        if (StringUtils.isBlank(tenant)) {
            return ResultData.fail("tenant is null.");
        }
        if (StringUtils.isBlank(account)) {
            return ResultData.fail("account is null.");
        }
        if (StringUtils.isBlank(openId)) {
            return ResultData.fail("OpenId is null.");
        }
        SocialAccount socialAccount = new SocialAccount();
        socialAccount.setTenantCode(tenant);
        socialAccount.setAccount(account);
        socialAccount.setChannelCode(channel);
        socialAccount.setOpenId(openId);
        socialAccount.setSinceDate(LocalDateTime.now());

        this.save(socialAccount);

        return ResultData.success("ok");
    }
}
