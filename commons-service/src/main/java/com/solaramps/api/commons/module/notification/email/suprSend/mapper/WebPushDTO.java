package com.solaramps.api.commons.module.notification.email.suprSend.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebPushDTO {

//    JSONObject webpush = new JSONObject()
//            .put("endpoint", "__end_point__")
//            .put("expirationTime", "")
//            .put("keys", new JSONObject()
//                    .put("p256dh", "__p256dh__")
//                    .put("auth", "__auth_key__"));

    private String endpoint;
    private String expirationTime;
    private Map<String, String> keys;
}
