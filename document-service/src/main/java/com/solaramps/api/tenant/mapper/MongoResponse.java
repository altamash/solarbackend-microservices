package com.solaramps.api.tenant.mapper;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoResponse<T> {
    private int code;
    private String message;
    private String data;
    private List<String> messages;
    private String error;



}