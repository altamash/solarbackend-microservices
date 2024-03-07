package com.solaramps.api.tenant.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocuTypeDTO {

    private Long id;
    private String docuType;
    private Set<DocuVolumeDTO> volumes;
    private Long docuTypeConfigId;
    private Long createdById;
    private Long updatedById;
    private Long numberOfDocuments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
