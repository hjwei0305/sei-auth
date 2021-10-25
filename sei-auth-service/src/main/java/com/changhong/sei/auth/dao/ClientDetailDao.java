package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.ClientDetail;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:41
 */
@Repository
public interface ClientDetailDao extends BaseEntityDao<ClientDetail> {

    ClientDetail findByClientIdAndTenantCode(String clientId, String tenantCode);
}
