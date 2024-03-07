package com.solaramps.api.commons.module.notification.email.suprSend.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailCallBackObject {

    private String email;
    private Long timestamp;
    @JsonProperty("smtp-id")
    private String smtpId;
    private String event;
    private List<String> category;
    @JsonProperty("sg_event_id")
    private String sgEventId;
    @JsonProperty("sg_message_id")
    private String sg_message_id;
    private String response;
    private String attempt;
    private String useragent;
    private String ip;
    private String url;
    private String reason;
    private String status;
    @JsonProperty("asm_group_id")
    private Integer asmGroupId;
}
