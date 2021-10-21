package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.AuthClientDao;
import com.changhong.sei.auth.entity.AuthClient;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:42
 */
@Service
public class AuthClientService extends BaseEntityService<AuthClient> {
    @Autowired
    private AuthClientDao dao;

    @Override
    protected BaseEntityDao<AuthClient> getDao() {
        return dao;
    }

    public AuthClient getByClientId(String tenantCode, String clientId) {
        return dao.findByClientIdAndTenantCode(clientId, tenantCode);
    }
}
