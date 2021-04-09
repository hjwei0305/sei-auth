package com.changhong.sei.auth.service.client;

import com.changhong.sei.auth.service.client.vo.FlowTaskPageResultVO;
import com.changhong.sei.core.dto.flow.FlowTask;
import com.changhong.sei.core.dto.serach.Search;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "flow-service", path = "flowTask")
public interface FlowClient {

    @ResponseBody
    @GetMapping(path = "findTaskById")
    FlowTask findTaskById(@RequestParam(value = "taskId") String taskId);

    /**
     * 获取待办信息
     *
     * @param searchConfig    查询条件
     * @param businessModelId 为空查询全部
     * @param appSign         应用标识
     * @return 可批量审批待办信息
     */
    @ResponseBody
    @PostMapping(path = "findByBusinessModelIdWithAllCount", consumes = MediaType.APPLICATION_JSON_VALUE)
    FlowTaskPageResultVO<FlowTask> findByBusinessModelIdWithAllCount(@RequestParam("businessModelId") String businessModelId,
                                                                     @RequestParam("appSign") String appSign,
                                                                     @RequestBody Search searchConfig);

}
