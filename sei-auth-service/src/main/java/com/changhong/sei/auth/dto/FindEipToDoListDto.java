package com.changhong.sei.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @Description:
 * @Author: chenzhiquan
 * @Date: 2023/4/7.
 */
@lombok.NoArgsConstructor
@lombok.Data
public class FindEipToDoListDto {

    /**
     * table
     */
    private List<ToDoListDTO> table;

    /**
     * TableDTO
     */
    @lombok.NoArgsConstructor
    @lombok.Data
    public static class ToDoListDTO {
        /**
         * account
         */
        @JsonProperty("Account")
        private String account;
        /**
         * mailID
         */
        @JsonProperty("MailID")
        private String mailID;
    }
}
