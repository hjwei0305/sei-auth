package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.AccessRecordDao;
import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


/**
 * 访问记录(AccessRecord)表服务实现类
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@Service("accessRecordService")
public class AccessRecordService extends BaseEntityService<AccessRecord> {
    @Autowired
    private AccessRecordDao dao;

    @Override
    protected BaseEntityDao<AccessRecord> getDao() {
        return dao;
    }

    /**
     * 添加访问记录
     *
     * @param record 参数
     * @return 添加结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultData<Void> addRecord(AccessRecord record) {
        if (Objects.nonNull(record)) {
            dao.save(record);
            return ResultData.success();
        } else {
            return ResultData.fail("访问记录不能为空");
        }
    }
}