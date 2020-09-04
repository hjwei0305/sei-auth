package com.changhong.sei.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * 实现功能：待办消息明细
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-16 00:01
 */
public class TodoTaskInfo implements Serializable {
    private static final long serialVersionUID = -8193644969367541033L;
    /**
     * 待办id
     */
    @JsonProperty("todo_id")
    private String todoId;
    /**
     * 待办单据号
     */
    @JsonProperty("todo_no")
    private String todoNo;
    /**
     * 待办主题
     */
    @JsonProperty("todo_title")
    private String todoTitle;
    /**
     * 发起人姓名
     */
    @JsonProperty("start_user")
    private String startUser;

    /**
     * 发起时间
     */
    @JsonProperty("start_time")
    private String startTime;
    /**
     * 待办处理跳转url
     */
    @JsonProperty("todo_url")
    private String todoUrl;

    /**
     * 移动是否能处理
     */
    @JsonProperty("IsCanExecute")
    private Boolean isCanExecute = Boolean.TRUE;

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoNo() {
        return todoNo;
    }

    public void setTodoNo(String todoNo) {
        this.todoNo = todoNo;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getStartUser() {
        return startUser;
    }

    public void setStartUser(String startUser) {
        this.startUser = startUser;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTodoUrl() {
        return todoUrl;
    }

    public void setTodoUrl(String todoUrl) {
        this.todoUrl = todoUrl;
    }

    public Boolean getCanExecute() {
        return isCanExecute;
    }

    public void setCanExecute(Boolean canExecute) {
        isCanExecute = canExecute;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TodoTaskInfo.class.getSimpleName() + "[", "]")
                .add("todoTitle='" + todoTitle + "'")
                .add("startUser='" + startUser + "'")
                .add("startTime='" + startTime + "'")
                .add("todoUrl='" + todoUrl + "'")
                .toString();
    }
}
