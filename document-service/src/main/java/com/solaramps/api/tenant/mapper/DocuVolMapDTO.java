package com.solaramps.api.tenant.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocuVolMapDTO {

    private Long id;
    private Long docuTypePAVId;
    private Long volPAVId;
    private Long docuTypeId;
    private List<Long> docuVolumeIds;
    private Boolean isMandatory;
    private Integer noFilesAllowed;
    private Integer maxFileSize;
    private List<String> allowedFileType;
    private Long createdById;
    private Long updatedById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
