package com.solaramps.api.tenant.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocuMeasureSoftDeleteDTO {

    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("section_id")
    private String sectionId;
    @JsonProperty("measure_id")
    private String measureId;
}


