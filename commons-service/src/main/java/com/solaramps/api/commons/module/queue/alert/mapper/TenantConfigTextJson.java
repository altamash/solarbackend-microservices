package com.solaramps.api.commons.module.queue.alert.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantConfigTextJson {

    @JsonProperty("event_name")
    private String eventName;
    private String topic;
    @JsonProperty("subject_name")
    private String subjectName;
    @JsonProperty("subject_default")
    private String subjectDefault;
    @JsonProperty("to_list_name")
    private String toListName;
    @JsonProperty("cc_list_name")
    private String ccListName;
    @JsonProperty("bcc_list_name")
    private String bccListName;
    @JsonProperty("attachment_dir_ref")
    private String attachmentDirRef;

    @Override
    public String toString() {
        return "TenantConfigTextJson{" +
                "eventName='" + eventName + '\'' +
                ", topic='" + topic + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectDefault='" + subjectDefault + '\'' +
                ", toListName=" + toListName +
                ", ccListName=" + ccListName +
                ", bccListName=" + bccListName +
                '}';
    }
}
