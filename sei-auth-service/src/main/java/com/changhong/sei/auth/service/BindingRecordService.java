package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.BindingRecordDao;
import com.changhong.sei.auth.entity.BindingRecord;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

}