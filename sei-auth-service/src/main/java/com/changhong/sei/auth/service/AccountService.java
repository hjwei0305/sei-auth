package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.AccountDao;
import com.changhong.sei.auth.dto.UpdatePasswordRequest;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.client.UserClient;
import com.changhong.sei.auth.service.client.UserInformation;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.service.BaseEntityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 实现功能：平台账户业务逻辑实现
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:53
 */
@Service
public class AccountService extends BaseEntityService<Account> {

    @Autowired
    private AccountDao dao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserClient userClient;

    @Value("${sei.auth.default.password:123456}")
    private String defaultPassword;

    @Autowired
    private IEncrypt encrypt;

    @Override
    protected BaseEntityDao<Account> getDao() {
        return dao;
    }

    /**
     * @param account 账户
     * @param ipAddr  ip地址
     * @param lang    语言
     */
    public ResultData<SessionUser> getSessionUser(Account account, String ipAddr, String lang) {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode(account.getTenantCode());
        sessionUser.setUserId(account.getUserId());
        sessionUser.setAccount(account.getAccount());
        sessionUser.setUserName(account.getName());
        sessionUser.setIp(ipAddr);

        ResultData<UserInformation> resultData = userClient.getUserInformation(account.getUserId());
        if (resultData.failed()) {
            return ResultData.fail(resultData.getMessage());
        }
        UserInformation userInformation = resultData.getData();

        sessionUser.setUserType(userInformation.getUserType());
        sessionUser.setAuthorityPolicy(userInformation.getUserAuthorityPolicy());
        // 设置语言
        sessionUser.setLocale(StringUtils.isBlank(lang) ? userInformation.getLanguageCode() : lang);

        return ResultData.success(sessionUser);
    }

    /**
     * 创建账户
     *
     * @param account 账户
     * @return 创建账户结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultData<String> createAccount(Account account) {
        if (Objects.isNull(account)) {
            return ResultData.fail("参数不能为空！");
        }

        // 检查账户是否已存在
        Account oldAccount = dao.findByAccountAndTenantCode(account.getAccount(), account.getTenantCode());
        if (Objects.nonNull(oldAccount)) {
            return ResultData.fail(String.format("账户[%s]已在租户[%s]下存在！", account.getAccount(), account.getTenantCode()));
        }

        // 检查是否有密码
        if (StringUtils.isBlank(account.getPassword())) {
            // 无密码,使用平台默认密码策略.后续考虑产生随机密码并通知用户
            account.setPassword(getDefaultPassword());
        }
        // 对密码md5散列值进行再次散列
        account.setPassword(encodePassword(account.getPassword()));
        // 有效期
        if (Objects.isNull(account.getAccountExpired())) {
            // 无有效期设置默认有效期
            account.setAccountExpired(LocalDate.of(2099, 12, 31));
        }
        // 注册时间
        account.setSinceDate(LocalDateTime.now());

        dao.save(account);
        return ResultData.success(account.getAccount());
    }

    /**
     * 更新密码
     *
     * @param request 账户更新密码
     * @return 更新结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultData<String> updatePassword(UpdatePasswordRequest request) {
        if (Objects.isNull(request)) {
            return ResultData.fail("参数不能为空！");
        }
        Account account = this.getByAccountAndTenantCode(request.getAccount(), request.getTenant());
        if (Objects.isNull(account)) {
            return ResultData.fail("账户不存在,密码变更失败！");
        }

        if (verifyPassword(request.getOldPassword(), account.getPassword())) {
            return ResultData.fail("新密码与原密码相同，请重新输入！");
        }

        int i = dao.updatePassword(account.getId(), this.encodePassword(request.getNewPassword()));
        if (i != 1) {
            return ResultData.fail("密码更新失败！");
        }
        return ResultData.success("密码更新成功！", request.getAccount());
    }

    /**
     * 密码重置
     *
     * @param account 账户
     * @param tenant  租户代码
     * @return 密码重置结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultData<String> resetPassword(@NotBlank String tenant, @NotBlank String account) {
        Account oldAccount = this.getByAccountAndTenantCode(account, tenant);
        if (Objects.isNull(oldAccount)) {
            return ResultData.fail("账户不存在,密码重置失败！");
        }

        int i = dao.updatePassword(oldAccount.getId(), this.encodePassword(getDefaultPassword()));
        if (i != 1) {
            return ResultData.fail("密码重置失败！");
        }
        return ResultData.success("密码重置新成功！", account);
    }

    /**
     * 账户冻结
     *
     * @param id 账户
     * @return 账户冻结结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultData<String> frozen(@NotBlank String id, boolean frozen) {
        Account oldAccount = this.findOne(id);
        if (Objects.isNull(oldAccount)) {
            return ResultData.fail("账户不存在,账户冻结失败！");
        }

        int i = dao.updateFrozen(oldAccount.getId(), frozen);
        if (i != 1) {
            return ResultData.fail("账户冻结失败！");
        }
        return ResultData.success("账户冻结成功！", oldAccount.getAccount());
    }

    /**
     * 账户锁定
     *
     * @param id 账户
     * @return 账户锁定结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultData<String> locked(@NotBlank String id, boolean locked) {
        Account oldAccount = this.findOne(id);
        if (Objects.isNull(oldAccount)) {
            return ResultData.fail("账户不存在,账户锁定失败！");
        }

        int i = dao.updateLocked(oldAccount.getId(), locked);
        if (i != 1) {
            return ResultData.fail("账户锁定失败！");
        }
        return ResultData.success("账户锁定成功！", oldAccount.getAccount());
    }

    /**
     * 根据账号查询账户
     *
     * @param account 账号
     * @return 存在返回账号, 不存在返回null
     */
    public List<Account> getByAccount(String account) {
        return dao.findByAccount(account);
    }

    /**
     * 根据账号,租户代码查询账户
     *
     * @param account 账号
     * @param tenant  租户代码
     * @return 存在返回账号, 不存在返回null
     */
    public Account getByAccountAndTenantCode(String account, String tenant) {
        return dao.findByAccountAndTenantCode(account, tenant);
    }

    /**
     * 检查账户是否被冻结
     *
     * @param account 账户
     * @return 冻结返回true, 反之返回false
     */
    public boolean checkFrozen(Account account) {
        return account.getFrozen();
    }

    /**
     * 检查账户是否被锁定
     *
     * @param account 账户
     * @return 锁定返回true, 反之返回false
     */
    public boolean checkLocked(Account account) {
        return account.getLocked();
    }

    /**
     * 检查账户是否在有效期
     *
     * @param account 账户
     * @return 在有效期中返回true, 反之返回false
     */
    public boolean checkAccountExpired(Account account) {
        LocalDate validityDate = account.getAccountExpired();
        if (Objects.nonNull(validityDate)) {
            return validityDate.isAfter(LocalDate.now());
        } else {
            return true;
        }
    }

    /**
     * 使用平台默认密码策略.后续考虑产生随机密码并通知用户
     */
    public String getDefaultPassword() {
        return encrypt.encrypt(defaultPassword);
    }

    /**
     * 生成密码
     *
     * @param rawPassword 明文密码
     * @return 返回加密密文
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 密码校验
     *
     * @param rawPassword     明文密码
     * @param encodedPassword 密文密码
     * @return 匹配一致返回true, 反之返回false
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 获取用户前端权限检查的功能项键值
     *
     * @param userId 用户Id
     * @return 功能项键值
     */
    public ResultData<Map<String, Set<String>>> getAuthorizedFeatures(String userId) {
        return userClient.getUserAuthorizedFeatureMaps(userId);
    }
}
