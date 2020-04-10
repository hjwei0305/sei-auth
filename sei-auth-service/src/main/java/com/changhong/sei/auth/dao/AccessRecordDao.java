package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.AccessRecord;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * 访问记录(AccessRecord)表数据库访问层
 *
 * @author sei
 * @since 2020-03-30 11:09:01
 */
@Repository
public interface AccessRecordDao extends BaseEntityDao<AccessRecord> {

}