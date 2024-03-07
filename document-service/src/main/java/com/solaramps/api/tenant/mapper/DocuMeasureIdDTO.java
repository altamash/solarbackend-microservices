package com.solaramps.api.tenant.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocuMeasureIdDTO {

    private Long docuLibraryId;
    private String measureId;
}
