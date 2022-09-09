package com.changhong.sei.auth.service.client.vo;

import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2022-06-14 14:41
 */
public class FlowTaskVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 待办ID
     */
    private String taskId;

    /**
     * 流程实例ID
     */
    private String instanceId;

    /**
     * 业务单据ID
     */
    private String businessId;

    /**
     * 是否待办
     */
    private Boolean todo = Boolean.TRUE;

    /**
     * pc跳转待办url
     */
    private String pcTodoUrl;

    /**
     * pc跳转已办url
     */
    private String pcDoneUrl;

    /**
     * 移动端待办跳转类型
     */
    private String appTaskType;

    /**
     * 移动端已办跳转类型
     */
    private String appTodoType;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAppTodoType() {
        return appTodoType;
    }

    public void setAppTodoType(String appTodoType) {
        this.appTodoType = appTodoType;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Boolean getTodo() {
        return todo;
    }

    public void setTodo(Boolean todo) {
        this.todo = todo;
    }

    public String getPcTodoUrl() {
        return pcTodoUrl;
    }

    public void setPcTodoUrl(String pcTodoUrl) {
        this.pcTodoUrl = pcTodoUrl;
    }

    public String getPcDoneUrl() {
        return pcDoneUrl;
    }

    public void setPcDoneUrl(String pcDoneUrl) {
        this.pcDoneUrl = pcDoneUrl;
    }

    public String getAppTaskType() {
        return appTaskType;
    }

    public void setAppTaskType(String appTaskType) {
        this.appTaskType = appTaskType;
    }
}
