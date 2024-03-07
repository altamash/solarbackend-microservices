package com.solaramps.api.commons.module.email.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalizationDTO {

    @JsonProperty("to")
    private List<EmailDTO> tos;

    @JsonProperty("from")
    private EmailDTO from;

    @JsonProperty("cc")
    private List<EmailDTO> ccs;

    @JsonProperty("bcc")
    private List<EmailDTO> bccs;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("headers")
    private Map<String, String> headers;

    @JsonProperty("substitutions")
    private Map<String, String> substitutions;

    @JsonProperty("custom_args")
    private Map<String, String> customArgs;

    @JsonProperty("dynamic_template_data")
    private Map<String, Object> dynamicTemplateData;

    @JsonProperty("send_at")
    private long sendAt;

    private String templateId;
    private String fileUrl;
    private String fileName;
    private String fileType;

    public void addDynamicTemplateData(String key, Object value) {
        if (dynamicTemplateData == null) {
            dynamicTemplateData = new HashMap<>();
        }
        dynamicTemplateData.put(key, value);
    }
}
