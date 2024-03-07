package com.solaramps.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonitorAPIResponse {
    //  Custom field values
    private Long id;
    private List<MeasureDefinitionSAASDTO> measures;

}
