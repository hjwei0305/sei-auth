package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.ClientDetailDto;
import com.changhong.sei.core.api.BaseEntityApi;
import com.changhong.sei.core.api.FindByPageApi;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:45
 */
public interface ClientDetailApi extends BaseEntityApi<ClientDetailDto>, FindByPageApi<ClientDetailDto> {
    String PATH = "client";
}
