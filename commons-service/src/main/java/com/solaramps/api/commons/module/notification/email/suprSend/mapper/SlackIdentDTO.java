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
public class SlackIdentDTO {

//    JSONObject slackIdent = new JSONObject()
//            .put("access_token", "xoxb-XXXXXXXX")
//            .put("email", "user@example.com");
//        user.addSlack(slackIdent);

//    private String accessToken;
//    private String email;
    private Map<String, String> keyValues;
}
