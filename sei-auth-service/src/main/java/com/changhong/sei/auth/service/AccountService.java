package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.AccountDao;
import com.changhong.sei.auth.dto.BindingAccountRequest;
import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.auth.dto.UpdatePasswordRequest;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.entity.BindingRecord;
import com.changhong.sei.auth.service.client.UserClient;
import com.changhong.sei.auth.service.client.UserInformation;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.dto.serach.SearchFilter;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.service.BaseEntityService;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.stream.Collectors;

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
    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private BindingRecordService bindingRecordService;

    /**
     * 默认密码
     */
    @Value("${sei.auth.password:123456}")
    private String defaultPassword;
    /**
     * 密码默认过期天数
     */
    @Value("${sei.auth.password-expire:30}")
    private int defaultPasswordExpire;

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
        // 归集到主账号上
        sessionUser.setAccount(account.getAccount());
        // 当前登录账号
        sessionUser.setLoginAccount(account.getOpenId());
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
    public ResultData<String> createAccount(Account account, boolean isUpdateUserId) {
        if (Objects.isNull(account)) {
            return ResultData.fail("参数不能为空！");
        }

        // 检查账户是否已存在
        Account oldAccount = dao.findByOpenIdAndTenantCodeAndChannel(account.getAccount(), account.getTenantCode(), ChannelEnum.SEI);
        if (Objects.nonNull(oldAccount)) {
            if (!isUpdateUserId) {
                return ResultData.fail(String.format("账户[%s]已在租户[%s]下存在！", account.getAccount(), account.getTenantCode()));
            } else {
                account.setId(oldAccount.getId());
            }
        }
        if (StringUtils.isBlank(account.getOpenId())) {
            // 默认将注册时的账号作为主账号
            account.setOpenId(account.getAccount());
        }

        // 检查主账号
        ResultData<String> resultData = checkMainAccount(account.getUserId(), account.getAccount());
        if (resultData.failed()) {
            return resultData;
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
        // 密码过期时间(默认一个月后)
        account.setPasswordExpireTime(LocalDate.now().plusDays(defaultPasswordExpire));

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

        if (StringUtils.equals(request.getOldPassword(), request.getNewPassword())) {
            return ResultData.fail("新密码与原密码相同，请重新输入！");
        }
        Account account = this.getByAccountAndTenantCode(request.getAccount(), request.getTenant());
        if (Objects.isNull(account)) {
            return ResultData.fail("账户不存在,密码变更失败！");
        }
        if (!verifyPassword(request.getOldPassword(), account.getPassword())) {
            return ResultData.fail("原密码错误，密码变更失败！");
        }

        updatePassword(account.getId(), request.getNewPassword(), defaultPasswordExpire);

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
    public ResultData<String> resetPassword(@NotBlank String tenant, @NotBlank String account, String password) {
        Account oldAccount = this.getByAccountAndTenantCode(account, tenant);
        if (Objects.isNull(oldAccount)) {
            return ResultData.fail("账户不存在,密码重置失败！");
        }

        if (StringUtils.isBlank(password)) {
            password = getDefaultPassword();
        }

        updatePassword(oldAccount.getId(), password, defaultPasswordExpire);

        return ResultData.success("密码重置新成功！", account);
    }

    /**
     * 更新密码,同时更新密码过期时间
     *
     * @param accountId      账号id
     * @param password       密码
     * @param passwordExpire 过期时间(天)
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String accountId, String password, int passwordExpire) {
        if (passwordExpire < 1) {
            throw new IllegalArgumentException("密码过期时间天数不能小于1");
        }
        // 密码过期时间(默认一个月后)
        dao.updatePassword(accountId, this.encodePassword(password), LocalDate.now().plusDays(passwordExpire));
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
        return dao.findByOpenId(account);
    }

    /**
     * 根据账号,租户代码查询账户
     *
     * @param account 账号
     * @param tenant  租户代码
     * @param channel 账号渠道
     * @return 存在返回账号, 不存在返回null
     */
    public Account getByAccountAndTenantCode(String account, String tenant) {
        return dao.findByOpenIdAndTenantCodeAndChannel(account, tenant, ChannelEnum.SEI);
    }

    /**
     * 根据账号,租户代码查询账户
     *
     * @param account 账号
     * @param channel 账号渠道
     * @return 存在返回账号, 不存在返回null
     */
    public List<Account> findByOpenIdAndChannel(String account, ChannelEnum channel) {
        return dao.findByOpenIdAndChannel(account, channel);
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

    /**
     * 验证openId合法性:
     * 判断SocialAccount中是否存在该openid的数据。
     * 若存在，直接进行登录。
     * 若不存在，将数据，存储到SocialAccount中，引导用户绑定SEI平台账号。
     * 若本站已存在账号，直接关联账号即可。
     * 若本站不存在账号，引导用户注册，成功后与当前openid关联即可
     */
    public ResultData<Account> checkAccount(final ChannelEnum channel, final String openId) {
        if (StringUtils.isBlank(openId)) {
            return ResultData.fail("OpenId is null.");
        }

        List<Account> accounts = dao.findListByProperty(Account.FIELD_OPEN_ID, openId);
        if (CollectionUtils.isNotEmpty(accounts)) {
            // 检查系统来还原是否一致
            Account account = accounts.stream().filter(a -> Objects.equals(channel, a.getChannel())).findAny().orElse(null);
            if (Objects.nonNull(account)) {
                return ResultData.success(account);
            }
        }
        return ResultData.fail("openId未绑定SEI平台账号: " + openId);
    }

    /**
     * 绑定账号
     */
    @Transactional
    public ResultData<String> bindingAccount(final BindingAccountRequest request) {
        if (StringUtils.isBlank(request.getTenantCode())) {
            return ResultData.fail("租户代码不能为空.");
        }
        if (StringUtils.isBlank(request.getUserId())) {
            return ResultData.fail("用户id不能为空.");
        }
        if (StringUtils.isBlank(request.getAccount())) {
            return ResultData.fail("账号不能为空.");
        }
        if (StringUtils.isBlank(request.getName())) {
            return ResultData.fail("账户名称不能为空.");
        }
        if (StringUtils.isBlank(request.getOpenId())) {
            return ResultData.fail("OpenId is null.");
        }
        if (Objects.isNull(request.getChannel())) {
            return ResultData.fail("账号渠道不能为空.");
        }
        if (StringUtils.isBlank(request.getAccountType())) {
            return ResultData.fail("用户类型不能为空.");
        }
        // 邮箱或手机号必须由验证码验证
        if (ChannelEnum.EMAIL.equals(request.getChannel()) || ChannelEnum.Mobile.equals(request.getChannel())) {
            if (StringUtils.isBlank(request.getReqId())) {
                return ResultData.fail("reqId不能为空.");
            }
            if (StringUtils.isBlank(request.getVerifyCode())) {
                return ResultData.fail("验证码不能为空.");
            }
            ResultData<String> resultData = validateCodeService.check(request.getReqId(), request.getVerifyCode());
            if (resultData.failed()) {
                return resultData;
            }
        }

        // 检查是否有绑定
        ResultData<Account> resultData = checkAccount(request.getChannel(), request.getOpenId());
        if (resultData.failed()) {
            Account accountObj = new Account();
            accountObj.setTenantCode(request.getTenantCode());
            accountObj.setUserId(request.getUserId());
            // 用户主账号
            accountObj.setAccount(request.getAccount());
            // 使用绑定的openid作为子账号
            accountObj.setOpenId(request.getOpenId());
            accountObj.setName(request.getName());
            accountObj.setAccountType(request.getAccountType());
            accountObj.setChannel(request.getChannel());

            // 无有效期设置默认有效期
            accountObj.setAccountExpired(LocalDate.of(2099, 12, 31));
            // 注册时间
            accountObj.setSinceDate(LocalDateTime.now());

            // 检查主账号
            ResultData<String> checkResult = checkMainAccount(request.getUserId(), request.getAccount());
            if (checkResult.failed()) {
                return checkResult;
            }

            this.save(accountObj);

            BindingRecord record = new BindingRecord();
            record.setTenantCode(request.getTenantCode());
            record.setUserId(request.getUserId());
            record.setAccount(request.getAccount());
            record.setOpenId(request.getOpenId());
            record.setBindingDate(LocalDateTime.now());
            record.setChannel(request.getChannel());

            bindingRecordService.save(record);

            return ResultData.success("ok");
        }
        return ResultData.fail(resultData.getMessage());
    }

    @Transactional
    public ResultData<String> unbinding(String openId, ChannelEnum channel) {
        SessionUser user = ContextUtil.getSessionUser();

        Search search = Search.createSearch();
        search.addFilter(new SearchFilter(Account.FIELD_USER_ID, user.getUserId()));
        search.addFilter(new SearchFilter(Account.FIELD_OPEN_ID, openId));
        search.addFilter(new SearchFilter(Account.FIELD_CHANNEL, channel));
        Account account = this.findOneByFilters(search);
        if (Objects.isNull(account)) {
            return ResultData.fail("未找到绑定的账号.");
        }

        // 删除绑定账号
        this.delete(account.getId());

        // 更新绑定记录
        BindingRecord record = bindingRecordService.findOneByFilters(search);
        if (Objects.isNull(record)) {
            record = new BindingRecord();
            record.setTenantCode(user.getTenantCode());
            record.setUserId(user.getUserId());
            record.setAccount(user.getAccount());
            record.setOpenId(openId);
            record.setBindingDate(account.getSinceDate());
            record.setChannel(channel);
        }
        record.setUnbindingDate(LocalDateTime.now());
        bindingRecordService.save(record);
        return ResultData.success();
    }

    /**
     * 检查主账号是否存在多个或不一致
     *
     * @param userId  用户id
     * @param account 主账号
     * @return 返回检查结果
     */
    private ResultData<String> checkMainAccount(String userId, String account) {
        List<Account> accounts = dao.findListByProperty(Account.FIELD_USER_ID, userId);
        if (CollectionUtils.isNotEmpty(accounts)) {
            int count = accounts.stream().collect(Collectors.groupingBy(Account::getUserId)).size();
            if (count > 1) {
                return ResultData.fail("存在多个主账号.");
            } else {
                // 比较主账号是否一致a
                if (!StringUtils.equals(account, accounts.get(0).getAccount())) {
                    return ResultData.fail("主账号不一致.");
                }
            }
        }
        return ResultData.success();
    }
}
