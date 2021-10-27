package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.ClientDetailApi;
import com.changhong.sei.auth.dto.ClientDetailDto;
import com.changhong.sei.auth.entity.ClientDetail;
import com.changhong.sei.auth.service.ClientDetailService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:44
 */
@RestController
@Api(value = "ClientDetailApi", tags = "客户端信息服务")
@RequestMapping(path = ClientDetailApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientDetailController implements ClientDetailApi {

    @Autowired
    private ClientDetailService service;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    public ResultData<PageResult<ClientDetailDto>> findByPage(Search search) {
        List<ClientDetailDto> list;
        PageResult<ClientDetail> pageResult = service.findByPage(search);
        List<ClientDetail> details = pageResult.getRows();
        if (CollectionUtils.isNotEmpty(details)) {
            list = details.stream().map(d -> modelMapper.map(d, ClientDetailDto.class)).collect(Collectors.toList());
        } else {
            list = new ArrayList<>();
        }
        PageResult<ClientDetailDto> result = new PageResult<>(pageResult);
        result.setRows(list);
        return ResultData.success(result);
    }

    /**
     * 注册一个客户端
     *
     * @param dto 客户端实体DTO
     * @return 操作结果
     */
    @Override
    public ResultData<ClientDetailDto> registerClient(ClientDetailDto dto) {
        ClientDetail clientDetail = service.registerClient(modelMapper.map(dto, ClientDetail.class));
        return ResultData.success(modelMapper.map(clientDetail, ClientDetailDto.class));
    }

    /**
     * 更新客户端信息
     *
     * @param dto 客户端实体DTO
     * @return 操作结果
     */
    @Override
    public ResultData<ClientDetailDto> updateClient(ClientDetailDto dto) {
        ClientDetail clientDetail = service.updateClient(modelMapper.map(dto, ClientDetail.class));
        return ResultData.success(modelMapper.map(clientDetail, ClientDetailDto.class));
    }

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @Override
    public ResultData<String> getClientSecret(String id) {
        return ResultData.success(service.getClientSecret(id));
    }
}
