package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.ClientDetailDao;
import com.changhong.sei.auth.entity.ClientDetail;
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
public class ClientDetailService extends BaseEntityService<ClientDetail> {
    @Autowired
    private ClientDetailDao dao;

    @Override
    protected BaseEntityDao<ClientDetail> getDao() {
        return dao;
    }

    public ClientDetail getByClientId(String tenantCode, String clientId) {
        return dao.findByClientIdAndTenantCode(clientId, tenantCode);
    }
}
