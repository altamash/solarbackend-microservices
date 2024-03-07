package com.solaramps.api.tenant.mapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class DocuTypeSummaryDTO {
    private String documentTypeName;
    private Boolean required;
    private int maxFileSizeAllowed;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<DocuVolumeDTO> volumesAttached;
    private List<SupportedFileTypeCount> supportedFileTypes;
    private long totalDocuments;
    private Long totalDocumentsInAllTypes;
}
