package com.changhong.sei.auth.service;

import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.auth.dao.AccessRecordDao;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 访问记录(AccessRecord)表服务实现类
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@Service("accessRecordService")
public class AccessRecordService extends BaseEntityService<AccessRecord> {
    @Autowired
    private AccessRecordDao accessRecordDao;

    @Override
    protected BaseEntityDao<AccessRecord> getDao() {
        return accessRecordDao;
    }


}