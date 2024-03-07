package com.solaramps.api.tenant.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocuVolumeDTO {

    private Long id;
    private String volumeName;

    private Long codeRefId;

    private Set<Long> docuTypeIds;
    private Long docuModuleId;
//    private Long docuVolMapId;

    private Long createdById;
    private Long updatedById;
    private Long numberOfDocuments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
