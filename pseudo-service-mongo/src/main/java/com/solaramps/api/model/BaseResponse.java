package com.solaramps.api.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {
    private int code;
    private String message;
    //private T data;
    private String data;
    private List<String> messages;



}