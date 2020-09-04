package com.changhong.sei.auth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * 实现功能：待办消息返回vo
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-16 00:00
 */
public class TodoTaskResponse implements Serializable {
    private static final long serialVersionUID = 9037027252318532703L;
    /**
     * 错误码
     */
    private String errcode = "0";
    /**
     * 错误信息
     */
    private String errmsg = "";

    /**
     * 总数据行数
     */
    private String count = "0";
    /**
     * 待办数据集合
     */
    private List<TodoTaskInfo> todolist = new ArrayList<>();

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<TodoTaskInfo> getTodolist() {
        return todolist;
    }

    public void setTodolist(List<TodoTaskInfo> todolist) {
        this.todolist = todolist;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TodoTaskResponse.class.getSimpleName() + "[", "]")
                .add("errcode='" + errcode + "'")
                .add("errmsg='" + errmsg + "'")
                .add("count='" + count + "'")
                .add("todolist=" + todolist)
                .toString();
    }
}
