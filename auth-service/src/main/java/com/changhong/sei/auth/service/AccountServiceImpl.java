package com.changhong.sei.auth.service;

import com.changhong.sei.auth.api.AccountService;
import com.changhong.sei.auth.dto.AccountDto;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.manager.AccountManager;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.manager.BaseEntityManager;
import com.changhong.sei.core.manager.bo.OperateResultWithData;
import com.changhong.sei.core.service.DefaultBaseEntityService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:55
 */
@Service
@Api(value = "AccountService", tags = "账户接口服务")
public class AccountServiceImpl implements DefaultBaseEntityService<Account, AccountDto>, AccountService {

    @Autowired
    private AccountManager accountManager;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${sei.auth.default.password}")
    private String defaultPassword;

    @Override
    public BaseEntityManager<Account> getManager() {
        return accountManager;
    }

    @Override
    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    /**
     * 获取数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }

    /**
     * 获取传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<AccountDto> getDtoClass() {
        return AccountDto.class;
    }

    ///////////////////////

    /**
     * 通过账户id获取已有账户
     *
     * @param id 账户id
     */
    @Override
    public ResultData<AccountDto> getById(String id) {
        Account account = accountManager.findOne(id);
        if(account==null){
            return ResultData.fail("账户不存在！");
        }
        AccountDto dto = convertToDto(account);
        return ResultData.success(dto);
    }

    /**
     * 创建新账户
     *
     * @param dto 账户dto
     */
    @Override
    public ResultData<String> create(AccountDto dto) throws IllegalAccessException {
        Account account = convertToEntity(dto);
        ResultData<String> checkResult = checkEntity(account);
        if(checkResult.isFailed()){
            return checkResult;
        }
        OperateResultWithData<Account> resultWithData = accountManager.save(account);
        if(resultWithData.notSuccessful()){
            return ResultData.fail(resultWithData.getMessage());
        }
        return ResultData.success(dto.getAccount());
    }

    /**
     * 更新账户
     *
     * @param dto 账户dto
     */
    @Override
    public ResultData<String> update(AccountDto dto) throws IllegalAccessException {
        Account account = convertToEntity(dto);
        ResultData<String> checkResult = checkEntity(account);
        if(checkResult.isFailed()){
            return checkResult;
        }
        if(ObjectUtils.isEmpty(account.getId())){
            return ResultData.fail("参数id不能为空！");
        }
        Account oldAccount = accountManager.findOne(account.getId());
        if(oldAccount==null){
            return ResultData.fail("账户数据不存在！");
        }
        if(!oldAccount.getPasswordHash().equals(account.getPasswordHash())){
            return ResultData.fail("禁止修改密码！");
        }
        OperateResultWithData<Account> resultWithData = accountManager.save(account);
        if(resultWithData.notSuccessful()){
            return ResultData.fail(resultWithData.getMessage());
        }
        return ResultData.success(dto.getAccount());
    }

    /**
     * 参数为空检查
     * @param account 新增/更新账户数据
     * @return 检查结果
     */
    private ResultData<String> checkEntity(Account account) throws IllegalAccessException {
        if(account==null){
            return ResultData.fail("请求参数不能为空！");
        }
        for (Field field : account.getClass().getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if(column!=null && !column.nullable()){
                field.setAccessible(true);
                if(field.get(account)==null && !"tenantCode".equals(field.getName())){
                    return ResultData.fail(String.format("参数%s不能为空！", field.getName()));
                }
            }
        }
        return ResultData.success(account.getAccount());
    }

    /**
     * 更新密码
     *
     * @param dto 账户dto
     */
    @Override
    public ResultData<String> updatePassword(AccountDto dto) {
        if(dto == null){
            return ResultData.fail("请求参数不能为空！");
        }
        if(ObjectUtils.isEmpty(dto.getId())){
            return ResultData.fail("id不能为空！");
        }
        if(ObjectUtils.isEmpty(dto.getPasswordHash())){
            return ResultData.fail("密码不能为空！");
        }
        Account oldAccount = accountManager.findOne(dto.getId());
        if(ObjectUtils.isEmpty(oldAccount)){
            return ResultData.fail("账户不存在！");
        }
        if(oldAccount.getPasswordHash().equals(dto.getPasswordHash())){
            return ResultData.fail("新密码与原密码相同，请重新输入！");
        }
        return accountManager.updatePassword(dto.getId(), dto.getPasswordHash());
    }

    /**
     * 重置密码
     *
     * @param dto 账户dto
     */
    @Override
    public ResultData<String> resetPassword(AccountDto dto) {
        if(dto == null){
            return ResultData.fail("请求参数不能为空！");
        }
        if(ObjectUtils.isEmpty(dto.getId())){
            return ResultData.fail("id不能为空！");
        }
        Account oldAccount = accountManager.findOne(dto.getId());
        if(ObjectUtils.isEmpty(oldAccount)){
            return ResultData.fail("账户不存在！");
        }
        return accountManager.updatePassword(dto.getId(), defaultPassword);
    }

    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    public ResultData<PageResult<AccountDto>> findByPage(Search search) {
        PageResult<AccountDto> newPageResult = new PageResult<>();
        List<AccountDto> newRows = new ArrayList<>();
        PageResult<Account> pageResult = accountManager.findByPage(search);
        pageResult.getRows().forEach(d -> newRows.add(convertToDto(d)));
        newPageResult.setPage(pageResult.getPage());
        newPageResult.setRecords(pageResult.getRecords());
        newPageResult.setTotal(pageResult.getTotal());
        newPageResult.setRows(newRows);
        return ResultData.success(newPageResult);
    }

    /**
     * 账户冻结
     *
     * @param id 账户id
     */
    @Override
    public ResultData<String> frozenById(String id) {
        Account account = accountManager.findOne(id);
        account.setFrozen(!account.getFrozen());
        accountManager.save(account);
        return ResultData.success(account.getFrozen()?"账户冻结成功！":"账户解冻成功！",account.getAccount());
    }

    /**
     * 账户锁定/解锁
     *
     * @param id 账户id
     */
    @Override
    public ResultData<String> lockedById(String id) {
        Account account = accountManager.findOne(id);
        account.setLocked(!account.getLocked());
        accountManager.save(account);
        return ResultData.success(account.getLocked()?"账户锁定成功！":"账户解锁成功！",account.getAccount());
    }
}
