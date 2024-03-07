package com.solaramps.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSectionDTO<T> implements Serializable {
    @JsonProperty("ref_id")
    private String refId;
    @JsonProperty("section_name")
    private String sectionName;
    @JsonProperty("content_type")
    private String contentType;
    @JsonProperty("system_indicator")
    private Boolean systemIndicator;
    @JsonProperty("complete_indicator")
    private Boolean completeIndicator;

}