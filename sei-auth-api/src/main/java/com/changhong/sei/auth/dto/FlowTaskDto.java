package com.changhong.sei.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2022-06-29 10:06
 */
public class FlowTaskDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 优先级：1》驳回  2》撤回  3》紧急  4》批注
     */
    private Integer priority;

    /**
     * 执行人ID
     */
    private String executorId;

    /**
     * 执行人名称
     */
    private String executorName;

    /**
     * 执行人账号
     */
    private String executorAccount;

    /**
     * 执行人组织机构ID
     */
    private String executorOrgId;

    /**
     * 执行人组织机构code
     */
    private String executorOrgCode;

    /**
     * 执行人组织机构名称
     */
    private String executorOrgName;

    /**
     * 拥有人ID
     */
    private String ownerId;

    /**
     * 任务所属人账号（拥有人）
     */
    private String ownerAccount;

    /**
     * 任务所属人名称（拥有人）
     */
    private String ownerName;

    /**
     * 拥有者组织机构ID
     */
    private String ownerOrgId;

    /**
     * 拥有者组织机构code
     */
    private String ownerOrgCode;

    /**
     * 拥有者组织机构名称
     */
    private String ownerOrgName;

    /**
     * 租户代码
     */
    private String tenantCode;

    /**
     * 任务创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskCreatedDate;

    /**
     * 推送的已办审批状态（审批任务：agree/disagree）
     */
    private String approveStatus;

    /**
     * 推送的待办是否为自动处理
     */
    private Boolean newTaskAuto;

    /**
     * 流程实例ID
     */
    private String flowInstanceId;

    /**
     * 流程名称
     */
    private String flowInstanceName;

    /**
     * 业务ID
     */
    private String instanceBusinessId;

    /**
     * 业务单号
     */
    private String instanceBusinessCode;

    /**
     * 业务单据名称
     */
    private String instanceBusinessName;

    /**
     * 业务摘要(工作说明)
     */
    private String instanceBusinessModelRemark;

    /**
     * 项目ID
     */
    private String instanceProjectId;


    /**
     * 项目名称
     */
    private String instanceProjectName;

    /**
     * 流程发起人ID
     */
    protected String instanceCreatorId;

    /**
     * 流程发起人账户
     */
    protected String instanceCreatorAccount;

    /**
     * 流程发起人名称
     */
    protected String instanceCreatorName;

    /**
     * 流程发起时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date instanceCreatedDate;

    /**
     * 流程版本ID
     */
    private String flowDefVersionId;

    /**
     * 流程版本名称
     */
    private String flowDefVersionName;

    /**
     * 流程定义ID
     */
    private String flowDefinationId;

    /**
     * 流程定义名称
     */
    private String flowDefinationName;


    /**
     * 流程定义key
     */
    private String flowDefinationKey;

    /**
     * 流程类型ID
     */
    private String flowTypeId;

    /**
     * 流程类型名称
     */
    private String flowTypeName;

    /**
     * 流程类型代码
     */
    private String flowTypeCode;

    /**
     * 业务实体ID
     */
    private String businessModelId;

    /**
     * 业务实体名称
     */
    private String businessModelName;

    /**
     * 业务实体代码
     */
    private String businessModelClassName;

    /**
     * 应用模块ID
     */
    private String appModuleId;

    /**
     * 应用模块名称
     */
    private String appModuleName;

    /**
     * 应用模块代码
     */
    private String appModuleCode;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getExecutorAccount() {
        return executorAccount;
    }

    public void setExecutorAccount(String executorAccount) {
        this.executorAccount = executorAccount;
    }

    public String getExecutorOrgId() {
        return executorOrgId;
    }

    public void setExecutorOrgId(String executorOrgId) {
        this.executorOrgId = executorOrgId;
    }

    public String getExecutorOrgCode() {
        return executorOrgCode;
    }

    public void setExecutorOrgCode(String executorOrgCode) {
        this.executorOrgCode = executorOrgCode;
    }

    public String getExecutorOrgName() {
        return executorOrgName;
    }

    public void setExecutorOrgName(String executorOrgName) {
        this.executorOrgName = executorOrgName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerOrgId() {
        return ownerOrgId;
    }

    public void setOwnerOrgId(String ownerOrgId) {
        this.ownerOrgId = ownerOrgId;
    }

    public String getOwnerOrgCode() {
        return ownerOrgCode;
    }

    public void setOwnerOrgCode(String ownerOrgCode) {
        this.ownerOrgCode = ownerOrgCode;
    }

    public String getOwnerOrgName() {
        return ownerOrgName;
    }

    public void setOwnerOrgName(String ownerOrgName) {
        this.ownerOrgName = ownerOrgName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Date getTaskCreatedDate() {
        return taskCreatedDate;
    }

    public void setTaskCreatedDate(Date taskCreatedDate) {
        this.taskCreatedDate = taskCreatedDate;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Boolean getNewTaskAuto() {
        return newTaskAuto;
    }

    public void setNewTaskAuto(Boolean newTaskAuto) {
        this.newTaskAuto = newTaskAuto;
    }

    public String getFlowInstanceId() {
        return flowInstanceId;
    }

    public void setFlowInstanceId(String flowInstanceId) {
        this.flowInstanceId = flowInstanceId;
    }

    public String getFlowInstanceName() {
        return flowInstanceName;
    }

    public void setFlowInstanceName(String flowInstanceName) {
        this.flowInstanceName = flowInstanceName;
    }

    public String getInstanceBusinessId() {
        return instanceBusinessId;
    }

    public void setInstanceBusinessId(String instanceBusinessId) {
        this.instanceBusinessId = instanceBusinessId;
    }

    public String getInstanceBusinessCode() {
        return instanceBusinessCode;
    }

    public void setInstanceBusinessCode(String instanceBusinessCode) {
        this.instanceBusinessCode = instanceBusinessCode;
    }

    public String getInstanceBusinessName() {
        return instanceBusinessName;
    }

    public void setInstanceBusinessName(String instanceBusinessName) {
        this.instanceBusinessName = instanceBusinessName;
    }

    public String getInstanceBusinessModelRemark() {
        return instanceBusinessModelRemark;
    }

    public void setInstanceBusinessModelRemark(String instanceBusinessModelRemark) {
        this.instanceBusinessModelRemark = instanceBusinessModelRemark;
    }

    public String getInstanceProjectId() {
        return instanceProjectId;
    }

    public void setInstanceProjectId(String instanceProjectId) {
        this.instanceProjectId = instanceProjectId;
    }

    public String getInstanceProjectName() {
        return instanceProjectName;
    }

    public void setInstanceProjectName(String instanceProjectName) {
        this.instanceProjectName = instanceProjectName;
    }

    public String getInstanceCreatorId() {
        return instanceCreatorId;
    }

    public void setInstanceCreatorId(String instanceCreatorId) {
        this.instanceCreatorId = instanceCreatorId;
    }

    public String getInstanceCreatorAccount() {
        return instanceCreatorAccount;
    }

    public void setInstanceCreatorAccount(String instanceCreatorAccount) {
        this.instanceCreatorAccount = instanceCreatorAccount;
    }

    public String getInstanceCreatorName() {
        return instanceCreatorName;
    }

    public void setInstanceCreatorName(String instanceCreatorName) {
        this.instanceCreatorName = instanceCreatorName;
    }

    public Date getInstanceCreatedDate() {
        return instanceCreatedDate;
    }

    public void setInstanceCreatedDate(Date instanceCreatedDate) {
        this.instanceCreatedDate = instanceCreatedDate;
    }

    public String getFlowDefVersionId() {
        return flowDefVersionId;
    }

    public void setFlowDefVersionId(String flowDefVersionId) {
        this.flowDefVersionId = flowDefVersionId;
    }

    public String getFlowDefVersionName() {
        return flowDefVersionName;
    }

    public void setFlowDefVersionName(String flowDefVersionName) {
        this.flowDefVersionName = flowDefVersionName;
    }

    public String getFlowDefinationId() {
        return flowDefinationId;
    }

    public void setFlowDefinationId(String flowDefinationId) {
        this.flowDefinationId = flowDefinationId;
    }

    public String getFlowDefinationName() {
        return flowDefinationName;
    }

    public void setFlowDefinationName(String flowDefinationName) {
        this.flowDefinationName = flowDefinationName;
    }

    public String getFlowDefinationKey() {
        return flowDefinationKey;
    }

    public void setFlowDefinationKey(String flowDefinationKey) {
        this.flowDefinationKey = flowDefinationKey;
    }

    public String getFlowTypeId() {
        return flowTypeId;
    }

    public void setFlowTypeId(String flowTypeId) {
        this.flowTypeId = flowTypeId;
    }

    public String getFlowTypeName() {
        return flowTypeName;
    }

    public void setFlowTypeName(String flowTypeName) {
        this.flowTypeName = flowTypeName;
    }

    public String getFlowTypeCode() {
        return flowTypeCode;
    }

    public void setFlowTypeCode(String flowTypeCode) {
        this.flowTypeCode = flowTypeCode;
    }

    public String getBusinessModelId() {
        return businessModelId;
    }

    public void setBusinessModelId(String businessModelId) {
        this.businessModelId = businessModelId;
    }

    public String getBusinessModelName() {
        return businessModelName;
    }

    public void setBusinessModelName(String businessModelName) {
        this.businessModelName = businessModelName;
    }

    public String getBusinessModelClassName() {
        return businessModelClassName;
    }

    public void setBusinessModelClassName(String businessModelClassName) {
        this.businessModelClassName = businessModelClassName;
    }

    public String getAppModuleId() {
        return appModuleId;
    }

    public void setAppModuleId(String appModuleId) {
        this.appModuleId = appModuleId;
    }

    public String getAppModuleName() {
        return appModuleName;
    }

    public void setAppModuleName(String appModuleName) {
        this.appModuleName = appModuleName;
    }

    public String getAppModuleCode() {
        return appModuleCode;
    }

    public void setAppModuleCode(String appModuleCode) {
        this.appModuleCode = appModuleCode;
    }
}
