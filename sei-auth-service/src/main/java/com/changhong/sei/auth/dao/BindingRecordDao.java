package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.BindingRecord;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * 账号绑定记录(BindingRecord)数据库访问类
 *
 * @author sei
 * @since 2020-09-04 15:24:04
 */
@Repository
public interface BindingRecordDao extends BaseEntityDao<BindingRecord> {

}