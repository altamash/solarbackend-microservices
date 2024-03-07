package com.solaramps.loginservice.tenant.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;


    public BaseResponse(String msg ,T data){
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(Integer code,String msg ,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
