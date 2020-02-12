package com.changhong.sei.auth.service.client;

import com.changhong.sei.core.dto.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

/**
 * 实现功能: 用户API接口
 *
 * @author 王锦光 wangjg
 * @version 2020-01-30 10:23
 */
@FeignClient(name = "sei-basic", path = "user")
public interface UserClient {
    /**
     * 通过用户userId查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @GetMapping(path = "getUserInformation")
    ResultData<UserInformation> getUserInformation(@RequestParam("userId") String userId);

    /**
     * 获取用户前端权限检查的功能项键值
     * @param userId 用户Id
     * @return 功能项键值
     */
    @GetMapping(path = "getUserAuthorizedFeatureMaps")
    ResultData<Map<String, Set<String>>> getUserAuthorizedFeatureMaps(@RequestParam("userId") String userId);
}
