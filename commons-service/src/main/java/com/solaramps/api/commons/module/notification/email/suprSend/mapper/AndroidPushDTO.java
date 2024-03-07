package com.solaramps.api.commons.module.notification.email.suprSend.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AndroidPushDTO {

    // Add Androidpush token.Pass the vendor as 2nd param.
//        user.addAndroidpush("androidpush_fcm_token__","fcm");
//        user.addAndroidpush("androidpush_xiaomi_token__", "xiaomi");

    private String token;
    private String vendor;
}
