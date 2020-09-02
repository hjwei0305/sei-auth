package com.changhong.sei.auth.certification.sso.ch.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * sso待办集成vo
 */
public class TodoTaskRequest implements Serializable {
    private static final long serialVersionUID = -9030912420717792341L;
    /**
     * 用户
     */
    @JsonProperty("user_id")
    private String account;
    /**
     * 当前页
     */
    private Integer page;
    /**
     * 行数
     */
    private Integer rows;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TodoTaskRequest.class.getSimpleName() + "[", "]")
                .add("account='" + account + "'")
                .add("page='" + page + "'")
                .add("rows='" + rows + "'")
                .toString();
    }
}