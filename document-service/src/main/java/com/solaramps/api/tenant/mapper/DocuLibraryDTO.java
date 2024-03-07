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
public class DocuLibraryDTO {

    private Long docuId;
    private String docuType;
    private String codeRefType;
    private String codeRefId;
    private String docuName;
    private String notes;
    private String tags;
    private String size;
    private String format;
    private String uri;
    private String status;
    private Boolean visibilityKey;
    private Long compKey;
    private Long resourceInterval;
    private Boolean lockedInd;
    private Boolean linkToMeasure;
    private String state;
    private String docuAlias;
    private String digitallySigned;
    private String signRefNo;
    private Long organizationId;
    private Long entityId;
    private Long contractId;
    private List<String> volumeNames;
    private List<String> moduleNames;
    private String volumeName;
    private String moduleName;
    private LocalDateTime createdAt;
}
