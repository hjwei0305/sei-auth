package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.AuthClientDto;
import com.changhong.sei.core.api.BaseEntityApi;
import com.changhong.sei.core.api.FindByPageApi;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:45
 */
public interface AuthClientApi extends BaseEntityApi<AuthClientDto>, FindByPageApi<AuthClientDto> {
    String PATH = "authClient";
}
