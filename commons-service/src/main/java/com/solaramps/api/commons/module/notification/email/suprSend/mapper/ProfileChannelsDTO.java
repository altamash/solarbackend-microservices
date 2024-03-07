package com.solaramps.api.commons.module.notification.email.suprSend.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileChannelsDTO {

    private List<String> emails;
    private List<String> sms;
    private List<String> whatsApp;


    // Add Androidpush token.Pass the vendor as 2nd param.
//        user.addAndroidpush("androidpush_fcm_token__","fcm");
//        user.addAndroidpush("androidpush_xiaomi_token__", "xiaomi");
    private Map<String, String> androidpushMap;
    private String androidpush;

    // Add iospush token
//        user.addIospush("__iospush_apns_token__");
    private String Iospush;

    // Add Slack using user email id
//    JSONObject slackIdent = new JSONObject()
//            .put("access_token", "xoxb-XXXXXXXX")
//            .put("email", "user@example.com");
//        user.addSlack(slackIdent);

    private List<SlackIdentDTO> slackIdentDTOs;

    // Add Slack incoming webhook
//    JSONObject slackIdent = new JSONObject()
//            .put("incoming_webhook", new JSONObject().put("url", "https://hooks.slack.com/services/TXXXXXXXXX/BXXXXXXXX/XXXXXXXXXXXXXXXXXXX"))
//        user.addSlack(slackIdent);
    private List<String> slackIncomingWebhooks;

    // Add Webpush token json (VAPID)
//    JSONObject webpush = new JSONObject()
//            .put("endpoint", "__end_point__")
//            .put("expirationTime", "")
//            .put("keys", new JSONObject()
//                    .put("p256dh", "__p256dh__")
//                    .put("auth", "__auth_key__"));
//        user.addWebpush(webpush, "vapid");

    private List<WebPushDTO> webPushDTOs;
}
