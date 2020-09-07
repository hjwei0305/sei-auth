package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.BindingRecordDao;
import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.entity.BindingRecord;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * 账号绑定记录(BindingRecord)业务逻辑实现类
 *
 * @author sei
 * @since 2020-09-04 15:24:04
 */
@Service("bindingRecordService")
public class BindingRecordService extends BaseEntityService<BindingRecord> {
    @Autowired
    private BindingRecordDao dao;

    @Override
    protected BaseEntityDao<BindingRecord> getDao() {
        return dao;
    }

    @Transactional
    public ResultData<String> recordBind(Account account, ChannelEnum channel, boolean isBind) {
        BindingRecord record = new BindingRecord();
        record.setTenantCode(account.getTenantCode());
        record.setUserId(account.getUserId());
        record.setAccount(account.getAccount());
        record.setOpenId(account.getOpenId());
        record.setOperationDate(LocalDateTime.now());
        record.setBind(isBind);
        record.setChannel(channel);

        this.save(record);
        return ResultData.success();
    }
}