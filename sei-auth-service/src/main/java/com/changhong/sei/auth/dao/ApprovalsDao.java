package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.Approvals;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:41
 */
@Repository
public interface ApprovalsDao extends BaseEntityDao<Approvals> {

    List<Approvals> getByClientIdAndUserIdAndTenantCode(String clientId, String userId, String tenantCode);
}
