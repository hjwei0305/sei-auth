package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.ApprovalsDao;
import com.changhong.sei.auth.entity.Approvals;
import com.changhong.sei.core.dto.ResultData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:42
 */
@Service
public class ApprovalsService {
    @Autowired
    private ApprovalsDao dao;

    @Transactional(rollbackFor = Exception.class)
    public ResultData<Void> saveGrantScope(String tenantCode, String clientId, String userId, String scope) {
        Approvals approvals = new Approvals();
        approvals.setTenantCode(tenantCode);
        approvals.setClientId(clientId);
        approvals.setUserId(userId);
        approvals.setContractScope(scope);
        approvals.setApprovalsTime(LocalDateTime.now());
        dao.save(approvals);
        return ResultData.success();
    }

    public boolean isGrant(String tenantCode, String clientId, String userId, String scope) {
        List<Approvals> approvals = dao.getByClientIdAndUserIdAndTenantCode(clientId, userId, tenantCode);
        return CollectionUtils.isNotEmpty(approvals);
    }
}
