package com.changhong.sei.auth.service;

import com.changhong.sei.auth.common.RandomUtils;
import com.changhong.sei.auth.dao.ClientDetailDao;
import com.changhong.sei.auth.entity.ClientDetail;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:42
 */
@Service
public class ClientDetailService {
    @Autowired
    private ClientDetailDao dao;

    public ClientDetail getByClientId(String tenantCode, String clientId) {
        return dao.findByClientIdAndTenantCode(clientId, tenantCode);
    }

    public PageResult<ClientDetail> findByPage(Search search) {
        return dao.findByPage(search);
    }

    /**
     * 注册一个客户端
     *
     * @param detail 客户端实体
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientDetail registerClient(ClientDetail detail) {
        dao.save(detail);
        return detail;
    }

    /**
     * 更新客户端信息
     *
     * @param detail 客户端实体DTO
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ClientDetail updateClient(ClientDetail detail) {
        dao.save(detail);
        return detail;
    }

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateClientSecret(String id) {
        RandomUtils.randomString(16);
        return null;
    }

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    public String getClientSecret(String id) {
        ClientDetail detail = dao.findOne(id);
        if (Objects.nonNull(detail)) {
            return detail.getClientSecret();
        }
        return null;
    }
}
