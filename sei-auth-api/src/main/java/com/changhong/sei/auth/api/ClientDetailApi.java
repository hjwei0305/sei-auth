package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.ClientDetailDto;
import com.changhong.sei.core.api.BaseEntityApi;
import com.changhong.sei.core.api.FindByPageApi;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:45
 */
public interface ClientDetailApi extends FindByPageApi<ClientDetailDto> {
    String PATH = "client";

    /**
     * 注册一个客户端
     *
     * @param dto 客户端实体DTO
     * @return 操作结果
     */
    @PostMapping(path = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "注册一个客户端", notes = "注册一个客户端")
    ResultData<ClientDetailDto> registerClient(@RequestBody @Valid ClientDetailDto dto);

    /**
     * 更新客户端信息
     *
     * @param dto 客户端实体DTO
     * @return 操作结果
     */
    @PostMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "更新客户端信息", notes = "更新客户端信息")
    ResultData<ClientDetailDto> updateClient(@RequestBody @Valid ClientDetailDto dto);

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @GetMapping(path = "getClientSecret")
    @ApiOperation(value = "获取一个业务实体", notes = "通过Id获取一个业务实体")
    ResultData<String> getClientSecret(@RequestParam("id") String id);
}
