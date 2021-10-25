package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.ClientDetailApi;
import com.changhong.sei.auth.dto.ClientDetailDto;
import com.changhong.sei.auth.entity.ClientDetail;
import com.changhong.sei.auth.service.ClientDetailService;
import com.changhong.sei.core.controller.BaseEntityController;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.service.BaseEntityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:44
 */
@RestController
@Api(value = "ClientDetailApi", tags = "客户端信息服务")
@RequestMapping(path = ClientDetailApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientDetailController extends BaseEntityController<ClientDetail, ClientDetailDto> implements ClientDetailApi {

    @Autowired
    private ClientDetailService service;

    /**
     * 获取使用的业务逻辑实现
     *
     * @return 业务逻辑
     */
    @Override
    public BaseEntityService<ClientDetail> getService() {
        return service;
    }

    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    public ResultData<PageResult<ClientDetailDto>> findByPage(Search search) {
        return convertToDtoPageResult(service.findByPage(search));
    }
}
