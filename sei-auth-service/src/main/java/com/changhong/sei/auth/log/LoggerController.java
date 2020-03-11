package com.changhong.sei.auth.log;

import com.changhong.sei.auth.api.AccountApi;
import com.changhong.sei.auth.dto.*;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.auth.service.TaskService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.controller.DefaultBaseController;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.service.BaseEntityService;
import com.changhong.sei.core.service.bo.OperateResultWithData;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:55
 */
@Controller
@Api(value = "log", tags = "日志服务")
public class LoggerController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("/websocket/log")
    public String log(Model model) {
        try {
            taskService.doTaskOne();
            taskService.doTaskTwo();
            taskService.doTaskThree();

//            Thread.sleep(100000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("appName", ContextUtil.getAppCode());
        return "logging";
    }
}
